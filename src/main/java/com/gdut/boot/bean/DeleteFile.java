package com.gdut.boot.bean;

import lombok.Data;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/4/2021:13
 */

@Data
public class DeleteFile {
    private Integer id;
    private Integer type;
    private String path;
    private String resource;
    private String name;
}
