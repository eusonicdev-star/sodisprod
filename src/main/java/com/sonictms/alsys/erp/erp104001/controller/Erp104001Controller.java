package com.sonictms.alsys.erp.erp104001.controller;

import com.sonictms.alsys.erp.erp104001.entity.Erp104001VO;
import com.sonictms.alsys.erp.erp104001.service.Erp104001Service;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp104001Controller {

    private final Erp104001Service erp104001Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp104001"})
    public ModelAndView getErp104001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp104001/erp104001");

        return modelAndView;
    }

    // 공통코드관리 화면 열기
    @PostMapping(value = {"erp104001p1"})
    public ModelAndView erp104001p1(ModelAndView modelAndView, @Valid Erp104001VO erp104001VO) {
        modelAndView.setViewName("erp/erp104001/erp104001p1");
        modelAndView.addObject("sendObject", erp104001VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp104001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp104001VO> erp104001List(Erp104001VO erp104001VO) {
        return erp104001Service.erp104001List(erp104001VO);
    }

    // 이미지 일괄 다운로드
    @RequestMapping(value = {"erp104001DownloadImages"}, method = RequestMethod.POST)
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
