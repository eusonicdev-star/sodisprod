package com.sonictms.alsys.hyapp.print.service;

import com.sonictms.alsys.hyapp.print.entity.PrintQueueVO;
import com.sonictms.alsys.hyapp.print.mapper.BarcodePrintMapper;
import com.sonictms.alsys.hyapp.common.service.UsbBarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BarcodePrintService {
    @Autowired private BarcodePrintMapper mapper;
    @Autowired private UsbBarcodeService usbService;

    // 대기 목록을 Map 리스트로 반환
    public List<Map<String, Object>> getPendingListAsMap() {
        return mapper.selectPendingList().stream().map(vo -> {
            Map<String, Object> map = new HashMap<>();
            map.put("SEQ", vo.getSeq());
            map.put("AGNT_NM", vo.getAgntNm());
            map.put("MTRL_NM", vo.getMtrlNm());
            map.put("MTRL_CD", vo.getMtrlCd());
            map.put("BARCODE", vo.getBarcode());
            map.put("QTY", vo.getQty());
            return map;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void executePrintBySeq(int seq) throws Exception {
        PrintQueueVO vo = mapper.selectPrintInfoBySeq(seq);
        if (vo == null) throw new Exception("데이터 없음");

        // --- 상품명 가공 로직 ---
        String agntNm = vo.getAgntNm() != null ? vo.getAgntNm().trim() : "";
        String mtrlNm = vo.getMtrlNm() != null ? vo.getMtrlNm().trim() : "";
        String finalTitle;

        if (!agntNm.isEmpty() && mtrlNm.contains(agntNm)) {
            // 상품명에 화주명이 포함된 경우 제거 후 결합
            String cleanedNm = mtrlNm.replace(agntNm, "").replace("[]", "").replace("()", "").trim();
            finalTitle = "[" + agntNm + "] " + cleanedNm;
        } else {
            finalTitle = agntNm.isEmpty() ? mtrlNm : "[" + agntNm + "] " + mtrlNm;
        }

        // 기본 폰트 크기 설정
        int fontSize = 30;
        int titleBytes = finalTitle.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
        System.out.println(titleBytes);
        if (titleBytes >= 120) {
            fontSize = 27; // 기존 35에서 28로 축소
        }

        // 바코드 가로폭을 키우고(BY3), 위치를 조정한 ZPL
        String zpl = "^XA^MTT^MD15" +
                "^PW560" +                         // 가로 70mm
                "^LL360" +                         // 세로 45mm
                "^SEE:UHANGUL.DAT^FS" +
                "^CW1,E:KFONT3.FNT^CI28^FS" +

                // 1. 상품명: 왼쪽 정렬(L), 줄간격 12, 시작점 Y=20
                "^FO20,20^FB520,6,6,L^A1N," + fontSize + "," + fontSize + "^FH^FD" + encodeZplUtf8(finalTitle) + "^FS" +

                // 2. 바코드: Y축을 180으로 내려서 상품명 3~4줄과 겹치지 않게 조정
                // 가로 중앙 배치를 위해 X=50으로 소폭 이동
                "^FO50,180^BY3,3,90^BCN,90,Y,N,N^FD" + vo.getBarcode() + "^FS" +

                // 3. 상품코드: 라벨 최하단에 가깝게 Y=320으로 조정
                // 위아래 빈 공간을 줄이기 위해 FO 위치를 최대한 아래로 설정
                "^FO20,320^FB520,1,0,C^A1N,35,35^FD" + vo.getMtrlCd() + "^FS" +

                "^PQ" + vo.getQty() + ",1,1,Y^FS^XZ";

        // [수정 포인트] 성공한 파일 구조에 맞게 생성된 zpl 데이터를 인자로 전달합니다.
        usbService.printLabel(zpl);

        // 상태 업데이트
        mapper.updatePrintStatus(seq, "1", null);
    }

    public String encodeZplUtf8(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytes = str.getBytes("UTF-8");
            for (byte b : bytes) {
                // 바이트 값을 _XX 형태의 16진수로 변환
                sb.append(String.format("_%02X", b));
            }
        } catch (Exception e) {
            return str;
        }
        return sb.toString();
    }

    @Transactional
    public void updateStatus(int seq) {
        // Mapper를 호출하여 DB의 STATUS를 '1'로 변경합니다.
        mapper.updatePrintStatus(seq, "1", null);
    }
}