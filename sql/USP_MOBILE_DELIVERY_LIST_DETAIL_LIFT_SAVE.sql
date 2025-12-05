/**********************************
1. 목적&기능 : 시공모바일 (시공기사 - 상차하기 )
2. 변경 내용 및 사유 :
3. 실행예시 :

 -- 조회하기
  USP_MOBILE_DELIVERY_LIST_DETAIL_LISTBYALL  612


 -- 상차하기
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'INS', 612,552,1,1
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'INS', 612,553,1,1
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'INS', 612,554,1,1
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'INS', 612,555,1,1
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'INS', 612,556,1,1   -- 미상차
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'INS', 612,557,1,1

 -- 상차취소하기
 USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE 'DEL', 612,0,0,1
 SELECT DLVY_STAT_CD, LIFT_CMPL_YN, * FROM TBL_INST_MOBILE_M WHERE INST_MOBILE_M_ID = 612
 SELECT PROD_STAT_CD, PROD_LIFT_CMPL_YN,* FROM TBL_INST_MOBILE_D WHERE INST_MOBILE_M_ID = 612
 SELECT * FROM TBL_INST_MOBILE_LIFT_D WHERE INST_MOBILE_M_ID = 612
 DELETE A FROM TBL_INST_MOBILE_LIFT_D A
**********************************/
CREATE PROCEDURE [dbo].USP_MOBILE_DELIVERY_LIST_DETAIL_LIFT_SAVE(
    @I_SAVE_GUBN VARCHAR(10), @I_INST_MOBILE_M_ID BIGINT, @I_INST_MOBILE_D_ID BIGINT, @I_LIFT_QTY INT,
    @I_SAVE_USER VARCHAR(30)
)
AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN

    DECLARE @V_QTY INT, @V_LIFT_QTY INT, @I_TBL_SO_M_ID BIGINT
    DECLARE @V_CURRENT_QTY INT, @V_AFTER_QTY INT
    DECLARE @V_SO_TYPE VARCHAR(50), @V_CMPY_CD VARCHAR(10), @V_AGNT_CD VARCHAR(10), @V_SO_NO VARCHAR(50)
    DECLARE @V_PROD_CD VARCHAR(50), @V_TBL_MTRL_M_ID BIGINT, @V_MTRL_NM VARCHAR(200)
    DECLARE @V_MTO_YN CHAR(1)
    -- 상차취소용 변수들
    DECLARE @V_SO_TYPE_CANCEL VARCHAR(50), @V_CMPY_CD_CANCEL VARCHAR(10), @V_AGNT_CD_CANCEL VARCHAR(10), @V_SO_NO_CANCEL VARCHAR(50)
    DECLARE @V_PROD_CD_CANCEL VARCHAR(50), @V_MTRL_NM_CANCEL VARCHAR(200), @V_CURRENT_QTY_CANCEL INT, @V_AFTER_QTY_CANCEL INT
    DECLARE @V_TBL_MTRL_M_ID_CANCEL BIGINT
/**************************************************************************************************
SELECT * FROM TBL_COMM_M
1) 상차하기
**************************************************************************************************/
    IF @I_SAVE_GUBN = 'INS'
        BEGIN
            INSERT INTO TBL_INST_MOBILE_LIFT_D ( INST_MOBILE_M_ID
                                               , INST_MOBILE_D_ID
                                               , LIFT_QTY
                                               , LIFT_DT
                                               , INPT_USER_ID
                                               , INPT_TIME
                                               , USE_YN
                                               , SAVE_USER
                                               , SAVE_TIME
                                               , UPDT_USER
                                               , UPDT_TIME)
            VALUES ( @I_INST_MOBILE_M_ID
                   , @I_INST_MOBILE_D_ID
                   , @I_LIFT_QTY
                   , CONVERT(VARCHAR(10), GETDATE(), 112)
                   , @I_SAVE_USER
                   , GETDATE()
                   , 'Y'
                   , @I_SAVE_USER
                   , GETDATE()
                   , @I_SAVE_USER
                   , GETDATE())

/**************************************************************************************************
2) 상차 아이템에 업데이트 하기
SELECT * FROM TBL_INST_MOBILE_D WHERE @I_INST_MOBILE_D_ID
**************************************************************************************************/
            UPDATE MD
            SET PROD_STAT_CD      = '7000'
              , PROD_LIFT_CMPL_YN ='Y'
              , UPDT_USER         = @I_SAVE_USER
              , UPDT_TIME         = GETDATE()
            FROM TBL_INST_MOBILE_D AS MD
            WHERE 1 = 1
              AND MD.INST_MOBILE_D_ID = @I_INST_MOBILE_D_ID

            /*************************************************************************
            상차 즉시 재고 차감 (부분상차 포함, 품목 단위)
            - 기존의 "상차완료 시 일괄 차감"은 제거하여 중복 차감 방지
            *************************************************************************/
            -- 주문 헤더 식별
            SELECT @I_TBL_SO_M_ID = MD.TBL_SO_M_ID
            FROM TBL_INST_MOBILE_D MD
            WHERE MD.INST_MOBILE_D_ID = @I_INST_MOBILE_D_ID

            -- 주문 정보 조회
            SELECT @V_SO_TYPE = M.SO_TYPE,
                   @V_CMPY_CD = M.CMPY_CD,
                   @V_AGNT_CD = M.AGNT_CD,
                   @V_SO_NO = M.SO_NO
            FROM TBL_SO_M M
            WHERE M.TBL_SO_M_ID = @I_TBL_SO_M_ID

            -- 차감 대상 주문유형만 처리
            IF @V_SO_TYPE IN ('1000', '3100', '4000', '5000', '6000', '9901', '9902', '9905', '9908')
                BEGIN
                    -- 상차된 단일 디테일 품목 조회
                    SELECT @V_PROD_CD = MD.PROD_CD,
                           @V_MTRL_NM = MD.PROD_NM
                    FROM TBL_INST_MOBILE_D MD
                    WHERE MD.INST_MOBILE_D_ID = @I_INST_MOBILE_D_ID

                    -- 해당 품목의 MTO 여부 확인 (MTO_YN = 'N' 인 경우만 재고관리 대상)
                    SELECT @V_MTO_YN = MTO_YN
                    FROM TBL_MTRL_M
                    WHERE CMPY_CD = @V_CMPY_CD
                      AND AGNT_CD = @V_AGNT_CD
                      AND MTRL_CD = @V_PROD_CD

                    IF ISNULL(@V_MTO_YN, 'N') = 'N'
                        BEGIN
                            -- 현재 재고 조회 (MTO_YN = 'N' 인 재고만 관리)
                            SELECT @V_CURRENT_QTY = ISNULL(CURRENT_QUANTITY, 0),
                                   @V_TBL_MTRL_M_ID = TBL_MTRL_M_ID
                            FROM TBL_MTRL_M
                            WHERE CMPY_CD = @V_CMPY_CD
                              AND AGNT_CD = @V_AGNT_CD
                              AND MTRL_CD = @V_PROD_CD
                              AND MTO_YN = 'N'

                            IF @V_CURRENT_QTY IS NULL
                                BEGIN
                                    SET @V_CURRENT_QTY = 0
                                    SET @V_TBL_MTRL_M_ID = 0
                                END

                            -- 차감 후 재고
                            SET @V_AFTER_QTY = @V_CURRENT_QTY - @I_LIFT_QTY

                            -- 재고 차감
                            UPDATE TBL_MTRL_M
                            SET CURRENT_QUANTITY = @V_AFTER_QTY,
                                UPDT_TIME        = GETDATE(),
                                UPDT_USER        = @I_SAVE_USER
                            WHERE CMPY_CD = @V_CMPY_CD
                              AND AGNT_CD = @V_AGNT_CD
                              AND MTRL_CD = @V_PROD_CD
                              AND MTO_YN = 'N'

                            IF @@ROWCOUNT = 0
                                BEGIN
                                    INSERT INTO TBL_MTRL_M (CMPY_CD, AGNT_CD, MTRL_CD, MTRL_NM, CURRENT_QUANTITY,
                                                            MTO_YN,
                                                            SAVE_TIME, SAVE_USER, UPDT_TIME, UPDT_USER)
                                    VALUES (@V_CMPY_CD, @V_AGNT_CD, @V_PROD_CD, @V_MTRL_NM, @V_AFTER_QTY, 'N',
                                            GETDATE(), @I_SAVE_USER, GETDATE(), @I_SAVE_USER)

                                    SELECT @V_TBL_MTRL_M_ID = TBL_MTRL_M_ID
                                    FROM TBL_MTRL_M
                                    WHERE CMPY_CD = @V_CMPY_CD
                                      AND AGNT_CD = @V_AGNT_CD
                                      AND MTRL_CD = @V_PROD_CD
                                      AND MTO_YN = 'N'
                                END

                            -- 재고 변동 이력
                            INSERT INTO TBL_INV_ADJUSTMENT (CMPY_CD, AGNT_CD, AGNT_NM, TBL_MTRL_M_ID, MTRL_CD, MTRL_NM,
                                                            ADJUSTMENT_TYPE, BEFORE_QTY, ADJUSTMENT_QTY, AFTER_QTY,
                                                            ADJUSTMENT_REASON, ADJUSTMENT_DATE,
                                                            SAVE_TIME, SAVE_USER, UPDT_TIME, UPDT_USER, DELETED_YN)
                            VALUES (@V_CMPY_CD, @V_AGNT_CD, '', @V_TBL_MTRL_M_ID, @V_PROD_CD, @V_MTRL_NM,
                                    'LIFT_DEDUCT', @V_CURRENT_QTY, @I_LIFT_QTY, @V_AFTER_QTY,
                                    '상차-자동차감-주문번호:' + @V_SO_NO, CONVERT(DATE, GETDATE()),
                                    GETDATE(), @I_SAVE_USER, GETDATE(), @I_SAVE_USER, 'N')
                        END
                END


/**************************************************************************************************
SELECT * FROM TBL_COMM_M WHERE COMM_CD = 'DLVY_STAT_CD'
SELECT * FROM TBL_COMM_M WHERE COMM_CD = 'SO_STAT_CD'



UPDATE A SET  SEQ = 3  FROM TBL_COMM_M A WHERE COMM_CD = 'DLVY_STAT_CD' AND TBL_COMM_M_ID = 459
UPDATE A SET  SEQ = 4  FROM TBL_COMM_M A WHERE COMM_CD = 'DLVY_STAT_CD' AND TBL_COMM_M_ID = 460
UPDATE A SET  SEQ = 2  FROM TBL_COMM_M A WHERE COMM_CD = 'DLVY_STAT_CD' AND TBL_COMM_M_ID = 461
UPDATE A SET  SEQ = 5  FROM TBL_COMM_M A WHERE COMM_CD = 'DLVY_STAT_CD' AND TBL_COMM_M_ID = 462
UPDATE A SET  SEQ = 6  FROM TBL_COMM_M A WHERE COMM_CD = 'DLVY_STAT_CD' AND TBL_COMM_M_ID = 463

2) 상차후 모바일 상태 바뀌기
4000	배송모바일 확정
5000	해피콜 완료
6999	부분상차(완료)
7000	상차완료
8000	배송완료(미마감)
9999	배송완료
**************************************************************************************************/


            SELECT @V_QTY = SUM(MD.QTY),
                   @V_LIFT_QTY = SUM(ISNULL(LD.LIFT_QTY, 0)),
                   @I_TBL_SO_M_ID = MD.TBL_SO_M_ID
            FROM TBL_INST_MOBILE_D AS MD
                     LEFT OUTER JOIN TBL_INST_MOBILE_LIFT_D AS LD
                                     ON MD.INST_MOBILE_D_ID = LD.INST_MOBILE_D_ID
            WHERE 1 = 1
              AND MD.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID
            GROUP BY MD.TBL_SO_M_ID


            IF (@V_QTY = @V_LIFT_QTY) -- 계획수량과 상차수량이 같으면
                BEGIN
                    --SELECT 1 , @V_QTY , @V_LIFT_QTY
                    UPDATE MM
                    SET DLVY_STAT_CD ='7000' --7000	상차완료
                      , LIFT_CMPL_YN ='Y'
                      , UPDT_USER    = @I_SAVE_USER
                      , UPDT_TIME    = GETDATE()
                    FROM TBL_INST_MOBILE_M AS MM
                    WHERE 1 = 1
                      AND MM.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID


                    /*************************************************************************
                   주문히스트로에 넣는다.
                   *************************************************************************/
                    INSERT TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER,
                                        SAVE_DATE)
                    SELECT TBL_SO_M_ID, SO_STAT_CD, '7000', '모바일-상차완료', @I_SAVE_USER, GETDATE()
                    FROM TBL_SO_M AS M WITH (NOLOCK)
                    WHERE 1 = 1
                      AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

                    /*************************************************************************
                    주문헤더변경
                    *************************************************************************/
                    UPDATE M
                    SET SO_STAT_CD = '7000' -- 상차완료
                    FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
                    WHERE 1 = 1
                      AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


                END
            ELSE
                BEGIN
                    UPDATE MM
                    SET DLVY_STAT_CD ='6999' --6999	부분상차(완료)
                      , LIFT_CMPL_YN ='N'
                      , UPDT_USER    = @I_SAVE_USER
                      , UPDT_TIME    = GETDATE()
                    FROM TBL_INST_MOBILE_M AS MM
                    WHERE 1 = 1
                      AND MM.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID

                    /*************************************************************************
                     주문히스트로에 넣는다.
                     *************************************************************************/
                    INSERT TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER,
                                        SAVE_DATE)
                    SELECT TBL_SO_M_ID, SO_STAT_CD, '6999', '모바일-부분상차(완료)', @I_SAVE_USER, GETDATE()
                    FROM TBL_SO_M AS M WITH (NOLOCK)
                    WHERE 1 = 1
                      AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

                    /*************************************************************************
                    주문헤더변경
                    *************************************************************************/
                    UPDATE M
                    SET SO_STAT_CD = '6999' -- 배송완료 취소  --> 상차완료
                    FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
                    WHERE 1 = 1
                      AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID


                END


            SELECT 'Y' AS RTN_YN, '상차 완료' AS RTN_MSG


        END
    -- 상차완료
/**************************************************************************************************
★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
상차 취소하기  -- 헤더와 디테일을 동시에 지움
**************************************************************************************************/
    IF @I_SAVE_GUBN = 'DEL'
        BEGIN
            -- TBL_SO_M_ID 조회 (재고 복원 처리에 필요)
            SELECT @I_TBL_SO_M_ID = MM.TBL_SO_M_ID
            FROM TBL_INST_MOBILE_M AS MM
            WHERE MM.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID

            /*************************************************************************
            재고 복원 처리 (상차취소 시) - 상차테이블 삭제 전에 실행
            MTO가 아닌 상품만 TBL_MTRL_M.CURRENT_QUANTITY 복원
            *************************************************************************/
            -- 상차취소 시 재고 복원 처리

            SELECT @V_SO_TYPE_CANCEL = M.SO_TYPE,
                   @V_CMPY_CD_CANCEL = M.CMPY_CD,
                   @V_AGNT_CD_CANCEL = M.AGNT_CD,
                   @V_SO_NO_CANCEL = M.SO_NO
            FROM TBL_SO_M M WITH (NOLOCK)
            WHERE M.TBL_SO_M_ID = @I_TBL_SO_M_ID

            -- 재고 차감 대상 주문 유형이었던 경우 재고 복원 처리
            IF @V_SO_TYPE_CANCEL IN ('1000', '3100', '4000', '5000', '6000', '9901', '9902', '9905', '9908')
                BEGIN
                    -- 취소할 상차 품목별 재고 복원 처리
                    DECLARE cancel_cursor CURSOR FOR
                        SELECT MD.PROD_CD, LD.LIFT_QTY, MD.PROD_NM
                        FROM TBL_INST_MOBILE_D MD
                                 INNER JOIN TBL_INST_MOBILE_LIFT_D LD ON MD.INST_MOBILE_D_ID = LD.INST_MOBILE_D_ID
                        WHERE MD.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID
                          AND LD.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID

                    OPEN cancel_cursor
                    FETCH NEXT FROM cancel_cursor INTO @V_PROD_CD_CANCEL, @V_LIFT_QTY, @V_MTRL_NM_CANCEL

                    WHILE @@FETCH_STATUS = 0
                        BEGIN
                            -- TBL_MTRL_M에서 현재 재고 조회 (MTO가 아닌 상품만)
                            SELECT @V_CURRENT_QTY_CANCEL = ISNULL(CURRENT_QUANTITY, 0),
                                   @V_TBL_MTRL_M_ID_CANCEL = TBL_MTRL_M_ID
                            FROM TBL_MTRL_M WITH (NOLOCK)
                            WHERE CMPY_CD = @V_CMPY_CD_CANCEL
                              AND AGNT_CD = @V_AGNT_CD_CANCEL
                              AND MTRL_CD = @V_PROD_CD_CANCEL
                              AND MTO_YN = 'N'
                            -- MTO가 아닌 상품만 재고 관리

                            -- MTO가 아닌 상품만 재고 복원
                            IF @V_TBL_MTRL_M_ID_CANCEL IS NOT NULL
                                BEGIN
                                    -- 복원 후 재고 계산
                                    SET @V_AFTER_QTY_CANCEL = @V_CURRENT_QTY_CANCEL + @V_LIFT_QTY

                                    -- TBL_MTRL_M의 CURRENT_QUANTITY 복원
                                    UPDATE TBL_MTRL_M
                                    SET CURRENT_QUANTITY = @V_AFTER_QTY_CANCEL,
                                        UPDT_TIME        = GETDATE(),
                                        UPDT_USER        = @I_SAVE_USER
                                    WHERE CMPY_CD = @V_CMPY_CD_CANCEL
                                      AND AGNT_CD = @V_AGNT_CD_CANCEL
                                      AND MTRL_CD = @V_PROD_CD_CANCEL
                                      AND MTO_YN = 'N'

                                    -- 재고 변동 이력 기록
                                    INSERT INTO TBL_INV_ADJUSTMENT (CMPY_CD, AGNT_CD, AGNT_NM, TBL_MTRL_M_ID, MTRL_CD,
                                                                    MTRL_NM,
                                                                    ADJUSTMENT_TYPE, BEFORE_QTY, ADJUSTMENT_QTY,
                                                                    AFTER_QTY,
                                                                    ADJUSTMENT_REASON, ADJUSTMENT_DATE,
                                                                    SAVE_TIME, SAVE_USER, UPDT_TIME, UPDT_USER,
                                                                    DELETED_YN)
                                    VALUES (@V_CMPY_CD_CANCEL, @V_AGNT_CD_CANCEL, '', @V_TBL_MTRL_M_ID_CANCEL,
                                            @V_PROD_CD_CANCEL, LEFT(@V_MTRL_NM_CANCEL, 200),
                                            'LIFT_CANCEL', @V_CURRENT_QTY_CANCEL, @V_LIFT_QTY, @V_AFTER_QTY_CANCEL,
                                            '상차취소-자동복원-주문번호:' + @V_SO_NO_CANCEL, CONVERT(DATE, GETDATE()),
                                            GETDATE(), @I_SAVE_USER, GETDATE(), @I_SAVE_USER, 'N')
                                END

                            FETCH NEXT FROM cancel_cursor INTO @V_PROD_CD_CANCEL, @V_LIFT_QTY, @V_MTRL_NM_CANCEL
                        END

                    CLOSE cancel_cursor
                    DEALLOCATE cancel_cursor
                END

            --) 상차테이블 삭제 (재고 복원 처리 후)
            DELETE LD
            FROM TBL_INST_MOBILE_LIFT_D AS LD
            WHERE 1 = 1
              AND LD.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID

            --) 모바일 D 상태 업데이트
            UPDATE MD
            SET PROD_STAT_CD      = '5000' -- 배송기사 확정상태
              -- PROD_STAT_CD		=	'5000'   -- 20211012 정연호 추가해피콜전송 상태로 변경
              , PROD_LIFT_CMPL_YN ='N'
              , UPDT_USER         = @I_SAVE_USER
              , UPDT_TIME         = GETDATE()
            FROM TBL_INST_MOBILE_D AS MD
            WHERE 1 = 1
              AND MD.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID

            --) 모바일 M 상태 업데이트
            UPDATE MM
            SET DLVY_STAT_CD ='5000' -- 배송기사 확정상태
              -- DLVY_STAT_CD		='5000'   -- 20211012 정연호 추가해피콜전송 상태로 변경
              , LIFT_CMPL_YN ='N'
              , UPDT_USER    = @I_SAVE_USER
              , UPDT_TIME    = GETDATE()
            FROM TBL_INST_MOBILE_M AS MM
            WHERE 1 = 1
              AND MM.INST_MOBILE_M_ID = @I_INST_MOBILE_M_ID


            /*************************************************************************
           주문히스트로에 넣는다.
           *************************************************************************/
            INSERT TBL_SO_HIST (TBL_SO_M_ID, UPDT_BEFORE_TXT, UPDT_AFTER_TXT, UPDT_EVENT_TXT, SAVE_USER, SAVE_DATE)
            SELECT TBL_SO_M_ID, SO_STAT_CD, '5000', '모바일-상차취소', @I_SAVE_USER, GETDATE()
            FROM TBL_SO_M AS M WITH (NOLOCK)
            WHERE 1 = 1
              AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

            /*************************************************************************
            주문헤더변경
            *************************************************************************/
            UPDATE M
            SET SO_STAT_CD = '5000' -- 배송완료 취소  --> 상차완료
            FROM DBO.TBL_SO_M AS M WITH (NOLOCK)
            WHERE 1 = 1
              AND M.TBL_SO_M_ID = @I_TBL_SO_M_ID

            SELECT 'Y' AS RTN_YN, '상차 취소' AS RTN_MSG

        END --상차취소 완료

END
go

