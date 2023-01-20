package com.gdut.boot.mapper;

import com.gdut.boot.entity.CoachManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdut.boot.mapper.base.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Entity com.gdut.boot.entity.CoachManage
 */
@Mapper
public interface CoachManageMapper extends EasyBaseMapper<CoachManage> {

    void insertByGroupAndCoachId(@Param("groups") Set<Integer> groups, Integer coachManageId);
}




