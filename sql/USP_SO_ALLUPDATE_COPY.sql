CREATE PROCEDURE [dbo].[USP_SO_ALLUPDATE_COPY](
    @I_CMPY_CD VARCHAR(20)
, @I_TBL_SO_M_ID BIGINT
, @I_SAVE_USER VARCHAR(30)
)
/**********************************
1. 목적&기능 : 주문일괄변경 (주문복사하기)
2. 변경 내용 및 사유 :
3. 실행예시 :
   USP_SO_ALLUPDATE_COPY 'A', 36792, 'TEST'

   SELECT TOP 1000 * FROM TBL_SO_M
   WHERE ORDR_INPT_DT <='20201231'
   WHERE TBL_SO_M_ID = 36792

   SELECT * FROM TBL_SO_HIST
   오더조회 '102006140008'

   SELECT * FROM TBL_SO_P AS P WHERE TBL_SO_M_ID = 36792
   SELECT * FROM TBL_SO_M WHERE SAVE_USER ='TEST'
   SELECT * FROM TBL_SO_P WHERE SAVE_USER ='TEST'

   SELECT * FROM TBL_SO_HIST WHERE TBL_SO_M_ID =279800
   SELECT * FROM TBL_SO_M WHERE TBL_SO_M_ID =279800
   SELECT * FROM TBL_SO_P WHERE TBL_SO_M_ID =279800

   DELETE A FROM TBL_SO_HIST A WHERE TBL_SO_M_ID =279800
   DELETE A FROM TBL_SO_M   A WHERE SAVE_USER ='TEST'
   DELETE A FROM TBL_SO_P   A WHERE TBL_SO_M_ID =279800
   DELETE A FROM TBL_SO_D   A WHERE TBL_SO_M_ID =279800

   SELECT * FROM TBL_POST_M
   SELECT * FROM TBL_COMM_M

   SELECT VAL FROM SPLIT_STR ('', '/')
**********************************/
AS
    SET NOCOUNT ON;
    SET LOCK_TIMEOUT 60000;
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
BEGIN
    /************************************************************************************************************************************
        변수 선언
    ************************************************************************************************************************************/
    DECLARE @V_SRCH_NO VARCHAR(50)
        , @RTN_YN VARCHAR(100)
        , @RTN_MSG VARCHAR(200);

    DECLARE @V_NEW_SO_M_ID BIGINT;

    /************************************************************************************************************************************
        1) 오더번호 신규 채번
        -- EXEC @V_SRCH_NO = USP_COMM_NUM_LOAD_OUTPUT @I_CMPY_CD,'SO_NO','',@I_SAVE_USER,@V_SRCH_NO OUTPUT
    ************************************************************************************************************************************/
    DECLARE @V_MMDD VARCHAR(20), @V_LAST_NO VARCHAR(100);
    SET @V_MMDD = RIGHT(CONVERT(VARCHAR(10), GETDATE(), 112), 6);

    IF NOT EXISTS (SELECT TOP 1 1
                   FROM TBL_COMM_NUM A WITH (NOLOCK)
                   WHERE A.CMPY_CD = @I_CMPY_CD
                     AND A.NUM_TYPE = 'SO_NO'
                     AND A.PRE_FIX = @V_MMDD)
        BEGIN
            INSERT INTO TBL_COMM_NUM
            ( CMPY_CD, NUM_TYPE, TYPE_DESC, PRE_FIX, LAST_NO, INPT_NO
            , USE_YN, SAVE_USER, SAVE_TIME)
            VALUES ( @I_CMPY_CD, 'SO_NO', 'SO_NO', @V_MMDD, '1'
                   , ISNULL(@V_MMDD, '') + CONVERT(VARCHAR(20), '0001')
                   , 'Y', @I_SAVE_USER, GETDATE());

            SET @V_SRCH_NO = ISNULL(@V_MMDD, '') + '0001';
        END
    ELSE
        BEGIN
            UPDATE A
            SET LAST_NO   = CONVERT(VARCHAR(100), CONVERT(DECIMAL(20), A.LAST_NO) + 1)
              , INPT_NO   = ISNULL(@V_MMDD, '') +
                            RIGHT('0000' + CONVERT(VARCHAR(20), CONVERT(DECIMAL(20), A.LAST_NO) + 1), 4)
              , UPDT_USER = @I_SAVE_USER
              , UPDT_TIME = GETDATE()
            FROM TBL_COMM_NUM A
            WHERE A.CMPY_CD = @I_CMPY_CD
              AND A.NUM_TYPE = 'SO_NO'
              AND A.PRE_FIX = @V_MMDD;

            SELECT @V_SRCH_NO = CONVERT(VARCHAR(100), A.INPT_NO)
            FROM TBL_COMM_NUM A
            WHERE A.CMPY_CD = @I_CMPY_CD
              AND A.NUM_TYPE = 'SO_NO'
              AND A.PRE_FIX = @V_MMDD;
        END

    /************************************************************************************************************************************
        2) TBL_SO_M 오더헤더 생성
    ************************************************************************************************************************************/
    INSERT TBL_SO_M
    ( CMPY_CD, SO_NO, REF_SO_NO, ORGN_SO_NO, SO_TYPE, SO_STAT_CD
    , AGNT_CD, MALL_CD, ORDR_ER, ACPT_ER, ACPT_TEL1, ACPT_TEL2
    , POST_CD, ADDR1, ADDR2, DC_CD
    , ORDR_INPT_DT, ORDR_INPT_ER, ORDR_INPT_TIME
    , RCPT_DT, RQST_DT, RQST_INPT_ER, RQST_INPT_TIME
    , DLVY_CNFM_DT, DLVY_RQST_MSG, CUST_SPCL_TXT
    , DLVY_COST_TYPE, RCPT_COST, PASS_COST
    , USE_YN, MEMO, INPT_SYS
    , SAVE_USER, SAVE_TIME, UPDT_USER, UPDT_TIME
    , ZONE_TYPE, ZONE_CD)
    SELECT CMPY_CD
         , @V_SRCH_NO                           AS SO_NO
         , REF_SO_NO
         , SO_NO                                AS ORGN_SO_NO
         , SO_TYPE
         , '1000'                               AS SO_STAT_CD -- 주문접수 상태
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
         , CONVERT(VARCHAR(10), GETDATE(), 112)               -- 현재 날짜
         , @I_SAVE_USER
         , GETDATE()
         , CONVERT(VARCHAR(10), GETDATE(), 112) AS RCPT_DT
         , RQST_DT
         , RQST_INPT_ER
         , RQST_INPT_TIME
         , ''                                   AS DLVY_CNFM_DT
         , DLVY_RQST_MSG
         , CUST_SPCL_TXT
         , DLVY_COST_TYPE
         , RCPT_COST
         , PASS_COST
         , USE_YN
         , MEMO
         , 'SO_COPY'                            AS INPT_SYS
         , @I_SAVE_USER
         , GETDATE()
         , @I_SAVE_USER
         , GETDATE()
         , ZONE_TYPE
         , ZONE_CD
    FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
    WHERE M.TBL_SO_M_ID = @I_TBL_SO_M_ID;

    /************************************************************************************************************************************
        3) 생성된 ID 보관
    ************************************************************************************************************************************/
    SET @V_NEW_SO_M_ID = @@IDENTITY;

    /************************************************************************************************************************************
        4) 주문상세(TBL_SO_P) 생성
    ************************************************************************************************************************************/
    INSERT TBL_SO_P(TBL_SO_M_ID, SO_NO, PROD_SEQ, PROD_CD, PROD_NM, QTY, INST_COST, UNIT, CUST_PRC, SALE_PRC, FACT_PRC,
                    PURC_PRC, SIZE_NM, L, D, H, H2, MTRL_CTGR_1, MTRL_CTGR_2, MTRL_CTGR_3, SET_YN, BOM_YN, INST_YN,
                    INST_CTGR, INST_TYPE, INST_SEAT_TYPE, ZONE_TYPE, ZONE_CD, DLVY_YN, DLVY_TYPE, DLVY_COST,
                    DC_CD, RQST_DT, RQST_MSG, DLVY_CNFM_DT, NDLVY_YN, THR_DLVY_YN, USE_YN, MEMO,
                    SAVE_USER, SAVE_TIME, UPDT_USER, UPDT_TIME, REF_SO_NO, DLVY_COST_TYPE, RCPT_COST, PASS_COST)
    SELECT @V_NEW_SO_M_ID AS TBL_SO_M_ID,
           @V_SRCH_NO     AS SO_NO,
           P.PROD_SEQ,
           P.PROD_CD,
           P.PROD_NM,
           P.QTY,
           P.INST_COST,
           P.UNIT,
           P.CUST_PRC,
           P.SALE_PRC,
           P.FACT_PRC,
           P.PURC_PRC,
           P.SIZE_NM,
           P.L,
           P.D,
           P.H,
           P.H2,
           P.MTRL_CTGR_1,
           P.MTRL_CTGR_2,
           P.MTRL_CTGR_3,
           P.SET_YN,
           P.BOM_YN,
           P.INST_YN,
           ISNULL(P.INST_CTGR, MM.INST_CTGR),
           ISNULL(P.INST_TYPE, MM.INST_TYPE),
           ISNULL(P.INST_SEAT_TYPE, MM.INST_SEAT_TYPE),
           ISNULL(P.ZONE_TYPE, MM.ZONE_TYPE),
           P.ZONE_CD,
           P.DLVY_YN,
           P.DLVY_TYPE,
           P.DLVY_COST,
           P.DC_CD,
           P.RQST_DT,
           P.RQST_MSG,
           P.DLVY_CNFM_DT,
           P.NDLVY_YN,
           P.THR_DLVY_YN,
           'Y'            AS USE_YN,
           'SO_COPY'      AS MEMO,
           @I_SAVE_USER,
           GETDATE(),
           @I_SAVE_USER,
           GETDATE(),
           P.REF_SO_NO,
           P.DLVY_COST_TYPE,
           P.RCPT_COST,
           P.PASS_COST
    FROM DBO.TBL_SO_P AS P WITH (NOLOCK)
             LEFT JOIN TBL_MTRL_M AS MM WITH (NOLOCK)
                       ON P.PROD_CD = MM.MTRL_CD
    WHERE P.TBL_SO_M_ID = @I_TBL_SO_M_ID;

    /*************************************************************************
        주문헤더의 존타입과 존을 복사
    *************************************************************************/
    UPDATE M
    SET ZONE_TYPE = P.ZONE_TYPE
    FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
             INNER JOIN DBO.TBL_SO_P AS P WITH (NOLOCK)
                        ON M.TBL_SO_M_ID = P.TBL_SO_M_ID
    WHERE M.TBL_SO_M_ID = @V_NEW_SO_M_ID
      AND M.ZONE_TYPE <> P.ZONE_TYPE;

    /*************************************************************************
        주문히스토리에 기록
    *************************************************************************/
    INSERT TBL_SO_HIST
    ( TBL_SO_M_ID
    , UPDT_BEFORE_TXT
    , UPDT_AFTER_TXT
    , UPDT_EVENT_TXT
    , SAVE_USER
    , SAVE_DATE)
    SELECT @V_NEW_SO_M_ID
         , '신규-주문복사'
         , '주문접수-1000'
         , '신규접수-주문일괄복사'
         , @I_SAVE_USER
         , GETDATE();

    /*************************************************************************
        리턴 메시지
    *************************************************************************/
    SELECT 'Y'                            AS RTN_YN
         , '주문복사완료-신규오더번호: ' + @V_SRCH_NO AS RTN_MSG
         , @I_TBL_SO_M_ID                 AS TBL_SO_M_ID;
END
go

