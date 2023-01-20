package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.CoachManage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.vo.CoachManageVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public interface CoachManageService extends IService<CoachManage> {

    Object toVo(CoachManage record);

    Msg dealWithOrgAndCoach(CoachManageVo coachManage);

    Msg getStuByCoachId(String number);

    Msg getOneOrgById(String number);

    boolean judgeIsUnExit(CoachManageVo coachManageVo);

    public void insertCoachManage(List<CoachManage> beans);

    Msg dealWithExcelBean(List<Object> beans);

    //插入前判断
    public Msg dealBeanBefore(List<Object> beans);

    Msg getAllStuAndOrg(String number);

    void dealIfPutOrg(CoachManage resource, CoachManageVo after);
}
