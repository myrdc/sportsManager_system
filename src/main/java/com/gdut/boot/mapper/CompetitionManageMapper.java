package com.gdut.boot.mapper;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.CompetitionManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdut.boot.mapper.base.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Entity com.gdut.boot.entity.CompetitionManage
 */
@Mapper
public interface CompetitionManageMapper extends EasyBaseMapper<CompetitionManage> {

    List<CompetitionManage> selectByNumber(String number, String state);
}




