CREATE PROCEDURE [dbo].[USP_SO_ALLUPDATE_UPDT](
    @I_TBL_SO_M_ID BIGINT,
    @I_UPDT_GUBN VARCHAR(10), -- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
    -- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
    @I_SO_STAT_CD VARCHAR(50),
    @I_SO_TYPE VARCHAR(20),
    @I_DC_CD VARCHAR(50),
    @I_DLVY_CNFM_DT VARCHAR(10),
    @I_GI_LIFT_DT VARCHAR(10),
    @I_DLVY_RQST_MSG VARCHAR(1000),
    @I_INST_MEMO VARCHAR(500),
    @I_DLVY_STAT_CD VARCHAR(50),
    @I_DLVY_CMPL_DT VARCHAR(10),
    @I_SAVE_USER VARCHAR(30)
)
/**********************************
1. 목적&기능 : 주문일괄변경 (변경하기)
2. 변경 내용 및 사유 :
3. 실행예시 :
USP_SO_ALLUPDATE_UPDT 286684,'4','','','','20220130','','','','','', 'SYS'
USP_SO_ALLUPDATE_UPDT 243946,'1','1200','','','','','','','','','1'

SELECT * FROM TBL_SO_M WHERE SO_STAT_CD ='9999' AND SO_TYPE ='1000' AND ORDR_INPT_DT ='20211111'

SELECT * FROM TBL_SO_HIST WHERE TBL_SO_M_ID =
SELECT * FROM TBL_SO_D
SELECT  * FROM TBL_POST_M
SELECT * FROM TBL_COMM_M

SELECT VAL FROM SPLIT_STR ('', '/')

SELECT A.DC_CD,B.DC_CD, * FROM TBL_SO_M A WITH(NOLOCK) INNER JOIN TBL_SO_P B WITH(NOLOCK) ON A.TBL_SO_M_ID = B.TBL_SO_M_ID
WHERE A.SO_NO IN (
'102108020349',
'102108020350',
'102108020351',
'102108020352')

SELECT * FROM TBL_INST_PLAN_M



**********************************/
AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN


    DECLARE
@V_SO_STAT_CD VARCHAR(20) , @V_SO_STAT_NM VARCHAR(20) , @V_GI_LIFT_DT VARCHAR(10) , @V_DLVY_CNFM_DT VARCHAR(10)
    DECLARE
@V_INST_DT VARCHAR(20) , @V_SO_NO VARCHAR(30), @V_SO_TYPE VARCHAR(10)
    DECLARE
@V_INST_CTGR VARCHAR(20), @V_INST_TYPE VARCHAR(20), @V_INST_SEAT_TYPE VARCHAR(20),@V_ZONE_TYPE VARCHAR(20),@V_ZONE_CD VARCHAR(20)
    DECLARE
@V_CAPA_ID BIGINT, @V_TOT_CAPA INT, @V_USE_CAPA INT , @V_POST_CD VARCHAR(10) , @V_WEEK_CD VARCHAR(10)
    DECLARE
@V_WEEK_YN VARCHAR(10) , @V_WEEK_NM VARCHAR(10), @V_DLVY_ZONE_TYPE VARCHAR(50)
    DECLARE
@V_DLVY_WORK_YN VARCHAR(10), @V_CDC_WORK_YN VARCHAR(10), @V_RDC_WORK_YN VARCHAR(10)


    DECLARE
@S_M_COUNT INT = 0 --20210916 정연호 주문헤더 성공 수
    DECLARE
@S_P_COUNT INT = 0
    --20210916 정연호 주문상세 성공 수


/*******************************************************************************************************************************************
■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
*******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
SELECT DBO.F_COL_NM('A','COMM','SO_STAT_CD','1200')
SELECT * FROM TBL_COMM_M WHERE COMM_CD ='SO_STAT_CD' ORDER BY COMD_CD
*************************************************************************/
    IF @I_UPDT_GUBN = '1'
BEGIN

            /*************************************************************************
            주문상태를 불러와서 체크 합니다.
            *************************************************************************/
SELECT @V_SO_STAT_CD = SO_STAT_CD,
       @V_SO_STAT_NM = DBO.F_COL_NM(M.CMPY_CD, 'COMM', 'SO_STAT_CD', SO_STAT_CD),
       @V_SO_TYPE = SO_TYPE
FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


/*

        --1200	주문확정
        ----------------------------------------------------------------------------------------------------------------------------------
        IF (@I_SO_STAT_CD = '1200') BEGIN
        -- 1200 보다 현재 상태가 크면 변경하지 않습니다.
                IF (@V_SO_STAT_CD >=  @I_SO_STAT_CD) BEGIN
                            SELECT 	'N' AS  RTN_YN  ,
                            '주문상태변경불가 :' + '[' + @V_SO_STAT_CD +']'+  @V_SO_STAT_NM +'는 이미 주문확정되었습니다.'   AS RTN_MSG
                            ,@I_TBL_SO_M_ID AS TBL_SO_M_ID
                            RETURN
                END
        END
*/


/************************************************************************************************************************
	시공좌석을 체크하지 않는 오더들  12월 03일 요청사항
		공통코드 'SO_ORDR_TYPE'
		4000	업체직출
		9906	택배반품
		9905	택배
************************************************************************************************************************/


SET @V_SO_STAT_NM = DBO.F_COL_NM('A', 'COMM', 'SO_STAT_CD', @I_SO_STAT_CD)


    --2000	배송일확정 /2500	상차일확정
    -- 주문삭제나 해피콜보류이면 시공좌석을 삭제한다.
    IF (@V_SO_STAT_CD IN ('2000', '2500') AND @I_SO_STAT_CD IN ('1200'))
BEGIN

                    /*************************************************************************
                    주문헤더변경
                    *************************************************************************/
UPDATE M
SET SO_STAT_CD   = @I_SO_STAT_CD,
    DLVY_CNFM_DT = '',
    GI_LIFT_DT   = '',
    USE_YN       ='Y' FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문히스트로에 넣는다.
*************************************************************************/
INSERT
TBL_SO_HIST
(
TBL_SO_M_ID
,
UPDT_BEFORE_TXT
,
UPDT_AFTER_TXT
,
UPDT_EVENT_TXT
,
SAVE_USER
,
SAVE_DATE
)
SELECT TBL_SO_M_ID,
       SO_STAT_CD,
       @I_SO_STAT_CD + '-' + ISNULL(@V_SO_STAT_NM, ''),
       '주문일괄변경-주문상태변경(배송확정일/상차일 초기화)',
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


END
ELSE
BEGIN
                    /*************************************************************************
                    주문헤더변경
                    *************************************************************************/
UPDATE M
SET SO_STAT_CD = @I_SO_STAT_CD,
    USE_YN     ='Y' FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문히스트로에 넣는다.
*************************************************************************/
INSERT
TBL_SO_HIST
(
TBL_SO_M_ID
,
UPDT_BEFORE_TXT
,
UPDT_AFTER_TXT
,
UPDT_EVENT_TXT
,
SAVE_USER
,
SAVE_DATE
)
SELECT TBL_SO_M_ID,
       SO_STAT_CD,
       @I_SO_STAT_CD + '-' + ISNULL(@V_SO_STAT_NM, ''),
       '주문일괄변경-주문상태변경',
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


END


            -- 주문삭제나 해피콜보류이면 시공좌석을 삭제한다.
            IF
(@I_SO_STAT_CD IN ('0000', '1000', '1200', '1900'))
BEGIN

                    -- 주문캐파 삭제
                    DELETE
A
                    FROM TBL_INST_CAPA_D A
                    WHERE TBL_SO_M_ID = @I_TBL_SO_M_ID

END

            -- 주문상태가 8050(지방상차후취소)로 변경된 경우 TBL_RESTOCK_WAIT 테이블에 삽입
            IF
@I_SO_STAT_CD = '8050'
BEGIN
INSERT INTO TBL_RESTOCK_WAIT (TBL_SO_M_ID, SO_NO, SO_TYPE, PROD_CD, PROD_NM, RETURN_QTY,
                              INBOUND_YN, INBOUND_DT, INBOUND_USER, INBOUND_TIME, CMPY_CD,
                              SAVE_USER, SAVE_TIME, UPDT_USER, UPDT_TIME)
SELECT @I_TBL_SO_M_ID,
       M.SO_NO,
       M.SO_TYPE,
       P.PROD_CD,
       P.PROD_NM,
       P.QTY,
       'N',
       NULL,
       NULL,
       NULL,
       M.CMPY_CD,
       @I_SAVE_USER,
       GETDATE(),
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M M WITH (NOLOCK)
                             INNER JOIN TBL_SO_P P
WITH (NOLOCK)
ON M.TBL_SO_M_ID = P.TBL_SO_M_ID
WHERE M.TBL_SO_M_ID = @I_TBL_SO_M_ID
END

-- 주문이 배송완료(9999) 또는 주문취소(0000)로 변경된 경우 TBL_RESTOCK_WAIT에서 삭제
--             IF @I_SO_STAT_CD IN ('0000', '9999')
--                 BEGIN
--                     DELETE
--                     FROM TBL_RESTOCK_WAIT
--                     WHERE TBL_SO_M_ID = @I_TBL_SO_M_ID
--                 END


SELECT 'Y' AS RTN_YN, '수정완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID

--END

END

    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/


    IF
@I_UPDT_GUBN = '2'
BEGIN
            /*********************************************************************************************************************
                    주문상태를 불러와서 체크 합니다.
            **************************************************************************************************************************/
SELECT @V_SO_STAT_CD = SO_STAT_CD,
       @V_SO_STAT_NM = DBO.F_COL_NM(M.CMPY_CD, 'COMM', 'SO_STAT_CD', SO_STAT_CD),
       @V_SO_TYPE = SO_TYPE
FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


/************************************************************************************************************************
	주문유형: 주문접수(1000), 주문확정(1200) 상태값에서 주문유형 변경가능
	공통코드 'SO_STAT_CD'
************************************************************************************************************************/

    IF (@V_SO_STAT_CD IN ('1000'
    , '1200'))
BEGIN


                    /*************************************************************************
                        주문히스트로에 넣는다.
                    *************************************************************************/
                    INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER,
                                        SAVE_DATE)
SELECT TBL_SO_M_ID, SO_TYPE, @I_SO_TYPE, '주문유형변경', @I_SAVE_USER, GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문헤더변경
*************************************************************************/
UPDATE M
SET SO_TYPE = @I_SO_TYPE FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


-- 기존주문이 좌석을 체크하지 않는 주문이면,,캐파를 삭제한다.
    IF (@V_SO_TYPE IN ('3200'
    , '4000'
    , '4200'
    , '9904'
    , '9905'
    , '9906'
    , '9907'))
BEGIN

                            -- 주문캐파 삭제
                            DELETE
A
                            FROM TBL_INST_CAPA_D A
                            WHERE TBL_SO_M_ID = @I_TBL_SO_M_ID


END

-- 주문유형이 9904(반품(정품))로 변경된 경우 TBL_RESTOCK_WAIT 테이블에 삽입
                    IF
@I_SO_TYPE = '9904'
BEGIN
INSERT INTO TBL_RESTOCK_WAIT (TBL_SO_M_ID, SO_NO, SO_TYPE, PROD_CD, PROD_NM, RETURN_QTY,
                              INBOUND_YN, INBOUND_DT, INBOUND_USER, INBOUND_TIME, CMPY_CD,
                              SAVE_USER, SAVE_TIME, UPDT_USER, UPDT_TIME)
SELECT @I_TBL_SO_M_ID,
       M.SO_NO,
       @I_SO_TYPE,
       P.PROD_CD,
       P.PROD_NM,
       P.QTY,
       'N',
       NULL,
       NULL,
       NULL,
       M.CMPY_CD,
       @I_SAVE_USER,
       GETDATE(),
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M M WITH (NOLOCK)
                                     INNER JOIN TBL_SO_P P
WITH (NOLOCK)
ON M.TBL_SO_M_ID = P.TBL_SO_M_ID
WHERE M.TBL_SO_M_ID = @I_TBL_SO_M_ID
END

-- -- 주문유형이 9904(반품(정품))가 아닌 다른 유형으로 변경된 경우 TBL_RESTOCK_WAIT에서 삭제
--                     IF @I_SO_TYPE != '9904' AND @I_SO_TYPE != ''
--                         BEGIN
--                             DELETE
--                             FROM TBL_RESTOCK_WAIT
--                             WHERE TBL_SO_M_ID = @I_TBL_SO_M_ID
--                         END

SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID


END
ELSE
BEGIN
SELECT 'N'            AS RTN_YN,
       '주문접수(1000), 주문확정(1200) 상태값에서 주문유형 변경가능/' + '주문유형변경불가 :' + '[' + @V_SO_STAT_CD + ']' +
       @V_SO_STAT_NM +
       ' 상태입니다. '     AS RTN_MSG
        ,
       @I_TBL_SO_M_ID AS TBL_SO_M_ID
    RETURN

END


END
    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '3'
BEGIN
            /*************************************************************************
            주문히스트로에 넣는다.
            SELECT DBO.F_COL_NM('A','DC_CD','DC_CD','1000')


            *************************************************************************/
            INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT TBL_SO_M_ID,
       DBO.F_COL_NM('A', 'DC_CD', 'DC_CD', DC_CD),
       DBO.F_COL_NM('A', 'DC_CD', 'DC_CD', @I_DC_CD),
       '주문일괄변경-물류센터변경',
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문헤더변경
*************************************************************************/
UPDATE M
SET DC_CD = @I_DC_CD FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문아이템변경
*************************************************************************/
UPDATE P
SET DC_CD = @I_DC_CD FROM DBO.TBL_SO_P AS P
WITH (NOLOCK)
WHERE 1 = 1
  AND P.TBL_SO_M_ID = @I_TBL_SO_M_ID

SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID

END

    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
-- SELECT * FROM TBL_COMM_M WHERE COMM_CD = 'SO_STAT_CD'
*************************************************************************/
    IF
@I_UPDT_GUBN = '4'
BEGIN
            --배송일 확정에는 상태값에 따라 바꿀수 있다.
            /*************************************************************************
            주문상태를 불러와서 체크 합니다.
            *************************************************************************/
SELECT @V_SO_STAT_CD = SO_STAT_CD,
       @V_SO_STAT_NM = DBO.F_COL_NM(M.CMPY_CD, 'COMM', 'SO_STAT_CD', SO_STAT_CD),
       @V_INST_CTGR = P.INST_CTGR
FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
                     INNER JOIN DBO.TBL_SO_P AS P
ON M.TBL_SO_M_ID = P.TBL_SO_M_ID
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

    /*************************************************************************
    시공카테고리에 의해 배송권역-권역을 세팅하여 가져옵니다.
    SELECT * FROM TBL_COMM_M WHERE COMM_CD ='ZONE_TYPE'
    1000 --> 가구 / 2000 --> 가전 / 3000 --> 케어서비스
    *************************************************************************/
    IF (@V_INST_CTGR = '1000')
BEGIN
                    SET
@V_DLVY_ZONE_TYPE = 'DLVY_ZONE_CD'
END
            IF
(@V_INST_CTGR = '2000')
BEGIN
                    SET
@V_DLVY_ZONE_TYPE = 'DLVY_ZONE_CD_2'
END
            IF
(@V_INST_CTGR = '3000')
BEGIN
                    SET
@V_DLVY_ZONE_TYPE = 'DLVY_ZONE_CD_3'
END


            --박종현 요청 1225
            IF
(@V_SO_STAT_CD <= '1000' AND @V_SO_STAT_CD >= '2500')
BEGIN
SELECT 'N'                                                                         AS RTN_YN,
       '변경불가:' + '[' + @V_SO_STAT_CD + ']' + @V_SO_STAT_NM + '는 배송확정일 변경 불가합니다.  ' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                                              AS TBL_SO_M_ID
    RETURN
END


            -- 요일배송가져오기
            --@V_DLVY_WORK_YN VARCHAR(10), @V_CDC_WORK_YN VARCHAR(10), @V_RDC_WORK_YN VARCHAR(10)

SELECT @V_WEEK_CD = CLND_WEEK_CD,
       @V_DLVY_WORK_YN = DLVY_WORK_YN,
       @V_CDC_WORK_YN = CDC_WORK_YN,
       @V_RDC_WORK_YN = RDC_WORK_YN
FROM TBL_CALENDA_M AS C WITH (NOLOCK)
WHERE CLND_DAY = REPLACE(@I_DLVY_CNFM_DT, '-', '')

SELECT @V_SO_TYPE = SM.SO_TYPE,
       @V_POST_CD = SM.POST_CD,
       @V_SO_NO = SM.SO_NO,
       @V_ZONE_TYPE = SM.ZONE_TYPE,
       @V_ZONE_CD = SM.ZONE_CD,
       @V_INST_CTGR = SP.INST_CTGR,
       @V_INST_TYPE = SP.INST_TYPE,
       @V_INST_SEAT_TYPE = SP.INST_SEAT_TYPE,

       @V_SO_STAT_CD = SM.SO_STAT_CD,
       @V_SO_STAT_NM = DBO.F_COL_NM('A', 'COMM', 'SO_STAT_CD', SM.SO_STAT_CD),
       @V_DLVY_CNFM_DT = SM.DLVY_CNFM_DT,
       @V_WEEK_YN = CASE
                        WHEN @V_WEEK_CD = 'MON' THEN WD.MON_YN
                        WHEN @V_WEEK_CD = 'TUE' THEN WD.TUE_YN
                        WHEN @V_WEEK_CD = 'WED' THEN WD.WED_YN
                        WHEN @V_WEEK_CD = 'THU' THEN WD.THU_YN
                        WHEN @V_WEEK_CD = 'FRI' THEN WD.FRI_YN
                        WHEN @V_WEEK_CD = 'SAT' THEN WD.SAT_YN
                        WHEN @V_WEEK_CD = 'SUN' THEN WD.SUN_YN
                        ELSE ' ' END


FROM TBL_SO_M AS SM WITH (NOLOCK)
                     INNER JOIN TBL_SO_P AS SP
WITH (NOLOCK)
ON SM.TBL_SO_M_ID = SP.TBL_SO_M_ID
    LEFT OUTER JOIN TBL_LGST_ZONEPOST_M AS ZM
WITH (NOLOCK)
ON ZM.POST_CD = SM.POST_CD AND ZM.CMPY_CD = SM.CMPY_CD AND
    ZM.ZONE_TYPE = @V_DLVY_ZONE_TYPE
    --AND ZM.ZONE_TYPE ='DLVY_ZONE_CD'

    LEFT OUTER JOIN TBL_LGST_WEEKDLVY_M AS WD
WITH (NOLOCK)
ON ZM.ZONE_CD = WD.ZONE_CD AND ZM.ZONE_TYPE = WD.ZONE_TYPE AND
    ZM.CMPY_CD = WD.CMPY_CD
WHERE SM.TBL_SO_M_ID = @I_TBL_SO_M_ID


/*************************************************************************
신규 배송일지정이 안되어 있으면 메시지 추가
*************************************************************************/
    IF ISNULL(@V_WEEK_CD
    , '') = ''
BEGIN
SELECT 'N'                                                         AS RTN_YN,
       '변경불가:' + '[배송달력]이 세팅이 안되어 있습니다. 기준정보>물류정보>배송달력관리를 확인하세요! ' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                              AS TBL_SO_M_ID
    RETURN


SELECT *
FROM TBL_COMM_M
WHERE COMM_CD = 'DLVY_ZONE_CD'
  AND COMD_NM LIKE '%동작%'

END


/*************************************************************************
공장달력이 막혀있으면 막혀잇다고 표시
*************************************************************************/
            IF
ISNULL(@V_DLVY_WORK_YN, '') = 'N'
BEGIN
SELECT 'N'                            AS RTN_YN,
       '변경불가:' + '[공장달력]이 배송불가일입니다. ' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                 AS TBL_SO_M_ID
    RETURN
END


/*************************************************************************
권역별/요일배송 체크
*************************************************************************/
            IF
ISNULL(@V_WEEK_YN, '') = ''
BEGIN
SELECT 'N'                                                                   AS RTN_YN,
       '변경불가:' + '[권역별/요일배송]이 세팅이 안되어 있습니다.1)우편번호-배송권역 확인, 2)배송권역-요일배송제 확인 ' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                                        AS TBL_SO_M_ID
    RETURN
END


/************************************************************************************************************************
	시공좌석을 체크하지 않는 오더들  220110 박종현 요청사항
		공통코드 'SO_ORDR_TYPE'

3200	교환오더(반품)
4000	업체직출
4200	업체직출(회수)
9904	반품(정품)
9905	택배
9906	택배반품
9907	기타


************************************************************************************************************************/

            IF
(@V_SO_TYPE IN ('3200', '4000', '4200', '9904', '9905', '9906', '9907'))
BEGIN

                    /*************************************************************************
                    주문헤더변경
                    공통코드 'SO_STAT_CD'
                    *************************************************************************/
UPDATE M
SET SO_STAT_CD          = '2000' -- 배송일확정
  , DLVY_CNFM_DT        = REPLACE(@I_DLVY_CNFM_DT, '-', '')
  , DLVY_CNFM_INPT_ER   = @I_SAVE_USER
  , DLVY_CNFM_INPT_TIME = GETDATE() FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문아이템변경
*************************************************************************/
UPDATE P
SET DLVY_CNFM_DT = REPLACE(@I_DLVY_CNFM_DT, '-', '') FROM DBO.TBL_SO_P AS P
WITH (NOLOCK)
WHERE 1 = 1
  AND P.TBL_SO_M_ID = @I_TBL_SO_M_ID


/*************************************************************************
주문히스트로에 넣는다.
*************************************************************************/
INSERT
TBL_SO_HIST
(
TBL_SO_M_ID
,
UPDT_BEFORE_TXT
,
UPDT_AFTER_TXT
,
UPDT_EVENT_TXT
,
SAVE_USER
,
SAVE_DATE
)
SELECT TBL_SO_M_ID,
       DLVY_CNFM_DT + @V_SO_STAT_NM,
       REPLACE(@I_DLVY_CNFM_DT, '-', '') + 'SO_STAT_CD - 2000 배송일확정',
       '주문일괄변경-배송일지정/오더상태변경',
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


SELECT 'Y'                                           AS RTN_YN
     , '변경완료' + CONVERT(VARCHAR (10), ISNULL(@V_CAPA_ID, 0)) +
       CONVERT(VARCHAR (10), ISNULL(@V_TOT_CAPA, 0))
     , CONVERT(VARCHAR (10), ISNULL(@V_USE_CAPA, 0)) AS RTN_MSG
     , @I_TBL_SO_M_ID                                AS TBL_SO_M_ID


END
ELSE
BEGIN


                    /************************************************************************************************************************
                        요일배송 적용하지 않는 오더유형  20220110박종현 요청사항
                            공통코드 'SO_ORDR_TYPE'
                            9902	익일
                            9903	익일(반품)
                            9904	정상(반품)
                            9906	택배반품
                            9905	택배
                            -->
                    4000	업체직출
                    4200	업체직출(회수)
                    9902	익일
                    9903	익일(반품)
                    9904	반품(정품)
                    9905	택배
                    9906	택배반품
                    9907	기타
                    9908	익일(설치)


                            위 오더 유형은 요일배송을 체크하지 않습니다.
                    ************************************************************************************************************************/
                    IF
(@V_SO_TYPE IN ('4000', '4200', '9902', '9903', '9904', '9905', '9906', '9907', '9908'))
BEGIN
                            SET
@V_WEEK_YN = 'Y'
END
/************************************************************************************************************************/


                    IF
(ISNULL(@V_WEEK_YN, '') = 'N')
BEGIN
SELECT 'N'                                                                    AS RTN_YN,
       '요일배송제 :' + ISNULL(@V_WEEK_CD, '') + ' - ' + ISNULL(@V_WEEK_YN, '못찾음') AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                                         AS TBL_SO_M_ID
    RETURN
END


                    -------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------

-------------------------------------------------------------------------
/************************************************************************************************************************
공통코드 'INST_CAPA_TYPE'  ; 익일/익일반품
************************************************************************************************************************/
                    IF
(@V_SO_TYPE IN ('9902', '9903', '9908'))
BEGIN
                            SET
@V_INST_TYPE = 'NEXTDAY'
END


                    /*************************************************************************
                 시공좌석
                 *************************************************************************/
                    --SELECT * FROM TBL_SO
                    -- 신규날짜의 총캐파와 CAPA-ID를 구하기
SELECT @V_CAPA_ID = CM.CAPA_ID, @V_TOT_CAPA = ISNULL(CM.TOT_CAPA, 0)
FROM TBL_INST_CAPA_M AS CM WITH (NOLOCK)
WHERE CM.CAPA_CTGR = ISNULL(@V_INST_CTGR
    , '1000')
  AND CM.CAPA_TYPE = ISNULL(@V_INST_TYPE
    , 'NORMAL')
  AND CM.SEAT_TYPE = ISNULL(@V_INST_SEAT_TYPE
    , '1111')
  AND CM.ZONE_TYPE = ISNULL(@V_ZONE_TYPE
    , '1000_ZONE_CD')
  AND CM.ZONE_CD = ISNULL(@V_ZONE_CD
    , '1000')
  AND CM.INST_DT = REPLACE(@I_DLVY_CNFM_DT
    , '-'
    , '')

    /*************************************************************************
 시공좌석 생성이 안되어 있는경우,
 *************************************************************************/
    IF (ISNULL(@V_CAPA_ID
    , '') = '')
BEGIN
SELECT 'N'                                                       AS RTN_YN,
       REPLACE(@I_DLVY_CNFM_DT, '-', '') + ' 시공좌석 생성이 안되어 있습니다.' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                            AS TBL_SO_M_ID
    RETURN
END

                    -- 사용 캐파 구하기
SELECT @V_USE_CAPA = SUM(ISNULL(USE_CAPA, 0))
FROM TBL_INST_CAPA_D AS CD WITH (NOLOCK)
WHERE CD.CAPA_ID = @V_CAPA_ID
  AND CD.TBL_SO_M_ID <> @I_TBL_SO_M_ID
-- 본인오더는 캐파에서 빼주기


    /*************************************************************************
    시공좌석  --> -- 총캐파가 남아 잇는지 체크
    *************************************************************************/
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
    IF (ISNULL(@V_TOT_CAPA
    , 0)
    > ISNULL(@V_USE_CAPA
    , 0))
BEGIN

                            -- 1) 기존좌석이 있으면 삭제한다. --> 과거나 미래 어느날 오더
                            DELETE
A
                            FROM TBL_INST_CAPA_D AS A WITH (NOLOCK)
                            WHERE A.TBL_SO_M_ID = @I_TBL_SO_M_ID

                            -- 2) 좌석 추가하기
                            INSERT TBL_INST_CAPA_D (CAPA_ID, TBL_SO_M_ID, INST_DT, ORDR_NO, USE_CAPA, USE_YN, SAVE_USER,
                                                    SAVE_TIME)
                            VALUES (@V_CAPA_ID, @I_TBL_SO_M_ID, REPLACE(@I_DLVY_CNFM_DT, '-', ''), @V_SO_NO, 1, 'Y',
                                    @I_SAVE_USER, GETDATE())


                            /*************************************************************************
                            주문헤더변경
                            공통코드 'SO_STAT_CD'
                            *************************************************************************/
UPDATE M
SET SO_STAT_CD          = '2000' -- 배송일확정
  , DLVY_CNFM_DT        = REPLACE(@I_DLVY_CNFM_DT, '-', '')
  , DLVY_CNFM_INPT_ER   = @I_SAVE_USER
  , DLVY_CNFM_INPT_TIME = GETDATE() FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문아이템변경
*************************************************************************/
UPDATE P
SET DLVY_CNFM_DT = REPLACE(@I_DLVY_CNFM_DT, '-', '') FROM DBO.TBL_SO_P AS P
WITH (NOLOCK)
WHERE 1 = 1
  AND P.TBL_SO_M_ID = @I_TBL_SO_M_ID


/*************************************************************************
주문히스트로에 넣는다.
*************************************************************************/
INSERT
TBL_SO_HIST
(
TBL_SO_M_ID
,
UPDT_BEFORE_TXT
,
UPDT_AFTER_TXT
,
UPDT_EVENT_TXT
,
SAVE_USER
,
SAVE_DATE
)
SELECT TBL_SO_M_ID,
       DLVY_CNFM_DT + @V_SO_STAT_NM,
       REPLACE(@I_DLVY_CNFM_DT, '-', '') + 'SO_STAT_CD - 2000 배송일확정',
       '주문일괄변경-배송일지정/오더상태변경',
       @I_SAVE_USER,
       GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


SELECT 'Y'                                           AS RTN_YN
     , '변경완료' + CONVERT(VARCHAR (10), ISNULL(@V_CAPA_ID, 0)) +
       CONVERT(VARCHAR (10), ISNULL(@V_TOT_CAPA, 0))
     , CONVERT(VARCHAR (10), ISNULL(@V_USE_CAPA, 0)) AS RTN_MSG
     , @I_TBL_SO_M_ID                                AS TBL_SO_M_ID
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
END
ELSE
BEGIN

SELECT 'N'                                            AS RTN_YN,
       '시공좌석부족 총:' + CONVERT(VARCHAR (10), ISNULL(@V_TOT_CAPA, '')) + '사용:' +
       CONVERT(VARCHAR (10), ISNULL(@V_USE_CAPA, '')) AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                 AS TBL_SO_M_ID
    RETURN

END -- 시공좌석 체크 END

END --교환오더 , 업체직출, 택배 날짜 변경 END


END
    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '5'
BEGIN


            /*************************************************************************
            주문의 상태 가져오기
            1) 주문상태가 팩킹준비중 이후이면 변경이 불가능합니다. ---> 4000 배송모바일 확정
            SELECT * FROM TBL_COMM_M WHERE COMM_CD = 'SO_STAT_CD'
            *************************************************************************/
SELECT @V_SO_STAT_CD = SO_STAT_CD,
       @V_SO_STAT_NM = DBO.F_COL_NM(M.CMPY_CD, 'COMM', 'SO_STAT_CD', M.SO_STAT_CD),
       @V_GI_LIFT_DT = REPLACE(M.GI_LIFT_DT, '-', ''),
       @V_DLVY_CNFM_DT = REPLACE(M.DLVY_CNFM_DT, '-', '')
FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


    /*************************************************************************
    1) 주문상태가 팩킹준비중 이후이면 변경이 불가능합니다. ---> 4000 배송모바일 확정
    공통코드 'SO_STAT_CD'
    *************************************************************************/
--박종현 요청 1225

--2500 상차일 확정  3000 배송계획확정 되면 배송일확정 불가
    IF (@V_SO_STAT_CD <> '2000')
BEGIN
SELECT 'N'                                                                                AS RTN_YN,
       '상차일변경 불가 : ' + '[' + @V_SO_STAT_CD + ']' + @V_SO_STAT_NM + ' 주문상태가 배송일 확정이 아닙니다 ' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                                                     AS TBL_SO_M_ID
    RETURN
END


            /*************************************************************************
            2) 배송확정일이 결정되어 있지 않으면 상차일을 변경할수 없다.
            *************************************************************************/
            IF
(ISNULL(@V_DLVY_CNFM_DT, '') = '')
BEGIN
SELECT 'N'                                  AS RTN_YN,
       '상차일변경 불가 : ' + '배송확정일이 지정되지 않았습니다.' AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                       AS TBL_SO_M_ID
    RETURN

END

            /*************************************************************************
            3)  상차일은 배송일보다 클수 없습니다.
            *************************************************************************/
            IF
(REPLACE(@I_GI_LIFT_DT, '-', '') > @V_DLVY_CNFM_DT)
BEGIN
SELECT 'N'                                                                              AS RTN_YN,
       '상차일변경 불가 : ' + '상차일은 배송확정일보다 빨라야 합니다. 배송확정일을 변경하세요. [배송확정일]-' + @V_DLVY_CNFM_DT AS RTN_MSG
        ,
       @I_TBL_SO_M_ID                                                                   AS TBL_SO_M_ID
    RETURN

END


            /*************************************************************************
            주문헤더변경
            *************************************************************************/
UPDATE M
SET GI_LIFT_DT        = REPLACE(@I_GI_LIFT_DT, '-', '')
  , GI_LIFT_INPT_ER   = @I_SAVE_USER
  , GI_LIFT_INPT_TIME = GETDATE()
  , SO_STAT_CD        = '2500' -- 상차일 확정
  , USE_YN='Y' FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


/*************************************************************************
주문히스트로에 넣는다.
*************************************************************************/
INSERT
TBL_SO_HIST
(
TBL_SO_M_ID
,
UPDT_BEFORE_TXT
,
UPDT_AFTER_TXT
,
UPDT_EVENT_TXT
,
SAVE_USER
,
SAVE_DATE
)
VALUES (@I_TBL_SO_M_ID, @V_GI_LIFT_DT, @I_GI_LIFT_DT, '주문일괄변경-상차일변경', @I_SAVE_USER, GETDATE())

-- 성공시 리턴됩니다.
SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID

END
    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '6'
BEGIN
            /*************************************************************************
            주문히스트로에 넣는다.
            *************************************************************************/
            INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT TBL_SO_M_ID, DLVY_RQST_MSG, @I_DLVY_RQST_MSG, '주문일괄변경-배송메시지변경', @I_SAVE_USER, GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문헤더변경
*************************************************************************/
UPDATE M
SET DLVY_RQST_MSG = ISNULL(DLVY_RQST_MSG, '') + @I_DLVY_RQST_MSG FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID
SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID
END
    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '7'
BEGIN
            /*************************************************************************
            주문히스트로에 넣는다. (배송기사변경)
            *************************************************************************/
            INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT TBL_SO_M_ID, DLVY_RQST_MSG, @I_DLVY_RQST_MSG, '주문일괄변경-배송메시지변경', @I_SAVE_USER, GETDATE()
FROM TBL_SO_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문헤더변경
*************************************************************************/
UPDATE M
SET DLVY_RQST_MSG = @I_DLVY_RQST_MSG FROM DBO.TBL_SO_M AS M
WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID
END
    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '8'
BEGIN
            SET
@S_P_COUNT = @@ROWCOUNT


SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID

END

    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '9'
BEGIN
            --SET @S_P_COUNT = @@ROWCOUNT


            /*************************************************************************
            주문히스트로에 넣는다. (배송상태 변경)
            *************************************************************************/
            INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT TBL_SO_M_ID, DLVY_STAT_CD, @I_DLVY_STAT_CD, '주문일괄변경-배송상태변경', @I_SAVE_USER, GETDATE()
FROM TBL_INST_MOBILE_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
주문헤더변경
*************************************************************************/

UPDATE MM
SET DLVY_STAT_CD = @I_DLVY_STAT_CD FROM TBL_INST_MOBILE_M AS MM
WITH (NOLOCK)
WHERE 1 = 1
  AND MM.TBL_SO_M_ID = @I_TBL_SO_M_ID

SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID

END

    /*******************************************************************************************************************************************
    ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    *******************************************************************************************************************************************/
/*************************************************************************
-- 1 : 주문상태변경, 2: 주문유형변경, 3:물류센터변경, 4:배송확정일변경, 5: 상차일변경,
-- 6 : 배송요구사항변경, 7:배송기사변경, 8:기사메모변경, 9:배송상태변경 10: 배송완료일변경
*************************************************************************/
    IF
@I_UPDT_GUBN = '10'
BEGIN
            /*************************************************************************
    주문히스트로에 넣는다. (배송완료일 변경)
    *************************************************************************/
            INSERT
TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
SELECT TBL_SO_M_ID, DLVY_CMPL_DT, @I_DLVY_CMPL_DT, '주문일괄변경-배송완료일변경', @I_SAVE_USER, GETDATE()
FROM TBL_INST_MOBILE_M AS M WITH (NOLOCK)
WHERE 1 = 1
  AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

/*************************************************************************
배송완료일 변경
*************************************************************************/

UPDATE MM
SET DLVY_CMPL_DT = @I_DLVY_CMPL_DT FROM TBL_INST_MOBILE_M AS MM
WITH (NOLOCK) -- INNER JOIN TBL_INST_MOBILE_COMPLETE  AS MC WITH(NOLOCK)  ON MM.INST_MOBILE_M_ID = MC.INST_MOBILE_M_ID
WHERE 1 = 1
  AND MM.TBL_SO_M_ID = @I_TBL_SO_M_ID


SELECT 'Y' AS RTN_YN, '변경완료' AS RTN_MSG, @I_TBL_SO_M_ID AS TBL_SO_M_ID
END


    /*************************************************************************
    배송계획을 삭제한다.
    --TBL_INST_PLAN_M
    *************************************************************************/
/*************************************************************************
모바일 데이터 삭제
 --TBL_INST_MOBILE_M
*************************************************************************/

--SELECT 	'Y' AS  RTN_YN  , '수정완료' AS RTN_MSG  ,@I_TBL_SO_M_ID AS TBL_SO_M_ID

--	SET @S_P_COUNT = @@ROWCOUNT


    /********************************************************
    --SELECT 	'Y' AS  RTN_YN  , '수정완료' AS RTN_MSG
    IF(@S_M_COUNT = 1 and @S_P_COUNT > 0)
    BEGIN

    END
    ELSE
    BEGIN
        IF(@S_M_COUNT = 0)
        BEGIN
            SELECT 	'N' AS  RTN_YN  , '헤더수정실패' AS RTN_MSG
        END
        ELSE IF (@S_M_COUNT > 1)
        BEGIN
            SELECT 	'N' AS  RTN_YN  , '헤더에 동일한 오더번호가 '+@S_M_COUNT+'건 존재' AS RTN_MSG
        END
        ELSE IF (@S_P_COUNT = 0)
        BEGIN
            SELECT 	'N' AS  RTN_YN  , '상세품목 수정실패' AS RTN_MSG
        END

    END

    ********************************************************/

END
go

