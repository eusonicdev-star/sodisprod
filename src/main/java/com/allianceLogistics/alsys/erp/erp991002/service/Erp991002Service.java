package com.allianceLogistics.alsys.erp.erp991002.service;

import com.allianceLogistics.alsys.erp.erp991002.entity.Erp991002VO;
import com.allianceLogistics.alsys.erp.erp991002.mapper.Erp991002Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp991002Service {

    private final Erp991002Mapper erp991002Mapper;

    //메뉴 권한관리 그리드 리스트 불러오기
    public List<Erp991002VO> erp991002List(Erp991002VO erp991002VO) {
        List<Erp991002VO> list = erp991002Mapper.erp991002List(erp991002VO);
        return list;
    }

    //메뉴 권한관리 수정내용 저장하기
    public Erp991002VO erp991002Updt(List<Erp991002VO> list) {
        Erp991002VO erp991002VO = null;
        for (int i = 0; i < list.size(); i++) {
            erp991002VO = erp991002Mapper.erp991002Updt(list.get(i));
        }
        return erp991002VO;
    }


    //권한그룹 추가 팝업에서 간편 권한그룹 중복체크
    public Erp991002VO erp991002p1DoubleChk(Erp991002VO erp991002VO) {

        erp991002VO = erp991002Mapper.erp991002p1DoubleChk(erp991002VO);
        return erp991002VO;
    }

    //권한그룹 간편등록
    public Erp991002VO erp991002p1Save(Erp991002VO erp991002VO) {

        erp991002VO = erp991002Mapper.erp991002p1Save(erp991002VO);

        return erp991002VO;
    }


} 