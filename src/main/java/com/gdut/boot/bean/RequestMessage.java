package com.gdut.boot.bean;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.boot.constance.common.RequestStatus;
import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yihang
 */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RequestMessage{

    /**
     * 请求的类型，四种
     */
    private RequestStatus reqType;
    /**
     * 实体类或者条件类
     */
    private Object entity;
    /**
     * 请求
     */
    private HttpServletRequest request;
    /**
     * 实体类类型
     */
    private int type;
    /**
     * 真正的类Vo
     */
    private Object vo;
    /**
     * 对于get请求的封装
     */
    private GetCondition getCondition;

}
