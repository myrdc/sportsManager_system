package com.gdut.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/7/1016:07
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneSecondRes {
    //一级
    private String value;

    //组织名
    private List<String> label;
}
