package com.sonictms.alsys.erp.erp102001.controller;

import com.sonictms.alsys.erp.erp102001.entity.Erp102001VO;
import com.sonictms.alsys.erp.erp102001.service.Erp102001Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp102001Controller {

    private final Erp102001Service erp102001Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp102001"})
    public ModelAndView getErp102001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp102001/erp102001");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp102001p1List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102001VO> erp102001p1List(Erp102001VO erp102001VO) {
        return erp102001Service.erp102001p1List(erp102001VO);
    }

    // 주문리스트 선택 팝업 화면 열기
    @PostMapping(value = {"erp102001p1"})
    public ModelAndView erp102001p1(ModelAndView modelAndView, @Valid Erp102001VO erp102001VO) {
        modelAndView.setViewName("erp/erp102001/erp102001p1");
        modelAndView.addObject("sendObject", erp102001VO);
        return modelAndView;
    }

    // 해피콜상세리스트 팝업 화면 열기
    @PostMapping(value = {"erp102001p2"})
    public ModelAndView erp102001p2(ModelAndView modelAndView, @Valid Erp102001VO erp102001VO) {
        modelAndView.setViewName("erp/erp102001/erp102001p2");
        modelAndView.addObject("sendObject", erp102001VO);
        return modelAndView;
    }

    // 알림톡이력보기 팝업 화면 열기
    @PostMapping(value = {"erp102001p3"})
    public ModelAndView erp102001p3(ModelAndView modelAndView, @Valid Erp102001VO erp102001VO) {
        modelAndView.setViewName("erp/erp102001/erp102001p3");
        modelAndView.addObject("sendObject", erp102001VO);
        return modelAndView;
    }

    // 알림톡보내기 팝업 화면 열기
    @PostMapping(value = {"erp102001p4"})
    public ModelAndView erp102001p4(ModelAndView modelAndView, @Valid Erp102001VO erp102001VO) {
        modelAndView.setViewName("erp/erp102001/erp102001p4");
        modelAndView.addObject("sendObject", erp102001VO);
        return modelAndView;
    }

    // 알림톡보내기 팝업 화면 열기
    @PostMapping(value = {"erp102001p5"})
    public ModelAndView erp102001p5(ModelAndView modelAndView, @Valid Erp102001VO erp102001VO) {
        modelAndView.setViewName("erp/erp102001/erp102001p5");
        modelAndView.addObject("sendObject", erp102001VO);
        return modelAndView;
    }

    // 메인화면 주문헤더조회
    @RequestMapping(value = {"erp102001SoHeadSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001SoHeadSrch(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001SoHeadSrch(erp102001VO);
        return erp102001VO;
    }

    // 메인화면 주문상세조회
    @RequestMapping(value = {"erp102001SoDetailSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102001VO> erp102001SoDetailSrch(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Service.erp102001SoDetailSrch(erp102001VO);
        return list;
    }

    // 메인화면 상담내역조회
    @RequestMapping(value = {"erp102001HappyCallSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001HappyCallSrch(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001HappyCallSrch(erp102001VO);
        return erp102001VO;
    }

    // 메인화면 상담정보 등록하기
    @RequestMapping(value = {"erp102001CnslSave"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001CnslSave(@RequestBody Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001CnslSave(erp102001VO);
        return erp102001VO;
    }

    // 메인화면 오더정보 변경한 것 수정하기
    @RequestMapping(value = {"erp102001p1Updt"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001p1Updt(@RequestBody Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001p1Updt(erp102001VO);

        return erp102001VO;
    }

    // 해피콜상담리스트 팝업 조회
    @RequestMapping(value = {"erp102001p2List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102001VO> erp102001p2List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Service.erp102001p2List(erp102001VO);
        return list;
    }

    // 메인화면 알림톡 조회
    @RequestMapping(value = {"erp102001talkCallSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001talkCallSrch(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001talkCallSrch(erp102001VO);
        return erp102001VO;
    }

    // 알림톡 발송할 내용을 불러온다
    @RequestMapping(value = {"erp102001p4LoadAlrmTmp"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001p4LoadAlrmTmp(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001p4LoadAlrmTmp(erp102001VO);
        return erp102001VO;
    }

    // 알림톡발송리스트 팝업 조회
    @RequestMapping(value = {"erp102001p3List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102001VO> erp102001p3List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Service.erp102001p3List(erp102001VO);
        return list;
    }

    // 배송정보 & 이미지
    @RequestMapping(value = {"erp102001soDlvyInfoData"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001soDlvyInfoData(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001soDlvyInfoData(erp102001VO);
        return erp102001VO;
    }

    //배송일 확정
    @RequestMapping(value = {"erp101001DlvyCnfm"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp101001DlvyCnfm(@RequestBody Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp101001DlvyCnfm(erp102001VO);
        return erp102001VO;
    }

    // 주문이력보기 팝업의 리스트조회
    @RequestMapping(value = {"erp102001p5List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102001VO> erp102001p5List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Service.erp102001p5List(erp102001VO);
        return list;
    }

    //20211231 정연호 추가. 제품정보 그리드 영역 수정한것을 저장하기 (전체 말고 그리드 영역만)
    @RequestMapping(value = {"erp102001UpdtGrid"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO erp102001UpdtGrid(@RequestBody Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001UpdtGrid(erp102001VO);
        return erp102001VO;
    }

    // 이미지 일괄 다운로드
    @RequestMapping(value = {"erp102001DownloadImages"}, method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadImages(HttpServletRequest request) {
        try {
            String imageUrlsJson = request.getParameter("imageUrls");
            String imageNamesJson = request.getParameter("imageNames");

            if (imageUrlsJson == null || imageNamesJson == null) {
                return ResponseEntity.badRequest().build();
            }

            // JSON 파싱 (간단한 파싱)
            String[] imageUrls = imageUrlsJson.replace("[", "").replace("]", "").replace("\"", "").split(",");
            String[] imageNames = imageNamesJson.replace("[", "").replace("]", "").replace("\"", "").split(",");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            for (int i = 0; i < imageUrls.length; i++) {
                try {
                    String imageUrl = imageUrls[i].trim();
                    String imageName = imageNames[i].trim();
                    
                    if (!imageUrl.isEmpty() && !imageName.isEmpty()) {
                        URL url = new URL(imageUrl);
                        InputStream inputStream = url.openStream();
                        
                        ZipEntry zipEntry = new ZipEntry(imageName);
                        zos.putNextEntry(zipEntry);
                        
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        
                        inputStream.close();
                        zos.closeEntry();
                    }
                } catch (Exception e) {
                    System.err.println("이미지 다운로드 실패: " + imageUrls[i] + " - " + e.getMessage());
                }
            }

            zos.close();
            baos.close();

            byte[] zipBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "images.zip");
            headers.setContentLength(zipBytes.length);

            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}