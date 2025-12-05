CREATE PROCEDURE dbo.USP_SO_M_UPDT(
    @I_TBL_SO_M_ID BIGINT
, @I_REF_SO_NO VARCHAR(30) --참조번호
, @I_ORGN_SO_NO VARCHAR(20) --원주문번호
, @I_SO_TYPE VARCHAR(50) --오더유형
, @I_SO_STAT_CD VARCHAR(50) --오더상태
, @I_AGNT_CD VARCHAR(10) --화주코드
, @I_MALL_CD VARCHAR(50) --쇼핑몰
, @I_BRAND VARCHAR(50) --브랜드
, @I_ORDR_INPT_DT VARCHAR(10) --주문입력일		--사용안됨
, @I_RCPT_DT VARCHAR(10) --접수일
, @I_RQST_DT VARCHAR(10) --배송희망일
, @I_DLVY_CNFM_DT VARCHAR(10) --배송확정일
, @I_ACPT_ER VARCHAR(50) --수취인(수령인)
, @I_ORDR_ER VARCHAR(50) --주문자
, @I_ACPT_TEL1 VARCHAR(20) --연락처1
, @I_ACPT_TEL2 VARCHAR(20) --연락처2
, @I_POST_CD VARCHAR(10)
, @I_ADDR1 VARCHAR(100)
, @I_ADDR2 VARCHAR(200)
, @I_DLVY_RQST_MSG VARCHAR(500) --배송메시지
, @I_CUST_SPCL_TXT VARCHAR(500) --고객특이사항

, @I_COST_TYPE VARCHAR(10) --운임구분
, @I_RCPT_COST VARCHAR(20) --착불비
, @I_PASS_COST VARCHAR(20) --통행비

, @I_DC_CD VARCHAR(10) --물류센터
, @I_USE_YN VARCHAR(50) --사용유무		--사용안됨
, @I_MEMO VARCHAR(10)
, @I_INPT_SYS VARCHAR(10) -- 입력시스템  : ERP_SO , ERP_EXCL
, @I_SAVE_USER VARCHAR(30)
)


/**********************************
1. 목적&기능 : 주문정보 (TBL_SO_M) --> 업데이트
2. 변경 내용 및 사유 :
3. 실행예시 :

USP_SO_M_UPDT 26617,'REF_SO_NO','','1000','1000','8013','12번가','GOOD','20210906','20210905','20210809','20210809',
'김장호','김은호','01024759888','01057884121','11027','서울시 은평구 녹번동','녹번빌라102호','문앞에두세요','엘리베이터없음',
'BEFORE','15000','0','8000','Y','TEST ORDER','ERP_SO','GENIUS'

USP_SO_M_INS  'A','','REF_SO_NO','','1000','1000','8013','11번가','NOBRAND','20210807','20210809','20210809','20210810',





SELECT * FROM TBL_SO_M WHERE TBL_SO_M_ID = 26617
SELECT * FROM TBL_SO_P WHERE TBL_SO_M_ID = 26617

**********************************/
    AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN
    --20210914 정연호.		업데이트 부분이 2개 있으니깐 2개가 다 되어야 성공한것으로 판단해야한다
    DECLARE
@S_UPDATE_1ST INT =0
    DECLARE
@S_UPDATE_2ND INT =0
    DECLARE
@S_UPDATE_3RD INT =0
    DECLARE
@V_ZONE_CD VARCHAR(20)

    -- 물류센터를 가져오지 않으면 자동세팅합니다
    IF ISNULL(@I_DC_CD, '') = ''
BEGIN
            -- 물류센터는 우편번호별 DC를 자동결정합니다.
EXEC @I_DC_CD = USP_COMM_DC_POST_OUTPUT 'A', @I_POST_CD, @I_ADDR1, @I_DC_CD OUTPUT
END

    --ZONE_TYPE과 ZONE_CD를 다시계산해야 한다.
SELECT @V_ZONE_CD = ZONE_CD
FROM TBL_LGST_ZONEPOST_M WITH (NOLOCK)
WHERE POST_CD = @I_POST_CD
  AND ZONE_TYPE = '1000_ZONE_CD'

UPDATE M
SET REF_SO_NO      = @I_REF_SO_NO
  , ORGN_SO_NO     = @I_ORGN_SO_NO
  , SO_TYPE        = @I_SO_TYPE
  , SO_STAT_CD     = @I_SO_STAT_CD
  , AGNT_CD        = @I_AGNT_CD

  , MALL_CD        = @I_MALL_CD
  , BRAND          = @I_BRAND --20210914 정연호 추가
  , ORDR_ER        = @I_ORDR_ER
  , ACPT_ER        = @I_ACPT_ER
  , ACPT_TEL1      = REPLACE(@I_ACPT_TEL1, '-', '')
  , ACPT_TEL2      = REPLACE(@I_ACPT_TEL2, '-', '')
  , POST_CD        = @I_POST_CD
  , ADDR1          = @I_ADDR1
  , ADDR2          = @I_ADDR2
  , DC_CD          = @I_DC_CD
  , ZONE_CD        = @V_ZONE_cD
  --,ADDR1_OLD
  --,ADDR2_OLD					=	-,@I_ORDR_INPT_DT
  --,ORDR_INPT_DT					=	CONVERT(VARCHAR(10),GETDATE(),112)				-- 현재날짜를 구해옵
  --,ORDR_INPT_ER					=	@I_SAVE_USER
  --,ORDR_INPT_TIME					=	GETDATE()
  , RCPT_DT        = REPLACE(@I_RCPT_DT, '-', '')
  , RQST_DT        = REPLACE(@I_RQST_DT, '-', '')
  , RQST_INPT_ER   = @I_SAVE_USER
  , RQST_INPT_TIME = GETDATE()

  , DLVY_CNFM_DT   = REPLACE(@I_DLVY_CNFM_DT, '-', '')

  , DLVY_RQST_MSG  = @I_DLVY_RQST_MSG
  , CUST_SPCL_TXT  = @I_CUST_SPCL_TXT
  , DLVY_COST_TYPE = @I_COST_TYPE
  , RCPT_COST      = @I_RCPT_COST
  , PASS_COST      = @I_PASS_COST
  --,USE_YN							=	@I_USE_YN		--20210914 정연호 추가

  , MEMO           = @I_MEMO
  , INPT_SYS       = @I_INPT_SYS

  , UPDT_USER      = @I_SAVE_USER
  , UPDT_TIME      = GETDATE() FROM TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID
    IF (@@ROWCOUNT = 1)
BEGIN
            SET
@S_UPDATE_1ST = 1
END
    -- 상품에 있는 것도 업데이트 해야 한다.
UPDATE P
SET RQST_DT      = @I_RQST_DT
  , DLVY_CNFM_DT = @I_DLVY_CNFM_DT
  , DC_CD        = @I_DC_CD

  , UPDT_USER    = @I_SAVE_USER
  , UPDT_TIME    = GETDATE() FROM TBL_SO_P AS P
WITH (NOLOCK)
WHERE 1 = 1
  AND P.TBL_SO_M_ID = @I_TBL_SO_M_ID
    IF (@@ROWCOUNT >= 1)
BEGIN
            SET
@S_UPDATE_2ND = 1
END

    -- 삭제 성공 여부와 관계없이 성공으로 처리 (데이터가 없을 수도 있음)
    SET @S_UPDATE_3RD = 1

    --리턴할 오더번호 추출. 그래야 주문 수정의 상세 입력시 오더번호를 넣을수있음
    DECLARE
@S_SO_NO VARCHAR(100)
SELECT TOP 1 @S_SO_NO = SO_NO
FROM TBL_SO_M WITH (NOLOCK)
WHERE 1 = 1
  AND TBL_SO_M_ID = @I_TBL_SO_M_ID
    IF (@S_UPDATE_1ST = 1
  AND @S_UPDATE_2ND = 1
  AND @S_UPDATE_3RD = 1)
BEGIN
SELECT 'Y'                             AS RTN_YN,
       '수정성공 ' + convert(varchar, @S_UPDATE_1ST) + '-' + convert(varchar, @S_UPDATE_2ND) + '-' +
       convert(varchar, @S_UPDATE_3RD) AS RTN_MSG,
       @I_TBL_SO_M_ID                  AS TBL_SO_M_ID,
       @S_SO_NO                        AS SO_NO
END
ELSE
BEGIN
SELECT 'N'                             AS RTN_YN,
       '수정실패 ' + convert(varchar, @S_UPDATE_1ST) + '-' + convert(varchar, @S_UPDATE_2ND) + '-' +
       convert(varchar, @S_UPDATE_3RD) AS RTN_MSG
END


END
go

