package com.sonictms.alsys.erp.erp105010.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonictms.alsys.erp.erp105010.entity.Erp105010VO;
import com.sonictms.alsys.erp.erp105010.service.Erp105010Service;
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
public class Erp105010Controller {

    private final Erp105010Service erp105010Service;
    private final ObjectMapper objectMapper;

    /**
     * 택배/직출 출고관리 화면 열기
     */
    @GetMapping(value = {"erp105010"})
    public ModelAndView getErp105010(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105010/erp105010");
        return modelAndView;
    }

    /**
     * 택배/직출 리스트 조회
     */
    @RequestMapping(value = {"erp105010List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105010VO> erp105010List(Erp105010VO erp105010VO) {
        log.info("택배/직출 리스트 조회: {}", erp105010VO);
        List<Erp105010VO> list = erp105010Service.getCourierDirectList(erp105010VO);
        log.info("조회 결과 건수: {}", list != null ? list.size() : 0);
        return list;
    }

    /**
     * 일괄 출고 처리 (택배 + 업체직출 혼합 가능)
     * MTO인 경우 재고 차감하지 않음
     */
    @RequestMapping(value = {"erp105010BatchOutbound"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> batchOutbound(@RequestBody String jsonData, HttpSession session) {
        log.info("일괄 출고 처리 요청: {}", jsonData);

        try {
            // 세션에서 사용자 PK 가져오기 (TBL_USER_M_ID)
            Object tblUserMIdObj = session.getAttribute("tblUserMId");
            String tblUserMId = (tblUserMIdObj != null) ? tblUserMIdObj.toString() : "0";

            // JSON 파싱
            List<Erp105010VO> outboundList = objectMapper.readValue(jsonData, new TypeReference<List<Erp105010VO>>() {
            });

            // 각 VO에 사용자 PK 설정
            for (Erp105010VO vo : outboundList) {
                vo.setProcessUser(tblUserMId);
            }

            return erp105010Service.processBatchOutbound(outboundList);

        } catch (Exception e) {
            log.error("일괄 출고 처리 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("rtnYn", "N");
            errorResult.put("rtnMsg", "일괄 출고 처리 중 오류 발생: " + e.getMessage());
            return errorResult;
        }
    }
}
