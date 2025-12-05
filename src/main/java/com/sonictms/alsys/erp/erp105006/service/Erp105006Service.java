package com.sonictms.alsys.erp.erp105006.service;

import com.sonictms.alsys.erp.erp105006.entity.Erp105006VO;
import com.sonictms.alsys.erp.erp105006.mapper.Erp105006Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp105006Service {

    private final Erp105006Mapper erp105006Mapper;

    /**
     * 재입고 대기 목록 조회
     *
     * @param erp105006VO 검색 조건
     * @return 재입고 대기 목록
     */
    public List<Erp105006VO> erp105006List(Erp105006VO erp105006VO) {
        log.info("재입고 대기 목록 조회 시작");
        log.info("검색 조건: {}", erp105006VO);

        List<Erp105006VO> list = erp105006Mapper.erp105006List(erp105006VO);
        log.info("조회 결과: {}건", list.size());

        return list;
    }

    /**
     * 재입고 완료 처리
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105006InboundComplete(Erp105006VO erp105006VO) {
        log.info("재입고 완료 처리 시작");
        log.info("처리 정보: {}", erp105006VO);

        try {
            int result = erp105006Mapper.erp105006InboundComplete(erp105006VO);
            log.info("재입고 완료 처리 성공: {}건 처리", result);
            return result;
        } catch (Exception e) {
            log.error("재입고 완료 처리 실패", e);
            throw e;
        }
    }

    /**
     * 반출 완료 처리
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105006OutboundComplete(Erp105006VO erp105006VO) {
        log.info("반출 완료 처리 시작");
        log.info("처리 정보: {}", erp105006VO);

        try {
            int result = erp105006Mapper.erp105006OutboundComplete(erp105006VO);
            log.info("반출 완료 처리 성공: {}건 처리", result);
            return result;
        } catch (Exception e) {
            log.error("반출 완료 처리 실패", e);
            throw e;
        }
    }

    /**
     * 일괄 재입고 완료 처리 (재고 증가 포함)
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105006BatchInboundComplete(Erp105006VO erp105006VO) {
        log.info("일괄 재입고 완료 처리 시작");
        log.info("처리 정보: {}", erp105006VO);

        try {
            int successCount = 0;
            int stockIncreaseCount = 0;

            // 각 선택된 ID에 대해 처리
            for (String tblSoPId : erp105006VO.getSelectedIds()) {
                try {
                    // 상품 정보 조회 (MTO 여부 포함)
                    Erp105006VO productInfo = erp105006Mapper.selectRestockProductInfo(tblSoPId);

                    if (productInfo == null) {
                        log.warn("상품 정보 없음 - TBL_SO_P_ID: {}", tblSoPId);
                        continue;
                    }

                    log.info("재입고 처리 - 주문: {}, 상품: {}, MTO: {}, 수량: {}",
                            productInfo.getSoNo(), productInfo.getProdCd(),
                            productInfo.getMtoYn(), productInfo.getReturnQty());

                    // MTO가 아닌 경우에만 재고 증가
                    if (!"Y".equals(productInfo.getMtoYn())) {
                        // 현재 재고 조회
                        Erp105006VO inventory = erp105006Mapper.selectInventory(productInfo);

                        if (inventory != null && inventory.getTblMtrlMId() != null) {
                            productInfo.setCurrentQty(inventory.getCurrentQty());
                            productInfo.setTblMtrlMId(inventory.getTblMtrlMId());
                        } else {
                            log.warn("재고 정보 없음 - MTO가 아닌데 재고가 없습니다. 주문: {}, 상품: {}",
                                    productInfo.getSoNo(), productInfo.getProdCd());
                            productInfo.setCurrentQty(0);
                        }

                        // 증가 후 재고 계산
                        Integer returnQty = Integer.parseInt(productInfo.getReturnQty());
                        productInfo.setAfterQty(productInfo.getCurrentQty() + returnQty);

                        // 재고 증가
                        productInfo.setProcessUser(erp105006VO.getProcessUser());
                        productInfo.setProcessDt(erp105006VO.getProcessDt());
                        int inventoryUpdated = erp105006Mapper.updateInventoryIncrease(productInfo);

                        log.info("재고 증가 완료 - 주문: {}, 상품: {}, 증가전: {}, 증가후: {}",
                                productInfo.getSoNo(), productInfo.getProdCd(),
                                productInfo.getCurrentQty(), productInfo.getAfterQty());

                        // 재고 변동 이력 저장
                        if (productInfo.getTblMtrlMId() != null) {
                            erp105006Mapper.insertRestockInventoryHistory(productInfo);
                            stockIncreaseCount++;
                        }
                    } else {
                        log.info("MTO 상품 - 재고 증가 생략: 주문={}, 상품={}",
                                productInfo.getSoNo(), productInfo.getProdCd());
                    }

                    successCount++;

                } catch (Exception e) {
                    log.error("재입고 처리 실패 - TBL_SO_P_ID: {}", tblSoPId, e);
                    throw e;
                }
            }

            // TBL_SO_P 재입고 플래그 업데이트
            int result = erp105006Mapper.erp105006BatchInboundComplete(erp105006VO);
            log.info("일괄 재입고 완료 처리 성공: {}건 처리, {}건 재고 증가", result, stockIncreaseCount);

            return result;
        } catch (Exception e) {
            log.error("일괄 재입고 완료 처리 실패", e);
            throw e;
        }
    }

    /**
     * 일괄 반출 완료 처리
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105006BatchOutboundComplete(Erp105006VO erp105006VO) {
        log.info("일괄 반출 완료 처리 시작");
        log.info("처리 정보: {}", erp105006VO);

        try {
            int result = erp105006Mapper.erp105006BatchOutboundComplete(erp105006VO);
            log.info("일괄 반출 완료 처리 성공: {}건 처리", result);
            return result;
        } catch (Exception e) {
            log.error("일괄 반출 완료 처리 실패", e);
            throw e;
        }
    }

    /**
     * 반출 대상 여부 변경
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 건수
     */
    public int erp105006ToggleOutboundTarget(Erp105006VO erp105006VO) {
        log.info("반출 대상 여부 변경 처리");
        return erp105006Mapper.erp105006ToggleOutboundTarget(erp105006VO);
    }

    /**
     * 입고완료 되돌리기 처리
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105006RevertInboundComplete(Erp105006VO erp105006VO) {
        log.info("입고완료 되돌리기 처리 시작");
        log.info("처리 정보: {}", erp105006VO);

        try {
            int stockDecreaseCount = 0;

            // 각 선택된 ID에 대해 재고 감소 처리
            for (String tblSoPId : erp105006VO.getSelectedIds()) {
                try {
                    // 상품 정보 조회 (MTO 여부 포함)
                    Erp105006VO productInfo = erp105006Mapper.selectRestockProductInfo(tblSoPId);

                    if (productInfo == null) {
                        log.warn("상품 정보 없음 - TBL_SO_P_ID: {}", tblSoPId);
                        continue;
                    }

                    log.info("재입고 취소 처리 - 주문: {}, 상품: {}, MTO: {}, 수량: {}",
                            productInfo.getSoNo(), productInfo.getProdCd(),
                            productInfo.getMtoYn(), productInfo.getReturnQty());

                    // MTO가 아닌 경우에만 재고 감소
                    if (!"Y".equals(productInfo.getMtoYn())) {
                        // 현재 재고 조회
                        Erp105006VO inventory = erp105006Mapper.selectInventory(productInfo);

                        if (inventory != null && inventory.getTblMtrlMId() != null) {
                            productInfo.setCurrentQty(inventory.getCurrentQty());
                            productInfo.setTblMtrlMId(inventory.getTblMtrlMId());
                        } else {
                            log.warn("재고 정보 없음 - MTO가 아닌데 재고가 없습니다. 주문: {}, 상품: {}",
                                    productInfo.getSoNo(), productInfo.getProdCd());
                            productInfo.setCurrentQty(0);
                        }

                        // 감소 후 재고 계산
                        Integer returnQty = Integer.parseInt(productInfo.getReturnQty());
                        productInfo.setAfterQty(productInfo.getCurrentQty() - returnQty);

                        // 재고 감소
                        productInfo.setProcessUser(erp105006VO.getProcessUser());
                        productInfo.setRevertRemark(erp105006VO.getRevertRemark());
                        int inventoryUpdated = erp105006Mapper.updateInventoryDecrease(productInfo);

                        log.info("재고 감소 완료 - 주문: {}, 상품: {}, 감소전: {}, 감소후: {}",
                                productInfo.getSoNo(), productInfo.getProdCd(),
                                productInfo.getCurrentQty(), productInfo.getAfterQty());

                        // 재고 변동 이력 저장
                        if (productInfo.getTblMtrlMId() != null) {
                            erp105006Mapper.insertRestockRevertInventoryHistory(productInfo);
                            stockDecreaseCount++;
                        }
                    } else {
                        log.info("MTO 상품 - 재고 감소 생략: 주문={}, 상품={}",
                                productInfo.getSoNo(), productInfo.getProdCd());
                    }

                } catch (Exception e) {
                    log.error("재입고 취소 처리 실패 - TBL_SO_P_ID: {}", tblSoPId, e);
                    throw e;
                }
            }

            // TBL_SO_P 재입고 플래그 되돌리기
            int result = erp105006Mapper.erp105006RevertInboundComplete(erp105006VO);
            log.info("입고완료 되돌리기 처리 성공: {}건 처리, {}건 재고 감소", result, stockDecreaseCount);
            return result;
        } catch (Exception e) {
            log.error("입고완료 되돌리기 처리 실패", e);
            throw e;
        }
    }
}
