package com.allianceLogistics.alsys.erp.erpBarcode.service;

import com.allianceLogistics.alsys.erp.erpBarcode.entity.ErpBarcodeVO;
import com.allianceLogistics.alsys.erp.erpBarcode.mapper.ErpBarcodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ErpBarcodeService {

    private final ErpBarcodeMapper erpBarcodeMapper;

    //왼쪽 마스터공통 불러오기
    public List<ErpBarcodeVO> erpBarcodeLCommList(ErpBarcodeVO erpBarcodeVO) {
        List<ErpBarcodeVO> list = erpBarcodeMapper.erpBarcodeLCommList(erpBarcodeVO);
        return list;
    }

    //오른쪽 상세공통 불러오기
    public List<ErpBarcodeVO> erpBarcodeRComdList(ErpBarcodeVO erpBarcodeVO) {
        List<ErpBarcodeVO> list = erpBarcodeMapper.erpBarcodeRComdList(erpBarcodeVO);
        return list;
    }

    //등록/수정 팝업에서 수정일떄 내용 불러오기
    public ErpBarcodeVO erpBarcodep1ComdSearch(ErpBarcodeVO erpBarcodeVO) {
        erpBarcodeVO = erpBarcodeMapper.erpBarcodep1ComdSearch(erpBarcodeVO);
        return erpBarcodeVO;
    }

    //등록/수정 팝업에서 저장하기
    public ErpBarcodeVO erpBarcodep1ComdSave(ErpBarcodeVO erpBarcodeVO) {
        erpBarcodeVO = erpBarcodeMapper.erpBarcodep1ComdSave(erpBarcodeVO);
        return erpBarcodeVO;
    }

} 