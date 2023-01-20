package com.gdut.boot.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.conf.ScheduledConfig;
import com.gdut.boot.entity.Scheduled;
import com.gdut.boot.mapper.ScheduledMapper;
import com.gdut.boot.service.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static com.gdut.boot.constance.common.Task.scanBy000;
import static com.gdut.boot.util.FileUtils.redisDeleteFile;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 固定时间扫描固定路径
 * @verdion
 * @date 2021/11/1910:44
 */
@Configuration
public class ScanFileAndDelete extends ScheduledConfig {
    @Autowired
    private ScheduledMapper scheduledMapper;

    @Autowired
    private ScheduledService scheduledService;

    /**
     * 执行定时任务
     */
    @Override
    public void taskService() {
        int open = getOpen();
        if(open == 1){
            redisDeleteFile();
        }
    }

    @Override
    public String getCron() {
        QueryWrapper<Scheduled> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", scanBy000);
        Scheduled task = scheduledMapper.selectOne(queryWrapper);
        if(task != null){
            return task.getCron();
        }
        return null;
    }

    @Override
    public int getOpen() {
        QueryWrapper<Scheduled> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", scanBy000);
        Scheduled task = scheduledMapper.selectOne(queryWrapper);
        if(task != null){
            return task.getOpen();
        }
        return -1;
    }

}
