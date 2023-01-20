package com.gdut.boot.service;

import com.gdut.boot.entity.EntranceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.vo.EntranceInfoVo;
import com.gdut.boot.vo.res.EntranceInfoResultVo;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface EntranceInfoService extends IService<EntranceInfo> {

    public EntranceInfoResultVo toVo(EntranceInfo entranceInfo);
}
