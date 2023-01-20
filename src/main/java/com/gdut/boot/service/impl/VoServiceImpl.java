package com.gdut.boot.service.impl;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.*;
import com.gdut.boot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.gdut.boot.constance.cache.Cache.reflectors;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/4/1818:54
 */

@Service
public class VoServiceImpl implements VoService {

    @Autowired
    private CompetitionManageService competitionManageService;

    @Autowired
    private EntranceInfoService entranceInfoService;

    @Autowired
    private OneOrganizationService oneOrganizationService;

    @Autowired
    private CoachManageService coachManageService;

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private SecondOrganizationService secondOrganizationService;


    @Override
    public Object toVo(Object record, int type) {
        return toVoByType(record, type);
    }

    @Override
    public Object toVoByType(Object record, int type) {
        try {
            return reflectors.get(type).getServiceInvoker().invoke("toVo", new Object[]{record}, reflectors.get(type).getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
