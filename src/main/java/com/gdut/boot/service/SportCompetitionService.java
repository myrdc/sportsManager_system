package com.gdut.boot.service;

import com.gdut.boot.entity.SportCompetition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.entity.SportsBaseMsg;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public interface SportCompetitionService extends IService<SportCompetition> {

    public void insertSportCompetition(List<SportCompetition> beans);

}
