package com.sonictms.alsys.common.controller;

import com.sonictms.alsys.common.entity.CommonSrchVO;
import com.sonictms.alsys.common.service.CommonSrchService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/common/api")
public class CommonSrchController {

    private final CommonSrchService commonSrchService;

    public CommonSrchController(CommonSrchService commonSrchService) {
        this.commonSrchService = commonSrchService;
    }

    @GetMapping("/loc/search")
    public List<CommonSrchVO> searchLocation(
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session) {

        String cmpyCd = (String) session.getAttribute("cmpyCd");
        // 세션 방어 로직
        if (cmpyCd == null) cmpyCd = "A";

        CommonSrchVO vo = new CommonSrchVO();
        vo.setCmpyCd(cmpyCd);
        vo.setKeyword(keyword != null ? keyword.trim() : "");

        return commonSrchService.getLocationSearchList(vo);
    }
}