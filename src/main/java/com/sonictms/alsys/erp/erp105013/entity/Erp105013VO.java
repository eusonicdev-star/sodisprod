package com.sonictms.alsys.erp.erp105013.entity;


import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp105013VO extends CommonVO {
		
	private static final long serialVersionUID = 1L;

	private String cmpyCd;
	private String whCd;
	private String agntNm;     // 화주사명
	private String agntCd;     // 화주사명
	private String mtrlCd;     // 상품코드
	private String mtrlNm;     // 상품명
	private String mtrlYn;
	private String locCd;      // 로케이션
	private String locId;      // 로케이션
	private String locNm;      // 로케이션
	private String stockQty;   // 수량
	private String paltType;   // 팔렛트구분 (엑셀의 '팔렛트구분' 매핑)
	private String remark;
}