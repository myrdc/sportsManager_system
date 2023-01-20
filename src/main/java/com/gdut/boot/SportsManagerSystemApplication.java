package com.gdut.boot;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//开启异步消息
@EnableAsync
//开启aop
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
@EntityScan(basePackages = {"com.gdut.boot.entity"})
public class SportsManagerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportsManagerSystemApplication.class, args);
    }


    @Bean
    public WebServerFactoryCustomizer webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {
            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {
                factory.setPort(8090);
                factory.setContextPath("/sports");
            }
        };
    }

}
