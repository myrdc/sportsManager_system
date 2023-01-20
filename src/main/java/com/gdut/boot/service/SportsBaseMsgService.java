package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.SecondOrganization;
import com.gdut.boot.entity.SportsBaseMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.vo.SportsBaseMsgVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public interface SportsBaseMsgService extends IService<SportsBaseMsg> {


    Object toVo(SportsBaseMsg record);

    public void insertSportsBaseMsg(List<SportsBaseMsg> beans);

    Msg dealWithExcelBean(List<Object> beans);

    //插入前判断合法
    public Msg dealBeanBefore(List<Object> beans);

    boolean changeOrg(SportsBaseMsg sport, SportsBaseMsgVo sportsBaseMsgVo, SecondOrganization go);
}
