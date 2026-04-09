package com.sonictms.alsys.erp.erp104004.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Erp104004VO extends CommonVO {

    private static final long serialVersionUID = 1L;

    private String dtType;    //달력유형
    private String fromDt;    //시작일
    private String toDt;    //종료일
    private String dcList;    //물류센터
    private String teamList;    //팀
    private String zoneList;    //zone
    private String pickGrpArea;    //권역

    private String dcCd;    //물류센터코드
    private String dcNm;    //물류센터
    private String team;    //팀
    private String userNm;    //사용자이름
    private String paltNoxx;    //팔렛트번호
    private String seq;    //순번
    private String pickZone;    //zone

    private String smplCd;    //심플코드
    private String prodCd;    //상품코드
    private String prodNm;    //상품명칭
    private String qty;    //수량
    private String cbm;    //CBM
    private String pickLoc; // 추가: 피킹 로케이션

    //20220106 정연호 추사. 시공카테고리
    private String instCtgr;
    private String instCtgrNm;

    //20220423 정연호 추가. 고객정보 추가
    private String acptEr;            //고객명
    private String acptTel2;        //고객 주 전화번호(휴대폰)
    private String acptTel1;        //고객 보조 전화번호(일반전화번호)
}