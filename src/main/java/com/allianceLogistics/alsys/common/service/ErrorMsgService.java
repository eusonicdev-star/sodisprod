package com.allianceLogistics.alsys.common.service;

import com.allianceLogistics.alsys.common.entity.ErrorMsgVO;
import com.allianceLogistics.alsys.common.mapper.ErrorMsgMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class ErrorMsgService {

    private final ErrorMsgMapper errorMsgMapper;

    public ErrorMsgVO errorSave(ErrorMsgVO errorMsgVO) {
        errorMsgVO = errorMsgMapper.errorSave(errorMsgVO);
        return errorMsgVO;
    }

}