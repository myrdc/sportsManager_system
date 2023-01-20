package com.gdut.boot.bean;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/281:37
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVo {
    //bean
    private Object bean;
    //文件名字
    private String name;
    //文件路径
    private String path;
}
