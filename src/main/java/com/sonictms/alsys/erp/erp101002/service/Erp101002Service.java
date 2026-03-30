package com.sonictms.alsys.erp.erp101002.service;

import com.sonictms.alsys.erp.erp101002.entity.Erp101002VO;
import com.sonictms.alsys.erp.erp101002.mapper.Erp101002Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp101002Service {

    private final Erp101002Mapper erp101002Mapper;

    // 그리드 헤더 조회하기
    public List<Erp101002VO> gridHeaderSrch(Erp101002VO erp101002VO) {
        return erp101002Mapper.gridHeaderSrch(erp101002VO);
    }

    // 엑셀데이터를 json으로 바꾼것을 체크하기
    public Erp101002VO erp101002ExcelUploadCheck(Erp101002VO erp101002VO) {
        erp101002VO = erp101002Mapper.erp101002ExcelUploadCheck(erp101002VO);

        return erp101002VO;
    }


    // 엑셀데이터를 json으로 바꾼것을 저장하기
    public Erp101002VO erp101002ExcelUploadJson(Erp101002VO erp101002VO) {
        erp101002VO = erp101002Mapper.erp101002ExcelUploadJson(erp101002VO);

        return erp101002VO;
    }
}
