package com.gdut.boot.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.boot.bean.GetCondition;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.RequestStatus;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2022/1/2623:48
 */

public class MessageUtil {

    /**
     * 把接口的数据转化为 Message
     * @param request 请求
     * @param type 类型：GET POST, PUT, DELETE
     * @param vo 参数对象
     * @return
     */
    public static RequestMessage toReqestMessage(HttpServletRequest request, RequestStatus type, Object vo, int number, GetCondition getCondition){
        RequestMessage message = new RequestMessage();
        message.setReqType(type);
        message.setEntity(null);
        message.setRequest(request);
        message.setType(number);
        message.setVo(vo);
        message.setGetCondition(getCondition);
        return message;
    }

    public static<T> GetCondition<T> toPageContent(Map<String, String> map, T entity, String[] listType){
        //pn 和 size 默认是 1 10
        if(map.get("pn") == null || Integer.valueOf(map.get("pn")) <= 0){
            map.put("pn", "1");
        }
        if(map.get("size") == null || Integer.valueOf(map.get("size")) <= 0){
            map.put("size", "10");
        }
        Page<T> page = new Page<T>(Long.parseLong(map.get("pn")),Long.parseLong(map.get("size")));
        GetCondition<T> condition = new GetCondition<>();
        condition.setCondition(map);
        condition.setPage(page);
        condition.setEndTime(null);
        condition.setStartTime(null);
        condition.setListType(listType);
        return condition;
    }

    public static Page getPage(Integer pn, Integer size){
        Page page = new Page(pn, size);
        return page;
    }
}
