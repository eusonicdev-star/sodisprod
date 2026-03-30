package com.sonictms.alsys.erp.erp105008.service;

import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import com.sonictms.alsys.erp.erp105008.mapper.Erp105008Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp105008Service {

    private final Erp105008Mapper erp105008Mapper;

    /**
     * 출고대기 리스트 조회
     */
    public List<Erp105008VO> getOutboundWaitList(Erp105008VO vo) {
        return erp105008Mapper.selectOutboundWaitList(vo);
    }
    
    /**
     * 택배 출고 처리 (단건)
     */
    @Transactional
    public Map<String, Object> processCourierOutbound(Erp105008VO vo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 유효성 검사
            if (vo.getTblSoPId() == null) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "상품 ID가 없습니다.");
                return result;
            }
            
            if (vo.getCourierCd() == null || vo.getCourierCd().trim().isEmpty()) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "운송사를 선택해주세요.");
                return result;
            }
            
            if (vo.getWaybillNo() == null || vo.getWaybillNo().trim().isEmpty()) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "송장번호를 입력해주세요.");
                return result;
            }
            
            // 2. 상품 정보 조회
            Erp105008VO productInfo = erp105008Mapper.selectProductInfo(vo);
            if (productInfo == null) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "상품 정보를 찾을 수 없습니다.");
                return result;
            }
            
            // 상품 정보를 vo에 복사
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
            vo.setProcessType("COURIER");
            
            // 3. 재고 처리 (MTO_YN='N'인 상품만)
            if ("N".equals(vo.getMtoYn())) {
                Erp105008VO inventory = erp105008Mapper.selectInventory(vo);
                
                if (inventory != null && inventory.getTblMtrlMId() != null) {
                    vo.setTblMtrlMId(inventory.getTblMtrlMId());
                    vo.setCurrentQty(inventory.getCurrentQty());
                    vo.setAfterQty(inventory.getCurrentQty() - vo.getQty());
                    
                    // 재고 차감 (음수 허용)
                    erp105008Mapper.updateInventoryDecrease(vo);
                    
                    // 재고 변동 이력 기록
                    erp105008Mapper.insertInventoryHistory(vo);
                    
                    log.info("재고 차감 완료 - 상품: {}, 이전: {}, 차감: {}, 이후: {}", 
                        vo.getMtrlCd(), vo.getCurrentQty(), vo.getQty(), vo.getAfterQty());
                } else {
                    log.warn("재고 정보 없음 - 상품: {}, 출고 처리는 계속 진행", vo.getMtrlCd());
                }
            }
            
            // 4. 출고 정보 업데이트
            int outboundResult = erp105008Mapper.updateOutboundInfoCourier(vo);
            if (outboundResult == 0) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "출고 정보 업데이트 실패");
                return result;
            }
            
            // 5. 주문 상태 변경 (9999: 배송완료)
            int statusResult = erp105008Mapper.updateOrderStatus(vo);
            if (statusResult == 0) {
                log.warn("주문 상태 변경 실패 - 주문 ID: {}", vo.getTblSoMId());
            }
            
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "택배 출고 처리가 완료되었습니다.");
            log.info("택배 출고 완료 - 주문: {}, 송장: {}", vo.getSoNo(), vo.getWaybillNo());
            
        } catch (Exception e) {
            log.error("택배 출고 처리 중 오류 발생", e);
            result.put("rtnYn", "N");
            result.put("rtnMsg", "출고 처리 중 오류 발생: " + e.getMessage());
            throw e; // 트랜잭션 롤백을 위해 예외 던지기
        }
        
        return result;
    }
    
    /**
     * 업체직출 출고 처리 (단건)
     */
    @Transactional
    public Map<String, Object> processDirectOutbound(Erp105008VO vo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 유효성 검사
            if (vo.getTblSoPId() == null) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "상품 ID가 없습니다.");
                return result;
            }
            
            // 2. 상품 정보 조회
            Erp105008VO productInfo = erp105008Mapper.selectProductInfo(vo);
            if (productInfo == null) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "상품 정보를 찾을 수 없습니다.");
                return result;
            }
            
            // 상품 정보를 vo에 복사
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
            vo.setProcessType("DIRECT");
            
            // 3. 재고 처리 (MTO_YN='N'인 상품만)
            if ("N".equals(vo.getMtoYn())) {
                Erp105008VO inventory = erp105008Mapper.selectInventory(vo);
                
                if (inventory != null && inventory.getTblMtrlMId() != null) {
                    vo.setTblMtrlMId(inventory.getTblMtrlMId());
                    vo.setCurrentQty(inventory.getCurrentQty());
                    vo.setAfterQty(inventory.getCurrentQty() - vo.getQty());
                    
                    // 재고 차감 (음수 허용)
                    erp105008Mapper.updateInventoryDecrease(vo);
                    
                    // 재고 변동 이력 기록
                    erp105008Mapper.insertInventoryHistory(vo);
                    
                    log.info("재고 차감 완료 - 상품: {}, 이전: {}, 차감: {}, 이후: {}", 
                        vo.getMtrlCd(), vo.getCurrentQty(), vo.getQty(), vo.getAfterQty());
                } else {
                    log.warn("재고 정보 없음 - 상품: {}, 출고 처리는 계속 진행", vo.getMtrlCd());
                }
            }
            
            // 4. 출고 정보 업데이트
            int outboundResult = erp105008Mapper.updateOutboundInfoDirect(vo);
            if (outboundResult == 0) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "출고 정보 업데이트 실패");
                return result;
            }
            
            // 5. 주문 상태 변경 (9999: 배송완료)
            int statusResult = erp105008Mapper.updateOrderStatus(vo);
            if (statusResult == 0) {
                log.warn("주문 상태 변경 실패 - 주문 ID: {}", vo.getTblSoMId());
            }
            
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "업체직출 출고 처리가 완료되었습니다.");
            log.info("업체직출 출고 완료 - 주문: {}", vo.getSoNo());
            
        } catch (Exception e) {
            log.error("업체직출 출고 처리 중 오류 발생", e);
            result.put("rtnYn", "N");
            result.put("rtnMsg", "출고 처리 중 오류 발생: " + e.getMessage());
            throw e; // 트랜잭션 롤백을 위해 예외 던지기
        }
        
        return result;
    }
    
    /**
     * 일괄 출고 처리 (택배 다중 처리)
     * 각 건마다 개별 택배사, 송장번호를 가짐
     */
    @Transactional
    public Map<String, Object> processBatchOutbound(List<Erp105008VO> outboundList) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();
            
            for (Erp105008VO vo : outboundList) {
                try {
                    Map<String, Object> singleResult;
                    
                    // 택배 또는 업체직출 구분
                    if ("9905".equals(vo.getSoType())) {
                        // 택배 출고
                        singleResult = processCourierOutbound(vo);
                    } else if ("4000".equals(vo.getSoType())) {
                        // 업체직출 출고
                        singleResult = processDirectOutbound(vo);
                    } else {
                        errorMessages.add("주문번호 " + vo.getSoNo() + ": 택배(9905) 또는 업체직출(4000)만 처리 가능합니다.");
                        failCount++;
                        continue;
                    }
                    
                    if ("Y".equals(singleResult.get("rtnYn"))) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("주문번호 " + vo.getSoNo() + ": " + singleResult.get("rtnMsg"));
                    }
                    
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("주문번호 " + vo.getSoNo() + ": " + e.getMessage());
                    log.error("개별 출고 처리 실패 - 주문: {}", vo.getSoNo(), e);
                }
            }
            
            if (failCount == 0) {
                result.put("rtnYn", "Y");
                result.put("rtnMsg", successCount + "건 출고 처리 완료");
            } else if (successCount == 0) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "모든 출고 처리 실패\n" + String.join("\n", errorMessages));
            } else {
                result.put("rtnYn", "Y");
                result.put("rtnMsg", "성공: " + successCount + "건, 실패: " + failCount + "건\n" + String.join("\n", errorMessages));
            }
            
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            
        } catch (Exception e) {
            log.error("일괄 출고 처리 중 오류 발생", e);
            result.put("rtnYn", "N");
            result.put("rtnMsg", "일괄 출고 처리 중 오류 발생: " + e.getMessage());
        }
        
        return result;
    }
}
