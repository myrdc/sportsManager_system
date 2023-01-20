package com.gdut.boot.mapper;

import com.gdut.boot.entity.SecondOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Entity com.gdut.boot.entity.SecondOrganization
 */
@Mapper
public interface SecondOrganizationMapper extends BaseMapper<SecondOrganization> {

    List<SecondOrganization> selectByNames(@Param("names") Collection<?> names);

    List<SecondOrganization> selectByIds(@Param("ids") Set<Integer> ids);

    List<SecondOrganization> selectByCoachId(Integer coachId);
}




