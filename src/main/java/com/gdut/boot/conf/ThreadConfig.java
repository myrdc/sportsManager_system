package com.gdut.boot.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2021/9/120:06
 */

@Slf4j
@Configuration
public class ThreadConfig {

    /** 核心线程数 */
    private int corePoolSize = 5;
    /** 最大线程数 */
    private int maxPoolSize = 10;
    /** 队列数 */
    private int queueCapacity = 20;

    //返回线程
    @Bean
    public ExecutorService executorService(){
        log.info("开始执行线程");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程大小
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("chat_system_thread");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}
