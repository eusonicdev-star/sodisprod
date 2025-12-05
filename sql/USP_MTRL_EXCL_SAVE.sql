IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[USP_MTRL_EXCL_SAVE]') AND type in (N'P', N'PC'))
    DROP PROCEDURE [dbo].[USP_MTRL_EXCL_SAVE]
GO

CREATE PROCEDURE [dbo].[USP_MTRL_EXCL_SAVE](
    @I_CMPY_CD VARCHAR(10) --회사코드
, @I_AGNT_CD VARCHAR(10) --화주코드
, @I_MTRL_TYPE VARCHAR(50) --상품유형
, @I_MTRL_CD VARCHAR(100) --상품코드
, @I_MTRL_NM VARCHAR(200) --상품명
, @I_MTRL_DESC VARCHAR(500) --상품설명
, @I_STAT_CD VARCHAR(10) --상품상태
, @I_SMPL_CD VARCHAR(50) --심플코드
, @I_CUST_PRC INT --소비자가
, @I_SALE_PRC INT --판매가
, @I_FACT_PRC INT --공가
, @I_PURC_PRC INT --매입가
, @I_SIZE_NM VARCHAR(100) --규격명
, @I_LNTH INT -- 	--가로
, @I_DPTH INT -- 	--깊이
, @I_HGHT INT -- 	--높이1
, @I_HGHT2 INT --		--높이2
, @I_CBM FLOAT -- --CBM
, @I_WGHT INT -- 	--무게
, @I_MTRL_CTGR_1 VARCHAR(50) --대분류
, @I_MTRL_CTGR_2 VARCHAR(50) --중분류
, @I_MTRL_CTGR_3 VARCHAR(50) --소분류
, @I_SET_YN VARCHAR(10) --세트유무
, @I_BOM_YN VARCHAR(10) --BOM유무
, @I_MTO_YN VARCHAR(10) --MTO유무
, @I_STD_YN VARCHAR(10) --정규격유무
, @I_INST_YN VARCHAR(10) --시공유무
, @I_INST_CTGR VARCHAR(50) --시공카테고리
, @I_INST_TYPE VARCHAR(50) --시공유형
, @I_INST_SEAT_TYPE VARCHAR(50) --시공좌석유형
, @I_INST_COST INT --시공비
, @I_INST_URL VARCHAR(500) --시공URL
, @I_INST_IMG_PATH VARCHAR(50) --시공이미지경로
, @I_PSTN_NM VARCHAR(50) --제자리표
, @I_DLVY_YN VARCHAR(10) --배송유무
, @I_DLVY_TYPE VARCHAR(50) --배송유형
, @I_DLVY_COST INT --배송비
, @I_NDLVY_YN VARCHAR(10) --익일배송유무
, @I_THR_DLVY_YN VARCHAR(10) --3자배송유무
, @I_DLVY_LT INT --배송리드타임
, @I_CNVT_MTRL_CD VARCHAR(100) --변환코드
, @I_CNVT_MTRL_NM VARCHAR(200) --변환코드명
, @I_CNVT_QTY FLOAT --변환수량
, @I_CNVT_UNIT VARCHAR(10) --변환단위
, @I_MEMO VARCHAR(100) --메모
, @I_USE_YN VARCHAR(10) --사용유무
, @I_ZONE_TYPE VARCHAR(20) --시공권역유형
, @I_PICK_ZONE VARCHAR(50) --픽킹ZONE
, @I_SAVE_USER VARCHAR(30) --저장자
, @I_EXCHANGE_RETURN_EXCLUDE_YN VARCHAR(10) = 'N' --교환/반품 제외여부
)
/**********************************
1. 목적&기능 : 상품저장 EXCL_ UPLOAD
2. 변경 내용 및 사유 :
3. 실행예시 :

SELECT * FROM TBL_MTRL_M
**********************************/

AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN
    -- 화주사 코드와 상품코드로 기존 상품 확인
    IF EXISTS(SELECT TOP 1 *
              FROM TBL_MTRL_M WITH (NOLOCK)
              WHERE 1 = 1
                AND CMPY_CD = @I_CMPY_CD
                AND AGNT_CD = @I_AGNT_CD
                AND MTRL_CD = @I_MTRL_CD)
    BEGIN
        -- 기존 상품이 있으면 업데이트
        UPDATE TMM
        SET CMPY_CD                    = ISNULL(@I_CMPY_CD, 'A')
          , MTRL_TYPE                  = @I_MTRL_TYPE
          , MTRL_NM                    = @I_MTRL_NM
          , MTRL_DESC                  = @I_MTRL_DESC
          , STAT_CD                    = ISNULL(@I_STAT_CD, '1000')
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
          , USE_YN                     = ISNULL(@I_USE_YN, 'Y')
          , ZONE_TYPE                  = @I_ZONE_TYPE
          , PICK_ZONE                  = @I_PICK_ZONE
          , EXCHANGE_RETURN_EXCLUDE_YN = ISNULL(@I_EXCHANGE_RETURN_EXCLUDE_YN, 'N')
          , UPDT_USER                  = @I_SAVE_USER
          , UPDT_TIME                  = GETDATE() 
        FROM DBO.TBL_MTRL_M AS TMM WITH (NOLOCK)
        WHERE 1 = 1
          AND TMM.CMPY_CD = @I_CMPY_CD
          AND TMM.AGNT_CD = @I_AGNT_CD
          AND TMM.MTRL_CD = @I_MTRL_CD

        SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG
        RETURN
    END

    -- 기존 상품이 없으면 신규 등록
    INSERT
DBO.TBL_MTRL_M (CMPY_CD,
                                   AGNT_CD,
                                   MTRL_TYPE,
                                   MTRL_CD,
                                   MTRL_NM,
                                   MTRL_DESC,
                                   STAT_CD,
                                   SMPL_CD,
                                   CUST_PRC,
                                   SALE_PRC,
                                   FACT_PRC,
                                   PURC_PRC,
                                   SIZE_NM,
                                   LNTH,
                                   DPTH,
                                   HGHT,
                                   HGHT2,
                                   CBM,
                                   WGHT,
                                   MTRL_CTGR_1,
                                   MTRL_CTGR_2,
                                   MTRL_CTGR_3,
                                   SET_YN,
                                   BOM_YN,
                                   MTO_YN,
                                   STD_YN,
                                   INST_YN,
                                   INST_CTGR,
                                   INST_TYPE,
                                   INST_SEAT_TYPE,
                                   INST_COST,
                                   INST_URL,
                                   INST_IMG_PATH,
                                   PSTN_NM,
                                   DLVY_YN,
                                   DLVY_TYPE,
                                   DLVY_COST,
                                   NDLVY_YN,
                                   THR_DLVY_YN,
                                   DLVY_LT,
                                   CNVT_MTRL_CD, CNVT_MTRL_NM, CNVT_QTY,
                                   CNVT_UNIT, MEMO, USE_YN, ZONE_TYPE, PICK_ZONE, EXCHANGE_RETURN_EXCLUDE_YN, SAVE_USER,
                                   SAVE_TIME, UPDT_USER,
                                   UPDT_TIME)
            VALUES ( ISNULL(@I_CMPY_CD, 'A')
                   , @I_AGNT_CD
                   , @I_MTRL_TYPE
                   , @I_MTRL_CD
                   , @I_MTRL_NM
                   , @I_MTRL_DESC
                   , ISNULL(@I_STAT_CD, '1000')
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
                   , @I_ZONE_TYPE
                   , @I_PICK_ZONE
                   , ISNULL(@I_EXCHANGE_RETURN_EXCLUDE_YN, 'N')
                   , @I_SAVE_USER
                   , GETDATE()
                   , @I_SAVE_USER
                   , GETDATE())

SELECT 'Y' AS RTN_YN, '등록완료' AS RTN_MSG
END
END
go

