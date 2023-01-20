package com.gdut.boot.handler.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static com.gdut.boot.constance.cache.Cache.LoadMap;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2923:44
 */

@Component
public class StartListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        try {
            LoadMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
