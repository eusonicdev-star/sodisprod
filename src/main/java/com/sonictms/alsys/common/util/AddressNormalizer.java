package com.sonictms.alsys.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressNormalizer {

    // =========================================================================
    // 1. [검색용 & 저장용] 포맷팅 (가장 중요)
    // 예: "상암로 11 선사현대아파트 101 동 1107 호"
    // -> "서울특별시 강동구 상암로 11 101동 1107호(선사현대아파트)"
    // =========================================================================
    public static String formatAddress(String rawAddr) {
        if (rawAddr == null) return "";

        // 1. 전처리 (공백 제거, 동/호 붙이기)
        String s = preprocess(rawAddr);

        String coreAddress = "";    // 기본 주소 (도로명+번지)
        String buildingName = "";   // 건물명 (선사현대아파트)
        String detailAddress = "";  // 상세주소 (101동 1107호)

        // 2. 상세주소 (동/호) 추출
        // "101동 1107호", "101동1107호", "101동" 등을 찾음
        // (preprocess에서 이미 '101 동' -> '101동'으로 붙여놨음)
        Pattern pDetail = Pattern.compile("(\\d+동(?:\\s*\\d+호)?)");
        Matcher mDetail = pDetail.matcher(s);

        int detailStart = -1;
        if (mDetail.find()) {
            detailAddress = mDetail.group(1); // "101동 1107호"
            detailStart = mDetail.start();

            // 원본에서 상세주소 제거
            // "서울특별시 강동구 상암로 11 선사현대아파트" 만 남음
            s = s.substring(0, detailStart) + s.substring(mDetail.end());
        }

        // 3. 건물명 추출 (괄호가 있는 경우 & 없는 경우 모두 대응)

        // (Case A) 괄호 안에 있는 경우 "(선사현대)"
        Pattern pParen = Pattern.compile("\\(([^)]+)\\)");
        Matcher mParen = pParen.matcher(s);
        if (mParen.find()) {
            buildingName = mParen.group(1).trim();
            s = s.replace(mParen.group(0), " "); // 제거
        }
        // (Case B) 괄호 없이 도로명 번지 뒤에 덩그러니 있는 경우 "상암로 11 선사현대아파트"
        else {
            // 도로명+번지(또는 지번+번지) 패턴을 찾아서 그 뒤에 있는 텍스트를 건물명으로 간주
            Pattern pAddrEnd = Pattern.compile("(\\d+(?:-\\d+)?)(?=\\s|$)"); // 번지로 끝나는 지점 찾기
            Matcher mAddrEnd = pAddrEnd.matcher(s);

            if (mAddrEnd.find()) {
                // 번지수가 끝나는 지점 이후부터 끝까지가 건물명
                int endOfNumber = mAddrEnd.end();
                if (endOfNumber < s.length()) {
                    String candidate = s.substring(endOfNumber).trim();
                    // "호"나 "층"으로 시작하지 않는 경우만 건물명으로 인정
                    if (!candidate.isEmpty() && !candidate.matches("^(호|층).*")) {
                        buildingName = candidate;
                        s = s.substring(0, endOfNumber); // 건물명 잘라내고 주소만 남김
                    }
                }
            }
        }

        // 4. 남은게 진짜 주소 (Road Addr)
        coreAddress = s.replaceAll("\\s+", " ").trim();

        // 5. 최종 조립: [기본] + [상세] + (건물명)
        StringBuilder result = new StringBuilder();
        result.append(coreAddress);

        if (!detailAddress.isEmpty()) {
            result.append(" ").append(detailAddress);
        }

        if (!buildingName.isEmpty()) {
            // 건물명 내부에 불필요한 공백/콤마 제거
            buildingName = buildingName.replaceAll("[,\\s]+", " ").trim();
            result.append("(").append(buildingName).append(")");
        }

        return result.toString();
    }

    // =========================================================================
    // 2. [검색용] 1차 키 생성 (formatAddress 결과 활용)
    // =========================================================================
    public static String generateSearchKey(String rawAddr) {
        // formatAddress를 거치면 "도로명 번지 동 호(건물명)" 형태로 예쁘게 나옴
        // 여기서 괄호(건물명)만 떼고 검색하거나, 전체를 다 검색하거나 선택 가능
        // 카카오 API는 "도로명 번지 동" 까지를 좋아함

        String formatted = formatAddress(rawAddr); // 일단 예쁘게 만듦

        // 괄호 제거 후 리턴 (검색 정확도를 위해)
        return formatted.replaceAll("\\(.*?\\)", "").trim();
    }

    // =========================================================================
    // 3. [공통] 전처리 (이게 핵심!)
    // =========================================================================
    private static String preprocess(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        s = s.replaceAll("(?i)null", ""); // null 문자열 제거
        s = s.replace("　", " ").replace("\u00A0", " "); // 특수 공백

        // ★ [핵심] "숫자 + 공백 + 동/호/층" -> "숫자동/숫자호/숫자층" (붙이기)
        // 예: "101 동" -> "101동", "1107 호" -> "1107호"
        s = s.replaceAll("(\\d+)\\s+(동|호|층)", "$1$2");

        // 숫자와 문자 사이 분리 (번지수와 건물명 분리용)
        // "427-50A-ONE" -> "427-50 A-ONE" (단, 동/호/층 뒤는 제외)
        // s = s.replaceAll("(\\d+(?:-\\d+)?)(?=[가-힣A-Za-z])", "$1 "); // 이 정규식은 위 동/호 붙이기와 충돌날 수 있어 제거하거나 순서 조정 필요.
        // 동/호를 먼저 붙였으므로, 이제 '동/호'가 아닌 다른 문자가 숫자에 붙은 경우만 뗍니다.
        s = s.replaceAll("(\\d+(?:-\\d+)?)(?=[가-힣A-Za-z&&[^동호층]])", "$1 ");

        s = s.replaceAll("[,\\.]", " "); // 특수문자
        return s.replaceAll("\\s+", " ").trim();
    }

    // (fallback 메서드 등 기존 유지...)
    public static String extractCoreAddressOnly(String rawAddr) {
        // 기존 로직 유지하되, preprocess가 개선되었으므로 더 잘 동작함
        String cleaned = preprocess(rawAddr);
        Pattern pRoad = Pattern.compile("^(.+(?:로|길|번길))\\s+(\\d+(?:-\\d+)?)");
        Matcher mRoad = pRoad.matcher(cleaned);
        if (mRoad.find()) return mRoad.group(1).trim() + " " + mRoad.group(2).trim();

        Pattern pJibun = Pattern.compile("^(.+(?:동|리|가))\\s+(산\\s*)?(\\d+(?:-\\d+)?)");
        Matcher mJibun = pJibun.matcher(cleaned);
        if (mJibun.find()) {
            String prefix = mJibun.group(1).trim();
            String san = mJibun.group(2);
            String num = mJibun.group(3).trim();
            return (san != null && !san.trim().isEmpty()) ? prefix + " 산 " + num : prefix + " " + num;
        }
        return "";
    }

    /**
     * [Upgrade] 아파트 정보 분석기
     * 리턴: Map (key: searchKey, aptNm, dongNm)
     * 분석 실패 시 null 리턴
     */
    public static Map<String, String> analyzeApartment(String rawAddr) {
        String formatted = formatAddress(rawAddr); // 예: "... 101동 1107호(선사현대아파트)"

        String buildingName = "";
        String dong = "";
        String region = "";

        // 1. 건물명 추출
        Pattern pBuild = Pattern.compile("\\(([^)]+)\\)");
        Matcher mBuild = pBuild.matcher(formatted);
        if (mBuild.find()) {
            buildingName = mBuild.group(1).trim();
        } else {
            return null; // 건물명 없으면 아파트 로직 불가
        }

        // 2. 동 추출
        Pattern pDong = Pattern.compile("(\\d+동)");
        Matcher mDong = pDong.matcher(formatted);
        if (mDong.find()) {
            dong = mDong.group(1);
        } else {
            return null; // 동 없으면 아파트 로직 불가
        }

        // 3. 지역명 추출
        Pattern pRegion = Pattern.compile("(\\S+(?:시|군|구))");
        Matcher mRegion = pRegion.matcher(formatted);
        if (mRegion.find()) {
            region = mRegion.group(1);
        }

        // 4. 검색 키 조합
        String searchKey = (region + " " + buildingName + " " + dong).trim();

        // 5. 결과 맵 생성
        Map<String, String> result = new HashMap<>();
        result.put("searchKey", searchKey); // 강동구 선사현대아파트 101동
        result.put("aptNm", buildingName);  // 선사현대아파트
        result.put("dongNm", dong);         // 101동

        return result;
    }
}