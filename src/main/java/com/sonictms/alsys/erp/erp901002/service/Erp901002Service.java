package com.sonictms.alsys.erp.erp901002.service;


import com.sonictms.alsys.erp.erp901002.entity.Erp901002ExcelVO;
import com.sonictms.alsys.erp.erp901002.entity.Erp901002VO;
import com.sonictms.alsys.erp.erp901002.mapper.Erp901002Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp901002Service {

    private final Erp901002Mapper erp901002Mapper;

    //그리드 불러오기
    public List<Erp901002VO> erp901002List(Erp901002VO erp901002VO) {
        List<Erp901002VO> list = erp901002Mapper.erp901002List(erp901002VO);
        return list;
    }

    //팝업 등록/ 수정 하기
    public Erp901002VO erp901002p1ComdSave(Erp901002VO erp901002VO) {
        erp901002VO = erp901002Mapper.erp901002p1ComdSave(erp901002VO);
        return erp901002VO;
    }


    //고유 ID 로 정보 찾기
    public Erp901002VO erp901002p1mtrlSrch(Erp901002VO erp901002VO) {
        erp901002VO = erp901002Mapper.erp901002p1mtrlSrch(erp901002VO);
        return erp901002VO;
    }


    // 엑셀데이터를 json으로 바꾼것을 저장하기
    public Erp901002ExcelVO erp901002p2ExcelUploadJson(Erp901002ExcelVO erp901002ExcelVO) {

        erp901002ExcelVO = erp901002Mapper.erp901002p2ExcelUploadJson(erp901002ExcelVO);

        return erp901002ExcelVO;
    }


} 