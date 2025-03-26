package com.allianceLogistics.alsys.common.entity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
@Slf4j
@Transactional
@Repository
public class ReportsDaoImpl {

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	public JasperPrint exportPdfFile(ReportsVO reportsVO) throws SQLException, JRException, IOException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		String path = resourceLoader.getResource("classpath:reports/"+reportsVO.getReport()+".jrxml").getURI().getPath();
		log.info("Report : " + reportsVO.getReport()+".jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(path);
	
		Map<String, Object> parameters = new HashMap<String, Object>();
	  
		if(reportsVO.getReport().equals("test"))
		{
			  parameters.put("I_CMPY_CD", reportsVO.getCmpyCd()); 
			  parameters.put("I_SYS_CD", reportsVO.getSysCd());
			  parameters.put("I_USER_GRNT_CD", reportsVO.getUserGrntCd());
		}	
		if(reportsVO.getReport().equals("erp104003_01"))
		{
			  parameters.put("I_CMPY_CD", reportsVO.getCmpyCd()); 
			  parameters.put("I_SO_ID_LIST", reportsVO.getSoIdList());
			  parameters.put("I_SAVE_USER", reportsVO.getSaveUser());
		}	
		
		//erp104004 픽패킹리스트 01팀별,02기사별,03권역별,04지역별
		//20220106 정연호 그룹별 레포트 추가 erp104004_05
		String [] erp104004	= {"erp104004_01","erp104004_02","erp104004_03","erp104004_04","erp104004_05"};	
		Set<String> erp104004Report = new HashSet<String>(Arrays.asList(erp104004));
	
		if(erp104004Report.contains(reportsVO.getReport()))
		{
			parameters.put("I_CMPY_CD"		, reportsVO.getP1()); 
			parameters.put("I_DT_TYPE"		, reportsVO.getP2()); 
			parameters.put("I_FROM_DT"		, reportsVO.getP3()); 
			parameters.put("I_TO_DT"		, reportsVO.getP4()); 
			parameters.put("I_DC_LIST"		, reportsVO.getP5()); 
			parameters.put("I_TEAM_LIST"	, reportsVO.getP6()); 
			parameters.put("I_ZONE_LIST"	, reportsVO.getP7()); 
			parameters.put("I_PICK_GRP_AREA", reportsVO.getP8()); 
			parameters.put("I_SAVE_USER"	, reportsVO.getP9()); 
			//20220106 정연호 시공카테고리 추가
			parameters.put("I_INST_CTGR_LIST"	, reportsVO.getP10()); 
		}	
		
		
		JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
	
		conn.close();
		return print;
	}
}