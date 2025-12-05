CREATE PROCEDURE [dbo].[USP_SO_P_INS](
    @I_TBL_SO_M_ID BIGINT
, @I_SO_NO VARCHAR(100)
, @I_AGNT_CD VARCHAR(20)
, @I_PROD_SEQ INT
, @I_PROD_CD VARCHAR(100)
, @I_PROD_NM VARCHAR(200)
, @I_QTY FLOAT
, @I_INST_COST FLOAT --20210912 정연호. 시공비 파라미터 추가
, @I_ZONE_CD VARCHAR(20)
, @I_DC_CD VARCHAR(20)
, @I_RQST_DT VARCHAR(20)
, @I_DLVY_CNFM_DT VARCHAR(20)
, @I_DLVY_RQST_MSG VARCHAR(200)
, @I_USE_YN VARCHAR(10)
, @I_MEMO VARCHAR(100)
, @I_SAVE_USER VARCHAR(30)
---------------신규파라미터-----------------------------------
, @I_REF_SO_NO VARCHAR(100) = NULL
, @I_DLVY_COST_TYPE VARCHAR(100) = NULL --운임구분
, @I_RCPT_COST INT = NULL --착불비
, @I_PASS_COST INT = NULL --통행비

)
/**********************************
1. 목적&기능 : 주문정보 LOAD
2. 변경 내용 및 사유 :
3. 실행예시 :

USP_SO_P_INS 26617,'2109070003','8013','1','56734','오하임_상일리베가구 일마레(밀포드) ML0 내추럴 6인용 1750 식탁 (2S-ML0DT6-NA00)',1,'Y','','AVENGERS'
GO
USP_SO_P_INS 26617,'2109070003','8013','2','56735','오하임_상일리베가구 일마레(밀포드) ML0 내추럴 3인용 1350 벤치 (2S-ML0DB3-NA00)',1,'Y','','AVENGERS'
GO
USP_SO_P_INS 26617,'2109070003','8013','3','56737','오하임_상일리베가구 일마레(밀포드) ML0 내추럴 식탁의자 (2S-ML0DC1-NA00)',3,'Y','','AVENGERS'


SELECT * FROM TBL_SO_M

SELECT * FROM TBL_SO_P

56734
56735
56737


**********************************/
AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN

    -- 기준정보에서 값을 가져온다.
    DECLARE
@V_CUST_PRC FLOAT
        ,@V_SALE_PRC FLOAT
        ,@V_FACT_PRC FLOAT
        ,@V_PURC_PRC FLOAT
--,@V_DC_PRC				FLOAT
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
        --,@V_DC_CD			VARCHAR(10)
--,@V_RQST_DT			VARCHAR(10)
--,@V_RQST_MSG		VARCHAR(200)
        ,@V_DLVY_CNFM_DT VARCHAR(10)
        ,@V_NDLVY_YN VARCHAR(10)
        ,@V_THR_DLVY_YN VARCHAR(10)
        ,@V_REF_SO_NO VARCHAR(100)


SELECT @I_PROD_NM = M.MTRL_NM
     , @V_CUST_PRC = M.CUST_PRC
     , @V_SALE_PRC = M.SALE_PRC
     , @V_FACT_PRC = M.FACT_PRC
     , @V_PURC_PRC = M.PURC_PRC
     --,@V_DC_PRC			= M.DC_PRC
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
     , @V_ZONE_TYPE = M.ZONE_TYPE


     , @V_DLVY_YN = M.DLVY_YN
     , @V_DLVY_TYPE = M.DLVY_TYPE
     , @V_DLVY_COST = M.DLVY_COST

     --,@V_DC_CD			= M.DC_CD
     --,@V_RQST_DT			= M.RQST_DT
     --,@V_RQST_MSG		= M.RQST_MSG
     --,@V_DLVY_CNFM_DT	= M.DLVY_CNFM_DT

     , @V_NDLVY_YN = M.NDLVY_YN
     , @V_THR_DLVY_YN = M.THR_DLVY_YN

FROM TBL_MTRL_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.MTRL_CD = @I_PROD_CD
  AND M.AGNT_CD = @I_AGNT_CD


DECLARE
@V_ZONE_CD VARCHAR(20) ,@V_POST_CD VARCHAR(10)

    --SELECT @V_ZONE_TYPE = ZONE_TYPE FROM TBL_MTRL_M AS MM WITH(NOLOCK) WHERE MM.MTRL_CD = @I_PROD_CD
-- 권역을 업데이트 합니다.
UPDATE SM
SET ZONE_CD    = ISNULL(ZM.ZONE_CD, '1000')
  , @V_ZONE_CD = ISNULL(ZM.ZONE_CD, '1000')
  , @V_POST_CD = SM.POST_CD
  , ZONE_TYPE  = @V_ZONE_TYPE FROM TBL_SO_M AS SM
WITH (NOLOCK)
    LEFT OUTER JOIN TBL_LGST_ZONEPOST_M AS ZM
WITH (NOLOCK)
ON SM.POST_CD = ZM.POST_CD
    AND ZM.ZONE_TYPE = ISNULL(@V_ZONE_TYPE, '1000_ZONE_CD')
WHERE SM.TBL_SO_M_ID = @I_TBL_SO_M_ID


/**************************************************************************************************************************
3) 권역을 찾아 넣습니다. 유사권역을 찿습니다.  3자리 비슷한건
**************************************************************************************************************************/

    IF ISNULL(@V_ZONE_CD
    , '') = ''
BEGIN
SELECT @V_ZONE_CD = ZM.ZONE_CD
FROM TBL_LGST_ZONEPOST_M AS ZM WITH (NOLOCK)
WHERE ZM.POST_CD LIKE LEFT (@V_POST_CD
    , 4) + '%'
  AND ZM.ZONE_TYPE = @V_ZONE_TYPE

UPDATE SM
SET ZONE_CD = @V_ZONE_CD FROM TBL_SO_M AS SM
WITH (NOLOCK)
WHERE SM.TBL_SO_M_ID = @I_TBL_SO_M_ID
END

    IF
ISNULL(@V_ZONE_CD, '') = ''
BEGIN
SELECT @V_ZONE_CD = ZM.ZONE_CD
FROM TBL_LGST_ZONEPOST_M AS ZM WITH (NOLOCK)
WHERE ZM.POST_CD LIKE LEFT (@V_POST_CD
    , 3) + '%'
  AND ZM.ZONE_TYPE = @V_ZONE_TYPE

UPDATE SM
SET ZONE_CD = @V_ZONE_CD FROM TBL_SO_M AS SM
WITH (NOLOCK)
WHERE SM.TBL_SO_M_ID = @I_TBL_SO_M_ID
END

    IF
ISNULL(@V_ZONE_CD, '') = ''
BEGIN
SELECT @V_ZONE_CD = ZM.ZONE_CD
FROM TBL_LGST_ZONEPOST_M AS ZM WITH (NOLOCK)
WHERE ZM.POST_CD LIKE LEFT (@V_POST_CD
    , 2) + '%'
  AND ZM.ZONE_TYPE = @V_ZONE_TYPE

UPDATE SM
SET ZONE_CD = ISNULL(@V_ZONE_CD, '1000') FROM TBL_SO_M AS SM
WITH (NOLOCK)
WHERE SM.TBL_SO_M_ID = @I_TBL_SO_M_ID
END


/**************************************************************************************************************************
3) 시공상품을 결정합니다.  -- 입력한 상품에 2인시공이 있으면 2인시공으로 입력합니다.
**************************************************************************************************************************/
    IF
EXISTS (SELECT TOP 1 *
               FROM TBL_SO_P AS SP WITH (NOLOCK)
               WHERE 1 = 1
                 AND SP.TBL_SO_M_ID = @I_TBL_SO_M_ID
                 AND SP.INST_SEAT_TYPE = '2222'
                 AND SP.USE_YN = 'Y')
BEGIN
            SET
@V_INST_SEAT_TYPE = '2222'

END

-- 입력하고자 하는 상품이 2인시공이면 기존 상품을 2인시공으로 바꾼다.
    IF
@V_INST_SEAT_TYPE = '2222'
BEGIN
UPDATE SP
SET INST_SEAT_TYPE = '2222' FROM TBL_SO_P AS SP
WITH (NOLOCK)
WHERE SP.TBL_SO_M_ID = @I_TBL_SO_M_ID
END

    /**************************************************************************************************************************
    4) 착불비를 합계로 구한다. 2112300370
    SELECT * FROM TBL_SO_M A INNER JOIN TBL_SO_P B ON A.TBL_SO_M_ID = B.TBL_SO_M_ID

    SELECT A.TBL_SO_M_ID, DLVY_COST_TYPE, A.RCPT_COST, B.SUM_RCPT_COST,*
    --UPDATE A SET DLVY_COST_TYPE ='AFTER', RCPT_COST = B.SUM_RCPT_COST
    FROM TBL_SO_M A INNER JOIN (

    SELECT TBL_SO_M_ID , SUM(ISNULL(RCPT_COST,0)) AS SUM_RCPT_COST FROM TBL_SO_P GROUP BY TBL_SO_M_ID
    HAVING  SUM(ISNULL(RCPT_COST,0)) > 0
    ) B ON A.TBL_SO_M_ID = B.TBL_SO_M_ID

    **************************************************************************************************************************/
--20220227 정연호. 주석처리 USP_SO_M_INS, USP_SO_M_UPDT 에서 착불비 합계를 이미 넣기때문에 여기에서 다시 계산해서 넣을 필요없음
--USE_YN 값도 없어서 어차피 잘못 된 내용임
/*
IF EXISTS(
	SELECT TOP 1 SP.TBL_SO_M_ID
		FROM  TBL_SO_P AS SP WITH(NOLOCK)
		WHERE  SP.TBL_SO_M_ID = @I_TBL_SO_M_ID
  ) BEGIN
			UPDATE SM SET
			DLVY_COST_TYPE ='AFTER'
			,RCPT_COST =
			(SELECT SUM(ISNULL(SP.RCPT_COST,0)) +  ISNULL(@I_RCPT_COST,0)  AS TOT_COST
				FROM  TBL_SO_P AS SP WITH(NOLOCK)
				WHERE  SP.TBL_SO_M_ID = @I_TBL_SO_M_ID
		   )
		 FROM TBL_SO_M AS SM WITH(NOLOCK)
		 WHERE  SM.TBL_SO_M_ID = @I_TBL_SO_M_ID

  END ELSE BEGIN

		SET @I_RCPT_COST =  ISNULL(@I_RCPT_COST,0)

  END

*/
/***********************************************************************************************************************
1) 익일배송이면 익일코드르 넣기위해
2) 주문구조변경 REF_SO_NO  --> 상품코드에 넣기위해
***********************************************************************************************************************/

SELECT @V_INST_TYPE =
       CASE WHEN SM.SO_TYPE IN ('9902', '9903', '9908') THEN 'NEXTDAY' ELSE ISNULL(@V_INST_TYPE, 'NORMAL') END,
       @V_REF_SO_NO = SM.REF_SO_NO
FROM TBL_SO_M AS SM WITH (NOLOCK)
WHERE SM.TBL_SO_M_ID = @I_TBL_SO_M_ID


INSERT
INTO TBL_SO_P ( TBL_SO_M_ID
              , SO_NO
              , PROD_SEQ
              , PROD_CD
              , PROD_NM
              , QTY
              , INST_COST --20210913 정연호. 시공비추가
              , UNIT
              , CUST_PRC
              , SALE_PRC
              , FACT_PRC
              , PURC_PRC
    --,DC_PRC
              , SIZE_NM
              , L
              , D
              , H
              , H2
              , MTRL_CTGR_1
              , MTRL_CTGR_2
              , MTRL_CTGR_3
              , SET_YN
              , BOM_YN
              , INST_YN
              , INST_CTGR
              , INST_TYPE
              , INST_SEAT_TYPE
              , ZONE_TYPE
              , ZONE_CD
              , DLVY_YN
              , DLVY_TYPE
              , DLVY_COST
              , DC_CD
              , RQST_DT
              , RQST_MSG
              , DLVY_CNFM_DT
              , NDLVY_YN
              , THR_DLVY_YN
              , USE_YN
              , MEMO
              , SAVE_USER
              , SAVE_TIME
              , UPDT_USER
              , UPDT_TIME
              , REF_SO_NO
              , DLVY_COST_TYPE
              , RCPT_COST
              , PASS_COST)
VALUES ( @I_TBL_SO_M_ID
        , @I_SO_NO
        , @I_PROD_SEQ
        , @I_PROD_CD
        , @I_PROD_NM
        , @I_QTY
        , @I_INST_COST --20210913 정연호. 시공비 추가
        , 'EA'
        , @V_CUST_PRC
        , @V_SALE_PRC
        , @V_FACT_PRC
        , @V_PURC_PRC
    --,@V_DC_PRC
        , @V_SIZE_NM
        , @V_L
        , @V_D
        , @V_H
        , @V_H2
        , @V_MTRL_CTGR_1
        , @V_MTRL_CTGR_2
        , @V_MTRL_CTGR_3
        , @V_SET_YN
        , @V_BOM_YN
        , @V_INST_YN
        , ISNULL(@V_INST_CTGR, '1000')
        , ISNULL(@V_INST_TYPE, 'NORMAL')
        , ISNULL(@V_INST_SEAT_TYPE, '1111')
        , ISNULL(@V_ZONE_TYPE, '1000_ZONE_CD')
        , ISNULL(@V_ZONE_CD, '1000')
        , @V_DLVY_YN
        , @V_DLVY_TYPE
        , @V_DLVY_COST
        , @I_DC_CD
        , @I_RQST_DT
        , @I_DLVY_RQST_MSG
        , ''
        , @V_NDLVY_YN
        , @V_THR_DLVY_YN
        , @I_USE_YN
        , @I_MEMO
        , @I_SAVE_USER
        , GETDATE()
        , @I_SAVE_USER
        , GETDATE()
    --@V_REF_SO_NO
        , @I_REF_SO_NO
    --,@I_DLVY_COST_TYPE	--20220227 정연호 주석처리
        , CASE ISNULL(@I_RCPT_COST, 0)
    WHEN 0 THEN @I_DLVY_COST_TYPE
    ELSE 'AFTER' END   --20220227 정연호. 착불비가 없으면 넘긴대로 넣고 착불비가 있으면 착불코드 AFTER 넣기
        , @I_RCPT_COST
        , @I_PASS_COST)

SELECT 'Y' AS RTN_YN, '등록완료' AS RTN_MSG


END
go

