package com.gdut.boot.util;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/2/190:27
 */

public class TimeUtil {

    public static String getTimeNow(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

}
