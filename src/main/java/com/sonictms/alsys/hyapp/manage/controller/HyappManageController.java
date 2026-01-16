package com.sonictms.alsys.hyapp.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.sonictms.alsys.hyapp.ocr.*;

import java.io.IOException;

@Controller
public class HyappManageController {

    OcrClientMvc ocrClientMvc = new OcrClientMvc();

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

    @GetMapping("/hyapp/in_barcode")
    public String hyappInboundBarcode() {
        return "/hyapp/inbound/HyappInboundBarcode";
    }

    @GetMapping("/hyapp/in_ass")
    public String hyappInboundAcc() {
        return "/hyapp/inbound/HyappInboundAcc";
    }

    @GetMapping("/hyapp/out")
    public String hyappPicking() {
        return "hyapp/outbound/HyappPicking";
    }

    @GetMapping("/hyapp/put")
    public String hyappPut() {
        return "hyapp/move/HyappPut";
    }

    @GetMapping("/hyapp/putSave")
    public String hyappPutSave() {
        return "hyapp/move/HyappPutSave";
    }

    @GetMapping("/hyapp/move")
    public String hyappMove() {
        return "hyapp/move/HyappMove";
    }

    @GetMapping("/hyapp/re")
    public String hyappRe() {
        return "hyapp/HyappRe";
    }

    @GetMapping("/hyapp/inv")
    public String hyappInv() {
        return "hyapp/info/HyappInv";
    }

    @GetMapping("/hyapp/inv/mtrl")
    public String hyappInvMtrl() {
        return "hyapp/info/HyappInvMtrl";
    }
}
