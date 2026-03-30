package com.sonictms.alsys.erp.erp105005.service;

import com.sonictms.alsys.erp.erp105005.entity.Erp105005VO;
import com.sonictms.alsys.erp.erp105005.mapper.Erp105005Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp105005Service {

    private final Erp105005Mapper erp105005Mapper;

    /**
     * 교환/반품 재입고 목록 조회
     *
     * @param erp105005VO 검색 조건
     * @return 재입고 목록
     */
    public List<Erp105005VO> erp105005List(Erp105005VO erp105005VO) {
        log.info("교환/반품 재입고 목록 조회 시작");
        log.info("검색 조건: {}", erp105005VO);

        List<Erp105005VO> list = erp105005Mapper.erp105005List(erp105005VO);
        log.info("조회 결과: {}건", list.size());

        return list;
    }

    /**
     * 재입고 완료 처리
     *
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105005InboundComplete(Erp105005VO erp105005VO) {
        log.info("재입고 완료 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005InboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105005OutboundComplete(Erp105005VO erp105005VO) {
        log.info("반출 완료 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005OutboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105005BatchInboundComplete(Erp105005VO erp105005VO) {
        log.info("일괄 재입고 완료 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005BatchInboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    @Transactional
    public int erp105005BatchOutboundComplete(Erp105005VO erp105005VO) {
        log.info("일괄 반출 완료 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005BatchOutboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @return 처리된 건수
     */
    public int erp105005ToggleOutboundTarget(Erp105005VO erp105005VO) {
        log.info("반출 대상 여부 변경 처리");
        return erp105005Mapper.erp105005ToggleOutboundTarget(erp105005VO);
    }

    /**
     * 입고완료 되돌리기 처리
     *
     * @param erp105005VO 처리 정보
     * @return 처리된 건수
     */
    @Transactional
    public int erp105005RevertInboundComplete(Erp105005VO erp105005VO) {
        log.info("입고완료 되돌리기 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005RevertInboundComplete(erp105005VO);
            log.info("입고완료 되돌리기 성공: {}건 처리", result);
            return result;
        } catch (Exception e) {
            log.error("입고완료 되돌리기 실패", e);
            throw e;
        }
    }

    /**
     * 반출완료 되돌리기 처리
     *
     * @param erp105005VO 처리 정보
     * @return 처리된 건수
     */
    @Transactional
    public int erp105005RevertOutboundComplete(Erp105005VO erp105005VO) {
        log.info("반출완료 되돌리기 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005RevertOutboundComplete(erp105005VO);
            log.info("반출완료 되돌리기 성공: {}건 처리", result);
            return result;
        } catch (Exception e) {
            log.error("반출완료 되돌리기 실패", e);
            throw e;
        }
    }

    /**
     * 기타비고 업데이트
     *
     * @param erp105005VO 처리 정보
     * @return 처리된 건수
     */
    @Transactional
    public int erp105005UpdateEtcRemark(Erp105005VO erp105005VO) {
        log.info("기타비고 업데이트 처리 시작");
        log.info("처리 정보: {}", erp105005VO);

        try {
            int result = erp105005Mapper.erp105005UpdateEtcRemark(erp105005VO);
            log.info("기타비고 업데이트 성공: {}건 처리", result);
            return result;
        } catch (Exception e) {
            log.error("기타비고 업데이트 실패", e);
            throw e;
        }
    }
}

