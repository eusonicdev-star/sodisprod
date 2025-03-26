package com.allianceLogistics.alsys.common.Controller;

import com.allianceLogistics.alsys.common.entity.ReportsVO;
import com.allianceLogistics.alsys.common.service.ReportsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    private final ReportsService reportsService;

    @RequestMapping(value = "/report", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(ModelAndView model, HttpServletResponse response, HttpServletRequest request, @Valid ReportsVO reportsVO) throws IOException, JRException, SQLException {
        JasperPrint jasperPrint = reportsService.exportPdfFile(reportsVO);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        out.flush();
        out.close();
    }
}