package com.gdut.boot.mapper;

import com.gdut.boot.entity.OneOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Entity com.gdut.boot.entity.OneOrganization
 */
@Mapper
public interface OneOrganizationMapper extends BaseMapper<OneOrganization> {

    List<OneOrganization> listByNames(@Param("names") Set<String> keySet);
}




