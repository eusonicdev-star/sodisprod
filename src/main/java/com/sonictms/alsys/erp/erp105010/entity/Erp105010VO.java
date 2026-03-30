package com.sonictms.alsys.erp.erp105010.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Erp105010VO extends CommonVO {
    
    private static final long serialVersionUID = 1L;
    
    // 조회 조건
    private String cmpyCd;
    private String dtType;
    private String fromDt;
    private String toDt;
    private String agntList;
    private String dcList;
    private String soList;
    private String soTypeList;
    private String acptNm;
    private String prodList;
    private String prodNm;
    private String outboundStatus; // ALL: 전체, WAIT: 미출고, DONE: 출고완료
    
    // 조회 결과
    private Long tblSoPId;
    private Long tblSoMId;
    private String agntNm;
    private String agntCd;
    private String soTypeNm;
    private String soType;
    private String soStatNm;
    private String soStatCd;
    private String dcCd;
    private String dcNm;
    private String dlvyCnfmDt;
    private String giLiftDt;
    private String liftCmplDt;
    private String soNo;
    private String refSoNo;
    private String acptEr;
    private String paltNoxx;
    private String instEr;
    private String dlvyStatNm;
    private String cprodCd;
    private String cprodNm;
    private Integer cqty;
    private String mtoYn; // MTO 여부 (Y: MTO, N: 일반)
    
    // 출고 정보 (기존 데이터 표시)
    private String courierCd;
    private String courierNm;
    private String waybillNo;
    private String outboundDt;
    private String outboundUser;
    
    // 출고 처리용
    private String selectedIds; // 쉼표 구분 TBL_SO_P_ID 목록
    private String processUser;
    private String processType; // COURIER: 택배, DIRECT: 업체직출
    
    // 재고 관련
    private String mtrlCd;
    private Integer qty;
    private Long tblMtrlMId;
    private Integer currentQty;
    private Integer afterQty;
}
