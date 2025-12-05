IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[USP_MTRL_SAVE]') AND type in (N'P', N'PC'))
    DROP PROCEDURE [dbo].[USP_MTRL_SAVE]
GO

CREATE PROCEDURE [dbo].[USP_MTRL_SAVE](
    @I_SAVE_GUBN VARCHAR(10)
, @I_TBL_MTRL_M_ID BIGINT
, @I_CMPY_CD VARCHAR(10)
, @I_AGNT_CD VARCHAR(10)
, @I_MTRL_TYPE VARCHAR(50)
, @I_MTRL_CD VARCHAR(100)
, @I_MTRL_NM VARCHAR(200)
, @I_MTRL_DESC VARCHAR(500)
, @I_STAT_CD VARCHAR(10)
, @I_SMPL_CD VARCHAR(50)
, @I_CUST_PRC FLOAT
, @I_SALE_PRC FLOAT
, @I_FACT_PRC FLOAT
, @I_PURC_PRC FLOAT
, @I_SIZE_NM VARCHAR(100)
, @I_LNTH INT --
, @I_DPTH INT --
, @I_HGHT INT --
, @I_HGHT2 INT --
, @I_CBM FLOAT --
, @I_WGHT INT --
, @I_MTRL_CTGR_1 VARCHAR(50)
, @I_MTRL_CTGR_2 VARCHAR(50)
, @I_MTRL_CTGR_3 VARCHAR(50)
, @I_SET_YN VARCHAR(10)
, @I_BOM_YN VARCHAR(10)
, @I_MTO_YN VARCHAR(10)
, @I_STD_YN VARCHAR(10)
, @I_INST_YN VARCHAR(10)
, @I_INST_CTGR VARCHAR(50)
, @I_INST_TYPE VARCHAR(50)
, @I_INST_SEAT_TYPE VARCHAR(50)
, @I_INST_COST FLOAT
, @I_INST_URL VARCHAR(200)
, @I_INST_IMG_PATH VARCHAR(50)
, @I_PSTN_NM VARCHAR(50)
, @I_DLVY_YN VARCHAR(10)
, @I_DLVY_TYPE VARCHAR(50)
, @I_DLVY_COST FLOAT
, @I_NDLVY_YN VARCHAR(10)
, @I_THR_DLVY_YN VARCHAR(10)
, @I_DLVY_LT INT
, @I_CNVT_MTRL_CD VARCHAR(100)
, @I_CNVT_MTRL_NM VARCHAR(200)
, @I_CNVT_QTY FLOAT
, @I_CNVT_UNIT VARCHAR(10)
, @I_MEMO VARCHAR(100)
, @I_USE_YN VARCHAR(10)
, @I_SAVE_USER VARCHAR(30)
, @I_PICK_ZONE VARCHAR(50) = NULL
, @I_EXCHANGE_RETURN_EXCLUDE_YN VARCHAR(10) = 'N' --교환/반품 제외여부

)
AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN

    DECLARE
@V_ZONE_TYPE VARCHAR(20)
    SET @V_ZONE_TYPE = @I_INST_CTGR + '_ZONE_CD'


    IF @I_SAVE_GUBN = 'INS'
BEGIN
            -- 화주사 코드와 상품코드 중복 체크
            IF EXISTS(SELECT TOP 1 *
                      FROM DBO.TBL_MTRL_M WITH (NOLOCK)
                      WHERE 1 = 1
                        AND CMPY_CD = @I_CMPY_CD
                        AND AGNT_CD = @I_AGNT_CD
                        AND MTRL_CD = @I_MTRL_CD)
            BEGIN
                SELECT 'N' AS RTN_YN, '이미 등록된 화주사 코드와 상품코드입니다.' AS RTN_MSG
                RETURN
            END

            INSERT
DBO.TBL_MTRL_M ( CMPY_CD
                                  , AGNT_CD
                                  , MTRL_TYPE
                                  , MTRL_CD
                                  , MTRL_NM
                                  , MTRL_DESC
                                  , STAT_CD
                                  , SMPL_CD
                                  , CUST_PRC
                                  , SALE_PRC
                                  , FACT_PRC
                                  , PURC_PRC
                                  , SIZE_NM
                                  , LNTH
                                  , DPTH
                                  , HGHT
                                  , HGHT2
                                  , CBM
                                  , WGHT
                                  , MTRL_CTGR_1
                                  , MTRL_CTGR_2
                                  , MTRL_CTGR_3
                                  , SET_YN
                                  , BOM_YN
                                  , MTO_YN
                                  , STD_YN
                                  , INST_YN
                                  , INST_CTGR
                                  , INST_TYPE
                                  , INST_SEAT_TYPE
                                  , INST_COST
                                  , INST_URL
                                  , INST_IMG_PATH
                                  , PSTN_NM
                                  , DLVY_YN
                                  , DLVY_TYPE
                                  , DLVY_COST
                                  , NDLVY_YN
                                  , THR_DLVY_YN
                                  , DLVY_LT
                                  , CNVT_MTRL_CD
                                  , CNVT_MTRL_NM
                                  , CNVT_QTY
                                  , CNVT_UNIT
                                  , MEMO
                                  , USE_YN
                                  , SAVE_USER
                                  , SAVE_TIME
                                  , UPDT_USER
                                  , UPDT_TIME
                                  , PICK_ZONE
                                  , ZONE_TYPE
                                  , EXCHANGE_RETURN_EXCLUDE_YN)
            VALUES ( @I_CMPY_CD
                   , @I_AGNT_CD
                   , ISNULL(@I_MTRL_TYPE, 'LGST')
                   , @I_MTRL_CD
                   , @I_MTRL_NM
                   , @I_MTRL_DESC
                   , @I_STAT_CD
                   , @I_SMPL_CD
                   , @I_CUST_PRC
                   , @I_SALE_PRC
                   , @I_FACT_PRC
                   , @I_PURC_PRC
                   , @I_SIZE_NM
                   , @I_LNTH
                   , @I_DPTH
                   , @I_HGHT
                   , @I_HGHT2
                   , @I_CBM
                   , @I_WGHT
                   , @I_MTRL_CTGR_1
                   , @I_MTRL_CTGR_2
                   , @I_MTRL_CTGR_3
                   , @I_SET_YN
                   , @I_BOM_YN
                   , @I_MTO_YN
                   , @I_STD_YN
                   , @I_INST_YN
                   , @I_INST_CTGR
                   , @I_INST_TYPE
                   , @I_INST_SEAT_TYPE
                   , @I_INST_COST
                   , @I_INST_URL
                   , @I_INST_IMG_PATH
                   , @I_PSTN_NM
                   , @I_DLVY_YN
                   , @I_DLVY_TYPE
                   , @I_DLVY_COST
                   , @I_NDLVY_YN
                   , @I_THR_DLVY_YN
                   , @I_DLVY_LT
                   , @I_CNVT_MTRL_CD
                   , @I_CNVT_MTRL_NM
                   , @I_CNVT_QTY
                   , @I_CNVT_UNIT
                   , @I_MEMO
                   , ISNULL(@I_USE_YN, 'Y')
                   , @I_SAVE_USER
                   , GETDATE()
                   , @I_SAVE_USER
                   , GETDATE()
                   , @I_PICK_ZONE
                   , @V_ZONE_TYPE
                   , ISNULL(@I_EXCHANGE_RETURN_EXCLUDE_YN, 'N'))

SELECT 'Y' AS RTN_YN, '등록완료' AS RTN_MSG
END
    IF
@I_SAVE_GUBN = 'UPDT'
BEGIN
            -- 화주사 코드와 상품코드 중복 체크 (현재 레코드 제외)
            IF EXISTS(SELECT TOP 1 *
                      FROM DBO.TBL_MTRL_M WITH (NOLOCK)
                      WHERE 1 = 1
                        AND CMPY_CD = @I_CMPY_CD
                        AND AGNT_CD = @I_AGNT_CD
                        AND MTRL_CD = @I_MTRL_CD
                        AND TBL_MTRL_M_ID != @I_TBL_MTRL_M_ID)
            BEGIN
                SELECT 'N' AS RTN_YN, '이미 등록된 화주사 코드와 상품코드입니다.' AS RTN_MSG
                RETURN
            END

UPDATE TMM
SET CMPY_CD                    = @I_CMPY_CD
  , AGNT_CD                    = @I_AGNT_CD
  , MTRL_TYPE                  = @I_MTRL_TYPE
  , MTRL_CD                    = @I_MTRL_CD
  , MTRL_NM                    = @I_MTRL_NM
  , MTRL_DESC                  = @I_MTRL_DESC
  , STAT_CD                    = @I_STAT_CD
  , SMPL_CD                    = @I_SMPL_CD
  , CUST_PRC                   = @I_CUST_PRC
  , SALE_PRC                   = @I_SALE_PRC
  , FACT_PRC                   = @I_FACT_PRC
  , PURC_PRC                   = @I_PURC_PRC
  , SIZE_NM                    = @I_SIZE_NM
  , LNTH                       = @I_LNTH
  , DPTH                       = @I_DPTH
  , HGHT                       = @I_HGHT
  , HGHT2                      = @I_HGHT2
  , CBM                        = @I_CBM
  , WGHT                       = @I_WGHT
  , MTRL_CTGR_1                = @I_MTRL_CTGR_1
  , MTRL_CTGR_2                = @I_MTRL_CTGR_2
  , MTRL_CTGR_3                = @I_MTRL_CTGR_3
  , SET_YN                     = @I_SET_YN
  , BOM_YN                     = @I_BOM_YN
  , MTO_YN                     = @I_MTO_YN
  , STD_YN                     = @I_STD_YN
  , PICK_ZONE                  = @I_PICK_ZONE
  , ZONE_TYPE                  = @V_ZONE_TYPE
  , INST_YN                    = @I_INST_YN
  , INST_CTGR                  = @I_INST_CTGR
  , INST_TYPE                  = @I_INST_TYPE
  , INST_SEAT_TYPE             = @I_INST_SEAT_TYPE
  , INST_COST                  = @I_INST_COST
  , INST_URL                   = @I_INST_URL
  , INST_IMG_PATH              = @I_INST_IMG_PATH
  , PSTN_NM                    = @I_PSTN_NM
  , DLVY_YN                    = @I_DLVY_YN
  , DLVY_TYPE                  = @I_DLVY_TYPE
  , DLVY_COST                  = @I_DLVY_COST
  , NDLVY_YN                   = @I_NDLVY_YN
  , THR_DLVY_YN                = @I_THR_DLVY_YN
  , DLVY_LT                    = @I_DLVY_LT
  , CNVT_MTRL_CD               = @I_CNVT_MTRL_CD
  , CNVT_MTRL_NM               = @I_CNVT_MTRL_NM
  , CNVT_QTY                   = @I_CNVT_QTY
  , CNVT_UNIT                  = @I_CNVT_UNIT
  , MEMO                       = @I_MEMO
  , USE_YN                     = @I_USE_YN
  , EXCHANGE_RETURN_EXCLUDE_YN = ISNULL(@I_EXCHANGE_RETURN_EXCLUDE_YN, 'N')
  , SAVE_USER                  = @I_SAVE_USER
  , SAVE_TIME                  = GETDATE() FROM DBO.TBL_MTRL_M AS TMM
WITH (NOLOCK)
WHERE TMM.TBL_MTRL_M_ID = @I_TBL_MTRL_M_ID

SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG

END
END
go

