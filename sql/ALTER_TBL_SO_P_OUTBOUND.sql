-- TBL_SO_P 테이블에 출고 관련 컬럼 추가
-- 택배와 업체직출 출고 처리를 위한 컬럼

-- 실행 전 확인: 기존 컬럼이 있는지 확인
SELECT COLUMN_NAME 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'TBL_SO_P' 
AND COLUMN_NAME IN ('COURIER_CD', 'COURIER_NM', 'WAYBILL_NO', 'OUTBOUND_DT', 'OUTBOUND_USER');

-- 컬럼 추가
ALTER TABLE TBL_SO_P ADD COURIER_CD varchar(20) NULL;
ALTER TABLE TBL_SO_P ADD COURIER_NM varchar(100) NULL;
ALTER TABLE TBL_SO_P ADD WAYBILL_NO varchar(100) NULL;
ALTER TABLE TBL_SO_P ADD OUTBOUND_DT varchar(10) NULL;
ALTER TABLE TBL_SO_P ADD OUTBOUND_USER varchar(30) NULL;

-- 컬럼 설명
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'택배운송사 코드', 
    @level0type = N'SCHEMA', @level0name = N'dbo',
    @level1type = N'TABLE', @level1name = N'TBL_SO_P',
    @level2type = N'COLUMN', @level2name = N'COURIER_CD';

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'택배운송사 명칭', 
    @level0type = N'SCHEMA', @level0name = N'dbo',
    @level1type = N'TABLE', @level1name = N'TBL_SO_P',
    @level2type = N'COLUMN', @level2name = N'COURIER_NM';

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'송장번호', 
    @level0type = N'SCHEMA', @level0name = N'dbo',
    @level1type = N'TABLE', @level1name = N'TBL_SO_P',
    @level2type = N'COLUMN', @level2name = N'WAYBILL_NO';

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'출고일 (YYYYMMDD)', 
    @level0type = N'SCHEMA', @level0name = N'dbo',
    @level1type = N'TABLE', @level1name = N'TBL_SO_P',
    @level2type = N'COLUMN', @level2name = N'OUTBOUND_DT';

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'출고 처리자', 
    @level0type = N'SCHEMA', @level0name = N'dbo',
    @level1type = N'TABLE', @level1name = N'TBL_SO_P',
    @level2type = N'COLUMN', @level2name = N'OUTBOUND_USER';

PRINT '컬럼 추가 완료';

-- 확인 쿼리
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'TBL_SO_P' 
AND COLUMN_NAME IN ('COURIER_CD', 'COURIER_NM', 'WAYBILL_NO', 'OUTBOUND_DT', 'OUTBOUND_USER')
ORDER BY ORDINAL_POSITION;

