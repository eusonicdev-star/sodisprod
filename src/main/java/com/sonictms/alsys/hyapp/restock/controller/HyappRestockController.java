package com.sonictms.alsys.hyapp.restock.controller;

import com.sonictms.alsys.erp.erp105006.entity.Erp105006VO;
import com.sonictms.alsys.erp.erp105006.service.Erp105006Service;
import com.sonictms.alsys.hyapp.restock.entity.HyappRestockVO;
import com.sonictms.alsys.hyapp.restock.service.HyappRestockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hyapp/api/restock")
@RequiredArgsConstructor
public class HyappRestockController {
    private final HyappRestockService restockService;
    private final Erp105006Service erp105006Service;

    @PostMapping("/process")
    public Map<String, Object> process(@RequestBody HyappRestockVO vo) {
        // 1. 바코드로 해당 상품이 재입고 대상인지 조회
        // 2. 대상이면 updateRestockComplete 호출
        // 3. 기존 ERP 서비스의 재고 가산 로직 호출
        return restockService.processRestock(vo);
    }
    /**
     * 리스트 조회 메서드 (에러 해결: getRestockWaitList)
     */
    @GetMapping("/list")
    public List<Erp105006VO> getRestockWaitList(Erp105006VO vo) {
        return erp105006Service.erp105006List(vo);
    }

    /**
     * 적치 대기 리스트 조회 (검수 완료 건만)
     */
    @GetMapping("/put-list")
    public List<HyappRestockVO> getPutList(
            @RequestParam(value = "cmpyCd", required = false, defaultValue = "A") String cmpyCd, // [보완] 기본값 설정
            @RequestParam(value = "dcCd") String dcCd
    ) {
        return restockService.getPutawayWaitList(cmpyCd, dcCd);
    }

    /**
     * 로케이션 적치 실행 (+1 수량 증가 및 상태 갱신)
     */
    @PostMapping("/put-increment")
    public Map<String, Object> incrementPutaway(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            // [수정] 프론트에서 보낸 locBarcode를 추가로 받아서 서비스에 전달합니다.
            restockService.processPutawayIncrement(
                    params.get("waitId"),
                    params.get("saveUser"),
                    params.get("locBarcode") // 추가된 파라미터
            );
            result.put("status", "OK");
        } catch (Exception e) {
            result.put("status", "FAIL");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/return-list")
    public List<HyappRestockVO> getReturnList(HyappRestockVO vo) {
        // 세션에서 cmpyCd 등을 가져오는 로직이 필요할 수 있습니다.
        return restockService.getReturnInboundWaitList(vo);
    }
}