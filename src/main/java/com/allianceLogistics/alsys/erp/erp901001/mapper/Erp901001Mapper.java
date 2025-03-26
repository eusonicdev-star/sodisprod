package com.allianceLogistics.alsys.erp.erp901001.mapper;

import com.allianceLogistics.alsys.erp.erp901001.entity.Erp901001DetailVO;
import com.allianceLogistics.alsys.erp.erp901001.entity.Erp901001VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp901001Mapper {


    //그리드 리스트 조회
    List<Erp901001VO> erp901001List(Erp901001VO erp90100VO);

    //고유 ID 로 정보 찾기
    Erp901001VO erp901001p1agencySrch(Erp901001VO erp90100VO);


    //팝업 화주 등록/ 수정 하기
    Erp901001VO erp901001p1ComdSave(Erp901001VO erp90100VO);


    //20220124 정연호 알림톡 연락처  저장/수정하기
    Erp901001DetailVO erp901001p2DetailSave(Erp901001DetailVO erp901001DetailVO);

    //20220124 정연호. 화주사 코드로 알림톡 연락처 조회하기
    List<Erp901001DetailVO> erp901001p2alrmTlkSrch(Erp901001DetailVO erp901001DetailVO);
}