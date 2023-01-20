package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.SecondOrganization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.vo.SportsBaseMsgVo;

/**
 *
 */
public interface SecondOrganizationService extends IService<SecondOrganization> {

    Msg deal(SportsBaseMsgVo sportsBaseMsgVo);

    Msg selectStusById(Integer id, Integer page, Integer size);

    Msg selectCoachById(Integer id, Integer page, Integer size);

    Object toVo(SecondOrganization record);
}
