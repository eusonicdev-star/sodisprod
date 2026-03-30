package com.sonictms.alsys.erp.erp105002.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Erp105002ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tblMtrlMId;
    private String mtrlCd;
    private String mtrlNm;
    private String cmpyCd;
    private String agntCd;
    private String useYn;
    private String mtoYn;
}
