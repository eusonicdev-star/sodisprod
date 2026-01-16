package com.sonictms.alsys.hyapp.info.service;

import com.sonictms.alsys.hyapp.info.dto.HyappInvMtrlDetailResponse;
import com.sonictms.alsys.hyapp.info.mapper.HyappInvMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HyappInvService {


    private final HyappInvMapper mapper;

    public HyappInvService(HyappInvMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 상품별 재고 상세 조회
     * - 검색조건: mtrlCd / mtrlNm / barcode (3개 중 하나 이상)
     * - 결과: 상품 정보 + 전체수량 + 로케이션별 재고
     *
     * 주의:
     *  - 상품명/바코드로 검색하면 여러 상품이 걸릴 수 있으므로,
     *    상세 화면에서는 "1개 상품"만 허용하는 것이 안전.
     *  - 여러 건이면 409(CONFLICT)로 에러를 내려 프론트에서 "상품코드를 입력하세요" 안내.
     */
    public HyappInvMtrlDetailResponse getMtrlDetail(String mtrlCd, String mtrlNm, String mtrlBarcode) {

        String mtrlCdTrim = trim(mtrlCd);
        String mtrlNmTrim = trim(mtrlNm);
        String mtrlBarcodeTrim = trim(mtrlBarcode);

        // 1) Validation: 조건이 하나도 없으면 400
        if (isBlank(mtrlCdTrim) && isBlank(mtrlNmTrim) && isBlank(mtrlBarcodeTrim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "검색조건이 없습니다.");
        }

        // 2) 상품 후보 조회 (최대 2건 조회해서 다건 여부 판단)
        List<ProductRow> products = mapper.findProducts(mtrlCdTrim, mtrlNmTrim, mtrlBarcodeTrim);

        // 데이터 없음: 빈 응답 (프론트에서 "데이터 없음" 처리)
        if (products == null || products.isEmpty()) {
            HyappInvMtrlDetailResponse.Info info = new HyappInvMtrlDetailResponse.Info(
                    nvl(mtrlCdTrim, ""),
                    nvl(mtrlNmTrim, ""),
                    nvl(mtrlBarcodeTrim, ""),
                    0L,
                    "-",
                    "-"
            );
            return new HyappInvMtrlDetailResponse(info, Collections.<HyappInvMtrlDetailResponse.LocationRow>emptyList());
        }

        // 다건이면 상세 화면에서 선택 불가 → 에러(409)
        if (products.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "검색 결과가 여러 건입니다. 상품코드를 정확히 입력하세요.");
        }

        // 3) 1건 확정
        ProductRow p = products.get(0);
        String fixedMtrlCd = trim(p.getMtrlCd());
        String fixedMtrlnm = trim(p.getMtrlNm());
        String fixedMtrlBarcode = trim(p.getBarcode());

        // 4) 전체 수량 + 로케이션별 재고 조회
        Long totalQty = mapper.selectTotalQty(fixedMtrlCd, fixedMtrlnm, fixedMtrlBarcode);
        if (totalQty == null) totalQty = 0L;

        List<LocationStockRow> locs = mapper.selectLocStocks(fixedMtrlCd, fixedMtrlnm, fixedMtrlBarcode);
        if (locs == null) locs = Collections.emptyList();

        // 5) response 구성
        HyappInvMtrlDetailResponse.Info info = new HyappInvMtrlDetailResponse.Info(
                nvl(p.getMtrlCd(), fixedMtrlCd),
                nvl(p.getMtrlNm(), "-"),
                nvl(p.getBarcode(), "-"),
                totalQty,
                nvl(p.getAgntNm(), "-"),
                nvl(p.getWhNm(), "-")
        );

        List<HyappInvMtrlDetailResponse.LocationRow> locations = new ArrayList<HyappInvMtrlDetailResponse.LocationRow>();
        for (LocationStockRow r : locs) {
            locations.add(new HyappInvMtrlDetailResponse.LocationRow(
                    nvl(r.getZoneNm(), "-"),
                    nvl(r.getLocBarcode(), "-"),
                    (r.getQty() == null ? 0L : r.getQty()),
                    nvl(r.getLotNo(), "-"),
                    nvl(r.getExpireDate(), "-")
            ));
        }

        return new HyappInvMtrlDetailResponse(info, locations);
    }

    // ---------------------------
    // 내부 util
    // ---------------------------
    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nvl(String s, String def) {
        return (s == null || s.trim().isEmpty()) ? def : s;
    }

    // ---------------------------
    // 내부용 Row 클래스 (record 대신)
    // Mapper에서 결과를 이 클래스로 매핑하도록 구성
    // ---------------------------
    public static class ProductRow {
        private String mtrlCd;
        private String mtrlNm;
        private String mtrlBarcode;
        private String agntNm;
        private String whNm;

        public ProductRow() {}

        public String getMtrlCd() { return mtrlCd; }
        public void setMtrlCd(String mtrlCd) { this.mtrlCd = mtrlCd; }

        public String getMtrlNm() { return mtrlNm; }
        public void setMtrlNm(String mtrlNm) { this.mtrlNm = mtrlNm; }

        public String getBarcode() { return mtrlBarcode; }
        public void setBarcode(String mtrlBarcode) { this.mtrlBarcode = mtrlBarcode; }

        public String getAgntNm() { return agntNm; }
        public void setAgntNm(String agntNm) { this.agntNm = agntNm; }

        public String getWhNm() { return whNm; }
        public void setWhNm(String whNm) { this.whNm = whNm; }
    }

    public static class LocationStockRow {
        private String zoneNm;
        private String locCd;
        private Long qty;
        private String lotNo;
        private String expireDate;

        public LocationStockRow() {}

        public String getZoneNm() { return zoneNm; }
        public void setZoneNm(String zoneNm) { this.zoneNm = zoneNm; }

        public String getLocBarcode() { return locCd; }
        public void setLocBarcode(String locBarcode) { this.locCd = locBarcode; }

        public Long getQty() { return qty; }
        public void setQty(Long qty) { this.qty = qty; }

        public String getLotNo() { return lotNo; }
        public void setLotNo(String lotNo) { this.lotNo = lotNo; }

        public String getExpireDate() { return expireDate; }
        public void setExpireDate(String expireDate) { this.expireDate = expireDate; }
    }


}
