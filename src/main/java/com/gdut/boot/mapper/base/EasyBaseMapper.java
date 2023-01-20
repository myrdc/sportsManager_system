package com.gdut.boot.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author JLHWASX
 * @Description mapper类，用于继承BaseMapper，excel批量导入
 * @verdion
 * @date 2022/7/15 0:51
 */

@Mapper
public interface EasyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入 仅适用于MysqL
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(List<T> entityList);
}
