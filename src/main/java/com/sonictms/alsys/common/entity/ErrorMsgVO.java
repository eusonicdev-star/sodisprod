package com.sonictms.alsys.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ErrorMsgVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String cmpyCd;
    private String tblUserMId;
    private String msg;
    private String saveTime;
    private String system;
}