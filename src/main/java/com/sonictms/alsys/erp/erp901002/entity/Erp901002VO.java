package com.sonictms.alsys.erp.erp901002.entity;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901002VO extends CommonVO {
		
	private static final long serialVersionUID = 1L;
	
		private	String	agntCd			;		//화주(물류센터)코드
		private	String	agntNm			;		//화주(물류센터)명칭
		
		private	String	mtrlCd	;		//상품코드
		private	String	mtrlCtgr1			;		//제품분류 대
		private	String	mtrlCtgr2			;		//제품분류 중
		private	String	mtrlCtgr3			;		//제품분류 소
		private	String	statCd			;		//상태코드
		private	String	tblMtrlMId			;	//고유ID
		

		private String mtrlType             ;	//상품유형
		private String mtrlNm               ;	//상품명칭
		private String mtrlDesc             ;	//상품 비고
		private String statNm               ;	//상태명칭
		private String smplCd               ;	//심플코드
		private String custPrc              ;	//소비자가
		private String salePrc              ;	//판매가
		private String factPrc              ;	//공가
		private String purcPrc              ;	//매입가
		private String sizeNm               ;	//규격
		private String lnth                 ;	//사이즈 길이(가로)
		private String dpth                 ;	//사이즈 깊이
		private String hght                 ;	//사이즈 높이(세로)
		private String hght2                ;	//사이즈 높이2
		private String cbm                  ;	//CBM
		private String wght                 ;	//무게
		private String mtrlCtgr1Nm          ;	//제품분류 대 명칭
		private String mtrlCtgr2Nm          ;	//제품분류 중 명칭
		private String mtrlCtgr3Nm          ;	//제품분류 소 명칭
		private String setYn                ;	//세트여부
		private String bomYn                ;	//BOM여부
		private String mtoYn                ;	//MTO여부
		private String stdYn                ;	//정규격여부
		
		//20211204 피킹존 추가
		private	String	pickZone;		
		
		private String instYn               ;	//시공여부
		private String instCtgr             ;	//시공카테고리 코드
		private String instCtgrNm           ;	//시공카테고리 명칭
		private String instType             ;	//시공유형코드
		private String instTypeNm           ;	//시공유형명칭
		private String instSeatType         ;	//좌석유형코드
		private String instSeatTypeNm       ;	//좌석유형명칭
		private String instCost             ;	//시공비
		private String instUrl              ;	//시공URL
		private String instImgPath          ;	//시공이미지경로
		private String pstnNm               ;	//위치명칭
		private String dlvyYn               ;	//배송여부
		private String dlvyType             ;	//배송유형코드
		private String dlvyCost             ;	//배송비
		private String ndlvyYn              ;	
		private String thrDlvyYn            ;	//3자배송유무
		private String dlvyLt               ;	
		private String cnvtMtrlCd           ;	//변환상품 코드
		private String cnvtMtrlNm           ;	//변환상품 명칭
		private String cnvtQty              ;	//변환 수량?????????????
		private String cnvtUnit             ;	//변환 유닛??????????
		private String memo                 ;	//비고
		
		private	String	saveGubn		;		


		
	
}