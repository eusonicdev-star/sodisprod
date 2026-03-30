package com.sonictms.alsys.hyapp.common.service;

import javax.print.*;

import org.springframework.stereotype.Service;

@Service
public class UsbBarcodeService {

    public void printLabel(String zpl) throws Exception {
        // 이미지에서 확인된 정확한 프린터 이름
        String printerName = "ZDesigner ZD421-203dpi ZPL";

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
        PrintService myService = null;

        for (PrintService service : services) {
            if (service.getName().equals(printerName)) {
                myService = service;
                break;
            }
        }

        if (myService != null) {
            DocPrintJob job = myService.createPrintJob();
            // 한글 인코딩을 고려하여 EUC-KR로 변환
            byte[] bytes = zpl.getBytes("EUC-KR");
            Doc doc = new SimpleDoc(bytes, flavor, null);
            job.print(doc, null);
        } else {
            throw new Exception("프린터를 찾을 수 없습니다. 이름 및 연결을 확인하세요.");
        }
    }
}