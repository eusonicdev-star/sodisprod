CREATE PROCEDURE dbo.USP_SO_EXCL_INS(
    @I_EXCL_ROW BIGINT
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
         , @V_ZONE_TYPE = ISNULL(M.ZONE_TYPE, M.INST_CTGR + '_ZONE_CD')

         , @V_DLVY_YN = M.DLVY_YN
         , @V_DLVY_TYPE = M.DLVY_TYPE
         , @V_DLVY_COST = M.DLVY_COST

         , @V_NDLVY_YN = M.NDLVY_YN
         , @V_THR_DLVY_YN = M.THR_DLVY_YN

    FROM TBL_MTRL_M AS M WITH (NOLOCK)
    WHERE 1 = 1
      AND M.MTRL_CD = @I_PROD_CD
      AND M.AGNT_CD = @I_AGNT_CD

    DECLARE @V_ZONE_CD VARCHAR(20)

/************************************************************************************************************************************
우편번호를 권역에 있는 우편번호로 업데이트
************************************************************************************************************************************/
    IF NOT EXISTS(SELECT TOP 1 * FROM TBL_POST_M AS PM WITH (NOLOCK) WHERE PM.POST_CD = @I_POST_CD)
        BEGIN
            SELECT @I_POST_CD = PM.POST_CD
            FROM TBL_POST_M AS PM WITH (NOLOCK)
            WHERE LEFT(PM.POST_CD, 4) = LEFT(@I_POST_CD, 4)
        END

    IF NOT EXISTS(SELECT TOP 1 * FROM TBL_POST_M AS PM WITH (NOLOCK) WHERE PM.POST_CD = @I_POST_CD)
        BEGIN
            SELECT @I_POST_CD = PM.POST_CD
            FROM TBL_POST_M AS PM WITH (NOLOCK)
            WHERE LEFT(PM.POST_CD, 3) = LEFT(@I_POST_CD, 3)
        END

    IF NOT EXISTS(SELECT TOP 1 * FROM TBL_POST_M AS PM WITH (NOLOCK) WHERE PM.POST_CD = @I_POST_CD)
        BEGIN
            SELECT @I_POST_CD = PM.POST_CD
            FROM TBL_POST_M AS PM WITH (NOLOCK)
            WHERE LEFT(PM.POST_CD, 2) = LEFT(@I_POST_CD, 2)
        END

    IF NOT EXISTS(SELECT TOP 1 * FROM TBL_POST_M AS PM WITH (NOLOCK) WHERE PM.POST_CD = @I_POST_CD)
        BEGIN
            SET @I_POST_CD = '01000' --> 강북구 우이동으로 세팅
        END

    /************************************************************************************************************************************
    권역을 업데이트 합니다.
    ************************************************************************************************************************************/

    SELECT @V_ZONE_CD = ISNULL(ZM.ZONE_CD, '1000')
    FROM TBL_LGST_ZONEPOST_M AS ZM WITH (NOLOCK)
    WHERE ZM.ZONE_TYPE = ISNULL(@V_ZONE_TYPE, '1000_ZONE_CD')
      AND ZM.POST_CD = @I_POST_CD

    /************************************************************************************************************************************
    전화번호 검증 및 치환하기
    ************************************************************************************************************************************/
-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF ISNULL(@I_ACPT_TEL2, '') = ''
        BEGIN
            SET @I_ACPT_TEL2 = @I_ACPT_TEL1
        END

-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF LEFT(ISNULL(@I_ACPT_TEL1, ''), 3) = '010' AND LEFT(ISNULL(@I_ACPT_TEL2, ''), 3) <> '010'
        BEGIN
            SET @I_ACPT_TEL2 = @I_ACPT_TEL1
        END

-- 번호 채번하기
    DECLARE @V_SRCH_NO VARCHAR(50) ,@RTN_YN VARCHAR(100), @RTN_MSG VARCHAR(200)
    DECLARE @V_ADDR VARCHAR(100), @V_ADDR_ROAD VARCHAR(100) , @V_ADDR_SMRY VARCHAR(100)

    SELECT @V_ADDR = ADDR, @V_ADDR_ROAD = ADDR_ROAD, @V_ADDR_SMRY = ADDR_SMRY
    FROM TBL_POST_M AS PM WITH (NOLOCK)
    WHERE PM.POST_CD = @I_POST_CD

    /************************************************************************************************************************************
                제약조건 체크하기
    ************************************************************************************************************************************/
-- 초기값 세팅
    SET @I_ORDR_INPT_DT = REPLACE(REPLACE(@I_ORDR_INPT_DT, '-', ''), '.', '')
    SET @I_RCPT_DT = REPLACE(REPLACE(@I_RCPT_DT, '-', ''), '.', '')
    SET @I_RQST_DT = REPLACE(REPLACE(@I_RQST_DT, '-', ''), '.', '')
    SET @I_DLVY_CNFM_DT = REPLACE(REPLACE(@I_DLVY_CNFM_DT, '-', ''), '.', '')

    DECLARE
        @V_TBL_SO_M_ID BIGINT
        ,@V_PROD_SEQ INT
        ,@I_ZONE_CD VARCHAR(20)

    /************************************************************************************************************************************
    수취인명 + 수취인번호 포함 중복 주문 체크
    ************************************************************************************************************************************/
-- 입력값 정리
    SET @I_REF_SO_NO = LTRIM(RTRIM(ISNULL(@I_REF_SO_NO, '')))
    SET @I_PROD_CD = LTRIM(RTRIM(ISNULL(@I_PROD_CD, '')))
    SET @I_ACPT_ER = LTRIM(RTRIM(ISNULL(@I_ACPT_ER, '')))
    SET @I_ADDR1 = LTRIM(RTRIM(ISNULL(@I_ADDR1, '')))
    SET @I_SO_TYPE = LTRIM(RTRIM(ISNULL(@I_SO_TYPE, '')))
    SET @I_ACPT_TEL1 = LTRIM(RTRIM(ISNULL(@I_ACPT_TEL1, '')))

-- 주문번호가 있는 경우: 주문번호 + 상품코드 + 주문유형 + 수취인명 + 수취인번호 + 등록일로 중복 체크
    IF @I_REF_SO_NO <> ''
        BEGIN
            IF EXISTS (SELECT TOP 1 *
                       FROM TBL_SO_M AS SM WITH (NOLOCK)
                                INNER JOIN TBL_SO_P AS SP WITH (NOLOCK) ON SM.TBL_SO_M_ID = SP.TBL_SO_M_ID
                       WHERE 1 = 1
                         AND SM.USE_YN = 'Y'
                         AND SP.USE_YN = 'Y'
                         AND SM.CMPY_CD = @I_CMPY_CD
                         AND SM.AGNT_CD = @I_AGNT_CD
                         AND LTRIM(RTRIM(CAST(SP.REF_SO_NO AS VARCHAR(100)))) = LTRIM(RTRIM(@I_REF_SO_NO))
                         AND LTRIM(RTRIM(SP.PROD_CD)) = @I_PROD_CD
                         AND SM.SO_TYPE = @I_SO_TYPE
                         AND LTRIM(RTRIM(SM.ACPT_ER)) = LTRIM(RTRIM(@I_ACPT_ER))
                         AND LTRIM(RTRIM(SM.ACPT_TEL1)) = LTRIM(RTRIM(@I_ACPT_TEL1))
                         AND SM.ORDR_INPT_DT = @I_ORDR_INPT_DT)
                BEGIN
                    SELECT @I_EXCL_ROW                  AS EXCL_ROW,
                           'N'                          AS RTN_YN,
                           '이미 등록된 주문입니다. (주문번호:' + @I_REF_SO_NO + ', 상품:' + @I_PROD_CD + ', 수취인:' + @I_ACPT_ER +
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
        END

-- 주문번호가 없는 경우: 수취인 + 주소 + 상품 + 주문유형 + 수취인명 + 수취인번호 + 등록일로 중복 체크
    ELSE
        BEGIN
            IF EXISTS (SELECT TOP 1 *
                       FROM TBL_SO_M AS SM WITH (NOLOCK)
                                INNER JOIN TBL_SO_P AS SP WITH (NOLOCK) ON SM.TBL_SO_M_ID = SP.TBL_SO_M_ID
                       WHERE 1 = 1
                         AND SM.USE_YN = 'Y'
                         AND SP.USE_YN = 'Y'
                         AND SM.CMPY_CD = @I_CMPY_CD
                         AND SM.AGNT_CD = @I_AGNT_CD
                         AND LTRIM(RTRIM(SM.ACPT_ER)) = @I_ACPT_ER
                         AND LTRIM(RTRIM(SM.ADDR1)) = @I_ADDR1
                         AND LTRIM(RTRIM(SP.PROD_CD)) = @I_PROD_CD
                         AND SM.SO_TYPE = @I_SO_TYPE
                         AND LTRIM(RTRIM(SM.ACPT_TEL1)) = LTRIM(RTRIM(@I_ACPT_TEL1))
                         AND SM.ORDR_INPT_DT = @I_ORDR_INPT_DT)
                BEGIN
                    SELECT @I_EXCL_ROW                  AS EXCL_ROW,
                           'N'                          AS RTN_YN,
                           '이미 등록된 주문입니다. (수취인:' + @I_ACPT_ER + ', 상품:' + @I_PROD_CD + ', 주문유형:' + @I_SO_TYPE +
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
        END

    /************************************************************************************************************************************
    수정된 주문 그룹핑 조건 (데이터 정리 방식 통일, 엑셀 등록일 기준)
    ************************************************************************************************************************************/
    -- 데이터 정리 함수 정의
    DECLARE @V_CLEAN_ACPT_ER VARCHAR(50) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_ACPT_ER, CHAR(10), ''), CHAR(13), ''),
                                                               '?', ' ')))
    DECLARE @V_CLEAN_ADDR1 VARCHAR(100) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_ADDR1, CHAR(10), ''), CHAR(13), ''),
                                                              '?', ' ')))
    DECLARE @V_CLEAN_SO_TYPE VARCHAR(50) = LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(@I_SO_TYPE, CHAR(10), ''), CHAR(13), ''),
                                                               '?', ' ')))
    DECLARE @V_CLEAN_ACPT_TEL1 VARCHAR(20) = LTRIM(RTRIM(REPLACE(
            REPLACE(REPLACE(REPLACE(@I_ACPT_TEL1, '-', ''), CHAR(10), ''), CHAR(13), ''), '?', ' ')))

    IF NOT EXISTS (SELECT TOP 1 *
                   FROM TBL_SO_M AS M WITH (NOLOCK)
                   WHERE 1 = 1
                     AND M.CMPY_CD = @I_CMPY_CD
                     AND M.AGNT_CD = @I_AGNT_CD
                     AND LTRIM(RTRIM(M.ACPT_ER)) = @V_CLEAN_ACPT_ER
                     AND LTRIM(RTRIM(M.ADDR1)) = @V_CLEAN_ADDR1
                     AND LTRIM(RTRIM(M.SO_TYPE)) = @V_CLEAN_SO_TYPE
                     AND LTRIM(RTRIM(M.ACPT_TEL1)) = @V_CLEAN_ACPT_TEL1
                     AND M.ORDR_INPT_DT = @I_ORDR_INPT_DT
                     AND M.USE_YN = 'Y'
                     AND M.SAVE_USER = @I_SAVE_USER
                     AND DATEDIFF(SECOND, M.SAVE_TIME, GETDATE()) <= 30)
        BEGIN

            /**************************************************************************************************************
            -- 1) 오더번호를 신규로 채번한다.
            **************************************************************************************************************/
            DECLARE @V_MMDD VARCHAR(20) ,@V_LAST_NO VARCHAR(100)
            SET @V_MMDD = RIGHT(CONVERT(VARCHAR(10), GETDATE(), 112), 6)

            IF NOT EXISTS (SELECT TOP 1 *
                           FROM TBL_COMM_NUM A WITH (NOLOCK)
                           WHERE A.CMPY_CD = @I_CMPY_CD
                             AND A.NUM_TYPE = 'SO_NO'
                             AND A.PRE_FIX = @V_MMDD)
                BEGIN
                    INSERT INTO TBL_COMM_NUM (CMPY_CD, NUM_TYPE, TYPE_DESC, PRE_FIX, LAST_NO, INPT_NO, USE_YN,
                                              SAVE_USER, SAVE_TIME)
                    VALUES (@I_CMPY_CD, 'SO_NO', 'SO_NO', @V_MMDD, '1',
                            ISNULL(@V_MMDD, '') + CONVERT(VARCHAR(20), '0001'), 'Y', @I_SAVE_USER, GETDATE())

                    SET @V_SRCH_NO = ISNULL(@V_MMDD, '') + '0001'

                END
            ELSE
                BEGIN

                    UPDATE A
                    SET LAST_NO   = CONVERT(VARCHAR(100), CONVERT(DECIMAL(20), A.LAST_NO) + 1),
                        INPT_NO   = ISNULL(@V_MMDD, '') +
                                    RIGHT('0000' + CONVERT(VARCHAR(20), CONVERT(DECIMAL(20), A.LAST_NO) + 1), 4),
                        UPDT_USER = @I_SAVE_USER,
                        UPDT_TIME = GETDATE()
                    FROM TBL_COMM_NUM A
                    WHERE 1 = 1
                      AND A.CMPY_CD = @I_CMPY_CD
                      AND A.NUM_TYPE = 'SO_NO'
                      AND A.PRE_FIX = @V_MMDD

                    SELECT @V_SRCH_NO = CONVERT(VARCHAR(100), A.INPT_NO)
                    FROM TBL_COMM_NUM A
                    WHERE 1 = 1
                      AND A.CMPY_CD = @I_CMPY_CD
                      AND A.NUM_TYPE = 'SO_NO'
                      AND A.PRE_FIX = @V_MMDD
                END

            /************************************************************************************************************************************
                         물류센터 결정
            ************************************************************************************************************************************/
            -- 물류센터는 우편번호별 DC를 자동결정합니다.
            EXEC @I_DC_CD = USP_COMM_DC_POST_OUTPUT @I_CMPY_CD, @I_POST_CD, @I_ADDR1, @I_DC_CD OUTPUT

            /************************************************************************************************************************************
                         오더 헤더 입력하기
            ************************************************************************************************************************************/
            -- 착불비가 있으면 운임은 착불로 한다.
            IF ISNULL(@I_RCPT_COST, 0) > 0
                BEGIN
                    SET @I_COST_TYPE = 'AFTER'
                END
            -- 오더유형이 없으면 일반오더
            IF ISNULL(@I_SO_TYPE, '') = ''
                BEGIN
                    SET @I_SO_TYPE = '1000'
                END -- 일반오더

            INSERT INTO TBL_SO_M ( CMPY_CD
                                 , SO_NO
                                 , REF_SO_NO
                                 , ORGN_SO_NO
                                 , SO_TYPE
                                 , SO_STAT_CD
                                 , AGNT_CD
                                 , MALL_CD
                                 , ORDR_ER
                                 , ACPT_ER
                                 , ACPT_TEL1
                                 , ACPT_TEL2
                                 , POST_CD
                                 , ADDR1
                                 , ADDR2
                                 , DC_CD
                                 , ORDR_INPT_DT
                                 , ORDR_INPT_ER
                                 , ORDR_INPT_TIME
                                 , RCPT_DT
                                 , RQST_DT
                                 , RQST_INPT_ER
                                 , RQST_INPT_TIME
                                 , DLVY_CNFM_DT
                                 , DLVY_RQST_MSG
                                 , CUST_SPCL_TXT
                                 , DLVY_COST_TYPE
                                 , RCPT_COST
                                 , PASS_COST
                                 , USE_YN
                                 , MEMO
                                 , INPT_SYS
                                 , SAVE_USER
                                 , SAVE_TIME
                                 , UPDT_USER
                                 , UPDT_TIME
                                 , ZONE_TYPE
                                 , ZONE_CD)
            VALUES ( @I_CMPY_CD
                   , @V_SRCH_NO
                   , @I_REF_SO_NO
                   , @I_ORGN_SO_NO
                   , @V_CLEAN_SO_TYPE
                   , '1000' --@I_SO_STAT_CD		 '주문접수'
                   , @I_AGNT_CD
                   , @I_MALL_CD
                   , @I_ORDR_ER
                   , @V_CLEAN_ACPT_ER
                   , @V_CLEAN_ACPT_TEL1
                   , REPLACE(@I_ACPT_TEL2, '-', '')
                   , @I_POST_CD
                   , @V_CLEAN_ADDR1
                   , @I_ADDR2
                   , ISNULL(@I_DC_CD, '1000')
                   , @I_ORDR_INPT_DT --ORDR_INPT_DT (엑셀에서 입력한 등록일 사용)
                   , @I_SAVE_USER --ORDR_INPT_ER
                   , GETDATE() --ORDR_INPT_TIME
                   , ISNULL(@I_RCPT_DT, '') --고객접수일
                   , ISNULL(@I_RQST_DT, '') --고객배송요청일
                   , @I_SAVE_USER --RQST_INPT_ER
                   , GETDATE() --RQST_INPT_TIME
                   , '' --@I_DLVY_CNFM_DT
                   , @I_DLVY_RQST_MSG
                   , @I_CUST_SPCL_TXT
                   , @I_COST_TYPE
                   , @I_RCPT_COST
                   , @I_PASS_COST
                   , 'Y'
                   , @I_MEMO
                   , 'ERP_EXCL'
                   , @I_SAVE_USER
                   , GETDATE()
                   , @I_SAVE_USER
                   , GETDATE()
                   , ISNULL(@V_ZONE_TYPE, '1000_ZONE_CD')
                   , ISNULL(@V_ZONE_CD, '1000'))

            SET @V_TBL_SO_M_ID = @@IDENTITY
            /************************************************************************************************************************************
                        아이템 넣기
            ************************************************************************************************************************************/
            -- 아이템 넣기 SP 호출
            EXEC DBO.USP_SO_P_EXCL_INS_VER2 @V_TBL_SO_M_ID, @V_SRCH_NO, @I_AGNT_CD, 1, @I_PROD_CD, @I_PROD_NM, @I_QTY,
                 '', @I_DC_CD, @I_RQST_DT, @I_DLVY_CNFM_DT, @I_DLVY_RQST_MSG,
                 'Y', 'EXCL_UPLOAD', @I_SAVE_USER, @I_REF_SO_NO,
                 @I_COST_TYPE, @I_RCPT_COST, @I_PASS_COST

            SET @RTN_YN = 'Y'
            SET @RTN_MSG = '등록성공'

        END
        -- 기존오더 있으면 헤더를 넣지 않고 아이템만 넣는다.
    ELSE
        BEGIN
            /************************************************************************************************************************************
                         아이템 넣기
            ************************************************************************************************************************************/

            /************************************************************************************************************************************
                         물류센터 결정
            ************************************************************************************************************************************/

            -- 오더가 있으면 기존오더 가져오기 (수정된 조건, 엑셀 등록일 기준)
            SELECT @V_SRCH_NO = M.SO_NO
                 , @V_TBL_SO_M_ID = M.TBL_SO_M_ID

            FROM TBL_SO_M AS M WITH (NOLOCK)
            WHERE 1 = 1
              AND M.CMPY_CD = @I_CMPY_CD
              AND M.AGNT_CD = @I_AGNT_CD
              AND LTRIM(RTRIM(M.ACPT_ER)) = @V_CLEAN_ACPT_ER
              AND LTRIM(RTRIM(M.ADDR1)) = @V_CLEAN_ADDR1
              AND LTRIM(RTRIM(M.SO_TYPE)) = @V_CLEAN_SO_TYPE
              AND LTRIM(RTRIM(M.ACPT_TEL1)) = @V_CLEAN_ACPT_TEL1
              AND M.ORDR_INPT_DT = @I_ORDR_INPT_DT
              AND M.USE_YN = 'Y'
              AND M.SAVE_USER = @I_SAVE_USER
              AND DATEDIFF(SECOND, M.SAVE_TIME, GETDATE()) <= 30

            /************************************************************************************************************************************
                        아이템 MAX 번호 구하기
            ************************************************************************************************************************************/
            SELECT @V_PROD_SEQ = MAX(PROD_SEQ) + 1 FROM TBL_SO_P AS P WITH (NOLOCK) WHERE P.TBL_SO_M_ID = @V_TBL_SO_M_ID

            /************************************************************************************************************************************
                        아이템 넣기
            ************************************************************************************************************************************/
            -- 아이템 넣기 SP 호출
            EXEC DBO.USP_SO_P_EXCL_INS_VER2 @V_TBL_SO_M_ID, @V_SRCH_NO, @I_AGNT_CD, @V_PROD_SEQ, @I_PROD_CD, @I_PROD_NM,
                 @I_QTY,
                 @I_ZONE_CD, @I_DC_CD, @I_RQST_DT, @I_DLVY_CNFM_DT, @I_DLVY_RQST_MSG,
                 'Y', 'EXCL_UPLOAD', @I_SAVE_USER, @I_REF_SO_NO,
                 @I_COST_TYPE, @I_RCPT_COST, @I_PASS_COST

            SET @RTN_YN = 'Y'
            SET @RTN_MSG = '등록성공'

        END

    /*************************************************************************
    주문히스트로에 넣는다.
    *************************************************************************/
    INSERT TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
    SELECT @V_TBL_SO_M_ID, '오더접수-1000', '오더접수-1000', '신규접수-엑셀업로드', @I_SAVE_USER, GETDATE();

    SELECT @I_EXCL_ROW  AS EXCL_ROW,
           @RTN_YN      AS RTN_YN,
           @RTN_MSG     AS RTN_MSG,
           @I_AGNT_CD   AS AGNT_CD,
           @I_REF_SO_NO AS REF_SO_NO,
           @V_SRCH_NO   AS SO_NO,
           @I_ACPT_ER   AS ACPT_ER,
           @I_PROD_CD   AS PROD_CD,
           @I_PROD_NM   AS PROD_NM,
           @I_QTY       AS QTY

END
go

