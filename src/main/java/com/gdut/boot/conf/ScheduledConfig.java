package com.gdut.boot.conf;

import com.gdut.boot.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 配置动态定时任务
 * @verdion
 * @date 2021/11/199:07
 */
@Slf4j
@Configuration
@EnableScheduling
public abstract class ScheduledConfig implements SchedulingConfigurer {



    //定时任务周期表达式
    private String cron;

    /**
     * @Description:  重写配置定时任务的方法
     * @param: scheduledTaskRegistrar
     * @return: void
     * @Author:
     * @Date: 2020/8/28
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                //执行定时任务
                () ->
                {
                    taskService();
                },
                //设置触发器
                triggerContext -> {

                    cron = getCron();//获取定时任务周期表达式
                    if(cron == null){
                        log.error("定时任务执行出现异常, 请检查数据库scheduled表");
                    }
                    CronTrigger trigger = new CronTrigger(cron);

                    return trigger.nextExecutionTime(triggerContext);
                }
        );
    }

    private Executor taskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = BeanUtils.getBean("taskScheduled");
        return threadPoolTaskScheduler;
    }


    /**
     * @Description: 执行定时任务
     * @param:
     * @return: void
     * @Author:
     * @Date: 2020/8/28
     */
    public abstract void taskService();

    /**
     * @Description: 获取定时任务周期表达式
     * @param:
     * @return: java.lang.String
     * @Author:
     * @Date: 2020/8/28
     */
    public abstract String getCron();

    public abstract int getOpen();

}
