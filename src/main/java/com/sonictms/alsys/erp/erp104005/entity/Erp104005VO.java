package com.sonictms.alsys.erp.erp104005.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Erp104005VO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cmpyCd			;
	private String soNo				;
	private String refSoNo			;
	private String acptEr			;
	private String soStatCd			;
	private String soStatNm			;
	private String updtEventTxt		;
	private String updtBeforeTxt	;
	private String updtAfterTxt		;
	private String saveUser			;
	private String saveUserNm		;
	private String saveDate			;
}