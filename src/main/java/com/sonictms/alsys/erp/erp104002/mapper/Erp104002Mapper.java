package com.sonictms.alsys.erp.erp104002.mapper;

import com.sonictms.alsys.erp.erp104002.entity.Erp104002VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp104002Mapper {

	List<Erp104002VO> erp104002List( Erp104002VO erp90100VO);

}