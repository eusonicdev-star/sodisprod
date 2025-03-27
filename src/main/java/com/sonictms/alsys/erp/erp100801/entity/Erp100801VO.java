package com.sonictms.alsys.erp.erp100801.entity;

import java.util.List;

import com.sonictms.alsys.common.entity.commonVO;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp100801VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	//20220411
	private	String	dlvDtFrom;		//날짜 시작일
	private	String	dlvDtTo;		//날짜 종료일
	private	String	Cnt;			//조회결과 수
	private	String	dtType;			//날짜 조회 종류	
	//private	String	outCmpyCd; 		//외부회사 KPP 코드 KPP컬럼 : DLV_COMP_NM   
	private	String	refSoNoList;	//고객사(외부회사 KPP에게 배송시공의뢰를 한 회사)의 주문번호 리스트
	private	String	outSoNoList;	//외부회사 KPP의 주문번

	private List<Erp100801VO> results;

	@SerializedName("TRUST_CUST_CD")    	private String  trustCustCd;  //화주코드
	@SerializedName("DLV_CUSTOMER_NM")    	private String  dlvCustomerNm;	//수취인명
	@SerializedName("TRN_DLV_ORD_TYPE")    	private String  trnDlvOrdType;	//등록구분
	@SerializedName("BUYED_DT")		    	private String  buyedDt;				//발주일(주문)
	@SerializedName("DLV_IN_REQ_DT")    	private String  dlvInReqDt;				//입고예정일(간선예정일)
	@SerializedName("DLV_SET_DT")			private String  dlvSetDt;    		//지정일      
	@SerializedName("SALES_COMPANY_NM")		private String  salesCompanyNm;    	//판매처
	@SerializedName("BUY_CUST_NM")			private String  buyCustNm;         	//주문자
	@SerializedName("DLV_ZIP")				private String  dlvZip;             //우편번호
	@SerializedName("DLV_ADDR")				private String  dlvAddr;            //
	@SerializedName("DLV_CITY_NM")			private String  dlvCityNm;         
	@SerializedName("DLV_PHONE_1")			private String  dlvPhone1;         
	@SerializedName("DLV_PHONE_2")			private String  dlvPhone2;         
	@SerializedName("DLV_PRODUCT_NM")		private String  dlvProductNm;      
	@SerializedName("DLV_PRODUCT_CD")		private String  dlvProductCd;      
	@SerializedName("TEMP_DLV_PRODUCT_NM")	private String  tempDlvProductNm; 
	@SerializedName("DLV_QTY")				private String  dlvQty;             
	@SerializedName("ORG_ORD_ID")			private String  orgOrdId;          
	@SerializedName("DLV_ORD_SEQ")			private String  dlvOrdSeq;         
	@SerializedName("ETC1")					private String  etc1;                
	@SerializedName("ETC2")					private String  etc2;                
	@SerializedName("DLV_COMP_NM")			private String  dlvCompNm;         
	@SerializedName("REQ_DLV_JOIN_NM")		private String  reqDlvJoinNm;     
	@SerializedName("MSG_SND_YN")			private String  msgSndYn;          
	@SerializedName("MSG_SND_DT")			private String  msgSndDt;          
	@SerializedName("DLV_SET_TYPE_NM")		private String  dlvSetTypeNm;     
	@SerializedName("REQ_DLV_JOIN_TEL")		private String  reqDlvJoinTel;    
	@SerializedName("DLV_ORD_ID")			private String  dlvOrdId;      
	
	@SerializedName("DATA_CHK_YN")			private String  dataChkYn;   
	@SerializedName("DATA_CMPL_YN")			private String  dataCmplYn;   
	@SerializedName("DATA_UPDT_MEMO")		private String  dataUpdtMemo;   
	@SerializedName("AGNT_CD")				private String  agntCd; 		 //KPP컬럼 : CNVT_TRUST_CUST_CD 
	@SerializedName("AGNT_NM")				private String  agntNm;   
	@SerializedName("SO_ORDR_TYPE_CD")		private String  soOrdrTypeCd;   
	@SerializedName("SO_ORDR_TYPE_NM")		private String  soOrdrTypeNm;   
	@SerializedName("MTRL_CD")				private String  mtrlCd;  
	@SerializedName("MTRL_NM")				private String  mtrlNm;   
	@SerializedName("SO_TRANS_YN")			private String  soTransYn;   
	@SerializedName("SO_TRANS_RESN")		private String  soTransResn;   
	@SerializedName("SO_TRANS_TIME")		private String  soTransTime;   
	
	//20220427 추가
	@SerializedName("WMS_IF_FLAG")			private String  wmsIfFlag;   //kpp데이터를 alliance가 IF해 갔냐 여부 
	@SerializedName("DLV_ORD_SUBSEQ")		private String  dlvOrdSubseq;   //kpp쪽 보조순번
	
	//20220625 추가
	@SerializedName("DLV_REQ_DT")			private String  dlvReqDt;	//배송요청일 추가
	@SerializedName("MEMO")					private String  memo;		//인수증 메모
	
	//20220706 정연호 추가. KPP에서 운영에 적용시켜서 갑자기 수정함
	  
	@SerializedName("DLV_PRODUCT_CD1")		private String  dlvProductCd1;   
	@SerializedName("DLV_PRODUCT_NM1")		private String  dlvProductNm1;    
	@SerializedName("DLV_QTY1")				private String  dlvQty1;  

	@SerializedName("DLV_PRODUCT_CD2")		private String  dlvProductCd2;   
	@SerializedName("DLV_PRODUCT_NM2")		private String  dlvProductNm2;    
	@SerializedName("DLV_QTY2")				private String  dlvQty2;  
	
	@SerializedName("DLV_PRODUCT_CD3")		private String  dlvProductCd3;   
	@SerializedName("DLV_PRODUCT_NM3")		private String  dlvProductNm3;    
	@SerializedName("DLV_QTY3")				private String  dlvQty3;  
	
	@SerializedName("DLV_PRODUCT_CD4")		private String  dlvProductCd4;   
	@SerializedName("DLV_PRODUCT_NM4")		private String  dlvProductNm4;    
	@SerializedName("DLV_QTY4")				private String  dlvQty4;  
	
	@SerializedName("DLV_PRODUCT_CD5")		private String  dlvProductCd5;   
	@SerializedName("DLV_PRODUCT_NM5")		private String  dlvProductNm5;    
	@SerializedName("DLV_QTY5")				private String  dlvQty5;  
	
	@SerializedName("DLV_PRODUCT_CD6")		private String  dlvProductCd6;   
	@SerializedName("DLV_PRODUCT_NM6")		private String  dlvProductNm6;    
	@SerializedName("DLV_QTY6")				private String  dlvQty6;  
	
	@SerializedName("DLV_PRODUCT_CD7")		private String  dlvProductCd7;   
	@SerializedName("DLV_PRODUCT_NM7")		private String  dlvProductNm7;    
	@SerializedName("DLV_QTY7")				private String  dlvQty7;  
	
	@SerializedName("DLV_PRODUCT_CD8")		private String  dlvProductCd8;   
	@SerializedName("DLV_PRODUCT_NM8")		private String  dlvProductNm8;    
	@SerializedName("DLV_QTY8")				private String  dlvQty8;  
	
	@SerializedName("DLV_PRODUCT_CD9")		private String  dlvProductCd9;   
	@SerializedName("DLV_PRODUCT_NM9")		private String  dlvProductNm9;    
	@SerializedName("DLV_QTY9")				private String  dlvQty9;  
	
	
	private	String	no;
	
	private String 	brand		;          //	KPP컬럼 : BUY_CUST_NM	
	private String 	ordrInptDt	;          //	KPP컬럼 :  X	
	private String 	rcptDt		;          //	KPP컬럼 :  BUYED_DT  	
	private String 	rqstDt		;          //	KPP컬럼 :  DLV_IN_REQ_DT	
	private String 	dlvyCnfmDt	;          //	KPP컬럼 :  X	
	private String 	acptEr		;          //	KPP컬럼 : DLV_CUSTOMER_NM	
	private String 	ordrEr		;          //	KPP컬럼 : BUY_CUST_NM	
	private String 	acptTel1	;          //	KPP컬럼 : DLV_PHONE_1	
	private String 	acptTel2	;          //	KPP컬럼 : DLV_PHONE_2	
	private String 	postCd		;          //	KPP컬럼 : CNVT_DLV_ZIP	
	private String 	addr1		;          //	KPP컬럼 : CNVT_DLV_ADDR	
	private String 	addr2		;          //	KPP컬럼 : X	
	private String 	dlvyRqstMsg	;          //	KPP컬럼 : ETC1 	
	private String 	custSpclTxt	;          //	KPP컬럼 : ETC2	
	private String 	costType	;          //	KPP컬럼 : X	
	private String 	rcptCost	;          //	KPP컬럼 : X	
	private String 	passCost	;          //	KPP컬럼 : X	
	private String 	prodCd		;          //	KPP컬럼 : CNVT_DLV_PRODUCT_CD	
	private String 	prodNm		;          //	KPP컬럼 :	CNVT_DLV_PRODUCT_NM
	private String 	qty			;          //	KPP컬럼 : DLV_QTY	
	private String 	dcCd		;          //	KPP컬럼 : X	
	//common private String 	useYn	;          //	사용유무	
	//private String 	memo		;          //	메모				//위에(20220625) 인수증 메모를 추가하여 이부분을 주석처리함 
	private String 	inptSys		;          //	고정값 :  'KPP_IF'	
	
	private	String	refSoNo;	//화주사주문번호	KPP컬럼 : ORG_ORD_ID    

	private	String	soNo;	//오더번호
	private	String	excelRow;		//로우 순번
	private	String	exclRow;		//로우 순번
	private	String	orgnSoNo;	//원 오더번호	KPP컬럼 : DLV_ORD_ID
	private	String	soType;	//오더유형	KPP컬럼 : CNVT_TRN_DLV_ORD_TYPE_CD
	private	String	soStatCd;	//오더 상태	고정값 :  '1200' -주문확정 	
	private	String	mallCd;		 //KPP컬럼 : SALES_COMPANY_NM
	
	private	String	alSoNo;		//생성된al오더번호
	private	String	cnvtTrustCustCd;	//화주코드 얼라이언스 
	private	String	cnvtTrustCustNm;	//화주명칭 얼라이언스
	
	private	String	cnvtTrnDlvOrdTypeCd;	//오더유형코드 얼라이언스 
	private	String	cnvtTrnDlvOrdTypeNm;	//오더유형명칭 얼라이언스 
	
	private	String	cnvtDlvZip;
	private	String	cnvtDlvAddr;
	
	private	String	cnvtDlvProductCd;
	private	String	cnvtDlvProductNm;
	
	private	String	transChkYn;		//Y 미전송만 보기 N 전체보기
		
	//20220427 정연호 추가 .
	private	String	ifId;				//인터페이스 한 아이디 
}
