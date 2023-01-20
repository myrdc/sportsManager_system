package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.AttendanceManege;
import com.gdut.boot.entity.EntranceInfo;
import com.gdut.boot.mapper.EntranceInfoMapper;
import com.gdut.boot.mapper.SportsBaseMsgMapper;
import com.gdut.boot.service.AttendanceManegeService;
import com.gdut.boot.mapper.AttendanceManegeMapper;
import com.gdut.boot.service.EntranceInfoService;
import com.gdut.boot.service.SportsBaseMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Service
public class AttendanceManegeServiceImpl extends ServiceImpl<AttendanceManegeMapper, AttendanceManege>
    implements AttendanceManegeService{

    @Autowired
    private EntranceInfoService entranceInfoService;

    @Autowired
    private AttendanceManegeService attendanceManegeService;

    @Override
    public Msg getByCoach(String coach) {
        List<EntranceInfo> belongCoach = entranceInfoService.list(new QueryWrapper<EntranceInfo>().like("belong_coach", coach));
        List<String> finalNum = new LinkedList<>();
        for (EntranceInfo entranceInfo : belongCoach) {
            if(entranceInfo.getBelongCoach().contains(coach)){
                //如果学生包含这个教练，就要加入学号
                finalNum.add(entranceInfo.getNumber());
            }
        }
        if(finalNum.isEmpty()){
            return Msg.fail("没有数据");
        }
        List<AttendanceManege> stus = attendanceManegeService.list(new QueryWrapper<AttendanceManege>().in("stu_number", finalNum));
        return Msg.success("成功", stus);
    }
}




