package com.sonictms.alsys.hyapp.inbound.controller;

import com.sonictms.alsys.hyapp.inbound.dto.HyappInboundDTO;
import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.inbound.mapper.HyappInboundMapper;
import com.sonictms.alsys.hyapp.inbound.service.HyappInboundService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam String cmpyCd,
            @RequestParam String zoneId,
            @RequestParam String locBarcode) {

        // 기존 매퍼 메서드 활용
        HyappInboundVO locInfo = inboundMapper.selectLocInfoByBarcode(cmpyCd, zoneId, locBarcode);

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
    public void savePut(@RequestBody HyappInboundDTO req) {
// 로케이션 정보 조회
        HyappInboundVO locInfo = inboundMapper.selectLocInfoByBarcode(req.getCmpyCd(), req.getZoneId(), req.getLocBarcode());

        // 로케이션 데이터가 없는 경우 예외 발생
        if (locInfo == null || locInfo.getLocId() == null) {
            // 이 메시지가 프론트엔드의 res.text()로 전달됩니다.
            throw new IllegalArgumentException("Invalid location barcode: " + req.getLocBarcode());
        }
        Long locId = locInfo.getLocId();
        String whCd = locInfo.getWhCd();

        // ✅ putawayUser는 서버에서 세팅 (Security 미사용이면 다른 방식으로 교체)
        String putawayUser = "SYSTEM"; // TODO: 실제 로그인 사용자로 교체

        List<HyappInboundVO> list = req.getLines().stream().map(line -> {
            HyappInboundVO vo = new HyappInboundVO();

            vo.setTblInboundProductId(req.getTblInboundProductId());
            vo.setCmpyCd(req.getCmpyCd());
            vo.setAgntCd(req.getAgntCd());
            vo.setMtrlCd(req.getMtrlCd());
            vo.setMtrlNm(req.getMtrlNm());

            vo.setZoneId(req.getZoneId());
            vo.setZoneCd(req.getZoneCd());
            vo.setZoneNm(req.getZoneNm());

            vo.setWhCd(whCd);
            vo.setLocId(locId);

            vo.setPutawayQty(line.getQty());
            vo.setLotNo(line.getLotNo());
            vo.setExpireDate(line.getExpireDate());

            vo.setPutawayUser(putawayUser);

            return vo;
        }).collect(Collectors.toList());

        // 3. ✅ 핵심: Mapper가 아닌 Service를 호출합니다!
        int processedCount = hyappInboundService.savePut(list);
    }

}
