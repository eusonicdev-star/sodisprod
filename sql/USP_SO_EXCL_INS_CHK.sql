CREATE PROCEDURE dbo.USP_SO_EXCL_INS_CHK(
    @I_EXCL_ROW bigINT
, @I_CMPY_CD VARCHAR(10)
, @I_REF_SO_NO VARCHAR(100) --참조번호
, @I_ORGN_SO_NO VARCHAR(100) --원주문번호
, @I_SO_TYPE VARCHAR(50) --오더유형
, @I_SO_STAT_CD VARCHAR(50) --오더상태
, @I_AGNT_CD VARCHAR(10) --화주코드
, @I_MALL_CD VARCHAR(50) --쇼핑몰
, @I_BRAND VARCHAR(50) --브랜드
, @I_ORDR_INPT_DT VARCHAR(10) --주문입력일
, @I_RCPT_DT VARCHAR(10) --주문접수일
, @I_RQST_DT VARCHAR(10) --배송희망일
, @I_DLVY_CNFM_DT VARCHAR(10) --배송확정일

, @I_ACPT_ER VARCHAR(50) --수취인(수령인)
, @I_ORDR_ER VARCHAR(50) --주문자
, @I_ACPT_TEL1 VARCHAR(20) --연락처1
, @I_ACPT_TEL2 VARCHAR(20) --연락처2
, @I_POST_CD VARCHAR(10) --우편번호
, @I_ADDR1 VARCHAR(100) --주소1
, @I_ADDR2 VARCHAR(200) --주소2
, @I_DLVY_RQST_MSG VARCHAR(500) --배송메시지
, @I_CUST_SPCL_TXT VARCHAR(500) --고객특이사항

, @I_COST_TYPE VARCHAR(10) --운임구분
, @I_RCPT_COST VARCHAR(20) --착불비
, @I_PASS_COST VARCHAR(20) --통행비

, @I_PROD_CD VARCHAR(100) -- 제품코드
, @I_PROD_NM VARCHAR(200) -- 제품명
, @I_QTY FLOAT -- 수량

, @I_DC_CD VARCHAR(10) --물류센터
, @I_USE_YN VARCHAR(50) --사용유무
, @I_MEMO VARCHAR(10)
, @I_INPT_SYS VARCHAR(10) -- 입력시스템  : ERP_SO , ERP_EXCL
, @I_SAVE_USER VARCHAR(30)
)


/**********************************
1. 목적&기능 : 주문정보 (엑셀업로드) 체크,
2. 변경 내용 및 사유 :
3. 실행예시 :

SELECT * FROM TBL_POST_M WHERE SIGU_NM LIKE '은평구%'
USP_SO_EXCL_INS_CHK  1,'A','123123123','','1000','1000','8009','11번가','NOBRAND','20210807','20210809','20210809','20210810',
'김은호','김은호','01024759888','01057884121','03300','서울 은평구 녹번동','녹번빌라102호','문앞에두세요','엘리베이터없음',
'BEFORE','5000','0','LS01SF0B220031-1','[BS008]_빅쉐어_린지홈_아멜리아 패브릭 소파베드_3인+스툴_에메랄드 그린_RAF1K_LS01ZHAF1K003_1-1_스툴',1,
'8000','Y','TEST ORDER','ERP_SO','GENIUS'

SELECT * FROM TBL_MTRL_M WHERE MTRL_CD ='LS01SF0B220031-1'

**********************************/
    AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN

    /************************************************************************************************************************
    기준정보에서 값을 가져오기
    ************************************************************************************************************************/
-- 기준정보에서 값을 가져온다.
    DECLARE
@V_CUST_PRC FLOAT
        ,@V_SALE_PRC FLOAT
        ,@V_FACT_PRC FLOAT
        ,@V_PURC_PRC FLOAT
        ,@V_SIZE_NM VARCHAR(100)
        ,@V_L INT
        ,@V_D INT
        ,@V_H INT
        ,@V_H2 INT
        ,@V_MTRL_CTGR_1 VARCHAR(50)
        ,@V_MTRL_CTGR_2 VARCHAR(50)
        ,@V_MTRL_CTGR_3 VARCHAR(50)
        ,@V_SET_YN VARCHAR(10)
        ,@V_BOM_YN VARCHAR(10)
        ,@V_MTO_YN VARCHAR(10)
        ,@V_STD_YN VARCHAR(10)

        ,@V_INST_YN VARCHAR(10)
        ,@V_INST_CTGR VARCHAR(50)
        ,@V_INST_TYPE VARCHAR(50)
        ,@V_INST_SEAT_TYPE VARCHAR(50)
        ,@V_ZONE_TYPE VARCHAR(20)
        ,@V_INST_COST FLOAT

        ,@V_DLVY_YN VARCHAR(10)
        ,@V_DLVY_TYPE VARCHAR(50)
        ,@V_DLVY_COST FLOAT

        ,@V_DLVY_CNFM_DT VARCHAR(10)
        ,@V_NDLVY_YN VARCHAR(10)
        ,@V_THR_DLVY_YN VARCHAR(10)
        ,@V_MTRL_CD VARCHAR(100)


SELECT @V_CUST_PRC = M.CUST_PRC
     , @V_SALE_PRC = M.SALE_PRC
     , @V_FACT_PRC = M.FACT_PRC
     , @V_PURC_PRC = M.PURC_PRC
     , @V_SIZE_NM = M.SIZE_NM
     , @V_L = M.LNTH
     , @V_D = M.DPTH
     , @V_H = M.HGHT
     , @V_H2 = M.HGHT2
     , @V_MTRL_CTGR_1 = M.MTRL_CTGR_1
     , @V_MTRL_CTGR_2 = M.MTRL_CTGR_2
     , @V_MTRL_CTGR_3 = M.MTRL_CTGR_3
     , @V_SET_YN = M.SET_YN
     , @V_BOM_YN = M.BOM_YN
     , @V_MTO_YN = M.MTO_YN
     , @V_STD_YN = M.STD_YN

     , @V_INST_YN = M.INST_YN
     , @V_INST_CTGR = M.INST_CTGR
     , @V_INST_TYPE = M.INST_TYPE
     , @V_INST_SEAT_TYPE = M.INST_SEAT_TYPE
     , @V_ZONE_TYPE = ISNULL(M.ZONE_TYPE, '1000_ZONE_CD')


     , @V_DLVY_YN = M.DLVY_YN
     , @V_DLVY_TYPE = M.DLVY_TYPE
     , @V_DLVY_COST = M.DLVY_COST


     , @V_NDLVY_YN = M.NDLVY_YN
     , @V_THR_DLVY_YN = M.THR_DLVY_YN
     , @V_MTRL_CD = M.MTRL_CD

FROM TBL_MTRL_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.MTRL_CD = @I_PROD_CD
  AND M.AGNT_CD = @I_AGNT_CD

DECLARE
@V_ZONE_CD VARCHAR(20)

    --SELECT @V_ZONE_TYPE = ZONE_TYPE FROM TBL_MTRL_M AS MM WITH(NOLOCK) WHERE MM.MTRL_CD = @I_PROD_CD
-- 권역을 업데이트 합니다.
SELECT @V_ZONE_CD = ISNULL(ZM.ZONE_CD, '1000')
FROM TBL_LGST_ZONEPOST_M AS ZM WITH (NOLOCK)
WHERE ZM.ZONE_TYPE = ISNULL(@V_ZONE_TYPE
    , '1000_ZONE_CD')
  AND ZM.POST_CD = @I_POST_CD

    /************************************************************************************************************************************
    전화번호 검증 및 치환하기
    ************************************************************************************************************************************/
-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF ISNULL(@I_ACPT_TEL2
    , '') = ''
BEGIN
            SET
@I_ACPT_TEL2 = @I_ACPT_TEL1
END

-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF LEFT
(ISNULL(@I_ACPT_TEL1, ''), 3) = '010' AND LEFT(ISNULL(@I_ACPT_TEL2, ''), 3) <> '010'
BEGIN
            SET
@I_ACPT_TEL2 = @I_ACPT_TEL1
END

-- 번호 채번하기
    DECLARE
@V_SRCH_NO VARCHAR(100) ,@RTN_YN VARCHAR(100), @RTN_MSG VARCHAR(200)
    DECLARE
@V_ADDR VARCHAR(100), @V_ADDR_ROAD VARCHAR(100) , @V_ADDR_SMRY VARCHAR(100)

SELECT @V_ADDR = ADDR, @V_ADDR_ROAD = ADDR_ROAD, @V_ADDR_SMRY = ADDR_SMRY
FROM TBL_POST_M AS PM WITH (NOLOCK)
WHERE PM.POST_CD = @I_POST_CD

/************************************************************************************************************************************
상품정보체크
************************************************************************************************************************************/
    IF (ISNULL(@V_MTRL_CD
    , '') = '')
BEGIN
SELECT @I_EXCL_ROW                   AS EXCL_ROW,
       'N'                           AS RTN_YN,
       '등록된상품코드가 없습니다. 상품코드를 확인하세요.' AS RTN_MSG,
       @I_AGNT_CD                    AS AGNT_CD,
       @I_REF_SO_NO                  AS REF_SO_NO,
       @V_SRCH_NO                    AS SO_NO,
       @I_ACPT_ER                    AS ACPT_ER,
       @I_PROD_CD                    AS PROD_CD,
       @I_PROD_NM                    AS PROD_NM,
       @I_QTY                        AS QTY
    RETURN

END


/************************************************************************************************************************************
수취인정보
************************************************************************************************************************************/
    IF
(ISNULL(@I_ACPT_ER, '') = '')
BEGIN
SELECT @I_EXCL_ROW    AS EXCL_ROW,
       'N'            AS RTN_YN,
       '수령인(명) 없습니다.' AS RTN_MSG,
       @I_AGNT_CD     AS AGNT_CD,
       @I_REF_SO_NO   AS REF_SO_NO,
       @V_SRCH_NO     AS SO_NO,
       @I_ACPT_ER     AS ACPT_ER,
       @I_PROD_CD     AS PROD_CD,
       @I_PROD_NM     AS PROD_NM,
       @I_QTY         AS QTY
    RETURN

END

/************************************************************************************************************************************

,@I_ACPT_ER			VARCHAR(50)		--수취인(수령인)
,@I_ORDR_ER			VARCHAR(50)		--주문자
,@I_ACPT_TEL1		VARCHAR(20)		--연락처1
,@I_ACPT_TEL2		VARCHAR(20)     --연락처2
,@I_POST_CD			VARCHAR(10)		--우편번호

SET @I_RCPT_DT = REPLACE(REPLACE(@I_RCPT_DT,'-',''),'.','')
SET @I_RQST_DT = REPLACE(REPLACE(@I_RQST_DT,'-',''),'.','')
SET @I_DLVY_CNFM_DT = REPLACE(REPLACE(@I_DLVY_CNFM_DT,'-',''),'.','')

************************************************************************************************************************************/
    IF
(ISNULL(@I_ACPT_TEL1, '') = '' AND ISNULL(@I_ACPT_TEL2, '') = '')
BEGIN
SELECT @I_EXCL_ROW         AS EXCL_ROW,
       'N'                 AS RTN_YN,
       '수취인 전화번호가 누락입니다. ' AS RTN_MSG,
       @I_AGNT_CD          AS AGNT_CD,
       @I_REF_SO_NO        AS REF_SO_NO,
       @V_SRCH_NO          AS SO_NO,
       @I_ACPT_ER          AS ACPT_ER,
       @I_PROD_CD          AS PROD_CD,
       @I_PROD_NM          AS PROD_NM,
       @I_QTY              AS QTY
    RETURN

END

    SET
@I_RCPT_DT = ISNULL(REPLACE(REPLACE(@I_RCPT_DT, '-', ''), '.', ''), '')
    SET @I_RQST_DT = ISNULL(REPLACE(REPLACE(@I_RQST_DT, '-', ''), '.', ''), '')
    SET @I_DLVY_CNFM_DT = ISNULL(REPLACE(REPLACE(@I_DLVY_CNFM_DT, '-', ''), '.', ''), '')

    IF (LEN(@I_RCPT_DT) > 8 OR LEN(@I_RQST_DT) > 8 OR LEN(@I_DLVY_CNFM_DT) > 8)
BEGIN
SELECT @I_EXCL_ROW    AS EXCL_ROW,
       'N'            AS RTN_YN,
       '날짜를 다시 확인하세요' AS RTN_MSG,
       @I_AGNT_CD     AS AGNT_CD,
       @I_REF_SO_NO   AS REF_SO_NO,
       @V_SRCH_NO     AS SO_NO,
       @I_ACPT_ER     AS ACPT_ER,
       @I_PROD_CD     AS PROD_CD,
       @I_PROD_NM     AS PROD_NM,
       @I_QTY         AS QTY
    RETURN

END

    /**
    동일한 주문 체크 (수취인 + 주소 + 주문유형 + 전화번호 + 등록일)
    */
-- 입력값 정리
    SET
@I_ACPT_ER = LTRIM(RTRIM(ISNULL(@I_ACPT_ER, '')))
    SET @I_ADDR1 = LTRIM(RTRIM(ISNULL(@I_ADDR1, '')))
    SET @I_SO_TYPE = LTRIM(RTRIM(ISNULL(@I_SO_TYPE, '')))
    SET @I_ACPT_TEL1 = LTRIM(RTRIM(ISNULL(@I_ACPT_TEL1, '')))
    SET @I_ORDR_INPT_DT = REPLACE(REPLACE(ISNULL(@I_ORDR_INPT_DT, ''), '-', ''), '.', '')

-- 데이터 정리 함수 정의
    DECLARE
@V_CLEAN_ACPT_ER VARCHAR(50) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_ACPT_ER, CHAR(10), ''), CHAR(13), ''),
                                                               '?', ' ')))
    DECLARE
@V_CLEAN_ADDR1 VARCHAR(100) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_ADDR1, CHAR(10), ''), CHAR(13), ''),
                                                              '?', ' ')))
    DECLARE
@V_CLEAN_SO_TYPE VARCHAR(50) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_SO_TYPE, CHAR(10), ''), CHAR(13), ''),
                                                               '?', ' ')))
    DECLARE
@V_CLEAN_ACPT_TEL1 VARCHAR(20) = LTRIM(RTRIM(REPLACE(
            REPLACE(REPLACE(REPLACE(@I_ACPT_TEL1, '-', ''), CHAR(10), ''), CHAR(13), ''), '?', ' ')))

-- 동일한 주문이 이미 존재하는지 체크
    IF EXISTS (SELECT TOP 1 *
               FROM TBL_SO_M AS SM WITH (NOLOCK)
               WHERE 1 = 1
                 AND SM.USE_YN = 'Y'
                 AND SM.CMPY_CD = @I_CMPY_CD
                 AND SM.AGNT_CD = @I_AGNT_CD
                 AND LTRIM(RTRIM(SM.ACPT_ER)) = @V_CLEAN_ACPT_ER
                 AND LTRIM(RTRIM(SM.ADDR1)) = @V_CLEAN_ADDR1
                 AND LTRIM(RTRIM(SM.SO_TYPE)) = @V_CLEAN_SO_TYPE
                 AND LTRIM(RTRIM(SM.ACPT_TEL1)) = @V_CLEAN_ACPT_TEL1
                 AND SM.ORDR_INPT_DT = @I_ORDR_INPT_DT)
BEGIN
SELECT @I_EXCL_ROW                  AS EXCL_ROW,
       'N'                          AS RTN_YN,
       '이미 등록된 주문입니다. (수취인:' + @I_ACPT_ER + ', 주문유형:' + @I_SO_TYPE +
       ', 번호:' + @I_ACPT_TEL1 + ')' AS RTN_MSG,
       @I_AGNT_CD                   AS AGNT_CD,
       @I_REF_SO_NO                 AS REF_SO_NO,
       @V_SRCH_NO                   AS SO_NO,
       @I_ACPT_ER                   AS ACPT_ER,
       @I_PROD_CD                   AS PROD_CD,
       @I_PROD_NM                   AS PROD_NM,
       @I_QTY                       AS QTY
    RETURN
END

/************************************************************************************************************************************
주소정보 체크
************************************************************************************************************************************/
    IF
(ISNULL(@I_ADDR1, '') = '')
BEGIN
SELECT @I_EXCL_ROW  AS EXCL_ROW,
       'N'          AS RTN_YN,
       '주소가 없습니다.'  AS RTN_MSG,
       @I_AGNT_CD   AS AGNT_CD,
       @I_REF_SO_NO AS REF_SO_NO,
       @V_SRCH_NO   AS SO_NO,
       @I_ACPT_ER   AS ACPT_ER,
       @I_PROD_CD   AS PROD_CD,
       @I_PROD_NM   AS PROD_NM,
       @I_QTY       AS QTY
    RETURN

END


    --/************************************************************************************************************************************
--우편번호 / 주소 비교하기
--************************************************************************************************************************************/
    IF
(ISNULL(@I_POST_CD, '') = '')
BEGIN
SELECT @I_EXCL_ROW             AS EXCL_ROW,
       'N'                     AS RTN_YN,
       '우편번호가 없습니다. 엑셀을 확인하세요' AS RTN_MSG,
       @I_AGNT_CD              AS AGNT_CD,
       @I_REF_SO_NO            AS REF_SO_NO,
       @V_SRCH_NO              AS SO_NO,
       @I_ACPT_ER              AS ACPT_ER,
       @I_PROD_CD              AS PROD_CD,
       @I_PROD_NM              AS PROD_NM,
       @I_QTY                  AS QTY
    RETURN

END


    /*
    IF (LEFT(@V_ADDR,5) <> LEFT(@I_ADDR1,5) ) AND   (LEFT(@V_ADDR_ROAD,5) <> LEFT(@I_ADDR1,5) ) AND  (LEFT(@V_ADDR_SMRY,5) <> LEFT(@I_ADDR1,5) )  BEGIN
         SELECT 	@I_EXCL_ROW AS EXCL_ROW,
                    'N'  AS  RTN_YN,
                    '['+@I_POST_CD + ']'+ @V_ADDR_SMRY +'은 입력하신 주소와 다릅니다.' AS RTN_MSG,
                    @I_AGNT_CD  AS AGNT_CD ,
                    @I_REF_SO_NO AS REF_SO_NO ,
                    @V_SRCH_NO AS SO_NO,
                    @I_ACPT_ER AS ACPT_ER,
                    @I_PROD_CD AS PROD_CD,
                    @I_PROD_NM AS PROD_NM,
                    @I_QTY AS QTY
            RETURN

    END

    */
--SELECT ISNULL(@V_INST_CTGR,'') ,  ISNULL(@V_INST_TYPE,'') , ISNULL(@V_INST_SEAT_TYPE,'') , ISNULL(@V_ZONE_TYPE,'')
    IF
(ISNULL(@V_INST_CTGR, '') = '' OR ISNULL(@V_INST_TYPE, '') = '' OR ISNULL(@V_INST_SEAT_TYPE, '') = '' OR
         ISNULL(@V_ZONE_TYPE, '') = '')
BEGIN
SELECT @I_EXCL_ROW                           AS EXCL_ROW,
       'N'                                   AS RTN_YN,
       '시공관련 기준정보 누락입니다. 얼라이언스 담당자에게 문의하세요.' AS RTN_MSG,
       @I_AGNT_CD                            AS AGNT_CD,
       @I_REF_SO_NO                          AS REF_SO_NO,
       @V_SRCH_NO                            AS SO_NO,
       @I_ACPT_ER                            AS ACPT_ER,
       @I_PROD_CD                            AS PROD_CD,
       @I_PROD_NM                            AS PROD_NM,
       @I_QTY                                AS QTY
    RETURN

END


    IF
(ISNULL(@I_SO_TYPE, '') NOT IN (SELECT COMD_CD FROM TBL_COMM_M WHERE COMM_CD = 'SO_ORDR_TYPE'))
BEGIN
SELECT @I_EXCL_ROW       AS EXCL_ROW,
       'N'               AS RTN_YN,
       '주문(오더)유형을 입력하세요' AS RTN_MSG,
       @I_AGNT_CD        AS AGNT_CD,
       @I_REF_SO_NO      AS REF_SO_NO,
       @V_SRCH_NO        AS SO_NO,
       @I_ACPT_ER        AS ACPT_ER,
       @I_PROD_CD        AS PROD_CD,
       @I_PROD_NM        AS PROD_NM,
       @I_QTY            AS QTY
    RETURN

END


SELECT @I_EXCL_ROW  AS EXCL_ROW,
       'Y'          AS RTN_YN,
       '체크 이상없음'    AS RTN_MSG,
       @I_AGNT_CD   AS AGNT_CD,
       @I_REF_SO_NO AS REF_SO_NO,
       @V_SRCH_NO   AS SO_NO,
       @I_ACPT_ER   AS ACPT_ER,
       @I_PROD_CD   AS PROD_CD,
       @I_PROD_NM   AS PROD_NM,
       @I_QTY       AS QTY
    RETURN


END
go

CREATE PROCEDURE dbo.USP_SO_EXCL_INS_CHK(
    @I_EXCL_ROW bigINT
, @I_CMPY_CD VARCHAR(10)
, @I_REF_SO_NO VARCHAR(100) --참조번호
, @I_ORGN_SO_NO VARCHAR(100) --원주문번호
, @I_SO_TYPE VARCHAR(50) --오더유형
, @I_SO_STAT_CD VARCHAR(50) --오더상태
, @I_AGNT_CD VARCHAR(10) --화주코드
, @I_MALL_CD VARCHAR(50) --쇼핑몰
, @I_BRAND VARCHAR(50) --브랜드
, @I_ORDR_INPT_DT VARCHAR(10) --주문입력일
, @I_RCPT_DT VARCHAR(10) --주문접수일
, @I_RQST_DT VARCHAR(10) --배송희망일
, @I_DLVY_CNFM_DT VARCHAR(10) --배송확정일

, @I_ACPT_ER VARCHAR(50) --수취인(수령인)
, @I_ORDR_ER VARCHAR(50) --주문자
, @I_ACPT_TEL1 VARCHAR(20) --연락처1
, @I_ACPT_TEL2 VARCHAR(20) --연락처2
, @I_POST_CD VARCHAR(10) --우편번호
, @I_ADDR1 VARCHAR(100) --주소1
, @I_ADDR2 VARCHAR(200) --주소2
, @I_DLVY_RQST_MSG VARCHAR(500) --배송메시지
, @I_CUST_SPCL_TXT VARCHAR(500) --고객특이사항

, @I_COST_TYPE VARCHAR(10) --운임구분
, @I_RCPT_COST VARCHAR(20) --착불비
, @I_PASS_COST VARCHAR(20) --통행비

, @I_PROD_CD VARCHAR(100) -- 제품코드
, @I_PROD_NM VARCHAR(200) -- 제품명
, @I_QTY FLOAT -- 수량

, @I_DC_CD VARCHAR(10) --물류센터
, @I_USE_YN VARCHAR(50) --사용유무
, @I_MEMO VARCHAR(10)
, @I_INPT_SYS VARCHAR(10) -- 입력시스템  : ERP_SO , ERP_EXCL
, @I_SAVE_USER VARCHAR(30)
)


/**********************************
1. 목적&기능 : 주문정보 (엑셀업로드) 체크,
2. 변경 내용 및 사유 :
3. 실행예시 :

SELECT * FROM TBL_POST_M WHERE SIGU_NM LIKE '은평구%'
USP_SO_EXCL_INS_CHK  1,'A','123123123','','1000','1000','8009','11번가','NOBRAND','20210807','20210809','20210809','20210810',
'김은호','김은호','01024759888','01057884121','03300','서울 은평구 녹번동','녹번빌라102호','문앞에두세요','엘리베이터없음',
'BEFORE','5000','0','LS01SF0B220031-1','[BS008]_빅쉐어_린지홈_아멜리아 패브릭 소파베드_3인+스툴_에메랄드 그린_RAF1K_LS01ZHAF1K003_1-1_스툴',1,
'8000','Y','TEST ORDER','ERP_SO','GENIUS'

SELECT * FROM TBL_MTRL_M WHERE MTRL_CD ='LS01SF0B220031-1'

**********************************/
    AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN

    /************************************************************************************************************************
    기준정보에서 값을 가져오기
    ************************************************************************************************************************/
-- 기준정보에서 값을 가져온다.
    DECLARE
@V_CUST_PRC FLOAT
        ,@V_SALE_PRC FLOAT
        ,@V_FACT_PRC FLOAT
        ,@V_PURC_PRC FLOAT
        ,@V_SIZE_NM VARCHAR(100)
        ,@V_L INT
        ,@V_D INT
        ,@V_H INT
        ,@V_H2 INT
        ,@V_MTRL_CTGR_1 VARCHAR(50)
        ,@V_MTRL_CTGR_2 VARCHAR(50)
        ,@V_MTRL_CTGR_3 VARCHAR(50)
        ,@V_SET_YN VARCHAR(10)
        ,@V_BOM_YN VARCHAR(10)
        ,@V_MTO_YN VARCHAR(10)
        ,@V_STD_YN VARCHAR(10)

        ,@V_INST_YN VARCHAR(10)
        ,@V_INST_CTGR VARCHAR(50)
        ,@V_INST_TYPE VARCHAR(50)
        ,@V_INST_SEAT_TYPE VARCHAR(50)
        ,@V_ZONE_TYPE VARCHAR(20)
        ,@V_INST_COST FLOAT

        ,@V_DLVY_YN VARCHAR(10)
        ,@V_DLVY_TYPE VARCHAR(50)
        ,@V_DLVY_COST FLOAT

        ,@V_DLVY_CNFM_DT VARCHAR(10)
        ,@V_NDLVY_YN VARCHAR(10)
        ,@V_THR_DLVY_YN VARCHAR(10)
        ,@V_MTRL_CD VARCHAR(100)


SELECT @V_CUST_PRC = M.CUST_PRC
     , @V_SALE_PRC = M.SALE_PRC
     , @V_FACT_PRC = M.FACT_PRC
     , @V_PURC_PRC = M.PURC_PRC
     , @V_SIZE_NM = M.SIZE_NM
     , @V_L = M.LNTH
     , @V_D = M.DPTH
     , @V_H = M.HGHT
     , @V_H2 = M.HGHT2
     , @V_MTRL_CTGR_1 = M.MTRL_CTGR_1
     , @V_MTRL_CTGR_2 = M.MTRL_CTGR_2
     , @V_MTRL_CTGR_3 = M.MTRL_CTGR_3
     , @V_SET_YN = M.SET_YN
     , @V_BOM_YN = M.BOM_YN
     , @V_MTO_YN = M.MTO_YN
     , @V_STD_YN = M.STD_YN

     , @V_INST_YN = M.INST_YN
     , @V_INST_CTGR = M.INST_CTGR
     , @V_INST_TYPE = M.INST_TYPE
     , @V_INST_SEAT_TYPE = M.INST_SEAT_TYPE
     , @V_ZONE_TYPE = ISNULL(M.ZONE_TYPE, '1000_ZONE_CD')


     , @V_DLVY_YN = M.DLVY_YN
     , @V_DLVY_TYPE = M.DLVY_TYPE
     , @V_DLVY_COST = M.DLVY_COST


     , @V_NDLVY_YN = M.NDLVY_YN
     , @V_THR_DLVY_YN = M.THR_DLVY_YN
     , @V_MTRL_CD = M.MTRL_CD

FROM TBL_MTRL_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.MTRL_CD = @I_PROD_CD
  AND M.AGNT_CD = @I_AGNT_CD

DECLARE
@V_ZONE_CD VARCHAR(20)

    --SELECT @V_ZONE_TYPE = ZONE_TYPE FROM TBL_MTRL_M AS MM WITH(NOLOCK) WHERE MM.MTRL_CD = @I_PROD_CD
-- 권역을 업데이트 합니다.
SELECT @V_ZONE_CD = ISNULL(ZM.ZONE_CD, '1000')
FROM TBL_LGST_ZONEPOST_M AS ZM WITH (NOLOCK)
WHERE ZM.ZONE_TYPE = ISNULL(@V_ZONE_TYPE
    , '1000_ZONE_CD')
  AND ZM.POST_CD = @I_POST_CD

    /************************************************************************************************************************************
    전화번호 검증 및 치환하기
    ************************************************************************************************************************************/
-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF ISNULL(@I_ACPT_TEL2
    , '') = ''
BEGIN
            SET
@I_ACPT_TEL2 = @I_ACPT_TEL1
END

-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF LEFT
(ISNULL(@I_ACPT_TEL1, ''), 3) = '010' AND LEFT(ISNULL(@I_ACPT_TEL2, ''), 3) <> '010'
BEGIN
            SET
@I_ACPT_TEL2 = @I_ACPT_TEL1
END

-- 번호 채번하기
    DECLARE
@V_SRCH_NO VARCHAR(100) ,@RTN_YN VARCHAR(100), @RTN_MSG VARCHAR(200)
    DECLARE
@V_ADDR VARCHAR(100), @V_ADDR_ROAD VARCHAR(100) , @V_ADDR_SMRY VARCHAR(100)

SELECT @V_ADDR = ADDR, @V_ADDR_ROAD = ADDR_ROAD, @V_ADDR_SMRY = ADDR_SMRY
FROM TBL_POST_M AS PM WITH (NOLOCK)
WHERE PM.POST_CD = @I_POST_CD

/************************************************************************************************************************************
상품정보체크
************************************************************************************************************************************/
    IF (ISNULL(@V_MTRL_CD
    , '') = '')
BEGIN
SELECT @I_EXCL_ROW                   AS EXCL_ROW,
       'N'                           AS RTN_YN,
       '등록된상품코드가 없습니다. 상품코드를 확인하세요.' AS RTN_MSG,
       @I_AGNT_CD                    AS AGNT_CD,
       @I_REF_SO_NO                  AS REF_SO_NO,
       @V_SRCH_NO                    AS SO_NO,
       @I_ACPT_ER                    AS ACPT_ER,
       @I_PROD_CD                    AS PROD_CD,
       @I_PROD_NM                    AS PROD_NM,
       @I_QTY                        AS QTY
    RETURN

END


/************************************************************************************************************************************
수취인정보
************************************************************************************************************************************/
    IF
(ISNULL(@I_ACPT_ER, '') = '')
BEGIN
SELECT @I_EXCL_ROW    AS EXCL_ROW,
       'N'            AS RTN_YN,
       '수령인(명) 없습니다.' AS RTN_MSG,
       @I_AGNT_CD     AS AGNT_CD,
       @I_REF_SO_NO   AS REF_SO_NO,
       @V_SRCH_NO     AS SO_NO,
       @I_ACPT_ER     AS ACPT_ER,
       @I_PROD_CD     AS PROD_CD,
       @I_PROD_NM     AS PROD_NM,
       @I_QTY         AS QTY
    RETURN

END

/************************************************************************************************************************************

,@I_ACPT_ER			VARCHAR(50)		--수취인(수령인)
,@I_ORDR_ER			VARCHAR(50)		--주문자
,@I_ACPT_TEL1		VARCHAR(20)		--연락처1
,@I_ACPT_TEL2		VARCHAR(20)     --연락처2
,@I_POST_CD			VARCHAR(10)		--우편번호

SET @I_RCPT_DT = REPLACE(REPLACE(@I_RCPT_DT,'-',''),'.','')
SET @I_RQST_DT = REPLACE(REPLACE(@I_RQST_DT,'-',''),'.','')
SET @I_DLVY_CNFM_DT = REPLACE(REPLACE(@I_DLVY_CNFM_DT,'-',''),'.','')

************************************************************************************************************************************/
    IF
(ISNULL(@I_ACPT_TEL1, '') = '' AND ISNULL(@I_ACPT_TEL2, '') = '')
BEGIN
SELECT @I_EXCL_ROW         AS EXCL_ROW,
       'N'                 AS RTN_YN,
       '수취인 전화번호가 누락입니다. ' AS RTN_MSG,
       @I_AGNT_CD          AS AGNT_CD,
       @I_REF_SO_NO        AS REF_SO_NO,
       @V_SRCH_NO          AS SO_NO,
       @I_ACPT_ER          AS ACPT_ER,
       @I_PROD_CD          AS PROD_CD,
       @I_PROD_NM          AS PROD_NM,
       @I_QTY              AS QTY
    RETURN

END

    SET
@I_RCPT_DT = ISNULL(REPLACE(REPLACE(@I_RCPT_DT, '-', ''), '.', ''), '')
    SET @I_RQST_DT = ISNULL(REPLACE(REPLACE(@I_RQST_DT, '-', ''), '.', ''), '')
    SET @I_DLVY_CNFM_DT = ISNULL(REPLACE(REPLACE(@I_DLVY_CNFM_DT, '-', ''), '.', ''), '')

    IF (LEN(@I_RCPT_DT) > 8 OR LEN(@I_RQST_DT) > 8 OR LEN(@I_DLVY_CNFM_DT) > 8)
BEGIN
SELECT @I_EXCL_ROW    AS EXCL_ROW,
       'N'            AS RTN_YN,
       '날짜를 다시 확인하세요' AS RTN_MSG,
       @I_AGNT_CD     AS AGNT_CD,
       @I_REF_SO_NO   AS REF_SO_NO,
       @V_SRCH_NO     AS SO_NO,
       @I_ACPT_ER     AS ACPT_ER,
       @I_PROD_CD     AS PROD_CD,
       @I_PROD_NM     AS PROD_NM,
       @I_QTY         AS QTY
    RETURN

END

    /**
    동일한 주문 체크 (수취인 + 주소 + 주문유형 + 전화번호 + 등록일)
    */
-- 입력값 정리
    SET
@I_ACPT_ER = LTRIM(RTRIM(ISNULL(@I_ACPT_ER, '')))
    SET @I_ADDR1 = LTRIM(RTRIM(ISNULL(@I_ADDR1, '')))
    SET @I_SO_TYPE = LTRIM(RTRIM(ISNULL(@I_SO_TYPE, '')))
    SET @I_ACPT_TEL1 = LTRIM(RTRIM(ISNULL(@I_ACPT_TEL1, '')))
    SET @I_ORDR_INPT_DT = REPLACE(REPLACE(ISNULL(@I_ORDR_INPT_DT, ''), '-', ''), '.', '')

-- 데이터 정리 함수 정의
    DECLARE
@V_CLEAN_ACPT_ER VARCHAR(50) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_ACPT_ER, CHAR(10), ''), CHAR(13), ''),
                                                               '?', ' ')))
    DECLARE
@V_CLEAN_ADDR1 VARCHAR(100) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_ADDR1, CHAR(10), ''), CHAR(13), ''),
                                                              '?', ' ')))
    DECLARE
@V_CLEAN_SO_TYPE VARCHAR(50) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_SO_TYPE, CHAR(10), ''), CHAR(13), ''),
                                                               '?', ' ')))
    DECLARE
@V_CLEAN_ACPT_TEL1 VARCHAR(20) = LTRIM(RTRIM(REPLACE(
            REPLACE(REPLACE(REPLACE(@I_ACPT_TEL1, '-', ''), CHAR(10), ''), CHAR(13), ''), '?', ' ')))

-- 동일한 주문이 이미 존재하는지 체크
    IF EXISTS (SELECT TOP 1 *
               FROM TBL_SO_M AS SM WITH (NOLOCK)
               WHERE 1 = 1
                 AND SM.USE_YN = 'Y'
                 AND SM.CMPY_CD = @I_CMPY_CD
                 AND SM.AGNT_CD = @I_AGNT_CD
                 AND LTRIM(RTRIM(SM.ACPT_ER)) = @V_CLEAN_ACPT_ER
                 AND LTRIM(RTRIM(SM.ADDR1)) = @V_CLEAN_ADDR1
                 AND LTRIM(RTRIM(SM.SO_TYPE)) = @V_CLEAN_SO_TYPE
                 AND LTRIM(RTRIM(SM.ACPT_TEL1)) = @V_CLEAN_ACPT_TEL1
                 AND SM.ORDR_INPT_DT = @I_ORDR_INPT_DT)
BEGIN
SELECT @I_EXCL_ROW                  AS EXCL_ROW,
       'N'                          AS RTN_YN,
       '이미 등록된 주문입니다. (수취인:' + @I_ACPT_ER + ', 주문유형:' + @I_SO_TYPE +
       ', 번호:' + @I_ACPT_TEL1 + ')' AS RTN_MSG,
       @I_AGNT_CD                   AS AGNT_CD,
       @I_REF_SO_NO                 AS REF_SO_NO,
       @V_SRCH_NO                   AS SO_NO,
       @I_ACPT_ER                   AS ACPT_ER,
       @I_PROD_CD                   AS PROD_CD,
       @I_PROD_NM                   AS PROD_NM,
       @I_QTY                       AS QTY
    RETURN
END

/************************************************************************************************************************************
주소정보 체크
************************************************************************************************************************************/
    IF
(ISNULL(@I_ADDR1, '') = '')
BEGIN
SELECT @I_EXCL_ROW  AS EXCL_ROW,
       'N'          AS RTN_YN,
       '주소가 없습니다.'  AS RTN_MSG,
       @I_AGNT_CD   AS AGNT_CD,
       @I_REF_SO_NO AS REF_SO_NO,
       @V_SRCH_NO   AS SO_NO,
       @I_ACPT_ER   AS ACPT_ER,
       @I_PROD_CD   AS PROD_CD,
       @I_PROD_NM   AS PROD_NM,
       @I_QTY       AS QTY
    RETURN

END


    --/************************************************************************************************************************************
--우편번호 / 주소 비교하기
--************************************************************************************************************************************/
    IF
(ISNULL(@I_POST_CD, '') = '')
BEGIN
SELECT @I_EXCL_ROW             AS EXCL_ROW,
       'N'                     AS RTN_YN,
       '우편번호가 없습니다. 엑셀을 확인하세요' AS RTN_MSG,
       @I_AGNT_CD              AS AGNT_CD,
       @I_REF_SO_NO            AS REF_SO_NO,
       @V_SRCH_NO              AS SO_NO,
       @I_ACPT_ER              AS ACPT_ER,
       @I_PROD_CD              AS PROD_CD,
       @I_PROD_NM              AS PROD_NM,
       @I_QTY                  AS QTY
    RETURN

END


    /*
    IF (LEFT(@V_ADDR,5) <> LEFT(@I_ADDR1,5) ) AND   (LEFT(@V_ADDR_ROAD,5) <> LEFT(@I_ADDR1,5) ) AND  (LEFT(@V_ADDR_SMRY,5) <> LEFT(@I_ADDR1,5) )  BEGIN
         SELECT 	@I_EXCL_ROW AS EXCL_ROW,
                    'N'  AS  RTN_YN,
                    '['+@I_POST_CD + ']'+ @V_ADDR_SMRY +'은 입력하신 주소와 다릅니다.' AS RTN_MSG,
                    @I_AGNT_CD  AS AGNT_CD ,
                    @I_REF_SO_NO AS REF_SO_NO ,
                    @V_SRCH_NO AS SO_NO,
                    @I_ACPT_ER AS ACPT_ER,
                    @I_PROD_CD AS PROD_CD,
                    @I_PROD_NM AS PROD_NM,
                    @I_QTY AS QTY
            RETURN

    END

    */
--SELECT ISNULL(@V_INST_CTGR,'') ,  ISNULL(@V_INST_TYPE,'') , ISNULL(@V_INST_SEAT_TYPE,'') , ISNULL(@V_ZONE_TYPE,'')
    IF
(ISNULL(@V_INST_CTGR, '') = '' OR ISNULL(@V_INST_TYPE, '') = '' OR ISNULL(@V_INST_SEAT_TYPE, '') = '' OR
         ISNULL(@V_ZONE_TYPE, '') = '')
BEGIN
SELECT @I_EXCL_ROW                           AS EXCL_ROW,
       'N'                                   AS RTN_YN,
       '시공관련 기준정보 누락입니다. 얼라이언스 담당자에게 문의하세요.' AS RTN_MSG,
       @I_AGNT_CD                            AS AGNT_CD,
       @I_REF_SO_NO                          AS REF_SO_NO,
       @V_SRCH_NO                            AS SO_NO,
       @I_ACPT_ER                            AS ACPT_ER,
       @I_PROD_CD                            AS PROD_CD,
       @I_PROD_NM                            AS PROD_NM,
       @I_QTY                                AS QTY
    RETURN

END


    IF
(ISNULL(@I_SO_TYPE, '') NOT IN (SELECT COMD_CD FROM TBL_COMM_M WHERE COMM_CD = 'SO_ORDR_TYPE'))
BEGIN
SELECT @I_EXCL_ROW       AS EXCL_ROW,
       'N'               AS RTN_YN,
       '주문(오더)유형을 입력하세요' AS RTN_MSG,
       @I_AGNT_CD        AS AGNT_CD,
       @I_REF_SO_NO      AS REF_SO_NO,
       @V_SRCH_NO        AS SO_NO,
       @I_ACPT_ER        AS ACPT_ER,
       @I_PROD_CD        AS PROD_CD,
       @I_PROD_NM        AS PROD_NM,
       @I_QTY            AS QTY
    RETURN

END


SELECT @I_EXCL_ROW  AS EXCL_ROW,
       'Y'          AS RTN_YN,
       '체크 이상없음'    AS RTN_MSG,
       @I_AGNT_CD   AS AGNT_CD,
       @I_REF_SO_NO AS REF_SO_NO,
       @V_SRCH_NO   AS SO_NO,
       @I_ACPT_ER   AS ACPT_ER,
       @I_PROD_CD   AS PROD_CD,
       @I_PROD_NM   AS PROD_NM,
       @I_QTY       AS QTY
    RETURN


END
go

