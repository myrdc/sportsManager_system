package com.gdut.boot.bean;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/302:05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCondition<T> {

    private Map<String, String> condition;
    private String endTime;
    private String startTime;
    /**
     * 分页插件
     */
    private Page<T> page;
    /**
     * list类型的值在数组中以json形式存放，得使用like匹配
     */
    private String[] listType;
}
