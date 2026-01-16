package com.sonictms.alsys.hyapp.outbound.mapper;

import com.sonictms.alsys.hyapp.outbound.entity.HyappOutboundVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HyappOutboundMapper {
    List<HyappOutboundVO> selectPickListByDlvyCnfmDt(
            @Param("cmpyCd") String cmpyCd,
            @Param("dcCd") String dcCd,
            @Param("fromDt") String fromDt,
            @Param("toDt") String toDt
    );
}
