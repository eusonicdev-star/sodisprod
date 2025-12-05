-- 택배사 공통코드 추가
-- COMM_CD = 'COURIER_CD'
-- 사용법: SQL Server Management Studio에서 실행

DECLARE
@RESULT_MSG VARCHAR(200);

-- CJ대한통운
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'CJ', 'CJ대한통운', '', 1, 'Y', 'ADMIN';

-- 경동택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'KD', '경동택배', '', 2, 'Y', 'ADMIN';

-- 한진택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'HANJIN', '한진택배', '', 3, 'Y', 'ADMIN';

-- 롯데택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'LOTTE', '롯데택배', '', 4, 'Y', 'ADMIN';

-- 로젠택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'LOGEN', '로젠택배', '', 5, 'Y', 'ADMIN';

-- 우체국택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'POST', '우체국택배', '', 6, 'Y', 'ADMIN';

-- 충청택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'CC', '충청택배', '', 7, 'Y', 'ADMIN';

-- GS25편의점택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'GS25', 'GS25편의점택배', '', 8, 'Y', 'ADMIN';

-- CU편의점택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'CU', 'CU 편의점택배', '', 9, 'Y', 'ADMIN';

-- 한의사랑
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'HANUI', '한의사랑', '', 10, 'Y', 'ADMIN';

-- 합동포스
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'HDPOST', '합동포스', '', 11, 'Y', 'ADMIN';

-- 농협택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'NH', '농협택배', '', 12, 'Y', 'ADMIN';

-- 일양로지스
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'ILYANG', '일양로지스', '', 13, 'Y', 'ADMIN';

-- 건영택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'KY', '건영택배', '', 14, 'Y', 'ADMIN';

-- EMS
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'EMS', 'EMS', '', 15, 'Y', 'ADMIN';

-- TNT Express
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'TNT', 'TNT express', '', 16, 'Y', 'ADMIN';

-- UPS
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'UPS', 'UPS', '', 17, 'Y', 'ADMIN';

-- Fedex
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'FEDEX', 'Fedex', '', 18, 'Y', 'ADMIN';

-- USPS
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'USPS', 'USPS', '', 19, 'Y', 'ADMIN';

-- 천일택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'CHUNIL', '천일택배', '', 20, 'Y', 'ADMIN';

-- GSMNtoN
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'GSMN', 'GSMNtoN', '', 21, 'Y', 'ADMIN';

-- KGL네트웍스
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'KGL', 'KGL네트웍스', '', 22, 'Y', 'ADMIN';

-- 우리택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'WOORI', '우리택배(구:로지스)', '', 23, 'Y', 'ADMIN';

-- GSI Express
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'GSI', 'GSI Express', '', 24, 'Y', 'ADMIN';

-- 대신택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'DAESIN', '대신택배', '', 25, 'Y', 'ADMIN';

-- 세방
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'SEBANG', '세방', '', 26, 'Y', 'ADMIN';

-- LOTOS
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'LOTOS', 'LOTOS CORPORATION', '', 27, 'Y', 'ADMIN';

-- EOMARS
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'EOMARS', '이투마스(ETOMARS)', '', 28, 'Y', 'ADMIN';

-- 한의사랑택배
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'HDEXPRESS', '한의사랑택배', '', 29, 'Y', 'ADMIN';

-- CJ지엘에스
EXEC USP_COMM_M_SAVE 
    'I', NULL, 'A', 'COURIER_CD', '택배운송사', '', 
    'CJSYS', '씨제이지엘에스', '', 30, 'Y', 'ADMIN';

PRINT
'택배사 공통코드 추가 완료';

-- 확인 쿼리
SELECT *
FROM TBL_COMM_M
WHERE COMM_CD = 'COURIER_CD'
ORDER BY SEQ;

