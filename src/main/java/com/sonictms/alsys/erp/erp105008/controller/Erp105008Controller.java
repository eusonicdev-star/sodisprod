package com.sonictms.alsys.erp.erp105008.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import com.sonictms.alsys.erp.erp105008.service.Erp105008Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp105008Controller {

    private final Erp105008Service erp105008Service;
    private final ObjectMapper objectMapper;

    // 출고대기리스트 화면 열기
    @GetMapping(value = {"erp105008"})
    public ModelAndView getErp105008(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105008/erp105008");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp105008List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105008VO> erp105008List(Erp105008VO erp105008VO) {
        log.info("출고대기 리스트 조회: {}", erp105008VO);
        List<Erp105008VO> list = erp105008Service.getOutboundWaitList(erp105008VO);
        return list;
    }

    @RequestMapping(value = {"erp105008ProcessCourierOutbound"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> processCourierOutbound(Erp105008VO erp105008VO, HttpSession session) {
        log.info("택배 출고 처리 요청: {}", erp105008VO);

        // 세션에서 사용자 정보 가져오기
        String userName = (String) session.getAttribute("userName");
        if (userName == null || userName.isEmpty()) {
            userName = "SYSTEM";
        }
        erp105008VO.setProcessUser(userName);

        return erp105008Service.processCourierOutbound(erp105008VO);
    }

    /**
     * 업체직출 출고 처리 (단건)
     */
    @RequestMapping(value = {"erp105008ProcessDirectOutbound"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> processDirectOutbound(Erp105008VO erp105008VO, HttpSession session) {
        log.info("업체직출 출고 처리 요청: {}", erp105008VO);

        // 세션에서 사용자 정보 가져오기
        String userName = (String) session.getAttribute("userName");
        if (userName == null || userName.isEmpty()) {
            userName = "SYSTEM";
        }
        erp105008VO.setProcessUser(userName);

        return erp105008Service.processDirectOutbound(erp105008VO);
    }

    /**
     * 일괄 출고 처리 (택배 다중 처리 지원)
     * JSON 배열로 여러 건의 출고 정보를 받아서 처리
     */
    @RequestMapping(value = {"erp105008BatchOutbound"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> batchOutbound(@RequestBody String jsonData, HttpSession session) {
        log.info("일괄 출고 처리 요청: {}", jsonData);

        try {
            // 세션에서 사용자 정보 가져오기
            String userName = (String) session.getAttribute("userName");
            if (userName == null || userName.isEmpty()) {
                userName = "SYSTEM";
            }

            // JSON 파싱
            List<Erp105008VO> outboundList = objectMapper.readValue(jsonData, new TypeReference<List<Erp105008VO>>() {
            });

            // 각 VO에 사용자 정보 설정
            for (Erp105008VO vo : outboundList) {
                vo.setProcessUser(userName);
            }

            return erp105008Service.processBatchOutbound(outboundList);

        } catch (Exception e) {
            log.error("일괄 출고 처리 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("rtnYn", "N");
            errorResult.put("rtnMsg", "일괄 출고 처리 중 오류 발생: " + e.getMessage());
            return errorResult;
        }
    }
}
