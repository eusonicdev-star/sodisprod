package com.sonictms.alsys.common.mapper;

import com.sonictms.alsys.common.entity.ErrorMsgVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorMsgMapper {

	ErrorMsgVO errorSave(ErrorMsgVO errorMsgVO);
}