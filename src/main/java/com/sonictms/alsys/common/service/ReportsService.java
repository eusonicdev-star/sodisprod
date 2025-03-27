package com.sonictms.alsys.common.service;

import com.sonictms.alsys.common.entity.ReportsDaoImpl;
import com.sonictms.alsys.common.entity.ReportsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReportsService {

    private final ReportsDaoImpl reportsDao;

    public JasperPrint exportPdfFile(ReportsVO reportsVO) throws SQLException, JRException, IOException {

        return reportsDao.exportPdfFile(reportsVO);
    }
}