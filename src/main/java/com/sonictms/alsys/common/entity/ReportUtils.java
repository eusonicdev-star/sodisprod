package com.sonictms.alsys.common.entity;

import lombok.experimental.UtilityClass;

/**
 * 리포트 관련 유틸리티 클래스
 */
@UtilityClass
public class ReportUtils {
    
    /**
     * 리포트 관련 상수
     */
    public static final class ReportNames {
        public static final String TEST_REPORT = "test";
        public static final String ERP104003_01_REPORT = "erp104003_01";
        public static final String ERP104004_PREFIX = "erp104004_";
        
        private ReportNames() {}
    }
    
    /**
     * 리포트 파라미터 관련 상수
     */
    public static final class Parameters {
        public static final String CMPY_CD = "I_CMPY_CD";
        public static final String SYS_CD = "I_SYS_CD";
        public static final String USER_GRNT_CD = "I_USER_GRNT_CD";
        public static final String SO_ID_LIST = "I_SO_ID_LIST";
        public static final String SAVE_USER = "I_SAVE_USER";
        public static final String DT_TYPE = "I_DT_TYPE";
        public static final String FROM_DT = "I_FROM_DT";
        public static final String TO_DT = "I_TO_DT";
        public static final String DC_LIST = "I_DC_LIST";
        public static final String TEAM_LIST = "I_TEAM_LIST";
        public static final String ZONE_LIST = "I_ZONE_LIST";
        public static final String PICK_GRP_AREA = "I_PICK_GRP_AREA";
        public static final String INST_CTGR_LIST = "I_INST_CTGR_LIST";
        
        private Parameters() {}
    }
    
    /**
     * ERP104004 시리즈 리포트인지 확인
     */
    public static boolean isErp104004Report(String reportName) {
        return reportName != null && reportName.startsWith(ReportNames.ERP104004_PREFIX);
    }
    
    /**
     * 리포트 파일 경로 생성
     */
    public static String getReportFilePath(String reportName) {
        return String.format("classpath:reports/%s.jrxml", reportName);
    }
} 