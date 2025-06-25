package com.sonictms.alsys.common.controller;

import com.sonictms.alsys.common.entity.ReportsVO;
import com.sonictms.alsys.common.service.ReportsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class ReportsController {

    private static final String CONTENT_TYPE = "application/pdf";

    private final ReportsService reportsService;

    @RequestMapping(value = "/report", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(HttpServletResponse response, @Valid ReportsVO reportsVO) throws IOException, JRException, SQLException {
        JasperPrint jasperPrint = reportsService.exportPdfFile(reportsVO);
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "inline");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        out.flush();
        out.close();
    }
}