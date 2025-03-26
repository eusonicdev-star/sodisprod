package com.allianceLogistics.alsys.erp.erpBarcode.entity;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class ErpBarcodeVO extends commonVO{
		
	private static final long serialVersionUID = 1L;
	
	private	String	seq					;	//순번
	
	
}