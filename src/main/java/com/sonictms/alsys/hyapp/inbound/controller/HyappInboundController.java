package com.sonictms.alsys.hyapp.inbound.controller;

import com.sonictms.alsys.hyapp.inbound.dto.HyappInboundDTO;
import com.sonictms.alsys.hyapp.inbound.dto.InboundProductSearchDTO;
import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.inbound.mapper.HyappInboundMapper;
import com.sonictms.alsys.hyapp.inbound.service.HyappInboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hyapp/api")
public class HyappInboundController<HyappInboundSavePutReq> {

    private final HyappInboundService hyappInboundService;
    @Autowired
    private HyappInboundMapper inboundMapper;

    public HyappInboundController(HyappInboundService hyappInboundService) {
        this.hyappInboundService = hyappInboundService;
    }

    @PostMapping("/inbound/pending-list")
    public Map<String, Object> getPendingList(@RequestBody Map<String, Object> param) {
        Map<String, Object> res = new HashMap<>();

        HyappInboundVO vo = new HyappInboundVO();
        vo.setCmpyCd("A"); // 기본 회사 코드

        // agntCd가 있을 때만 세팅
        if(param.get("agntCd") != null) {
            vo.setAgntCd(param.get("agntCd").toString());
        }

        // 프론트의 'dateFrom'을 SQL의 'expectedDate'로 매핑
        String dateStr = (String) param.get("dateFrom");
        if(dateStr == null || dateStr.isEmpty()) {
            // 날짜값이 없을 경우 오늘 날짜를 기본값으로 사용
            dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        }
        vo.setExpectedDate(dateStr);

        List<HyappInboundVO> list = hyappInboundService.getPendingInboundList(vo);

        res.put("success", true);
        res.put("data", list);
        return res;
    }

    // ✅ 프론트에서 쓰기 좋은 형태: 리스트를 그대로 반환
    @GetMapping("/inbound/plan/by-barcode")
    public List<HyappInboundVO> getInboundByBarcode(
            @RequestParam("barcode") String barcode,
            HttpSession session
    ) {
        String cmpyCd = (String) session.getAttribute("cmpyCd");

        HyappInboundVO vo = new HyappInboundVO();
        vo.setCmpyCd(cmpyCd);
        vo.setBarcode(barcode == null ? "" : barcode.trim());

        // 페이징 기본값 방어(선택)
        if (vo.getPageNum() <= 0) vo.setPageNum(1);
        if (vo.getPageSize() <= 0) vo.setPageSize(10);

        return hyappInboundService.getInbound_barcode(vo);
    }

    /**
     * ? 입고 검수 확인 저장
     * - 키: tblInboundProductId (TBL_INBOUND_PRODUCT_ID)
     * - 합격/불합격 수량(passQty/failQty), 차량톤수(vhclTonType), 입고방법(inMethod), 파렛트수량(palletQty), 메모(memo)
     */
    @PostMapping("/inbound/qc/save")
    public Map<String, Object> saveInboundQc(@RequestBody HyappInboundVO vo, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        // 세션값 보강
        String cmpyCd = (String) session.getAttribute("cmpyCd");
        vo.setCmpyCd(cmpyCd);

        // 사용자(프로젝트에 맞는 키로 바꾸세요)
        String user = (String) session.getAttribute("userId");
        if (user == null) user = (String) session.getAttribute("loginId");
        if (user == null) user = (String) session.getAttribute("userNm");
        if (user == null) user = "SYSTEM";
        vo.setCompleteUser(user);

        int updated = hyappInboundService.saveInboundQcAndInventory(vo);

        res.put("success", updated > 0);
        res.put("updated", updated);
        return res;
    }

    // =========================
    // ? 검수완료(COMPLETE) 목록 조회 (HyappPut.html에서 사용)
    // GET /hyapp/api/inbound/qc/complete?date=YYYY-MM-DD
    // =========================
    @GetMapping("/inbound/qc/complete")
    public List<HyappInboundVO> qcCompleteList(
            @RequestParam(value = "date", required = false) String date,
            HttpSession session
    ) {
        String cmpyCd = (String) session.getAttribute("cmpyCd");

        HyappInboundVO vo = new HyappInboundVO();
        vo.setCmpyCd(cmpyCd);
        vo.setDate(date);

        // 필요하면 paging도 추가 가능 (현재 HyappPut은 전체 리스트)
        return hyappInboundService.getQcCompleteList(vo);
    }

    // ✅ 프론트에서 쓰기 좋은 형태: 리스트를 그대로 반환
    @GetMapping("/wh/zone/list")
    public List<HyappInboundVO> getWhZoneList(
            HttpSession session
    ) {
        String cmpyCd = (String) session.getAttribute("cmpyCd");

        HyappInboundVO vo = new HyappInboundVO();
        vo.setCmpyCd(cmpyCd);

        return hyappInboundService.getWhZoneList(vo);
    }
    // 존에 매핑된 기본 로케이션(1:1)을 조회
    @GetMapping("/wh/loc/by-zone")
    public ResponseEntity<?> getLocByZone(
            @RequestParam String zoneId,
            HttpSession session
    ) {
        String cmpyCd = (String) session.getAttribute("cmpyCd");

        HyappInboundVO loc = inboundMapper.selectLocByZoneId(cmpyCd, zoneId);
        if (loc == null || loc.getLocId() == null) {
            return ResponseEntity.ok(Collections.emptyMap());
        }
        // locCd(=바코드처럼 사용할 코드), locId, whCd 정도만 있어도 충분
        return ResponseEntity.ok(loc);
    }

    @GetMapping("/wh/loc/check")
    public ResponseEntity<?> checkLocation(
            @RequestParam String locBarcode,
            HttpSession session) {

        String cmpyCd = (String) session.getAttribute("cmpyCd");

        // 기존 매퍼 메서드 활용
        HyappInboundVO locInfo = inboundMapper.selectLocInfoByBarcode(cmpyCd, locBarcode);

        if (locInfo == null) {
            // 데이터가 없으면 빈 객체나 404 응답
            return ResponseEntity.ok(Collections.emptyMap());
        }

        return ResponseEntity.ok(locInfo);
    }
    /**
     * ? 적치 정보 저장
     * - 외래키: tblInboundProductId (TBL_INBOUND_PRODUCT_ID)
     */
    @PostMapping("/wh/put/save")
    @ResponseBody
    public ResponseEntity<?> savePut(@RequestBody HyappInboundDTO req, HttpSession session) {
        // 1. 세션 체크 (최우선)
        String putawayUser = (String) session.getAttribute("userId");
        String sessionCmpyCd = (String) session.getAttribute("cmpyCd");

        if (putawayUser == null || sessionCmpyCd == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("SESSION_EXPIRED");
        }

        // 2. 요청 데이터 유효성 체크 (lines가 null인 경우 방어)
        if (req.getLines() == null || req.getLines().isEmpty()) {
            return ResponseEntity.badRequest().body("적치할 수량 내역(lines) 정보가 없습니다.");
        }

        // 3. 로케이션 정보 조회 및 Null 체크
        String targetCmpyCd = (req.getCmpyCd() == null) ? sessionCmpyCd : req.getCmpyCd();
        HyappInboundVO locInfo = inboundMapper.selectLocInfoByBarcode(targetCmpyCd, req.getLocBarcode());

        if (locInfo == null || locInfo.getLocId() == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 로케이션 바코드입니다: " + req.getLocBarcode());
        }

        try {
            // 4. 리스트 변환 및 데이터 세팅
            List<HyappInboundVO> list = req.getLines().stream().map(line -> {
                HyappInboundVO vo = new HyappInboundVO();
                vo.setTblInboundProductId(req.getTblInboundProductId());
                vo.setCmpyCd(targetCmpyCd);
                vo.setAgntCd(req.getAgntCd());
                vo.setMtrlCd(req.getMtrlCd());
                vo.setWhCd(locInfo.getWhCd());
                vo.setLocId(locInfo.getLocId());
                vo.setZoneId(locInfo.getZoneId());

                vo.setPutawayQty(line.getQty());
                vo.setLotNo(line.getLotNo());
                vo.setExpireDate(line.getExpireDate());
                vo.setPutawayUser(putawayUser); // 위에서 검증된 세션 사용자
                return vo;
            }).collect(Collectors.toList());

            hyappInboundService.savePut(list);
            return ResponseEntity.ok().body("SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("저장 중 시스템 오류: " + e.getMessage());
        }
    }

    /**
     * WMS 앱에서 바코드 출력 요청을 처리
     */
    @PostMapping("/print/request")
    public ResponseEntity<Map<String, Object>> requestPrint(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            // DB에 출력 대기 데이터 생성
            hyappInboundService.insertPrintQueue(params);

            result.put("status", "OK");
            result.put("message", "출력 요청이 등록되었습니다.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 상품 검색 API (고객사 마스터 + 상품 마스터 조인)
     */
    @GetMapping("/inbound/search/products")
    public List<InboundProductSearchDTO> searchProducts(
            @RequestParam("keyword") String keyword,
            HttpSession session
    ) {
        String cmpyCd = (String) session.getAttribute("cmpyCd");
        return hyappInboundService.findInboundProducts(keyword, cmpyCd);
    }

    /**
     * 입고 예정 정보 수동 등록
     */
    @PostMapping("/inbound/registerProduct")
    public Map<String, Object> registerProduct(@RequestBody HyappInboundVO vo, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        String cmpyCd = (String) session.getAttribute("cmpyCd");
        String userId = (String) session.getAttribute("userId");

        vo.setCmpyCd(cmpyCd);
        vo.setSaveUser(userId != null ? userId : "SYSTEM");

        int result = hyappInboundService.registerInboundProduct(vo);

        res.put("success", result > 0);
        res.put("message", result > 0 ? "등록 성공" : "등록 실패");
        return res;
    }

    @PostMapping("/inbound/master/save")
    public ResponseEntity<Map<String, Object>> saveMaster(@RequestBody HyappInboundVO vo) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 세션 등에서 회사코드(cmpyCd), 작성자 정보 세팅
            // vo.setCmpyCd("1000");

            // 2. 서비스 호출 (insert 후 MyBatis의 useGeneratedKeys를 통해 ID 획득)
            int count = hyappInboundService.insertInboundMaster(vo);

            if (count > 0) {
                result.put("success", true);
                result.put("masterId", vo.getTblInboundMasterId()); // 생성된 PK 반환
            } else {
                result.put("success", false);
                result.put("message", "데이터 저장에 실패했습니다.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/inbound/master/list")
    public List<HyappInboundVO> getMasterList(
            @RequestParam String cmpyCd,
            @RequestParam String agntCd,
            @RequestParam String date,
            HttpSession session
    ) {
        HyappInboundVO vo = new HyappInboundVO();
        vo.setCmpyCd(cmpyCd != null ? cmpyCd : "A"); // 기본값 'A'
        vo.setAgntCd(agntCd);
        vo.setActualDate(date); // 'yyyy-MM-dd'

        return hyappInboundService.getInboundMasterList(vo);
    }

    @GetMapping("/wh/put/check-item")
    @ResponseBody
    public Map<String, Object> checkPutawayItem(
            @RequestParam("barcode") String barcode,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        String cmpyCd = (String) session.getAttribute("cmpyCd");
        if (cmpyCd == null) cmpyCd = "A"; // 기본값 세팅

        HyappInboundVO vo = new HyappInboundVO();
        vo.setBarcode(barcode);
        vo.setCmpyCd(cmpyCd);

        // 바코드에 해당하는 검수 완료 내역 조회
        List<HyappInboundVO> list = hyappInboundService.getPutawayItemByBarcode(vo);

        if (list != null && !list.isEmpty()) {
            result.put("success", true);
            // 가장 먼저 적치해야 할 내역 1건을 전달
            result.put("item", list.get(0));
        } else {
            result.put("success", false);
            result.put("message", "적치 가능한 검수 내역이 없습니다.");
        }

        return result;
    }
}
