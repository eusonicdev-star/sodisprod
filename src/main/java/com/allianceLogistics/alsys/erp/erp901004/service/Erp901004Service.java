package com.allianceLogistics.alsys.erp.erp901004.service;

import com.allianceLogistics.alsys.erp.erp901004.entity.Erp901004ExcelVO;
import com.allianceLogistics.alsys.erp.erp901004.entity.Erp901004VO;
import com.allianceLogistics.alsys.erp.erp901004.mapper.Erp901004Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp901004Service {

    private final Erp901004Mapper erp901004Mapper;

    //그리드 불러오기
    public List<Erp901004VO> erp901004List(Erp901004VO erp901004VO) {
        List<Erp901004VO> list = erp901004Mapper.erp901004List(erp901004VO);
        return list;
    }


    //팝업 왼쪽 하위 품목 찾기
    public List<Erp901004VO> erp901004p1ComdSrch(Erp901004VO erp901004VO) {
        List<Erp901004VO> list = erp901004Mapper.erp901004p1ComdSrch(erp901004VO);
        return list;
    }

    //팝업  등록/ 수정 하기
    public Erp901004VO erp901004p1ComdSave(Erp901004VO erp901004VO) {
        erp901004VO = erp901004Mapper.erp901004p1ComdSave(erp901004VO);
        return erp901004VO;
    }

    // 정보 찾기
    public List<Erp901004VO> erp901004prntMtrlList(Erp901004VO erp901004VO) {
        List<Erp901004VO> list = erp901004Mapper.erp901004prntMtrlList(erp901004VO);
        return list;
    }


    // 로우삭제
		/*
		public Erp901004VO erp901004p1DelTblBomId(Erp901004VO erp901004VO) { 
			erp901004VO = erp901004Mapper.erp901004p1DelTblBomId(erp901004VO); 
			return erp901004VO; 
		}
*/
    // 엑셀데이터를 json으로 바꾼것을 저장하기
    public Erp901004ExcelVO erp901004p2ExcelUploadJson(Erp901004ExcelVO erp901004ExcelVO) {


        erp901004ExcelVO = erp901004Mapper.erp901004p2ExcelUploadJson(erp901004ExcelVO);

        return erp901004ExcelVO;
    }

} 