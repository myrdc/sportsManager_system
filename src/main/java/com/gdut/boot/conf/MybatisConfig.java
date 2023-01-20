package com.gdut.boot.conf;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.gdut.boot.bean.EasysqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author JLHWASX
 */

@Configuration
public class MybatisConfig {

    /**
     * 分页插件
     */
    @Bean(name="paginationInterceptor")
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        //paginationInnerInterceptor.setOverflow(true);     设置这个就会导致超出页数总是显示最后一页
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    /**
     *     注册乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    //需要注入的Bean
    @Bean
    public EasysqlInjector easysqlInjector() {
        return new EasysqlInjector();
    }
}
