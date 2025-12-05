CREATE PROCEDURE dbo.USP_SO_OUT_CMPY_SAVE_SO_CNFM(
    @I_EXCL_ROW BIGINT
, @I_OUT_CMPY_CD VARCHAR(10) -- 외부업체 코드			  KPP컬럼 : DLV_COMP_NM
, @I_CMPY_CD VARCHAR(10) -- 회사코드(A-Alliance)       CMPY_CD : 얼라이언스 : 'A' - 고정값
, @I_REF_SO_NO VARCHAR(100) --참조번호					  KPP컬럼 : ORG_ORD_ID
, @I_ORGN_SO_NO VARCHAR(100) --원주문번호				  KPP컬럼 : DLV_ORD_ID
, @I_SO_TYPE VARCHAR(50) --오더유형					  KPP컬럼 : CNVT_TRN_DLV_ORD_TYPE_CD --> SO_ORDR_TYPE_CD
, @I_SO_STAT_CD VARCHAR(50) --오더상태					  고정값 :  '1200' -주문확정

, @I_AGNT_CD VARCHAR(10) --화주코드					  KPP컬럼 : CNVT_TRUST_CUST_CD  --> AGNT_CD
, @I_MALL_CD VARCHAR(50) --쇼핑몰					  KPP컬럼 : SALES_COMPANY_NM
, @I_BRAND VARCHAR(50) --브랜드					  KPP컬럼 : BUY_CUST_NM

, @I_ORDR_INPT_DT VARCHAR(10) --주문입력일				  KPP컬럼 :  X
, @I_RCPT_DT VARCHAR(10) --주문접수일				  KPP컬럼 :  BUYED_DT
, @I_RQST_DT VARCHAR(10) --배송희망일				  KPP컬럼 :  DLV_IN_REQ_DT
, @I_DLVY_CNFM_DT VARCHAR(10) --배송확정일				  KPP컬럼 :  X

, @I_ACPT_ER VARCHAR(50) --수취인(수령인)			  KPP컬럼 : DLV_CUSTOMER_NM
, @I_ORDR_ER VARCHAR(50) --주문자					  KPP컬럼 : BUY_CUST_NM
, @I_ACPT_TEL1 VARCHAR(20) --연락처1					  KPP컬럼 : DLV_PHONE_1
, @I_ACPT_TEL2 VARCHAR(20) --연락처2					  KPP컬럼 : DLV_PHONE_2
, @I_POST_CD VARCHAR(10) --우편번호					  KPP컬럼 : CNVT_DLV_ZIP
, @I_ADDR1 VARCHAR(100) --주소1						  KPP컬럼 : CNVT_DLV_ADDR
, @I_ADDR2 VARCHAR(200) --주소2						  KPP컬럼 : X
, @I_DLVY_RQST_MSG VARCHAR(500) --배송메시지				  KPP컬럼 : ETC1
, @I_CUST_SPCL_TXT VARCHAR(500) --고객특이사항				  KPP컬럼 : ETC2

, @I_COST_TYPE VARCHAR(10) --운임구분					  KPP컬럼 : X
, @I_RCPT_COST VARCHAR(20) --착불비					  KPP컬럼 : X
, @I_PASS_COST VARCHAR(20) --통행비					  KPP컬럼 : X

, @I_PROD_CD VARCHAR(100) -- 제품코드					  KPP컬럼 : CNVT_DLV_PRODUCT_CD
, @I_PROD_NM VARCHAR(200) -- 제품명					  KPP컬럼 :	CNVT_DLV_PRODUCT_NM
, @I_QTY FLOAT -- 수량						  KPP컬럼 : DLV_QTY

, @I_DC_CD VARCHAR(10) --물류센터					  KPP컬럼 : X
, @I_USE_YN VARCHAR(50) --사용유무
, @I_MEMO VARCHAR(200)
, @I_INPT_SYS VARCHAR(10) -- 입력시스템  :              고정값 :  'KPP_IF'
, @I_SAVE_USER VARCHAR(30)
, @I_DLV_ORD_ID VARCHAR(20) = NULL -- 외부배송사 배송ID
, @I_DLV_ORD_SEQ VARCHAR(20) = NULL -- 외부배송사 배송순번
, @I_DLV_ORD_SUBSEQ VARCHAR(20) = NULL -- 외부배송사 배송하위순번
, @I_IF_ID BIGINT = NULL
, @I_DLV_REQ_DT VARCHAR(8) = NULL -- 배송예정일
--,@I_MEMO				VARCHAR(200) = NULL	-- 인수증메모	      --기존에 있어 추가 안함
)


/**********************************
1. 목적&기능 : 외부사(KPP) - 주문정보 일괄저장
2. 변경 내용 및 사유 :
3. 실행예시 :

SELECT * FROM TBL_POST_M WHERE SIGU_NM LIKE '은평구%'

IF_주문정보 기반 데이터 저장
SELECT * FROM TBL_SO_OUT_CMPY_WINUS_IF



**********************************/
    AS
SET NOCOUNT ON
SET LOCK_TIMEOUT 60000
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN


/************************************************************************************************************************
데이터 초기화
************************************************************************************************************************/
SET
@I_RCPT_DT = ISNULL(REPLACE(REPLACE(@I_RCPT_DT,'-',''),'.',''),'')
SET @I_RQST_DT = ISNULL(REPLACE(REPLACE(@I_RQST_DT,'-',''),'.',''),'')
SET @I_DLVY_CNFM_DT = ISNULL(REPLACE(REPLACE(@I_DLVY_CNFM_DT,'-',''),'.',''),'')
SET @I_ACPT_TEL1 = ISNULL(REPLACE(REPLACE(@I_ACPT_TEL1,'-',''),'.',''),'')
SET @I_ACPT_TEL2 = ISNULL(REPLACE(REPLACE(@I_ACPT_TEL1,'-',''),'.',''),'')
SET @I_POST_CD = ISNULL(REPLACE(REPLACE(@I_POST_CD,'-',''),'.',''),'')

SET @I_DLV_REQ_DT  = ISNULL(REPLACE(REPLACE(@I_DLV_REQ_DT,'-',''),'.',''),'')


/************************************************************************************************************************
기준정보에서 값을 가져오기
************************************************************************************************************************/
-- 기준정보에서 값을 가져온다.
DECLARE

@V_CUST_PRC			FLOAT
,@V_SALE_PRC			FLOAT
,@V_FACT_PRC			FLOAT
,@V_PURC_PRC			FLOAT
,@V_SIZE_NM			VARCHAR(100)
,@V_L					INT
,@V_D					INT
,@V_H					INT
,@V_H2					INT
,@V_MTRL_CTGR_1		VARCHAR(50)
,@V_MTRL_CTGR_2		VARCHAR(50)
,@V_MTRL_CTGR_3		VARCHAR(50)
,@V_SET_YN			VARCHAR(10)
,@V_BOM_YN			VARCHAR(10)
,@V_MTO_YN			VARCHAR(10)
,@V_STD_YN			VARCHAR(10)

,@V_INST_YN			VARCHAR(10)
,@V_INST_CTGR		VARCHAR(50)
,@V_INST_TYPE		VARCHAR(50)
,@V_INST_SEAT_TYPE	VARCHAR(50)
,@V_ZONE_TYPE		VARCHAR(20)
,@V_INST_COST			FLOAT

,@V_DLVY_YN			VARCHAR(10)
,@V_DLVY_TYPE		VARCHAR(50)
,@V_DLVY_COST			FLOAT

,@V_DLVY_CNFM_DT	VARCHAR(10)
,@V_NDLVY_YN		VARCHAR(10)
,@V_THR_DLVY_YN		VARCHAR(10)


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

FROM TBL_MTRL_M AS M WITH(NOLOCK)
WHERE 1=1
  AND M.MTRL_CD = @I_PROD_CD
  AND M.AGNT_CD = @I_AGNT_CD

DECLARE
@V_ZONE_CD VARCHAR(20)



/************************************************************************************************************************************
우편번호가 없다면 주소로 찿기
************************************************************************************************************************************/
IF (ISNULL(@I_POST_CD,'') ='')
BEGIN
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR, 13) = LEFT (@I_ADDR1, 13)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_ROAD, 13) = LEFT (@I_ADDR1, 13)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_SMRY, 13) = LEFT (@I_ADDR1, 13)
END

IF
(ISNULL(@I_POST_CD,'') ='')
BEGIN
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR, 12) = LEFT (@I_ADDR1, 12)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_ROAD, 12) = LEFT (@I_ADDR1, 12)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_SMRY, 12) = LEFT (@I_ADDR1, 12)
END

IF
(ISNULL(@I_POST_CD,'') ='')
BEGIN
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR, 11) = LEFT (@I_ADDR1, 11)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_ROAD, 11) = LEFT (@I_ADDR1, 11)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_SMRY, 11) = LEFT (@I_ADDR1, 11)
END

IF
(ISNULL(@I_POST_CD,'') ='')
BEGIN
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR, 10) = LEFT (@I_ADDR1, 10)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_ROAD, 10) = LEFT (@I_ADDR1, 10)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_SMRY, 10) = LEFT (@I_ADDR1, 10)
END

IF
(ISNULL(@I_POST_CD,'') ='')
BEGIN
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR, 9) = LEFT (@I_ADDR1, 9)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_ROAD, 9) = LEFT (@I_ADDR1, 9)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_SMRY, 9) = LEFT (@I_ADDR1, 9)
END

IF
(ISNULL(@I_POST_CD,'') ='')
BEGIN
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR, 8) = LEFT (@I_ADDR1, 8)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_ROAD, 8) = LEFT (@I_ADDR1, 8)
SELECT @I_POST_CD = POST_CD
FROM TBL_POST_M AS P WITH(NOLOCK)
WHERE LEFT (P.ADDR_SMRY, 8) = LEFT (@I_ADDR1, 8)
END


IF
(ISNULL(@I_POST_CD,'') ='')
BEGIN
		SET @I_POST_CD = '01000'     --> 강북구 우이동으로 세팅
END

/************************************************************************************************************************************
우편번호 마스터에 있는것으로 유사 우편번호로 치환하여 권역을 찿는다.
************************************************************************************************************************************/
IF
NOT EXISTS(
		SELECT TOP 1 * FROM TBL_POST_M AS PM WITH(NOLOCK) WHERE PM.POST_CD =@I_POST_CD )
BEGIN
SELECT @I_POST_CD = PM.POST_CD
FROM TBL_POST_M AS PM WITH(NOLOCK)
WHERE LEFT (PM.POST_CD, 4) = LEFT (@I_POST_CD, 4)
END


IF
NOT EXISTS(
		SELECT TOP 1 * FROM TBL_POST_M AS PM WITH(NOLOCK) WHERE PM.POST_CD =@I_POST_CD )
BEGIN
SELECT @I_POST_CD = PM.POST_CD
FROM TBL_POST_M AS PM WITH(NOLOCK)
WHERE LEFT (PM.POST_CD, 3) = LEFT (@I_POST_CD, 3)
END

IF
NOT EXISTS(
		SELECT TOP 1 * FROM TBL_POST_M AS PM WITH(NOLOCK) WHERE PM.POST_CD =@I_POST_CD )
BEGIN
SELECT @I_POST_CD = PM.POST_CD
FROM TBL_POST_M AS PM WITH(NOLOCK)
WHERE LEFT (PM.POST_CD, 2) = LEFT (@I_POST_CD, 2)
END

IF
NOT EXISTS(
		SELECT TOP 1 * FROM TBL_POST_M AS PM WITH(NOLOCK) WHERE PM.POST_CD =@I_POST_CD )
BEGIN
		SET
@I_POST_CD = '01000'     --> 강북구 우이동으로 세팅
END

/************************************************************************************************************************************
권역을 업데이트 합니다.
************************************************************************************************************************************/

--SELECT @V_ZONE_TYPE = ZONE_TYPE FROM TBL_MTRL_M AS MM WITH(NOLOCK) WHERE MM.MTRL_CD = @I_PROD_CD
-- 권역을 업데이트 합니다.
SELECT @V_ZONE_CD = ISNULL(ZM.ZONE_CD, '1000')
FROM TBL_LGST_ZONEPOST_M AS ZM WITH(NOLOCK)
WHERE ZM.ZONE_TYPE = ISNULL(@V_ZONE_TYPE
    , '1000_ZONE_CD')
  AND ZM.POST_CD = @I_POST_CD

/************************************************************************************************************************************
전화번호 검증 및 치환하기
************************************************************************************************************************************/
-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
    IF ISNULL(@I_ACPT_TEL2
    , '') =''
BEGIN
	SET @I_ACPT_TEL2 = @I_ACPT_TEL1
END

-- 휴대폰 번호 미기입시 TEL2에 넣는다.. 모든 전송은 TEL2로 처리한다.
IF LEFT
(ISNULL(@I_ACPT_TEL1,''),3) ='010'  AND  LEFT(ISNULL(@I_ACPT_TEL2,''),3) <> '010'
BEGIN
	SET @I_ACPT_TEL2 = @I_ACPT_TEL1
END

-- 번호 채번하기
DECLARE
@V_SRCH_NO VARCHAR(50) ,@RTN_YN VARCHAR(100), @RTN_MSG VARCHAR(200)
DECLARE
@V_ADDR VARCHAR(100), @V_ADDR_ROAD VARCHAR(100) , @V_ADDR_SMRY VARCHAR(100)

SELECT @V_ADDR = ADDR, @V_ADDR_ROAD = ADDR_ROAD, @V_ADDR_SMRY = ADDR_SMRY
FROM TBL_POST_M AS PM WITH(NOLOCK)
WHERE PM.POST_CD =@I_POST_CD



/************************************************************************************************************************************
			제약조건 체크하기
************************************************************************************************************************************/


--ELSE BEGIN 다음에 있던걸 여기로 옮김 --2021-11-25 정연호

DECLARE
@V_TBL_SO_M_ID		BIGINT
							,@V_PROD_SEQ			INT

							,@I_ZONE_CD			VARCHAR(20)
						--	,@I_DC_CD			VARCHAR(20)
						--	,@I_RQST_DT         VARCHAR(20)
						--	,@I_DLVY_CNFM_DT    VARCHAR(20)
						--	,@I_DLVY_RQST_MSG	VARCHAR(200)






IF EXISTS ( SELECT TOP 1 *
			FROM TBL_SO_M AS SM WITH(NOLOCK)
			INNER JOIN TBL_SO_P AS SP WITH(NOLOCK) ON SM.TBL_SO_M_ID = SP.TBL_SO_M_ID
			WHERE 1=1
			AND SM.USE_YN='Y' AND SP.USE_YN='Y'
			AND SM.CMPY_CD = @I_CMPY_CD
			AND SM.AGNT_CD = @I_AGNT_CD
			AND SM.REF_SO_NO = ISNULL(@I_REF_SO_NO,'')
			AND SM.ACPT_ER = @I_ACPT_ER
			AND SM.SO_TYPE = @I_SO_TYPE
			AND SM.ORDR_INPT_DT = @I_ORDR_INPT_DT
			AND SM.ACPT_ER = @I_ACPT_ER
			AND SM.ADDR1 = @I_ADDR1
			AND SM.POST_CD = @I_POST_CD
			AND SP.PROD_CD = @I_PROD_CD

)
BEGIN
SELECT @I_EXCL_ROW   AS EXCL_ROW,
       'N'           AS RTN_YN,
       ' 이미 입력한 오더 ' AS RTN_MSG,
       @I_AGNT_CD    AS AGNT_CD,
       @I_REF_SO_NO  AS REF_SO_NO,
       @V_SRCH_NO    AS SO_NO,
       @I_ACPT_ER    AS ACPT_ER,
       @I_PROD_CD    AS PROD_CD,
       @I_PROD_NM    AS PROD_NM,
       @I_QTY        AS QTY
    RETURN

END



			-- 20211225
			-- 오더입력일, 오더유형, 수령인, 수령인 주소가 같은면 한오더로 묶는다.
			IF
NOT EXISTS ( SELECT TOP 1 * FROM TBL_SO_M AS M WITH(NOLOCK)
									WHERE 1=1
									AND M.CMPY_CD = @I_CMPY_CD
									AND M.AGNT_CD = @I_AGNT_CD



									AND M.ACPT_ER = REPLACE(REPLACE(REPLACE(@I_ACPT_ER,CHAR(10),''),CHAR(13),''),'?',' ')
									AND M.ORDR_INPT_DT = CONVERT(VARCHAR(10),GETDATE(),112)
									AND M.ADDR1 =  REPLACE(REPLACE(REPLACE(@I_ADDR1,CHAR(10),''),CHAR(13),''),'?',' ')
									AND M.SO_TYPE = REPLACE(REPLACE(REPLACE(@I_SO_TYPE,CHAR(10),''),CHAR(13),''),'?',' ')
									AND M.USE_YN='Y'


						)
BEGIN


				--EXEC @V_SRCH_NO = USP_COMM_NUM_LOAD_OUTPUT @I_CMPY_CD,'SO_NO','',@I_SAVE_USER,@V_SRCH_NO OUTPUT
				/**************************************************************************************************************
				-- 1) 오더번호를 신규로 채번한다.
				EXEC @V_SRCH_NO = USP_COMM_NUM_LOAD_OUTPUT @I_CMPY_CD,'SO_NO','',@I_SAVE_USER,@V_SRCH_NO OUTPUT
				**************************************************************************************************************/
				DECLARE
@V_MMDD VARCHAR(20) ,@V_LAST_NO VARCHAR(100)
				SET @V_MMDD =  RIGHT(CONVERT(VARCHAR(10),GETDATE(),112),6)

				IF NOT EXISTS ( SELECT TOP 1 * FROM TBL_COMM_NUM A WITH(NOLOCK)
							WHERE A.CMPY_CD = @I_CMPY_CD AND A.NUM_TYPE = 'SO_NO'  AND A.PRE_FIX = @V_MMDD)
BEGIN
INSERT INTO TBL_COMM_NUM (CMPY_CD, NUM_TYPE, TYPE_DESC, PRE_FIX, LAST_NO, INPT_NO, USE_YN, SAVE_USER, SAVE_TIME)
VALUES (@I_CMPY_CD, 'SO_NO', 'SO_NO', @V_MMDD, '1', ISNULL(@V_MMDD, '') + CONVERT(VARCHAR (20), '0001'), 'Y',
        @I_SAVE_USER, GETDATE()) SET  @V_SRCH_NO = ISNULL(@V_MMDD,'') +'0001'


END
ELSE BEGIN

UPDATE A
SET
    -- LAST_NO = CONVERT(VARCHAR(100), CONVERT(BIGINT, A.LAST_NO) + 1 ),
    -- INPT_NO = ISNULL(@I_PRE_FIX,'') + RIGHT('0000' + CONVERT(VARCHAR(20), CONVERT(BIGINT,A.LAST_NO ) + 1) ,4),
    LAST_NO = CONVERT(VARCHAR (100), CONVERT(DECIMAL (20), A.LAST_NO) + 1),
    INPT_NO = ISNULL(@V_MMDD, '') + RIGHT ('0000' + CONVERT (VARCHAR (20), CONVERT (DECIMAL (20), A.LAST_NO ) + 1), 4), UPDT_USER = @I_SAVE_USER, UPDT_TIME = GETDATE()
FROM TBL_COMM_NUM A
WHERE 1=1
  AND A.CMPY_CD = @I_CMPY_CD
  AND A.NUM_TYPE = 'SO_NO'
  AND A.PRE_FIX = @V_MMDD


SELECT @V_SRCH_NO = CONVERT(VARCHAR (100), A.INPT_NO)
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
EXEC @I_DC_CD = USP_COMM_DC_POST_OUTPUT @I_CMPY_CD, @I_POST_CD, @I_ADDR1,@I_DC_CD OUTPUT




			/************************************************************************************************************************************
						 오더 헤더 입력하기

						 SELECT * FROM TBL_SO_M WHERE AGNT_CD ='8013' AND SO_NO >= '2111010011' AND ORDR_INPT_DT ='20211101'
						 SELECT * FROM TBL_COMM_M WHERE COMM_CD ='EXCEL_TMPL_SO'

						 SELECT * FROM TBL_COMM_M WHERE COMM_CD ='DLVY_COST_TYPE'
						 SELECT * FROM TBL_COMM_M WHERE COMM_CD ='SO_STAT_CD'
						 SELECT * FROM TBL_COMM_M WHERE COMM_CD ='SO_ORDR_TYPE'

			************************************************************************************************************************************/
			-- 착불비가 있으면 운임은 착불로 한다.
			IF ISNULL(@I_RCPT_COST,0) > 0
BEGIN  SET @I_COST_TYPE ='AFTER' END
			-- 오더유형이 없으면 일반오더
			IF ISNULL(@I_SO_TYPE,'') = ''
BEGIN  SET @I_SO_TYPE ='1000' END    -- 일반오더



			INSERT INTO TBL_SO_M (
									 CMPY_CD
									,SO_NO
									,REF_SO_NO
									,ORGN_SO_NO
									,SO_TYPE
									,SO_STAT_CD
									,AGNT_CD

									,MALL_CD
									,ORDR_ER
									,ACPT_ER
									,ACPT_TEL1
									,ACPT_TEL2
									,POST_CD
									,ADDR1
									,ADDR2
									,DC_CD
									--,ADDR1_OLD
									--,ADDR2_OLD
									,ORDR_INPT_DT
									,ORDR_INPT_ER
									,ORDR_INPT_TIME
									,RCPT_DT
									,RQST_DT
									,RQST_INPT_ER
									,RQST_INPT_TIME

									,DLVY_CNFM_DT

									,DLVY_RQST_MSG
									,CUST_SPCL_TXT
									,DLVY_COST_TYPE
									,RCPT_COST
									,PASS_COST

									,USE_YN
									--,DEL_RESN
									,MEMO
									,INPT_SYS
									,SAVE_USER
									,SAVE_TIME
									,UPDT_USER
									,UPDT_TIME
									,ZONE_TYPE
									,ZONE_CD
									,OUT_CMPY_CD
									,OUT_DLV_ORD_ID
									,OUT_DLV_ORD_SEQ
									,OUT_DLV_ORD_SUBSEQ

									) VALUES (
										 @I_CMPY_CD
										,@V_SRCH_NO
										,@I_REF_SO_NO
										,@I_ORGN_SO_NO
										--,@I_SO_TYPE
										,REPLACE(REPLACE(REPLACE(@I_SO_TYPE,CHAR(10),''),CHAR(13),''),'?',' ')
										,'1200'  --> @I_SO_STAT_CD		 --'주문확정' :1200
										,@I_AGNT_CD

										,@I_MALL_CD
										,@I_ORDR_ER
										--,@I_ACPT_ER
										,REPLACE(REPLACE(REPLACE(@I_ACPT_ER,CHAR(10),''),CHAR(13),''),'?',' ')
										,REPLACE(@I_ACPT_TEL1,'-','')
										,REPLACE(@I_ACPT_TEL2,'-','')
										,@I_POST_CD
										--,@I_ADDR1
										,  REPLACE(REPLACE(REPLACE(@I_ADDR1,CHAR(10),''),CHAR(13),''),'?',' ')
										,@I_ADDR2
										,ISNULL(@I_DC_CD,'1000')


										,CONVERT(VARCHAR(10),GETDATE(),112)									--ORDR_INPT_DT
										,@I_SAVE_USER														--ORDR_INPT_ER
										,GETDATE()															--ORDR_INPT_TIME
										,ISNULL(@I_RCPT_DT, '' )											--고객접수일
										--,ISNULL(@I_RQST_DT, '' )											--고객배송요청일
										,ISNULL(@I_DLV_REQ_DT,'')											--고객배송요청일<-- 신규
										,@I_SAVE_USER														--RQST_INPT_ER
										,GETDATE()															--RQST_INPT_TIME
										,'' --@I_DLVY_CNFM_DT


										--,@I_DLVY_RQST_MSG
										,@I_MEMO  --> 배송요청 메시지에 인수증 메모를 넣는다.
										,@I_CUST_SPCL_TXT
										,@I_COST_TYPE
										,@I_RCPT_COST
										,@I_PASS_COST


										--,@I_DC_CD
										,'Y'
										,@I_MEMO
										,'KPP_IF'
										,@I_SAVE_USER
										,GETDATE()
										,@I_SAVE_USER
										,GETDATE()
										,ISNULL(@V_ZONE_TYPE,'1000_ZONE_CD'	)
										,ISNULL(@V_ZONE_CD, '1000')
										,@I_OUT_CMPY_CD
										,@I_DLV_ORD_ID
										,@I_DLV_ORD_SEQ
										,@I_DLV_ORD_SUBSEQ
									)


					SET @V_TBL_SO_M_ID = @@IDENTITY
					/************************************************************************************************************************************
								아이템 넣기
					************************************************************************************************************************************/
					-- 아이템 넣기 SP 호출
					EXEC DBO.USP_SO_P_EXCL_INS_OUTCMPY  @V_TBL_SO_M_ID,@V_SRCH_NO,@I_AGNT_CD,1,@I_PROD_CD,@I_PROD_NM,@I_QTY,
												'' ,@I_DC_CD ,@I_RQST_DT,@I_DLVY_CNFM_DT ,@I_DLVY_RQST_MSG,
												'Y','KPP_IF',@I_SAVE_USER,@I_REF_SO_NO,
												@I_COST_TYPE,@I_RCPT_COST,@I_PASS_COST,
												@I_DLV_ORD_ID,@I_DLV_ORD_SEQ	,@I_DLV_ORD_SUBSEQ

					SET @RTN_YN ='Y'
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

					-- 오더가 있으면 기존오더 가져오기
SELECT @V_SRCH_NO = M.SO_NO
     , @V_TBL_SO_M_ID = M.TBL_SO_M_ID

FROM TBL_SO_M AS M WITH(NOLOCK)
WHERE 1=1

  AND M.CMPY_CD = @I_CMPY_CD
  AND M.AGNT_CD = @I_AGNT_CD

  AND M.ACPT_ER = REPLACE(REPLACE(REPLACE(@I_ACPT_ER
    , CHAR (10)
    , '')
    , CHAR (13)
    , '')
    , '?'
    , ' ')
  AND M.ORDR_INPT_DT = CONVERT (VARCHAR (10)
    , GETDATE()
    , 112)
  AND M.ADDR1 = REPLACE(REPLACE(REPLACE(@I_ADDR1
    , CHAR (10)
    , '')
    , CHAR (13)
    , '')
    , '?'
    , ' ')
  AND M.SO_TYPE = REPLACE(REPLACE(REPLACE(@I_SO_TYPE
    , CHAR (10)
    , '')
    , CHAR (13)
    , '')
    , '?'
    , ' ')
  AND M.USE_YN ='Y'
--AND M.SO_STAT_CD <> '0000' @I_SO_TYPE

--AND M.REF_SO_NO = ISNULL(@I_REF_SO_NO,'')
--AND M.POST_CD = @I_POST_CD

--AND M.SO_STAT_CD = @I_SO_STAT_CD


/************************************************************************************************************************************
            아이템 MAX 번호 구하기
************************************************************************************************************************************/
SELECT @V_PROD_SEQ = MAX(PROD_SEQ) + 1
FROM TBL_SO_P AS P WITH(NOLOCK)
WHERE P.TBL_SO_M_ID = @V_TBL_SO_M_ID


    /************************************************************************************************************************************
                아이템 넣기
    ************************************************************************************************************************************/
    -- 아이템 넣기 SP 호출
    EXEC DBO.USP_SO_P_EXCL_INS_OUTCMPY @V_TBL_SO_M_ID
    , @V_SRCH_NO
    , @I_AGNT_CD
    , 1
    , @I_PROD_CD
    , @I_PROD_NM
    , @I_QTY
    , ''
    , @I_DC_CD
    , @I_RQST_DT
    , @I_DLVY_CNFM_DT
    , @I_DLVY_RQST_MSG
    , 'Y'
    , 'KPP_IF'
    , @I_SAVE_USER
    , @I_REF_SO_NO
    , @I_COST_TYPE
    , @I_RCPT_COST
    , @I_PASS_COST
    , @I_DLV_ORD_ID
    , @I_DLV_ORD_SEQ
    , @I_DLV_ORD_SUBSEQ

SET @RTN_YN ='Y'
SET @RTN_MSG = '등록성공'


END

----------------------------------------------------------------------------------------------------------------------------------
--전체 체크하는구간
----------------------------------------------------------------------------------------------------------------------------------
		/*************************************************************************
		주문히스트로에 넣는다.
		*************************************************************************/
		INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT @V_TBL_SO_M_ID, '오더확정-1200', '오더확정-1200', '신규접수-KPP_IF', @I_SAVE_USER, GETDATE()


/*************************************************************************
인터페이스 테이블에 전송유무 저장
*************************************************************************/
UPDATE A
SET SO_TRANS_YN   ='Y',
    SO_TRANS_RESN ='주문생성:외부업체' + '[주문번호 : ' + @V_SRCH_NO + ']',
    SO_TRANS_TIME =GETDATE() --20220428 정연호추가 +'[주문번호 : '+@V_SRCH_NO+']'
    FROM  TBL_SO_OUT_CMPY_WINUS_IF A
WHERE IF_ID = @I_IF_ID

----------------------------------------------------------------------------------------------------------------------------------
--전체 체크하는구간
----------------------------------------------------------------------------------------------------------------------------------

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

