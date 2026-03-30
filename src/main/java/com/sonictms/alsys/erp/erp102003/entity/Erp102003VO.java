package com.sonictms.alsys.erp.erp102003.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
public class Erp102003VO extends CommonVO {

    private static final long serialVersionUID = 1L;

    private String agntCd;        //화주(물류센터)코드
    private String agntNm;        //화주(물류센터)명칭

    private String dtType;    //날짜유형
    private String fromDt;    //검색시작일
    private String toDt;    //검색종료일
    private String agntList;    //화주사코드리스트
    private String soList;    //오더번호리스트
    private String soTypeList;    //오더유형콤보 체크값 리스트
    private String soStatList;    //오더상태콤보 체크값 리스트
    private String rcptNm;    //이름
    private String rcptTel;    //전화번호
    private String alrmSendYn;    //알림톡 전송여부. 체크를 하면 값은 N 미전송, 체크 안하면 값은 공백 - 모두 조회

    private String mallCd;
    private String soNo;
    private String refSoNo;
    private String soType;
    private String soTypeNm;    //오더유형명칭
    private String soStatCd;    //오더상태코드
    private String soStatNm;    //오더상태명칭
    private String acptEr;    //고객명
    private String acptTel1;    //고객전화1
    private String acptTel2;    //고객전화2
    private String postCd;    //우편번호
    private String addr1;    //주소1
    private String addr2;    //주소2
    private String ordrDt;    //주문일
    private String rcptDt;    //접수일
    private String rqstDt;    //요청일
    private String rcptCost;    //착불비
    private String passCost;    //통행료
    private String ordrEr;    //주문자
    private String dlvyRqstMsg;    //배송메세지
    private String custSpclTxt;    //고객특이사항
    private String alrmSendDt;    //알림톡 최근 전송일자
    private String alrmSendCnt;    //알림톡전송횟수
    private String tblSoMId;
    private String tblSoNoList;
    private String tblSoMIdList;
    private String dlvyCnfmDt;    //배송확정일
    private String giLiftDt;    //공장출고일
}
