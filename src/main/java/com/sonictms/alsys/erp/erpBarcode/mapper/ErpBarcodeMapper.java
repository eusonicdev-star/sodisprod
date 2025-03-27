package com.sonictms.alsys.erp.erpBarcode.mapper;

import com.sonictms.alsys.erp.erpBarcode.entity.ErpBarcodeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ErpBarcodeMapper {


    //왼쪽 마스터 공통코드 내용조회
    List<ErpBarcodeVO> erpBarcodeLCommList(ErpBarcodeVO erp90100VO);

    //오른쪽 상세 공통코드 내용조회
    List<ErpBarcodeVO> erpBarcodeRComdList(ErpBarcodeVO erp90100VO);

    //등록/수정 팝업에서 수정일떄 내용 불러오기
    ErpBarcodeVO erpBarcodep1ComdSearch(ErpBarcodeVO erp90100VO);

    //등록/수정 팝업에서 저장하기
    ErpBarcodeVO erpBarcodep1ComdSave(ErpBarcodeVO erp90100VO);

}