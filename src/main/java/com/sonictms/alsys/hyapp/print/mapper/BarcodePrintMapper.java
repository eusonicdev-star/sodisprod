package com.sonictms.alsys.hyapp.print.mapper;

import com.sonictms.alsys.hyapp.print.entity.PrintQueueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BarcodePrintMapper {
    // 출력 대기 목록 조회 (STATUS = '0')
    List<PrintQueueVO> selectPendingList();

    // 최근 출력 이력 조회 (STATUS = '1' 또는 '9')
    List<PrintQueueVO> selectPrintHistory();

    // 상태 업데이트
    void updatePrintStatus(@Param("seq") int seq,
                           @Param("status") String status,
                           @Param("errMsg") String errMsg);

    // 단건 조회를 위한 메서드 추가
    PrintQueueVO selectPrintInfoBySeq(@Param("seq") int seq);
}