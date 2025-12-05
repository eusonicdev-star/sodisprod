-- 기능: 주문현황 상품 리스트
CREATE PROCEDURE [dbo].[USP_SO_REPORT_ITEM_LISTBYALL](
    @I_CMPY_CD VARCHAR(20),
    @I_DT_TYPE VARCHAR(10),
    @I_FROM_DT VARCHAR(10),
    @I_TO_DT VARCHAR(10),
    @I_AGNT_LIST VARCHAR(4000),
    @I_DC_LIST VARCHAR(4000),
    @I_SO_LIST VARCHAR(4000),
    @I_SO_TYPE_LIST VARCHAR(4000),
    @I_SO_STAT_LIST VARCHAR(4000),
    @I_MALL_NM VARCHAR(100),
    @I_ACPT_NM VARCHAR(100),
    @I_ACPT_TEL VARCHAR(100),
    @I_USE_YN VARCHAR(10),
    @I_REF_SO_LIST VARCHAR(4000),
    @I_PROD_LIST VARCHAR(4000),
    @I_PROD_NM VARCHAR(100),
    @I_ADDR VARCHAR(50),
    @I_INST_CTGR_LIST VARCHAR(1000) = NULL
)
AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN
    DECLARE @V_SO_FROM_DT VARCHAR(10), @V_SO_TO_DT VARCHAR(10) -- 주문일
    DECLARE @V_RCPT_FROM_DT VARCHAR(10), @V_RCPT_TO_DT VARCHAR(10) -- 접수일
    DECLARE @V_RQST_FROM_DT VARCHAR(10), @V_RQST_TO_DT VARCHAR(10) -- 배송요청일
    DECLARE @V_DLVY_CNFM_FROM_DT VARCHAR(10), @V_DLVY_CNFM_TO_DT VARCHAR(10) -- 배송확정일
    DECLARE @V_CONVERT_DT VARCHAR(10)
    SET @V_CONVERT_DT = ''
    SELECT @V_RCPT_FROM_DT = '20000101', @V_RCPT_TO_DT = '29991231'
    SELECT @V_DLVY_CNFM_FROM_DT = '20000101', @V_DLVY_CNFM_TO_DT = '29991231'

    IF @I_DT_TYPE = '1' -- 주문일자
        BEGIN
            SELECT @V_RCPT_FROM_DT = REPLACE(@I_FROM_DT, '-', ''), @V_RCPT_TO_DT = REPLACE(@I_TO_DT, '-', '')
            SET @V_CONVERT_DT = ''
        END

    IF @I_DT_TYPE = '3' -- 배송일자
        BEGIN
            SELECT @V_DLVY_CNFM_FROM_DT = REPLACE(@I_FROM_DT, '-', ''), @V_DLVY_CNFM_TO_DT = REPLACE(@I_TO_DT, '-', '')
            SET @V_CONVERT_DT = '29991231'
        END

    -- 판매오더나 참고번호(고객사)가 빈값이 아니면 오더번호로 조회한다.
    IF (ISNULL(@I_REF_SO_LIST, '') <> '' OR ISNULL(@I_SO_LIST, '') <> '')
        BEGIN
            SELECT DISTINCT         M.CMPY_CD,
                                    M.AGNT_CD,
                AGNT_NM           = DBO.F_COL_NM('A', 'AGNT_CD', 'AGNT_CD', M.AGNT_CD),
                                    M.SO_TYPE,
                SO_TYPE_NM        = DBO.F_COL_NM('A', 'COMM', 'SO_ORDR_TYPE', M.SO_TYPE),
                                    M.SO_STAT_CD,
                SO_STAT_NM        = DBO.F_COL_NM('A', 'COMM', 'SO_STAT_CD', M.SO_STAT_CD),
                                    M.DC_CD,
                DC_NM             = DBO.F_COL_NM('A', 'DC_CD', 'DC_CD', M.DC_CD),
                                    M.ORDR_INPT_DT,
                                    M.RCPT_DT,
                                    M.RQST_DT                                                        AS RQST_DT,
                                    M.DLVY_CNFM_DT                                                   AS DLVY_CNFM_DT,
                                    M.GI_LIFT_DT,
                                    M.MALL_CD,
                                    M.BRAND,
                                    M.SO_NO,
                                    P.REF_SO_NO,
                                    M.ACPT_ER,
                                    M.ACPT_TEL1,
                                    M.ACPT_TEL2,
                                    M.POST_CD,
                                    M.ADDR1 + ISNULL(M.ADDR2, '')                                    AS ADDR1,
                                    M.ADDR2,
                                    M.DLVY_RQST_MSG,
                                    M.CUST_SPCL_TXT,
                                    M.DLVY_COST_TYPE,
                DLVY_COST_TYPE_NM = DBO.F_COL_NM('A', 'COMM', 'DLVY_COST_TYPE', M.DLVY_COST_TYPE),
                                    M.RCPT_COST,
                                    M.PASS_COST,
                                    I.INST_DT,
                                    I.PALT_NOXX,
                                    CASE
                                        WHEN ISNULL(UM.USER_NM, '') = '' THEN ISNULL(M.DIST_CHAN_CD, '')
                                        ELSE ISNULL(UM2.USER_NM, UM.USER_NM)
                                        END                                                          AS INST_ER,
                                    ISNULL(UM2.TBL_USER_M_ID, UM.TBL_USER_M_ID)                      AS INST_ER_ID,
                                    CASE
                                        WHEN ISNULL(UM.TEAM, '') = '' THEN ISNULL(M.BRAND, '')
                                        ELSE ISNULL(UM2.TEAM, UM.TEAM) + '팀' END                     AS TEAM,
                                    ISNULL(ISNULL(MC.MEMO, MXC.MEMO), '')                            AS INST_MEMO,
                                    I.INST_COST,
                                    I.DLVY_COST,
                                    DBO.F_COL_NM(M.CMPY_CD, 'COMM', 'DLVY_STAT_CD', IM.DLVY_STAT_CD) AS DLVY_STAT_NM,
                                    ISNULL(IMD.PROD_LIFT_CMPL_YN, 'N')                               AS LIFT_YN, --상차유무
                                    P.PROD_SEQ,
                -- 상품코드 (FERT)
                                    ISNULL(BM.MTRL_CD, P.PROD_CD)                                    AS PROD_CD,
                                    ISNULL(MM.MTRL_NM, P.PROD_NM)                                    AS PROD_NM,
                                    P.QTY                                                            AS QTY,
                                    ISNULL(BM.C_MTRL_CD, P.PROD_CD)                                  AS C_PROD_CD,
                                    ISNULL(MM2.MTRL_NM, P.PROD_NM)                                   AS C_PROD_NM,
                                    ISNULL(BM.QTY * P.QTY, P.QTY)                                    AS C_QTY,
                                    M.FROM_ER,
                                    M.FROM_TEL1,
                                    M.FROM_TEL2,
                                    M.FROM_POST_CD,
                                    M.FROM_ADDR1,
                                    M.FROM_ADDR2,
                                    M.DIST_CHAN_CD,
                                    M.DLVY_CNFM_DT,
                                    M.ORGN_SO_NO,
                                    M.USE_YN,
                                    M.DEL_RESN,
                                    M.MEMO,
                                    M.SAVE_USER,
                                    M.SAVE_TIME,
                                    M.UPDT_USER,
                                    M.UPDT_TIME,
                                    P.INST_CTGR,
                INST_CTGR_NM      = DBO.F_COL_NM('A', 'COMM', 'INST_CAPA_CTGR', P.INST_CTGR)
            FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
                     INNER JOIN DBO.TBL_SO_P AS P WITH (NOLOCK) ON M.TBL_SO_M_ID = P.TBL_SO_M_ID
                     LEFT OUTER JOIN TBL_BOM_M AS BM WITH (NOLOCK)
                                     ON P.PROD_CD = BM.MTRL_CD AND M.AGNT_CD = BM.AGNT_CD AND BM.USE_YN = 'Y'
                     LEFT OUTER JOIN TBL_MTRL_M AS MM WITH (NOLOCK)
                                     ON BM.MTRL_CD = MM.MTRL_CD AND BM.AGNT_CD = MM.AGNT_CD
                     LEFT OUTER JOIN TBL_MTRL_M AS MM2 WITH (NOLOCK)
                                     ON BM.C_MTRL_CD = MM2.MTRL_CD AND BM.AGNT_CD = MM2.AGNT_CD
                     LEFT OUTER JOIN DBO.TBL_INST_PLAN_M AS I WITH (NOLOCK) ON M.TBL_SO_M_ID = I.TBL_SO_M_ID
                     LEFT OUTER JOIN TBL_USER_M AS UM WITH (NOLOCK) ON I.TBL_USER_M_ID = UM.TBL_USER_M_ID
                     LEFT OUTER JOIN TBL_INST_MOBILE_M AS IM WITH (NOLOCK) ON M.TBL_SO_M_ID = IM.TBL_SO_M_ID
                     LEFT OUTER JOIN TBL_INST_MOBILE_D AS IMD WITH (NOLOCK)
                                     ON P.TBL_SO_M_ID = IMD.TBL_SO_M_ID AND P.TBL_SO_P_ID = IMD.TBL_SO_P_ID AND
                                        IM.INST_MOBILE_M_ID = IMD.INST_MOBILE_M_ID
                                         --20220325 정연호 추가. 부분상차 일때 TBL_INST_MOBILE_D의 PROD_LIFT_CMPL_YN 때문에 뻥튀기 되어 나옴. 해당하는 하위품목 코드의 상차여부만 구하게 수정
                                         AND IMD.PROD_CD = ISNULL(BM.C_MTRL_CD, P.PROD_CD)
                     LEFT OUTER JOIN TBL_INST_MOBILE_COMPLETE AS MC WITH (NOLOCK)
                                     ON IM.INST_MOBILE_M_ID = MC.INST_MOBILE_M_ID
                     LEFT OUTER JOIN TBL_INST_MOBILE_X_COMPLETE AS MXC WITH (NOLOCK)
                                     ON IM.INST_MOBILE_M_ID = MXC.INST_MOBILE_M_ID
                     LEFT OUTER JOIN TBL_USER_M AS UM2 WITH (NOLOCK) ON UM2.TBL_USER_M_ID = IM.EXEC_USER_M_ID
            WHERE 1 = 1
              AND P.USE_YN = 'Y'
              AND M.CMPY_CD = @I_CMPY_CD
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_SO_LIST, ''), ',') AS T
                          WHERE T.VAL = M.SO_NO
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_SO_LIST, '') = '')
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_REF_SO_LIST, ''), ',') AS T
                          WHERE T.VAL = P.REF_SO_NO
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_REF_SO_LIST, '') = '')
            ORDER BY M.ORDR_INPT_DT, M.AGNT_CD, M.SO_NO, P.PROD_SEQ, PROD_CD
        END
    ELSE
        BEGIN
            SELECT DISTINCT                     M.CMPY_CD


                          ,                     M.AGNT_CD
                          , AGNT_NM           = DBO.F_COL_NM('A', 'AGNT_CD', 'AGNT_CD', M.AGNT_CD)                               -- 화주명
                          ,                     M.SO_TYPE
                          , SO_TYPE_NM        = DBO.F_COL_NM('A', 'COMM', 'SO_ORDR_TYPE', M.SO_TYPE)                             -- 오더유형
                          ,                     M.SO_STAT_CD
                          , SO_STAT_NM        = DBO.F_COL_NM('A', 'COMM', 'SO_STAT_CD', M.SO_STAT_CD)                            -- 오더상태
                          ,                     M.DC_CD                                                                          -- 물류센터
                          , DC_NM             = DBO.F_COL_NM('A', 'DC_CD', 'DC_CD', M.DC_CD)                                     -- 물류센터명
                          ,                     M.ORDR_INPT_DT                                                                   -- 시스템등록일
                          ,                     M.RCPT_DT                                                                        -- 고객접수일
                          ,                     M.RQST_DT                                                        AS RQST_DT      -- 고객배송요청일
                          ,                     M.DLVY_CNFM_DT                                                   AS DLVY_CNFM_DT -- 배송확정일
                          ,                     M.GI_LIFT_DT                                                                     -- 공장출고일

                          ,                     M.MALL_CD                                                                        -- 쇼핑몰
                          ,                     M.BRAND
                          ,                     M.SO_NO                                                                          -- 오더번호
                          ,                     P.REF_SO_NO                                                                      --참고오더번호(고객사)
                          ,                     M.ACPT_ER                                                                        -- 수령인
                          ,                     M.ACPT_TEL1
                          ,                     M.ACPT_TEL2
                          ,                     M.POST_CD
                          ,                     M.ADDR1 + ISNULL(M.ADDR2, '')                                    AS ADDR1
                          ,                     M.ADDR2
                          ,                     M.DLVY_RQST_MSG                                                                  -- 배송메시지
                          ,                     M.CUST_SPCL_TXT                                                                  -- 고객사 특이사항

                          ,                     M.DLVY_COST_TYPE
                          , DLVY_COST_TYPE_NM = DBO.F_COL_NM('A', 'COMM', 'DLVY_COST_TYPE', M.DLVY_COST_TYPE)                    -- 오더상태
                          ,                     M.RCPT_COST                                                                      -- 착불비
                          ,                     M.PASS_COST                                                                      -- 통행료


                          ------- 시공및 물류정보
                          ,                     I.INST_DT                                                                        -- 시공일
                          ,                     I.PALT_NOXX                                                                      -- 파렛트번호
                          ,                     CASE
                                                    WHEN ISNULL(UM.USER_NM, '') = '' THEN ISNULL(M.DIST_CHAN_CD, '')
                                                    ELSE ISNULL(UM2.USER_NM, UM.USER_NM) END                     AS INST_ER      -- 시공기사
                          -- 시공기사 Id추가 2025-0322 주경진
                          ,                     ISNULL(UM2.TBL_USER_M_ID, UM.TBL_USER_M_ID)                      AS INST_ER_ID

                          ,                     CASE
                                                    WHEN ISNULL(UM.TEAM, '') = '' THEN ISNULL(M.BRAND, '')
                                                    ELSE ISNULL(UM2.TEAM, UM.TEAM) + '팀' END                     AS TEAM         -- 시공기사(팀)

                          ,                     ISNULL(ISNULL(MC.MEMO, MXC.MEMO), '')                            AS INST_MEMO    ---시공기사의 메모


                          ,                     I.INST_COST                                                                      -- 시공비
                          ,                     I.DLVY_COST                                                                      -- 물류비

                          ,                     DBO.F_COL_NM(M.CMPY_CD, 'COMM', 'DLVY_STAT_CD', IM.DLVY_STAT_CD) AS DLVY_STAT_NM --배송상태
                          ,                     ISNULL(IMD.PROD_LIFT_CMPL_YN, 'N')                               AS LIFT_YN      --상차유무
                          ,                     P.PROD_SEQ                                                                       --순번
                          ,                     ISNULL(BM.MTRL_CD, P.PROD_CD)                                    AS PROD_CD      --상품코드(FERT)
                          ,                     ISNULL(MM.MTRL_NM, P.PROD_NM)                                    AS PROD_NM      --상품명(FERT)
                          ,                     P.QTY                                                            AS QTY          --수량(FERT)

                          ,                     ISNULL(BM.C_MTRL_CD, P.PROD_CD)                                  AS C_PROD_CD    --상품코드(HAWA)
                          ,                     ISNULL(MM2.MTRL_NM, P.PROD_NM)                                   AS C_PROD_NM    --상품명(HAWA)
                          ,                     ISNULL(BM.QTY * P.QTY, P.QTY)                                    AS C_QTY        --수량(HAWA)
                          ,                     M.FROM_ER
                          ,                     M.FROM_TEL1
                          ,                     M.FROM_TEL2
                          ,                     M.FROM_POST_CD
                          ,                     M.FROM_ADDR1
                          ,                     M.FROM_ADDR2


                          ,                     M.DIST_CHAN_CD
                          ,                     M.DLVY_CNFM_DT
                          ,                     M.ORGN_SO_NO
                          ,                     M.USE_YN
                          ,                     M.DEL_RESN
                          ,                     M.MEMO
                          ,                     M.SAVE_USER
                          ,                     M.SAVE_TIME
                          ,                     M.UPDT_USER
                          ,                     M.UPDT_TIME

                          ,                     P.INST_CTGR
                          , INST_CTGR_NM      = DBO.F_COL_NM('A', 'COMM', 'INST_CAPA_CTGR', P.INST_CTGR)


            FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
                     INNER JOIN DBO.TBL_SO_P AS P WITH (NOLOCK) ON M.TBL_SO_M_ID = P.TBL_SO_M_ID

                     LEFT OUTER JOIN TBL_BOM_M AS BM WITH (NOLOCK)
                                     ON P.PROD_CD = BM.MTRL_CD AND M.AGNT_CD = BM.AGNT_CD AND BM.USE_YN = 'Y'
                     LEFT OUTER JOIN TBL_MTRL_M AS MM WITH (NOLOCK)
                                     ON BM.MTRL_CD = MM.MTRL_CD AND BM.AGNT_CD = MM.AGNT_CD
                     LEFT OUTER JOIN TBL_MTRL_M AS MM2 WITH (NOLOCK)
                                     ON BM.C_MTRL_CD = MM2.MTRL_CD AND BM.AGNT_CD = MM2.AGNT_CD

                     LEFT OUTER JOIN DBO.TBL_INST_PLAN_M AS I WITH (NOLOCK) ON M.TBL_SO_M_ID = I.TBL_SO_M_ID
                     LEFT OUTER JOIN TBL_USER_M AS UM WITH (NOLOCK) ON I.TBL_USER_M_ID = UM.TBL_USER_M_ID

                -- 모바일 데이터
                     LEFT OUTER JOIN TBL_INST_MOBILE_M AS IM WITH (NOLOCK) ON M.TBL_SO_M_ID = IM.TBL_SO_M_ID
                     LEFT OUTER JOIN TBL_INST_MOBILE_D AS IMD WITH (NOLOCK)
                                     ON P.TBL_SO_M_ID = IMD.TBL_SO_M_ID AND P.TBL_SO_P_ID = IMD.TBL_SO_P_ID AND
                                        IM.INST_MOBILE_M_ID = IMD.INST_MOBILE_M_ID
                                         --20220325 정연호 추가. 부분상차 일때 TBL_INST_MOBILE_D의 PROD_LIFT_CMPL_YN 때문에 뻥튀기 되어 나옴. 해당하는 하위품목 코드의 상차여부만 구하게 수정
                                         AND IMD.PROD_CD = ISNULL(BM.C_MTRL_CD, P.PROD_CD)
                -- 모바일 데이터 (완료/미마감)
                     LEFT OUTER JOIN TBL_INST_MOBILE_COMPLETE AS MC WITH (NOLOCK)
                                     ON IM.INST_MOBILE_M_ID = MC.INST_MOBILE_M_ID
                     LEFT OUTER JOIN TBL_INST_MOBILE_X_COMPLETE AS MXC WITH (NOLOCK)
                                     ON IM.INST_MOBILE_M_ID = MXC.INST_MOBILE_M_ID


                     LEFT OUTER JOIN TBL_USER_M AS UM2 WITH (NOLOCK) ON UM2.TBL_USER_M_ID = IM.EXEC_USER_M_ID

            WHERE 1 = 1
              AND P.USE_YN = 'Y'
              AND M.CMPY_CD = @I_CMPY_CD
              AND (M.MALL_CD LIKE @I_MALL_NM + '%' OR M.MALL_CD IS NULL)
              AND (M.ACPT_ER LIKE @I_ACPT_NM + '%' OR M.ACPT_ER IS NULL)
              AND (M.ACPT_TEL2 LIKE REPLACE(@I_ACPT_TEL, '-', '') + '%' OR M.ACPT_TEL2 IS NULL)
              AND P.PROD_NM LIKE ISNULL(@I_PROD_NM, '') + '%'
              AND M.ADDR1 LIKE ISNULL(@I_ADDR, '') + '%'
              AND M.ORDR_INPT_DT BETWEEN @V_RCPT_FROM_DT AND @V_RCPT_TO_DT
              AND (M.DLVY_CNFM_DT BETWEEN @V_DLVY_CNFM_FROM_DT AND @V_DLVY_CNFM_TO_DT OR M.DLVY_CNFM_DT = @V_CONVERT_DT)
              AND M.AGNT_CD LIKE @I_AGNT_LIST + '%'
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_DC_LIST, ''), ',') AS T
                          WHERE T.VAL = M.DC_CD
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_DC_LIST, '') = '')
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_SO_LIST, ''), ',') AS T
                          WHERE T.VAL = M.SO_NO
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_SO_LIST, '') = '')
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_SO_TYPE_LIST, ''), ',') AS T
                          WHERE T.VAL = M.SO_TYPE
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_SO_TYPE_LIST, '') = '')
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_SO_STAT_LIST, ''), ',') AS T
                          WHERE T.VAL = M.SO_STAT_CD
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_SO_STAT_LIST, '') = '')
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_PROD_LIST, ''), ',') AS T
                          WHERE T.VAL = P.PROD_CD
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_PROD_LIST, '') = '')
              AND EXISTS (SELECT 1
                          FROM DBO.SPLIT_STR(ISNULL(@I_INST_CTGR_LIST, ''), ',') AS T
                          WHERE T.VAL = P.INST_CTGR
                          UNION ALL
                          SELECT 1
                          WHERE ISNULL(@I_INST_CTGR_LIST, '') = '')
            ORDER BY M.ORDR_INPT_DT, M.AGNT_CD, M.SO_NO, P.PROD_SEQ, PROD_CD
        END
END
go

