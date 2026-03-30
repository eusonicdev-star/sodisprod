package com.sonictms.alsys.user.entity;

import java.io.Serializable;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//@ToString
@ToString(callSuper=true)

public class Repw extends CommonVO implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String	password;			//암호

	private String	loginId;			//사용자아이디

	private	String	reqNo;				//리턴받을 인증번호
	private	String	phoneNo;			//전화번호

	
	
}