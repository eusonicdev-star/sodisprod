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
     * 일괄 재입고 완료 처리
     *
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105006BatchInboundComplete(Erp105006VO erp105006VO) {
        log.info("일괄 재입고 완료 처리 시작");
        log.info("처리 정보: {}", erp105006VO);

        try {
            int result = erp105006Mapper.erp105006BatchInboundComplete(erp105006VO);
            log.info("일괄 재입고 완료 처리 성공: {}건 처리", result);
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
}
