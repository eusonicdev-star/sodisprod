package com.sonictms.alsys.common.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ErpCommonVO extends commonVO {

	private static final long serialVersionUID = 1L;

	private String instMobileMId;    //모바일 고유 아이디
	private String soNoList;        //오더번호(주문번호)
	private String soStatCd;        //변경할 주문상태
	private String delResn;        //삭제사유
	private List<ErpCommonVO> request;

	@SerializedName("DLV_ORD_ID")
	private String dlvOrdId;        //배송주문번호(WINUS)
	@SerializedName("DLV_ORD_ID_SEQ")
	private String dlvOrdIdSeq;    //배송주문SEQ
	@SerializedName("IMG_SND_YN")
	private String imgSndYn;        //이미지여부 Y/N
	@SerializedName("DLV_REQ_DT")
	private String dlvReqDt;        //배송예정일
	@SerializedName("DLV_SET_DT")
	private String dlvSetDt;        //배송완료일
	@SerializedName("SET_DRIVER_ID")
	private String setDriverId;    //지정배송기사
	@SerializedName("SET_DRIVER_TL")
	private String setDriverTl;    //기사전화번호
	@SerializedName("DLV_ORD_STAT")
	private String dlvOrdStat;        //배송상태
	@SerializedName("DLV_ORD_STAT_NM")
	private String dlvOrdStatNm;    //배송상태명
	@SerializedName("ETC2")
	private String etc2;            //취소사유
	@SerializedName("SERIAL_NO")
	private String serialNo;        //시리얼번호
	@SerializedName("DLV_COMMENT")
	private String dlvComment;        //배송메모모바일
	@SerializedName("MEMO")
	private String memo;            //인수증메모
	@SerializedName("LIFTING_WORK_YN")
	private String liftingWorkYn;    //양중여부 Y/N
	@SerializedName("HAPPY_CALL_YN")
	private String happyCallYn;    //해피콜여부 Y/N
	@SerializedName("HAPPY_CALL_MEMO")
	private String happyCallMemo;    //해피콜메모
	@SerializedName("HAPPY_CALL_DT")
	private String happyCallDt;    //해피콜일자
	@SerializedName("MOBILE_COM_DT")
	private String mobileComDt;    //모바일완료일
	@SerializedName("PRINT_RECEIPT_YN")
	private String printReceiptYn;    //인수증출력확인 Y/N
	@SerializedName("MOBILE_COM_ID")
	private String mobileComId;    //모바일완료자

	public Data response;

    @Getter
    @Setter
	@ToString(callSuper = true)
	public class Data {
		@SerializedName("IF_YN")
		private String ifYn;            //성공유무 Y/N
		@SerializedName("IF_CNT")
		private String ifCnt;            //성공 건 수
		@SerializedName("IF_MESSAGE")
		private String ifMessage;        //성공유무,메세지처리
    }
}
