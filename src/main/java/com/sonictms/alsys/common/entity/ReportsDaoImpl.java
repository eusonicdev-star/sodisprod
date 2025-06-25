package com.sonictms.alsys.common.entity;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import static com.sonictms.alsys.common.entity.ReportUtils.*;

@Slf4j
@Repository
public class ReportsDaoImpl {

    private final ResourceLoader resourceLoader;
    private final JdbcTemplate jdbcTemplate;
    private final Map<String, ReportParameterBuilder> parameterBuilders;

    public ReportsDaoImpl(ResourceLoader resourceLoader, @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.resourceLoader = resourceLoader;
        this.jdbcTemplate = jdbcTemplate;
        this.parameterBuilders = initializeParameterBuilders();
    }

    public JasperPrint exportPdfFile(ReportsVO reportsVO) throws SQLException, JRException, IOException {
        String reportName = reportsVO.getReport();

        // JRXML 파일 로드 및 컴파일
        JasperReport jasperReport = loadAndCompileReport(reportName);
        log.info("Report loaded successfully: {}.jrxml", reportName);

        // 파라미터 빌드
        Map<String, Object> parameters = buildParameters(reportsVO);

        // 리포트 생성
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            return JasperFillManager.fillReport(jasperReport, parameters, conn);
        }
    }

    private JasperReport loadAndCompileReport(String reportName) throws IOException, JRException {
        String resourcePath = getReportFilePath(reportName);

        try (InputStream jrxmlStream = resourceLoader.getResource(resourcePath).getInputStream()) {
            return JasperCompileManager.compileReport(jrxmlStream);
        } catch (IOException e) {
            log.error("Failed to load report file: {}", resourcePath, e);
            throw new IOException("리포트 파일을 로드할 수 없습니다: " + reportName, e);
        } catch (JRException e) {
            log.error("Failed to compile report: {}", reportName, e);
            throw new JRException("리포트 컴파일에 실패했습니다: " + reportName, e);
        }
    }

    private Map<String, Object> buildParameters(ReportsVO vo) {
        String reportName = vo.getReport();

        // 정확한 매칭 먼저 시도
        ReportParameterBuilder builder = parameterBuilders.get(reportName);

        if (builder != null) {
            return builder.build(vo);
        }

        // ERP104004 시리즈 리포트 처리 (prefix 매칭)
        if (isErp104004Report(reportName)) {
            return buildErp104004Parameters(vo);
        }

        log.warn("No parameter builder found for report: {}", reportName);
        return new HashMap<>();
    }

    private Map<String, Object> buildErp104004Parameters(ReportsVO vo) {
        Map<String, Object> params = new HashMap<>();
        params.put(Parameters.CMPY_CD, vo.getP1());
        params.put(Parameters.DT_TYPE, vo.getP2());
        params.put(Parameters.FROM_DT, vo.getP3());
        params.put(Parameters.TO_DT, vo.getP4());
        params.put(Parameters.DC_LIST, vo.getP5());
        params.put(Parameters.TEAM_LIST, vo.getP6());
        params.put(Parameters.ZONE_LIST, vo.getP7());
        params.put(Parameters.PICK_GRP_AREA, vo.getP8());
        params.put(Parameters.SAVE_USER, vo.getP9());
        params.put(Parameters.INST_CTGR_LIST, vo.getP10());
        return params;
    }

    private Map<String, ReportParameterBuilder> initializeParameterBuilders() {
        Map<String, ReportParameterBuilder> builders = new HashMap<>();

        // 테스트 리포트
        builders.put(ReportNames.TEST_REPORT, vo -> {
            Map<String, Object> params = new HashMap<>();
            params.put(Parameters.CMPY_CD, vo.getCmpyCd());
            params.put(Parameters.SYS_CD, vo.getSysCd());
            params.put(Parameters.USER_GRNT_CD, vo.getUserGrntCd());
            return params;
        });

        // ERP104003_01 리포트
        builders.put(ReportNames.ERP104003_01_REPORT, vo -> {
            Map<String, Object> params = new HashMap<>();
            params.put(Parameters.CMPY_CD, vo.getCmpyCd());
            params.put(Parameters.SO_ID_LIST, vo.getSoIdList());
            params.put(Parameters.SAVE_USER, vo.getSaveUser());
            return params;
        });

        return builders;
    }

    @FunctionalInterface
    private interface ReportParameterBuilder {
        Map<String, Object> build(ReportsVO vo);
    }
}