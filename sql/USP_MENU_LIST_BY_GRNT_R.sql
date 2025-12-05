-- 기능 : 화면에 표시할 메뉴 조회 by 사용자 아이디의 권한 정보에 따라서 불러옴
CREATE PROCEDURE [dbo].[USP_MENU_LIST_BY_GRNT_R](@I_CMPY_CD VARCHAR(20), @I_SYS_CD VARCHAR(100),
                                                 @I_USER_GRNT_CD VARCHAR(100))
AS
    SET NOCOUNT ON
    SET LOCK_TIMEOUT 60000
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

BEGIN
SELECT             MM.CMPY_CD,
                   CM.COMD_NM                 AS CMPY_NM,
                   MG.USER_GRNT_CD,
                   USER_GRNT_NM = (SELECT COMD_NM FROM F_COMM_NM_LOAD(MM.CMPY_CD, 'USER_GRNT_CD', MG.USER_GRNT_CD)),
                   MM.SYS_CD,
                   SYS_NM       = (SELECT COMD_NM FROM F_COMM_NM_LOAD(MM.CMPY_CD, 'SYS_CD', MM.SYS_CD)),
                   MM.TBL_MENU_M_ID,
                   RTRIM(LTRIM(MM.MENU_NM_1)) AS MENU_NM_1,
                   RTRIM(LTRIM(MM.MENU_NM_2)) AS MENU_NM_2,
                   RTRIM(LTRIM(MM.MENU_NM_3)) AS MENU_NM_3,
                   RTRIM(LTRIM(MM.MENU_NM_4)) AS MENU_NM_4,
                   RTRIM(LTRIM(MM.MENU_CD))   AS MENU_CD,
                   RTRIM(LTRIM(MM.MENU_LVL))  AS MENU_LVL,
                   MM.URL

FROM DBO.TBL_MENU_M AS MM WITH (NOLOCK)
             INNER JOIN DBO.TBL_MENU_GRNT_M AS MG WITH (NOLOCK)
ON MG.TBL_MENU_M_ID = MM.TBL_MENU_M_ID
    AND MG.CMPY_CD = @I_CMPY_CD
    AND MG.USER_GRNT_CD LIKE @I_USER_GRNT_CD + '%'
    LEFT OUTER JOIN [sodisproddb_mssql].[dbo].[TBL_COMM_M] AS CM
    ON CM.COMD_CD = MM.CMPY_CD
    AND CM.COMM_CD = 'CMPY_CD'
    AND CM.CMPY_CD = 'A'
WHERE 1 = 1
  AND MM.USE_YN = 'Y'

order by convert(int, LEFT(RTRIM(LTRIM(MM.MENU_CD)), 2))
        , convert(int, LEFT(RTRIM(LTRIM(MM.MENU_CD)), 4))
        , convert(int, RTRIM(LTRIM(MM.MENU_LVL)))
        , convert(int, RTRIM(LTRIM(MM.MENU_CD)))
END
go

