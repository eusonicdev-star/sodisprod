package com.allianceLogistics.alsys.erp.erp100801.controller;

import com.allianceLogistics.alsys.erp.erp100801.entity.Erp100801VO;
import com.allianceLogistics.alsys.erp.erp100801.service.Erp100801Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp100801Controller {

    private final Erp100801Service erp100801Service;

    @GetMapping("erp100801")
    public ModelAndView getErp100801(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp100801/erp100801");
        return modelAndView;
    }

    @GetMapping("erp1008010")
    public ModelAndView getErp1008010(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp100801/erp1008010");
        return modelAndView;
    }

    //20220517 정연호 추가 application에서 값 가져오기
    @Value("${common.java.kppApiSearch}")
    private String kppApiSearch;

    @Value("${common.java.kppApiUsername}")
    private String kppApiUsername;

    @Value("${common.java.kppApiUserpass}")
    private String kppApiUserpass;


    @RequestMapping("kppSearch, method = RequestMethod.POST")
    @ResponseBody
    public Erp100801VO kppSearch(@RequestBody Erp100801VO erp100801VO) {
        disableSslVerification();

        JSONObject data = new JSONObject();

        data.put("DLV_DT_FROM", erp100801VO.getDlvDtFrom());
        data.put("DLV_DT_TO", erp100801VO.getDlvDtTo());

        Erp100801VO vo = null;

        //JSON 데이터 HTTP POST 전송하기
        try {
            String host_url = kppApiSearch;

            HttpURLConnection conn = null;

            URL url = new URL(host_url);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");// POST GET
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // 타입설정(application/json) 형식으로 전송
            conn.setRequestProperty("Accept", "application/json;charset=UTF-8");
            String encodedAuth = "Basic " + Base64Util.encode(kppApiUsername + ":" + kppApiUserpass);

            conn.setRequestProperty("Authorization", encodedAuth);

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            //log.info(data.toString());
            bw.write(data.toString());

            bw.flush();
            bw.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String returnMsg = in.readLine();

            Gson gson = new Gson();

            vo = gson.fromJson(returnMsg, new TypeToken<Erp100801VO>() {
            }.getType());

            // HTTP 응답 코드 수신
            int responseCode = conn.getResponseCode();
            if (responseCode == 400) {
                log.info("     L> Erp100801VO KPP 응답코드 400 : 명령을 실행 오류");
            } else if (responseCode == 500) {
                log.info("     L> Erp100801VO KPP 응답코드 500 : 서버 에러.");
            } else { // 정상 . 200 응답코드 . 기타 응답코드
                log.info("     L> Erp100801VO KPP 응답코드 " + responseCode);
            }
        } catch (IOException ie) {
            log.info("IOException " + ie.getCause());
            ie.printStackTrace();
        } catch (Exception ee) {
            log.info("Exception " + ee.getCause());
            ee.printStackTrace();
        }
        log.info("kpp에서 불러온 데이터 목록 수 : " + vo.getResults().size());
        //여기까지는 kpp가 제공한 ap를 이용하여 kpp의 주문정보를 불러오는것

        //여기부터는 kpp에서 가져온 주문정보를 얼라이언스 디비 테이블에 저장하는
        vo.setSaveUser(erp100801VO.getSaveUser());
        vo.setDlvDtFrom(erp100801VO.getDlvDtFrom());
        vo.setDlvDtTo(erp100801VO.getDlvDtTo());
        vo.setDtType(erp100801VO.getDtType());
        vo.setRefSoNoList(erp100801VO.getRefSoNoList());
        vo.setOutSoNoList(erp100801VO.getOutSoNoList());
        vo.setOutCmpyCd(erp100801VO.getOutCmpyCd());
        vo.setCmpyCd(erp100801VO.getCmpyCd());
        Erp100801VO outCmpySave = erp100801Service.outCmpySave(vo);

        return outCmpySave;
    }

    //외부업체 목록 조회
    @RequestMapping("erp100801OutCmpyList, method = RequestMethod.POST")
    @ResponseBody
    public Erp100801VO erp100801OutCmpyList(@RequestBody Erp100801VO erp100801VO) {
        erp100801VO = erp100801Service.erp100801OutCmpyList(erp100801VO);
        return erp100801VO;
    }

    //데이터 체크
    @RequestMapping("erp100801DataChk, method = RequestMethod.POST")
    @ResponseBody
    public Erp100801VO erp100801DataChk(@RequestBody Erp100801VO erp100801VO) {
        erp100801VO = erp100801Service.erp100801DataChk(erp100801VO);

        //20220517 정연호 추가 강제롤백
        ////log.info("테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
        ////TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return erp100801VO;
    }

    //테이블에 저장된 외부업체(KPP)의 정보를 얼라이언스의 오더로 입력하기 - 화면의 그리드 한줄한줄을 넘겨받아 한번씩 한번씩 입력한다
    @RequestMapping("erp100801AlOrdrSave, method = RequestMethod.POST")
    @ResponseBody
    public Erp100801VO erp100801AlOrdrSave(@RequestBody Erp100801VO erp100801VO) {
        erp100801Service.erp100801AlOrdrSave(erp100801VO);

        //20220517 정연호 추가 강제롤백
        ////log.info("테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
        ////TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return erp100801VO;
    }

    // ssl security Exception 방지
    public void disableSslVerification() {
        // TODO Auto-generated method stub
        log.info("disableSslVerification() 수행 : ssl security Exception 방지");
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}