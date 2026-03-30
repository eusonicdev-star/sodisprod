package com.sonictms.alsys.hyapp.info.dto;
import java.util.List;

public class HyappInvMtrlDetailResponse {
    private Info info;
    private List<LocationRow> locations;

    public HyappInvMtrlDetailResponse() {}

    public HyappInvMtrlDetailResponse(Info info, List<LocationRow> locations) {
        this.info = info;
        this.locations = locations;
    }

    public Info getInfo() { return info; }
    public void setInfo(Info info) { this.info = info; }

    public List<LocationRow> getLocations() { return locations; }
    public void setLocations(List<LocationRow> locations) { this.locations = locations; }

    public static class Info {
        private String keyword;
        private Long totalQty;
        private String agntNm;
        private String whNm;

        public Info() {}

        public Info(String keyword, String agntNm, String whNm) {
            this.keyword = keyword;
            this.agntNm = agntNm;
            this.whNm = whNm;
        }

        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public Long getTotalQty() { return totalQty; }
        public void setTotalQty(Long totalQty) { this.totalQty = totalQty; }
        public String getAgntNm() { return agntNm; }
        public void setAgntNm(String agntNm) { this.agntNm = agntNm; }
        public String getWhNm() { return whNm; }
        public void setWhNm(String whNm) { this.whNm = whNm; }
    }

    public static class LocationRow {
        private String zoneNm;
        private String locBarcode;
        private Long qty;
        private String lotNo;
        private String expireDate;

        public LocationRow() {}

        public LocationRow(String zoneNm, String locBarcode, Long qty, String lotNo, String expireDate) {
            this.zoneNm = zoneNm;
            this.locBarcode = locBarcode;
            this.qty = qty;
            this.lotNo = lotNo;
            this.expireDate = expireDate;
        }

        public String getZoneNm() { return zoneNm; }
        public void setZoneNm(String zoneNm) { this.zoneNm = zoneNm; }
        public String getLocBarcode() { return locBarcode; }
        public void setLocBarcode(String locBarcode) { this.locBarcode = locBarcode; }
        public Long getQty() { return qty; }
        public void setQty(Long qty) { this.qty = qty; }
        public String getLotNo() { return lotNo; }
        public void setLotNo(String lotNo) { this.lotNo = lotNo; }
        public String getExpireDate() { return expireDate; }
        public void setExpireDate(String expireDate) { this.expireDate = expireDate; }
    }
}
