package com.sonictms.alsys.hyapp;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.sonictms.alsys.hyapp.ocr.*;

import java.io.File;
import java.io.IOException;

@Controller
public class HyappController {

    OcrClientMvc ocrClientMvc = new OcrClientMvc();

    @GetMapping("/sodisW")
    public String hyappDownload() {
        return "hyapp/HyappDownload";
    }

    @GetMapping("/hyapp/index")
    public String hyappIndex() {
        return "hyapp/HyappIndex";
    }

    @GetMapping("/hyapp/login")
    public String hyappLogin() {
        return "hyapp/HyappLogin";
    }

    @GetMapping("/hyapp/main")
    public String hyappMain() {
        return "hyapp/HyappMain";
    }

    @GetMapping("/hyapp/scan")
    public String hyappScan() {
        return "hyapp/HyappScan";
    }


    @GetMapping("/hyapp/ocr")
    public String hyappOcr() {
        return "hyapp/HyappOcr";
    }

    @PostMapping("/api/ocr")
    public String ocr(@RequestParam("file") MultipartFile file) throws IOException {
        return ocrClientMvc.ocr(file.getBytes());
    }

    @GetMapping("/hyapp/in")
    public String hyappInboundMain() {
        return "/hyapp/inbound/HyappInboundMain";
    }

    @GetMapping("/hyapp/in_manual_reg")
    public String hyappInboundReg() {
        return "/hyapp/inbound/HyappInboundReg";
    }

    @GetMapping("/hyapp/in_barcode")
    public String hyappInboundBarcode() {
        return "/hyapp/inbound/HyappInboundBarcode";
    }

    @GetMapping("/hyapp/in_vehicle")
    public String hyappInboundVehicle() {
        return "/hyapp/inbound/HyappInboundVehicle";
    }

    @GetMapping("/hyapp/in_ass")
    public String hyappInboundAcc() {
        return "/hyapp/inbound/HyappInboundAcc";
    }

    @GetMapping("/hyapp/out")
    public String hyappOutboundMain() {
        return "hyapp/outbound/HyappOutboundMain";
    }

    @GetMapping("/hyapp/outcancle")
    public String hyappOutCancleMain() {
        return "hyapp/outbound/HyappOutCancleMain";
    }

    @GetMapping("/hyapp/out/detail")
    public String hyappOutboundDetail() {
        return "hyapp/outbound/HyappOutboundDetail";
    }

    @GetMapping("/hyapp/put")
    public String hyappPut() {
        return "hyapp/move/HyappPutaway";
    }

    @GetMapping("/hyapp/putSave")
    public String hyappPutSave() {
        return "hyapp/move/HyappPutSave";
    }

    @GetMapping("/hyapp/move")
    public String hyappMove() {
        return "hyapp/move/HyappMove";
    }

    @GetMapping("/hyapp/restock")
    public String hyappRestock() {
        return "hyapp/restock/HyappRestockMain";
    }

    @GetMapping("/hyapp/restock/acc")
    public String hyappRestockAcc() {
        return "hyapp/restock/HyappRestockAcc";
    }

    @GetMapping("/hyapp/restock/put")
    public String hyappRestockPut() {
        return "hyapp/restock/HyappRestockPut";
    }

    @GetMapping("/hyapp/return")
    public String hyappReturn() {
        return "hyapp/restock/HyappReturn";
    }

    @GetMapping("/hyapp/inv")
    public String hyappInv() {
        return "hyapp/info/HyappInv";
    }

    @GetMapping("/hyapp/inv/mtrl")
    public String hyappInvMtrl() {
        return "hyapp/info/HyappInvMtrl";
    }

    @GetMapping("/hyapp/inv/loc")
    public String hyappInvLocation() {
        return "hyapp/info/HyappInvLocation";
    }

    @GetMapping("/hyapp/inv/cust")
    public String hyappInvCust() {
        return "hyapp/info/HyappInvCust";
    }

    @GetMapping("/hyapp/inv/history")
    public String hyappInvHistory() {
        return "hyapp/info/HyappInvHistory";
    }

    @GetMapping("/hyapp/check")
    public String hyappInvCheckMain() {
        return "hyapp/manage/HyappInvCheckMain";
    }

    @GetMapping("/hyapp/check/reg")
    public String hyappInvCheckReg() {
        return "hyapp/manage/HyappInvCheckReg";
    }

    @GetMapping("/hyapp/check/hist")
    public String hyappInvCheckHist() {
        return "hyapp/manage/HyappInvCheckHist";
    }

    @GetMapping("/hyapp/printTest")
    public String hyappPrintTest() {
        return "hyapp/print/BarcodeTest";
    }

    @GetMapping("/labelPrint")
    public String hyappLabelPrint() {
        return "hyapp/print/LabelPrint";
    }

    // 존(zone) 화면 열기
    @GetMapping("/zone")
    public String getZone() { return "wh/zone"; }

    // 로케이션 화면 열기
    @GetMapping("/location")
    public String getLocation() { return "wh/location"; }

    @PostMapping("/commLocSrch")
    public String getCommLocSrch() { return "commCode/commLocSrch"; }

    @GetMapping("/hyapp/notice")
    public String hyappNotice() {
        return "hyapp/info/HyappNotice";
    }

}
