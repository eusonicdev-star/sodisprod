package com.sonictms.alsys.common.mapper;

import com.sonictms.alsys.common.entity.CommonSrchVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommonSrchMapper {
    List<CommonSrchVO> selectLocationSearchList(CommonSrchVO vo);
}