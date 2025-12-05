CREATE PROCEDURE dbo.USP_SO_PRINT_REPORT_PICKING_DLVYER(
    @I_CMPY_CD VARCHAR(20),
    @I_DT_TYPE VARCHAR(10),
    @I_FROM_DT VARCHAR(10),
    @I_TO_DT VARCHAR(10),
    @I_DC_LIST VARCHAR(4000),
    @I_TEAM_LIST VARCHAR(4000),
    @I_ZONE_LIST VARCHAR(4000),
    @I_PICK_GRP_AREA VARCHAR(10),
    @I_SAVE_USER VARCHAR(30),
    @I_INST_CTGR_LIST VARCHAR(1000) = NULL
)
/**********************************
1. 목적&기능 : 주문조회 (출력용) - 기사별 피킹리스트
2. 변경 내용 및 사유 :
3. 실행예시 :
USP_SO_PRINT_REPORT_PICKING_DLVYER 'A', '3','20210930','20210930','','','','','1'

SELECT * FROM TBL_SO_M M WHERE DLVY_CNFM_DT ='20210930'
			AND M.SO_TYPE NOT IN ('2000')  --> 2000 반품오더  1000:일반오더 , 2500:교환오더, 4000:AS오더
			AND M.USE_YN ='Y'

AND TEAM =''

SELECT * FROM TBL_SO_P
SELECT * FROM TBL_SO_D
SELECT * FROM TBL_INST_PLAN_M
SELECT * FROM TBL_LGST_DC_M



**********************************/
    AS
SET NOCOUNT ON
SET LOCK_TIMEOUT 60000
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN

/****************************************************************************************************
****************************************************************************************************/
DECLARE
@V_DLVY_CNFM_FROM_DT VARCHAR(10), @V_DLVY_CNFM_TO_DT VARCHAR(10)  	 -- 고객배송일
DECLARE
@V_GI_LIFT_FROM_DT VARCHAR(10), @V_GI_LIFT_TO_DT VARCHAR(10)		 -- 상차일

SELECT @V_DLVY_CNFM_FROM_DT = '20000101', @V_DLVY_CNFM_TO_DT = '29991231'
SELECT @V_GI_LIFT_FROM_DT = '20000101', @V_GI_LIFT_TO_DT = '29991231'

DECLARE
@V_CONVERT_DT VARCHAR(10)

SET @V_CONVERT_DT =''


-- 날짜 포맷 조회 세팅
IF  @I_DT_TYPE	= '3'
BEGIN     -- 고객배송일
SELECT @V_DLVY_CNFM_FROM_DT = @I_FROM_DT,
       @V_DLVY_CNFM_TO_DT = @I_TO_DT SET @V_CONVERT_DT =''
END
IF
@I_DT_TYPE	= '4'
BEGIN     -- 상차일
SELECT @V_GI_LIFT_FROM_DT = @I_FROM_DT,
       @V_GI_LIFT_TO_DT = @I_TO_DT SET @V_CONVERT_DT ='29991231'
END
/*----------------------------------------------------------------------
공장출고일로만 나오도록 하기 위해

----------------------------------------------------------------------*/
--SELECT  @V_GI_LIFT_FROM_DT =  @I_FROM_DT , @V_GI_LIFT_TO_DT =@I_TO_DT


--(A권역:1,3,5,팀  B권역 : 2,4,6팀)

IF
@I_PICK_GRP_AREA ='A'
BEGIN	SET @I_TEAM_LIST = '1,4,5'  END
IF  @I_PICK_GRP_AREA ='B'
BEGIN SET @I_TEAM_LIST = '2,3,6,7'  END
IF  @I_PICK_GRP_AREA ='TOT'
BEGIN SET @I_TEAM_LIST = '1,2,3,4,5,6,7'  END

/****************************************************************************************************
그룹순번을위한 테이블 총 5개를 세팅할수 있도록 구성
****************************************************************************************************/
CREATE TABLE #TMP_SEQ
(
    PAGE_NO INT IDENTITY(1,1),
    GRP1    VARCHAR(100),
    GRP2    VARCHAR(100),
    GRP3    VARCHAR(100),
    GRP4    VARCHAR(100),
    GRP5    VARCHAR(100),
    GRP6    VARCHAR(100)
)

/****************************************************************************************************
출력물 임시테이블 생성
DROP TABLE  #TMP_REPORT_M
****************************************************************************************************/

CREATE TABLE #TMP_REPORT_FINAL
(
    PAGE_NO       INT,
    DLVY_CNFM_DT  VARCHAR(50),
    GI_LIFT_DT    VARCHAR(50),
    PALT_NOXX     VARCHAR(50),
    DLVY_ER       VARCHAR(50),
    DLVY_TEAM     VARCHAR(50),
    DLVY_GRP_AREA VARCHAR(50),
    PROD_CD       VARCHAR(100),
    PROD_NM       VARCHAR(200),
    SMPL_CD       VARCHAR(50),
    PSTN_NM       VARCHAR(50),
    QTY           INT,
    CBM           DECIMAL(10, 2),
    TOT_QTY       INT,
    TOT_CBM       DECIMAL(10, 2),
    DC_NM         VARCHAR(100),
    PRINT_TIME    DATETIME,
    PRINT_USER    VARCHAR(100),
    INST_CTGR     VARCHAR(10)
)

/****************************************************************************************************
출력물 임시테이블 생성
DROP TABLE  #TMP_REPORT_M
****************************************************************************************************/
    INSERT #TMP_REPORT_FINAL  (

							 DLVY_CNFM_DT
							,GI_LIFT_DT
							,PALT_NOXX
							,DLVY_ER
							,DLVY_TEAM
							,DLVY_GRP_AREA


							,PROD_CD
							,PROD_NM
							,SMPL_CD
							,PSTN_NM
							,QTY
							,CBM

							,DC_NM
							,PRINT_TIME
							,PRINT_USER
							,INST_CTGR
			)
SELECT M.DLVY_CNFM_DT
     , M.GI_LIFT_DT
     , ISNULL(IP.PALT_NOXX, '시공계획 X')  AS PALT_NOXX
     , ISNULL(UM.USER_NM, '기사배정 X')    AS DLVY_ER
     , DLVY_TEAM = ISNULL(DBO.F_COL_NM('A', 'COMM', 'TEAM', UM.TEAM), '팀없음')
     , ''


     , ISNULL(BM.C_MTRL_CD, P.PROD_CD) AS PROD_CD
     , ISNULL(MM.MTRL_NM, P.PROD_NM)   AS PROD_NM
     , CASE
           WHEN M.SO_TYPE IN ('5000', '6000') OR ISNULL(MM.MTO_YN, MM2.MTO_YN) = 'Y' THEN M.ACPT_ER
           ELSE ISNULL(MM.SMPL_CD, MM2.SMPL_CD) END
     --,CASE WHEN M.SO_TYPE ='5000' THEN M.ACPT_ER ELSE  ISNULL(MM.SMPL_CD,'') END AS SMPL_CD
     --,ISNULL(MM.SMPL_CD,'') AS SMPL_CD
     , ISNULL(MM.PSTN_NM, '')          AS PSTN_NM

     , SUM(ISNULL(BM.QTY * P.QTY, P.QTY))
     , SUM(ISNULL(MM.CBM, MM2.CBM))


     , DC_NM = DBO.F_COL_NM('A', 'DC_CD', 'DC_CD', M.DC_CD) --물류센터
     , GETDATE()
     , PRINT_USER = (SELECT TOP 1 USER_NM FROM TBL_USER_M X
WITH (NOLOCK)
WHERE X.TBL_USER_M_ID = @I_SAVE_USER )
    , P.INST_CTGR
FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
    LEFT OUTER JOIN TBL_INST_PLAN_M AS IP
WITH (NOLOCK)
ON M.TBL_SO_M_ID = IP.TBL_SO_M_ID
    LEFT OUTER JOIN TBL_USER_M AS UM
WITH (NOLOCK)
ON IP.TBL_USER_M_ID = UM.TBL_USER_M_ID
    INNER JOIN DBO.TBL_SO_P AS P
WITH (NOLOCK)
ON M.TBL_SO_M_ID = P.TBL_SO_M_ID
    LEFT OUTER JOIN TBL_BOM_M AS BM
WITH (NOLOCK)
ON P.PROD_CD = BM.MTRL_CD AND M.AGNT_CD = BM.AGNT_CD AND BM.USE_YN='Y'
    LEFT OUTER JOIN TBL_MTRL_M AS MM
WITH (NOLOCK)
ON BM.C_MTRL_CD = MM.MTRL_CD AND BM.AGNT_CD = MM.AGNT_CD
    LEFT OUTER JOIN TBL_MTRL_M AS MM2
WITH (NOLOCK)
ON P.PROD_CD = MM2.MTRL_CD AND M.AGNT_CD = MM2.AGNT_CD


WHERE 1=1
--AND M.DLVY_CNFM_DT BETWEEN @I_FROM_DT AND @I_TO_DT
--AND M.GI_LIFT_DT BETWEEN @V_GI_LIFT_FROM_DT AND @V_GI_LIFT_TO_DT

  AND (M.DLVY_CNFM_DT BETWEEN @V_DLVY_CNFM_FROM_DT
  AND @V_DLVY_CNFM_TO_DT )
  AND (M.GI_LIFT_DT BETWEEN @V_GI_LIFT_FROM_DT
  AND @V_GI_LIFT_TO_DT
   OR ISNULL(M.GI_LIFT_DT
    , '') = @V_CONVERT_DT)

  AND M.SO_TYPE IN ('1000'
    , '3100'
    , '5000'
    , '6000'
    , '9901')
  AND M.USE_YN ='Y'
  AND P.USE_YN ='Y'
  AND M.SO_STAT_CD <> '0000'
  AND M.SO_STAT_CD >= '2500' -- 배송계획확정인것만 보이도록 합니다.


  AND EXISTS (SELECT 1 FROM DBO.SPLIT_STR (ISNULL(@I_DC_LIST
    , '')
    , ',') AS T WHERE T.VAL = M.DC_CD
    UNION ALL
    SELECT 1 WHERE ISNULL(@I_DC_LIST
    , '') ='' )

  AND EXISTS (SELECT 1 FROM DBO.SPLIT_STR (ISNULL(@I_TEAM_LIST
    , '')
    , ',') AS T WHERE T.VAL = UM.TEAM
    UNION ALL
    SELECT 1 WHERE ISNULL(@I_TEAM_LIST
    , '') ='' )

  AND EXISTS (SELECT 1 FROM DBO.SPLIT_STR (ISNULL(@I_ZONE_LIST
    , '')
    , ',') AS T WHERE T.VAL = MM.PICK_ZONE
    UNION ALL
    SELECT 1 WHERE ISNULL(@I_ZONE_LIST
    , '') ='' )

  AND EXISTS (SELECT 1 FROM DBO.SPLIT_STR (ISNULL(@I_INST_CTGR_LIST
    , '')
    , ',') AS T WHERE T.VAL = P.INST_CTGR
    UNION ALL
    SELECT 1 WHERE ISNULL(@I_INST_CTGR_LIST
    , '') ='' )

GROUP BY
    M.DLVY_CNFM_DT
        , M.GI_LIFT_DT
        , ISNULL(IP.PALT_NOXX, '시공계획 X')
        , UM.USER_NM
        , ISNULL(DBO.F_COL_NM('A', 'COMM', 'TEAM', UM.TEAM), '팀없음')
--		,DBO.F_COL_NM('A','COMM','TEAM',UM.TEAM)
--		,CASE WHEN 	UM.TEAM IN ( '1','3','5') THEN 'A'
--		  	  WHEN 	UM.TEAM IN ( '2','4','6','7') THEN 'B'
--			  ELSE 'X' END


        , ISNULL(BM.C_MTRL_CD, P.PROD_CD)
        , ISNULL(MM.MTRL_NM, P.PROD_NM)
        , CASE WHEN M.SO_TYPE IN ('5000', '6000') OR ISNULL(MM.MTO_YN, MM2.MTO_YN) ='Y' THEN M.ACPT_ER ELSE ISNULL(MM.SMPL_CD, MM2.SMPL_CD)
END
		--,ISNULL(MM.SMPL_CD,'')
		--,CASE WHEN M.SO_TYPE ='5000' THEN M.ACPT_ER ELSE  ISNULL(MM.SMPL_CD,'') END
		,ISNULL(MM.PSTN_NM,'')

		,DBO.F_COL_NM('A','DC_CD','DC_CD',M.DC_CD)
		,P.INST_CTGR



/****************************************************************************************************
3) 그룹별 페이징_순번을 구한다.
4) 최종테이블에 페이징 업데이트  (DLVY_CNFM_DT, DC_NM, PALT_NOXX,DLVY_ER, DLVY_TEAM)
****************************************************************************************************/
INSERT #TMP_SEQ (GRP1, GRP2, GRP3, GRP4,GRP5,GRP6)
SELECT DISTINCT DLVY_CNFM_DT, DC_NM, PALT_NOXX, DLVY_ER, DLVY_TEAM, INST_CTGR
FROM #TMP_REPORT_FINAL
ORDER BY DLVY_CNFM_DT, DC_NM, PALT_NOXX, DLVY_ER, DLVY_TEAM, INST_CTGR

UPDATE A
SET PAGE_NO = B.PAGE_NO FROM #TMP_REPORT_FINAL A
INNER JOIN #TMP_SEQ  B
ON A.DLVY_CNFM_DT = B.GRP1
    AND A.DC_NM = B.GRP2
    AND A.PALT_NOXX = B.GRP3
    AND A.DLVY_ER = B.GRP4
    AND A.DLVY_TEAM = B.GRP5
    AND A.INST_CTGR = B.GRP6

/****************************************************************************************************
총량업데이트  (DLVY_CNFM_DT, DC_NM, PALT_NOXX,DLVY_ER, DLVY_TEAM)
****************************************************************************************************/
UPDATE A
SET TOT_CBM = X.TOT_CBM,
    TOT_QTY = X.TOT_QTY FROM #TMP_REPORT_FINAL A  INNER JOIN
	(SELECT DLVY_CNFM_DT, DC_NM, PALT_NOXX,DLVY_ER, DLVY_TEAM,INST_CTGR,
	SUM(CBM) AS TOT_CBM , SUM(QTY) AS TOT_QTY FROM  #TMP_REPORT_FINAL
	GROUP BY  DLVY_CNFM_DT, DC_NM, PALT_NOXX,DLVY_ER, DLVY_TEAM,INST_CTGR
	) X
ON A.DLVY_CNFM_DT = X.DLVY_CNFM_DT
    AND A.DC_NM = X.DC_NM
    AND A.PALT_NOXX = X.PALT_NOXX
    AND A.DLVY_ER = X.DLVY_ER
    AND A.DLVY_TEAM = X.DLVY_TEAM
    AND A.INST_CTGR = X.INST_CTGR


/****************************************************************************************************
5) 최종데이터 출력
****************************************************************************************************/
SELECT PAGE_NO         -- 페이지 번호
     , PALT_NOXX       -- 팔렛트 번호
-- 헤더
--- 고객배송일
     , LEFT (DLVY_CNFM_DT
     , 4) + '년 ' + SUBSTRING (DLVY_CNFM_DT
     , 5
     , 2) +'월 ' + RIGHT (DLVY_CNFM_DT
     , 2) + '일' AS DLVY_CNFM_DT
     , LEFT (GI_LIFT_DT
     , 4) + '년 ' + SUBSTRING (GI_LIFT_DT
     , 5
     , 2) +'월 ' + RIGHT (GI_LIFT_DT
     , 2) + '일' AS GI_LIFT_DT
     , DLVY_ER         -- 배송기사
--  배송팀
     , ISNULL(DLVY_TEAM
     , '') +'('+ ISNULL(DLVY_GRP_AREA
     , '') + ')' AS DLVY_TEAM


     , PROD_CD         -- 상세 - 제품코드
     , ISNULL(SMPL_CD
     , '') AS SMPL_CD  -- 상세 - 심플코드
     , ISNULL(PSTN_NM
     , '') AS PSTN_NM  -- 상세 - 제자리표  --> 심플코드옆 추가
--,PROD_NM						-- 상세 - 제품명
     , replace(replace(PROD_NM
     , char (13)
     , '')
     , char (10)
     , ' ') AS PROD_NM -- 상세 - 제품명 -- 20220207 정연호 수정. 개행문자 수정
     , QTY             -- 상세 - 수량
     , ISNULL(CBM
     , 0) AS CBM       -- 상세 - CBM

     , ISNULL(TOT_CBM
     , 0) AS TOT_CBM   -- CBM 합계
     , TOT_QTY         -- 수량총합
     , DC_NM           -- 물류센터
     , PRINT_TIME      -- 출력시간
     , PRINT_USER      -- 출력자
     , INST_CTGR
     , INST_CTGR_NM = DBO.F_COL_NM('A'
     , 'COMM'
     , 'INST_CAPA_CTGR'
     , INST_CTGR)
FROM #TMP_REPORT_FINAL
ORDER BY PAGE_NO, PALT_NOXX, SMPL_CD, PROD_CD



DROP TABLE #TMP_SEQ
DROP TABLE #TMP_REPORT_FINAL



END



go

