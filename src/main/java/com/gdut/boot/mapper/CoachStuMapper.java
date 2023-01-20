package com.gdut.boot.mapper;

import com.gdut.boot.entity.CoachStu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @Entity com.gdut.boot.entity.CoachStu
 */
@Mapper
public interface CoachStuMapper extends BaseMapper<CoachStu> {

    /**
     * 通过set集合插入教练和学生关系表 一个教练多个学生
     * @param integers 教练id
     * @param stuId 学生id
     */
    void insertByCoachSet(@Param("integers") Set<Integer> integers, @Param("stuId") int stuId);

    /**
     * 一个学生多个教练
     * @param keySet
     * @param id
     */
    void insertByStuList(@Param("keySet") Set<Integer> keySet, Integer id);
}




