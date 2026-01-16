package com.sonictms.alsys.hyapp.outbound.service;

import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.inbound.mapper.HyappInboundMapper;
import com.sonictms.alsys.hyapp.outbound.entity.HyappOutboundVO;
import com.sonictms.alsys.hyapp.outbound.mapper.HyappOutboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HyappOutboundService {

    private final HyappOutboundMapper outboundMapper;

    public List<HyappOutboundVO> selectPickListByDlvyCnfmDt(
            String cmpyCd, String dcCd, String fromDt, String toDt) {
        return outboundMapper.selectPickListByDlvyCnfmDt(cmpyCd, dcCd, fromDt, toDt);
    }
}

