package com.sonictms.alsys.hyapp.info.controller;

import com.sonictms.alsys.hyapp.info.dto.HyappInvMtrlDetailResponse;
import com.sonictms.alsys.hyapp.info.service.HyappInvService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/hyapp/api")
public class HyappInvController {

    private final HyappInvService hyappInvService;

    public HyappInvController(HyappInvService hyappInvService) {
        this.hyappInvService = hyappInvService;
    }

    @GetMapping("/inv/mtrl/detail")
    public HyappInvMtrlDetailResponse getMtrlDetail(
            @RequestParam(required = false) String mtrlCd,
            @RequestParam(required = false) String mtrlNm,
            @RequestParam(required = false) String barcode
    ) {
        if (StringUtils.isBlank(mtrlCd)
                && StringUtils.isBlank(mtrlNm)
                && StringUtils.isBlank(barcode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "검색조건이 없습니다.");
        }
        return hyappInvService.getMtrlDetail(mtrlCd, mtrlNm, barcode);
    }
}
