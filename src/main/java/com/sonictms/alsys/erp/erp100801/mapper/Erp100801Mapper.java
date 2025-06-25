package com.sonictms.alsys.erp.erp100801.mapper;

import com.sonictms.alsys.erp.erp100801.entity.Erp100801VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp100801Mapper {

    //kpp에서 불러온 데이터를 얼라이언스 테이블에 저장하는 곳
    Erp100801VO outCmpySave(Erp100801VO erp100801VO);

    //kpp에서 불러온 데이터를 얼라이언스 테이블에 저장한것을 불러오는부
    List<Erp100801VO> erp100801OutCmpyList(Erp100801VO erp100801VO);

    //데이터 체크
    Erp100801VO erp100801DataChk(Erp100801VO erp100801VO);

    //테이블에 저장된 외부업체(KPP)의 정보를 얼라이언스의 오더로 입력하기 - 화면의 그리드 한줄한줄을 넘겨받아 한번씩 한번씩 입력한다
    Erp100801VO erp100801AlOrdrSave(Erp100801VO erp100801VO);

}