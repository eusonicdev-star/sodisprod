package com.sonictms.alsys.commCode.entity;



import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString

public class Button1VO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String type;
	private String url_mobile;

}