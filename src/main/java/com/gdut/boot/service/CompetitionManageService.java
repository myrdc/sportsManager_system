package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.CoachManage;
import com.gdut.boot.entity.CompetitionManage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.vo.res.CompetitionManageVoResultVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public interface CompetitionManageService extends IService<CompetitionManage> {

    public CompetitionManageVoResultVo toVo(CompetitionManage competitionManage);

    public void insertCompetitionManage(List<CompetitionManage> beans);

    Msg dealWithExcelBean(List<Object> beans);

    //插入前判断
    public Msg dealBeanBefore(List<Object> beans);

    Msg getCompetitionByCoachNumber(String number, String state);
}
