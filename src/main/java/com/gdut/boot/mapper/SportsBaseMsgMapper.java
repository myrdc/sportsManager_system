package com.gdut.boot.mapper;

import com.gdut.boot.entity.SportsBaseMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdut.boot.mapper.base.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Entity com.gdut.boot.entity.SportsBaseMsg
 */
@Mapper
public interface SportsBaseMsgMapper extends EasyBaseMapper<SportsBaseMsg> {

    List<SportsBaseMsg> listByNames(@Param("names")Queue<String> names);

    List<SportsBaseMsg> selectBySeId(Integer secondId);
}




