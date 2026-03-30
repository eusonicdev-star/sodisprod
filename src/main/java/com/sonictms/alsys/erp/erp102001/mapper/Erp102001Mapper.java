package com.sonictms.alsys.erp.erp102001.mapper;


import com.sonictms.alsys.erp.erp102001.entity.Erp102001VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface Erp102001Mapper {

    //그리드 리스트 조회
    List<Erp102001VO> erp102001p1List(Erp102001VO erp102001VO);

    //메인화면 주문헤더조회
    Erp102001VO erp102001SoHeadSrch(Erp102001VO erp102001VO);

    //메인화면 주문상세조회
    List<Erp102001VO> erp102001SoDetailSrch(Erp102001VO erp102001VO);

    //메인화면 상담내역조회
    Erp102001VO erp102001HappyCallSrch(Erp102001VO erp102001VO);

    //메인화면 상담정보 등록하기
    Erp102001VO erp102001CnslSave(Erp102001VO erp102001VO);

    //해피콜상담리스트 팝업 조회
    List<Erp102001VO> erp102001p2List(Erp102001VO erp102001VO);


    //메인화면 오더정보 변경한것 수정하기
    Erp102001VO erp102001p1Updt(Erp102001VO erp102001VO);


    //메인화면 알림톡 조회
    Erp102001VO erp102001talkCallSrch(Erp102001VO erp102001VO);


    //알림톡 발송할 내용을 불러온다
    Erp102001VO erp102001p4LoadAlrmTmp(Erp102001VO erp102001VO);


    //알림톡발송리스트 팝업 조회
    List<Erp102001VO> erp102001p3List(Erp102001VO erp102001VO);


    //배송정보 & 이미지
    Erp102001VO erp102001soDlvyInfoData(Erp102001VO erp102001VO);


    /// 메인화면 상담정보 등록하기
    Erp102001VO erp101001DlvyCnfm(Erp102001VO erp102001VO);

    // 주문이력보기 팝업의 리스트조회
    List<Erp102001VO> erp102001p5List(Erp102001VO erp102001VO);


    //20211231 정연호 메인화면의 착불비 합계 update
    Erp102001VO erp102001UpdtRcptCost(Erp102001VO erp102001VO);
}