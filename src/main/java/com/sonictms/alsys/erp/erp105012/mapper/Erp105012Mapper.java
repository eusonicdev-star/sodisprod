package com.sonictms.alsys.erp.erp105012.mapper;

import com.sonictms.alsys.erp.erp105012.entity.Erp105012VO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface Erp105012Mapper {
	List<Erp105012VO> getProductPickingRackList(Erp105012VO vo);
	void updateProductPickingRack(Erp105012VO vo);
	// 추가된 메서드
	List<Map<String, Object>> getStockLocationList(Erp105012VO vo);
	List<Map<String, Object>> getAvailablePickingRackList(Erp105012VO vo);

	void autoAssignPickingRack(Erp105012VO vo);

	String getLocIdByCode(Erp105012VO vo);

	void updatePickingRackByCode(Erp105012VO vo);

	void updateFixedLocFromFloor(Erp105012VO vo);
}