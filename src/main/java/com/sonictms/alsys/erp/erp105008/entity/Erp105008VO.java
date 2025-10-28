package com.sonictms.alsys.erp.erp105008.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Erp105008VO extends CommonVO {

    private static final long serialVersionUID = 1L;

    // 기본 정보
    private String agntCd;                        // 화주코드
    private String agntNm;                        // 화주명
    private String soType;                        // 주문유형
    private String soTypeNm;                      // 주문유형명
    private String soStatCd;                      // 주문상태
    private String soStatNm;                      // 주문상태명
    private String dcCd;                          // 물류센터코드
    private String dcNm;                          // 물류센터명
    private String ordrInptDt;                    // 시스템등록일
    private String rcptDt;                        // 고객접수일
    private String rqstDt;                        // 고객배송요청일
    private String dlvyCnfmDt;                    // 배송확정일
    private String giLiftDt;                      // 공장출고일
    private String mallCd;                        // 쇼핑몰
    private String brand;                         // 브랜드
    private String soNo;                          // AL 오더
    private String refSoNo;                       // 고객사 주문번호
    private String acptEr;                        // 수취인
    private String acptTel1;                      // 수취인 일반전화
    private String acptTel2;                      // 수취인 휴대전화
    private String postCd;                        // 우편번호
    private String addr1;                         // 주소
    private String addr2;                         // 상세주소
    private String dlvyRqstMsg;                   // 배송요청사항
    private String custSpclTxt;                   // 고객사 특이사항
    private String dlvyCostType;                  // 운임구분
    private String dlvyCostTypeNm;                // 운임구분명
    private String rcptCost;                      // 착불비
    private String passCost;                      // 통행료
    private String instDt;                        // 시공일
    private String paltNoxx;                      // 팔렛트번호
    private String instEr;                        // 시공기사
    private String instErId;                      // 시공기사ID
    private String team;                          // 팀
    private String instMemo;                      // 기사메모
    private String instCost;                      // 시공비
    private String dlvyCost;                      // 물류비
    private String dlvyStatNm;                    // 배송상태
    private String liftYn;                        // 상차완료
    private String prodSeq;                       // 순번
    private String prodCd;                        // 상품코드
    private String prodNm;                        // 상품명
    private String qty;                           // 수량
    private String cprodCd;                       // 상품코드(HAWA)
    private String cprodNm;                       // 상품명(HAWA)
    private String cqty;                          // 수량(HAWA)
    private String fromEr;                        // 발송인
    private String fromTel1;                      // 발송인 일반전화
    private String fromTel2;                      // 발송인 휴대전화
    private String fromPostCd;                    // 발송인 우편번호
    private String fromAddr1;                     // 발송인 주소
    private String fromAddr2;                     // 발송인 상세주소
    private String distChanCd;                    // distChanCd
    private String orgnSoNo;                      // 원AL 오더
    private String useYn;                         // 사용여부 
    private String delResn;                       // delResn
    private String memo;                          // 비고
    private String instCtgr;                      // 시공카테고리
    private String instCtgrNm;                    // 시공카테고리명

    // 검색 조건
    private String dtType;                        // 달력유형
    private String fromDt;                        // 시작일
    private String toDt;                          // 종료일
    private String agntList;                      // 화주사 목록
    private String dcList;                        // 물류센터 목록
    private String soList;                        // AL 오더 목록
    private String soTypeList;                    // 주문유형 목록
    private String mallNm;                        // 쇼핑몰명
    private String acptNm;                        // 수취인명
    private String acptTel;                       // 수취인 전화번호
    private String refSoList;                     // 고객사 주문번호 목록
    private String prodList;                      // 상품코드 목록
    private String srchAddr;                      // 주소검색
}
