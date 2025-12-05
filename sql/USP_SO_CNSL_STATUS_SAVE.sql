CREATE PROCEDURE [dbo].[USP_SO_CNSL_STATUS_SAVE] (
@I_TBL_SO_M_ID		BIGINT,
@I_ACPT_TEL1		VARCHAR(20),   -- 일반전화
@I_ACPT_TEL2		VARCHAR(20),   -- 휴대전화   < 주석해지해야함
@I_DLVY_CNFM_DT		VARCHAR(20),	--배송확정일
@I_POST_CD			VARCHAR(10),	--우편번호
@I_ADDR1			VARCHAR(200),	--주소1
@I_ADDR2			VARCHAR(200),	--주소2-상세주소[
@I_DLVY_RQST_MSG	VARCHAR(500),	--배송메세지
@I_CUST_SPCL_TXT	VARCHAR(500),	--고객사메세지 - 이건 저장안하는데, 화면에서 수정불가인데
@I_SAVE_USER		VARCHAR(30)	,	--저장자
--------------------------------------------------
@I_SO_TYPE			VARCHAR(20)	= NULL	--주문유형


)
/**********************************
1. 목적&기능 : 주문상담 (주문변경- 상담후)
2. 변경 내용 및 사유 :
3. 실행예시 :
USP_SO_CNSL_STATUS_SAVE   11950 ,'01075466913', '01075466913','20210714', '','','','테스트','테스트',1

의존도 'CNFMDT'

SELECT TOP 1000 *  FROM TBL_SO_M WHERE TBL_SO_M_ID = 11950
SELECT TOP 1000 *  FROM TBL_SO_D WHERE TBL_SO_M_ID = 11950

**********************************/
AS
SET NOCOUNT ON
SET LOCK_TIMEOUT 60000
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN


/**************************************************************************************************
SELECT TOP 1000 *  FROM TBL_SO_M
1) 헤더를 수정/변경한다.
**************************************************************************************************/
DECLARE
@I_DC_CD  VARCHAR(10)
			-- 물류센터를 가져오지 않으면 자동세팅합니다
			IF ISNULL(@I_DC_CD,'')  =''
BEGIN
			-- 물류센터는 우편번호별 DC를 자동결정합니다.
				EXEC @I_DC_CD = USP_COMM_DC_POST_OUTPUT 'A', @I_POST_CD, @I_ADDR1,@I_DC_CD OUTPUT
END



	DECLARE
@S_M_ROWCOUNT INT--20210925 정연호. 리턴 수정
UPDATE SM
SET ACPT_TEL1     = @I_ACPT_TEL1
  , ACPT_TEL2     = @I_ACPT_TEL2     --<-- 주석해지해야함
  , DLVY_RQST_MSG = @I_DLVY_RQST_MSG --배송메세지
  , CUST_SPCL_TXT = @I_CUST_SPCL_TXT --20210925 정연호. 주석처리함. 화면에서 고객사 메세지는 수정불가. SP에서도 UPDATE 하면 안됨

  --배송확정을 왜 여기서 하지?
  --,DLVY_CNFM_DT		=   REPLACE(@I_DLVY_CNFM_DT,'-','')  -- 배송확정일
  --,DLVY_CNFM_INPT_ER	=  	@I_SAVE_USER			--배송확정을 한 작업자
  --,DLVY_CNFM_INPT_TIME =	GETDATE()				--배송확정을 한 시간
  , DC_CD         = @I_DC_CD
  , POST_CD       = @I_POST_CD       ---20210925 정연호 추가. 우편번호
  , ADDR1         = @I_ADDR1         ---20210925 정연호 추가. 주소1
  , ADDR2         = @I_ADDR2         ---20210925 정연호 추가. 주소2-상세주소
  -- 20211029 수정
  --,SO_STAT_CD			=  CASE WHEN ISNULL(@I_DLVY_CNFM_DT,'') =''   THEN SM.SO_STAT_CD ELSE '2000' END
  --, SO_STAT_CD =
  , SO_TYPE       = ISNULL(@I_SO_TYPE, SM.SO_TYPE) FROM TBL_SO_M AS SM
WITH (NOLOCK)
WHERE 1=1
  AND SM.TBL_SO_M_ID = @I_TBL_SO_M_ID

SET @S_M_ROWCOUNT = @@ROWCOUNT

/**************************************************************************************************
SELECT TOP 1000 *  FROM TBL_SO_M
1) 아이템 를 수정/변경한다. TBL_SO_D 테이블이 아니고 TBL_SO_P 아닌가?
**************************************************************************************************/
/*
	DECLARE @S_D_ROWCOUNT INT--20210925 정연호. 리턴 수정
	UPDATE SD SET
				DLVY_CNFM_DT		=  REPLACE(@I_DLVY_CNFM_DT,'-','')



	FROM TBL_SO_D AS SD WITH(NOLOCK)
	WHERE 1=1
	AND SD.TBL_SO_M_ID = @I_TBL_SO_M_ID

	SET @S_D_ROWCOUNT = @@ROWCOUNT



	--20210925 정연호 추가. TBL_SO_P 테이블 추가 - 여기에 상품 목록이 들어있음
	DECLARE @S_P_ROWCOUNT INT--20210925 정연호. 리턴 수정
	UPDATE SP SET
				DLVY_CNFM_DT		=  REPLACE(@I_DLVY_CNFM_DT,'-','')



	FROM TBL_SO_P AS SP WITH(NOLOCK)
	WHERE 1=1
	AND SP.TBL_SO_M_ID = @I_TBL_SO_M_ID
	SET @S_P_ROWCOUNT = @@ROWCOUNT

*/

/**************************************************************************************************
시공좌석 점유하기
**************************************************************************************************/

--SELECT * FROM TBL_SO_M


--20210925 정연호. 리턴 수정
--IF(@S_M_ROWCOUNT = 1 and @S_P_ROWCOUNT > 0)
    IF(@S_M_ROWCOUNT = 1 )
BEGIN
SELECT 'Y'    AS RTN_YN,
       '변경완료' AS RTN_MSG

    /*************************************************************************
    주문히스트로에 넣는다.
    *************************************************************************/
    INSERT TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT @I_TBL_SO_M_ID, '주문상담-주문변경', '주문상담-주문변경', '주문상담-주문변경', @I_SAVE_USER, GETDATE()



END
ELSE
BEGIN
SELECT 'N' AS RTN_YN, '변경실패' AS RTN_MSG
END

END

go

