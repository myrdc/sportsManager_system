package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.CompetitionManage;
import com.gdut.boot.entity.Notice;
import com.gdut.boot.mapper.CompetitionManageMapper;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.NoticeService;
import com.gdut.boot.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

    @Autowired
    private CompetitionManageService competitionManageService;

    @Autowired
    private CompetitionManageMapper competitionManageMapper;

    @Override
    public Msg getContentById(int contentId, int type) {
        if(type == AllClass.CompetitionManage.getValue()){
            List<CompetitionManage> contents = competitionManageService.list(new QueryWrapper<CompetitionManage>().eq("id", contentId));
            return Msg.success().setData(competitionManageService.toVo(contents.get(0)));
        }
        return null;
    }
}




