package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.entity.SportCompetition;
import com.gdut.boot.service.SportCompetitionService;
import com.gdut.boot.mapper.SportCompetitionMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SportCompetitionServiceImpl extends ServiceImpl<SportCompetitionMapper, SportCompetition>
    implements SportCompetitionService{

    @Autowired
    private SportCompetitionMapper sportCompetitionMapper;

    @Override
    public void insertSportCompetition(List<SportCompetition> beans) {
        sportCompetitionMapper.insertBatchSomeColumn(beans);
    }
}




