package com.sonictms.alsys.erp.erp101004.service;

import com.sonictms.alsys.erp.erp101004.entity.Erp101004VO;
import com.sonictms.alsys.erp.erp101004.mapper.Erp101004Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp101004Service {

    private final Erp101004Mapper erp101004Mapper;

    // 그리드 헤더 조회하기
    public List<Erp101004VO> gridHeaderSrch101004(Erp101004VO erp101004VO) {
        return erp101004Mapper.gridHeaderSrch101004(erp101004VO);
    }

    // 엑셀데이터를 json으로 바꾼것을 체크하기
    public Erp101004VO erp101004ExcelUploadCheck(Erp101004VO erp101004VO) {
        erp101004VO = erp101004Mapper.erp101004ExcelUploadCheck(erp101004VO);

        return erp101004VO;
    }


    // 엑셀데이터를 json으로 바꾼것을 저장하기
    public Erp101004VO erp101004ExcelUploadJson(Erp101004VO erp101004VO) {
        erp101004VO = erp101004Mapper.erp101004ExcelUploadJson(erp101004VO);

        return erp101004VO;
    }
}
