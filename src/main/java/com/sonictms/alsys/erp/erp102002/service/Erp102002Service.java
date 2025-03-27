package com.sonictms.alsys.erp.erp102002.service;

import com.sonictms.alsys.erp.erp102002.entity.Erp102002VO;
import com.sonictms.alsys.erp.erp102002.mapper.Erp102002Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp102002Service {

    private final Erp102002Mapper erp102002Mapper;

    //리스트조회 그리드 불러오기
    public List<Erp102002VO> erp102002List(Erp102002VO erp102002VO) {
        List<Erp102002VO> list = erp102002Mapper.erp102002List(erp102002VO);
        return list;
    }

    //팝업  등록/ 수정 하기
    public Erp102002VO erp102002p1ComdSave(Erp102002VO erp102002VO) {
        erp102002VO = erp102002Mapper.erp102002p1ComdSave(erp102002VO);
        return erp102002VO;
    }


    //고유 ID 로 정보 찾기
    public Erp102002VO erp102002p1AlrmSrch(Erp102002VO erp102002VO) {
        erp102002VO = erp102002Mapper.erp102002p1AlrmSrch(erp102002VO);
        return erp102002VO;
    }


    //알림톡 스케줄 실행 대상 조회
    public List<Erp102002VO> erp102002ScdlExecLoad(Erp102002VO erp102002VO) {
        List<Erp102002VO> list = erp102002Mapper.erp102002ScdlExecLoad(erp102002VO);
        return list;
    }


} 