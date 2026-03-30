package com.sonictms.alsys.erp.erp105010.service;

import com.sonictms.alsys.erp.erp105010.entity.Erp105010VO;
import com.sonictms.alsys.erp.erp105010.mapper.Erp105010Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Erp105010Service {

    @Autowired
    private Erp105010Mapper erp105010Mapper;

    /**
     * 택배/직출 리스트 조회
     */
    public List<Erp105010VO> getCourierDirectList(Erp105010VO vo) {
        return erp105010Mapper.selectCourierDirectList(vo);
    }

    /**
     * 일괄 출고 처리 (택배 + 업체직출 혼합 가능)
     * MTO인 경우 재고 차감하지 않음
     */
    @Transactional
    public Map<String, Object> processBatchOutbound(List<Erp105010VO> outboundList) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMessages = new StringBuilder();

            for (Erp105010VO vo : outboundList) {
                try {
                    // 상품 정보 조회 (MTO 여부 확인 포함)
                    Erp105010VO productInfo = erp105010Mapper.selectProductInfo(vo.getTblSoPId());
                    
                    if (productInfo == null) {
                        failCount++;
                        errorMessages.append("주문번호 ").append(vo.getSoNo())
                                    .append(": 상품 정보를 찾을 수 없습니다.\n");
                        continue;
                    }

                    // VO에 조회된 정보 복사
                    vo.setTblSoMId(productInfo.getTblSoMId());
                    vo.setCmpyCd(productInfo.getCmpyCd());
                    vo.setAgntCd(productInfo.getAgntCd());
                    vo.setAgntNm(productInfo.getAgntNm());
                    vo.setSoType(productInfo.getSoType());
                    vo.setSoNo(productInfo.getSoNo());
                    vo.setDcCd(productInfo.getDcCd());
                    vo.setMtrlCd(productInfo.getMtrlCd());
                    vo.setCprodNm(productInfo.getCprodNm());
                    vo.setQty(productInfo.getQty());
                    vo.setMtoYn(productInfo.getMtoYn());

                    // MTO가 아닌 경우에만 재고 처리
                    if (!"Y".equals(vo.getMtoYn())) {
                        // 재고 조회
                        Erp105010VO inventory = erp105010Mapper.selectInventory(vo);
                        
                        if (inventory == null || inventory.getTblMtrlMId() == null) {
                            log.warn("재고 정보 없음 - MTO가 아닌데 재고가 없습니다. 주문: {}, 상품: {}", 
                                    vo.getSoNo(), vo.getMtrlCd());
                            // 재고가 없어도 출고는 진행 (음수 허용)
                            vo.setCurrentQty(0);
                            vo.setTblMtrlMId(null);
                        } else {
                            vo.setTblMtrlMId(inventory.getTblMtrlMId());
                            vo.setCurrentQty(inventory.getCurrentQty());
                        }

                        vo.setAfterQty(vo.getCurrentQty() - vo.getQty());

                        // 재고 차감
                        int inventoryUpdated = erp105010Mapper.updateInventoryDecrease(vo);
                        log.info("재고 차감 완료 - 주문: {}, 상품: {}, 차감전: {}, 차감후: {}", 
                                vo.getSoNo(), vo.getMtrlCd(), vo.getCurrentQty(), vo.getAfterQty());

                        // 재고 변동 이력 저장
                        if (vo.getTblMtrlMId() != null) {
                            erp105010Mapper.insertInventoryHistory(vo);
                        }
                    } else {
                        log.info("MTO 상품 - 재고 차감 생략: 주문={}, 상품={}", vo.getSoNo(), vo.getMtrlCd());
                    }

                    // 출고 정보 업데이트
                    if ("COURIER".equals(vo.getProcessType())) {
                        // 택배 출고
                        erp105010Mapper.updateOutboundInfoCourier(vo);
                        log.info("택배 출고 완료 - 주문: {}, 송장: {}", vo.getSoNo(), vo.getWaybillNo());
                    } else {
                        // 업체직출
                        erp105010Mapper.updateOutboundInfoDirect(vo);
                        log.info("업체직출 완료 - 주문: {}", vo.getSoNo());
                    }

                    // 주문 상태 변경 (배송완료)
                    erp105010Mapper.updateOrderStatus(vo.getTblSoMId());

                    successCount++;

                } catch (Exception e) {
                    failCount++;
                    log.error("출고 처리 실패 - 주문: {}", vo.getSoNo(), e);
                    errorMessages.append("주문번호 ").append(vo.getSoNo())
                                .append(": ").append(e.getMessage()).append("\n");
                }
            }

            // 결과 반환
            if (failCount == 0) {
                result.put("rtnYn", "Y");
                result.put("rtnMsg", successCount + "건의 출고 처리가 완료되었습니다.");
            } else if (successCount == 0) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "출고 처리 실패\n" + errorMessages.toString());
            } else {
                result.put("rtnYn", "Y");
                result.put("rtnMsg", 
                    "성공: " + successCount + "건, 실패: " + failCount + "건\n" +
                    "실패 내역:\n" + errorMessages.toString());
            }

        } catch (Exception e) {
            log.error("일괄 출고 처리 중 예외 발생", e);
            result.put("rtnYn", "N");
            result.put("rtnMsg", "출고 처리 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException("출고 처리 실패", e);
        }

        return result;
    }
}
