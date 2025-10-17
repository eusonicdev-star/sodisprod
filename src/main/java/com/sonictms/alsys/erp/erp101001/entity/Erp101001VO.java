package com.sonictms.alsys.erp.erp101001.entity;


import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
public class Erp101001VO extends CommonVO {
    private static final long serialVersionUID = 1L;

    private String agntCd;        //화주사
    private String agntNm;
    private String soType;        //주문유형
    private String soTypeNm;
    private String soStatCd;        //주문상태
    private String soStatNm;
    private String dcCd;        //물류센터
    private String dcNm;
    private String ordrInptDt;        //시스템등록일
    private String rcptDt;        //주문확정일
    private String rqstDt;        //배송확정일
    private String dlvyCnfmDt;        //배송일
    private String mallCd;        //쇼핑몰
    private String brand;        //브랜드
    private String soNo;        //AL 오더
    private String refSoNo;        //고객사 주문번호
    private String acptEr;        //수취인
    private String acptTel1;        //수취인 일반전화
    private String acptTel2;        //수취인 휴대전화
    private String postCd;        //우편번호
    private String addr1;
    private String addr2;
    private String dlvyRqstMsg;        //배송요청사항
    private String custSpclTxt;        //고객사특이사항
    private String dlvyCostType;        //운임구분상태. 착불 선불
    private String dlvyCostTypeNm;
    private String rcptCost;        //착불비
    private String passCost;        //통행료
    private String instDt;        //시공일
    private String instEr;        //시공기사
    private String instCost;        //시공비
    private String dlvyCost;        //물류비
    private String fromEr;        //발송인
    private String fromTel1;
    private String fromTel2;
    private String fromPostCd;
    private String fromAddr1;
    private String fromAddr2;
    private String distChanCd;
    private String orgnSoNo;
    private String useYn;        //사용여부
    private String delResn;
    private String memo;        //비고

    private String refSoList;

    private String dtType;        //날짜종류
    private String fromDt;        //날짜시작
    private String toDt;        //날짜종류
    private String agntList;        //화주
    private String dcList;        //물류센터
    private String soList;        //오더번호목록
    private String soTypeList;        //오더유형
    private String soStatList;        //오더상태
    private String mallNm;        //쇼핑몰
    private String acptNm;        //고객명
    private String acptTel;        //고객전화번호
    private String prodSeq;        //순번
    private String prodCd;        //상품코드
    private String prodNm;        //상품명
    private String qty;        //수량
    private String tblSoMId;        //주문 고유 아이디
    private String ordrEr;        //주문자
    private String dlvyLiftDt;        //공장출고일
    private String srchDay;        //찾을날짜 범위
    private String prodCdList;        //상품코드 리스트
    private String qtyList;        //수량리스트
    private String addr;        //주소
    private String capaId;        //좌석아이디
    private String weekCd;        //요일코드
    private String weekNm;        //요일명칭
    private String capaCtgr;        //시공카테고리코드
    private String capaCtgrNm;        //시공카테고리명칭
    private String capaType;        //시공유형코드
    private String capaTypeNm;        //시공유형명칭
    private String zoneType;        //권역유형코드
    private String zoneTypeNm;        //권역유형명칭
    private String zoneCd;        //권역코드
    private String zoneCdNm;        //권역명칭
    private String ableYn;        //날짜 사용가능
    private String totCapa;        //총시공케파
    private String useCapa;        //사용한케파
    private String remnCapa;        //남은케파
    private List<Erp101001VO> detailData;
    private String costType;        //운임구분
    private String inptSys;        //입력시스템 (1건식 입력 ERP_SO, 엑셀로 한꺼번에 입력 ERP_EXCEL)
    private String tblSoPId;        //상세 고유 아이디
    private String rtnSoType;
    private String rtnName;
    private String giLiftDt;        //공장출고일
    private String srchAddr;            //주소검색
    private String instCtgrList;
    private String instCtgr;
    private String instCtgrNm;
}
