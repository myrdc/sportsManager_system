package com.gdut.boot.util;

import com.alibaba.fastjson.JSON;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.AllClass;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gdut.boot.constance.common.Common.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2623:48
 */

public class CommonUtil {

    public static boolean isEmpty(String var){
        if(var == null || var.trim().equals("")){
            return true;
        }
        return false;
    }

    public static String getSet(String name){
        return SET + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String getGet(String name){
        return Get + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    //检测一个类是不是空的 TODO:可能有其他的类要扩展的
    /**
     * @param db 实体类对应的值
     * @return 返回转化后的数据库名称
     */
    public static String fieldNameReverseToDBName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int dbLength = db.length();
        for (int i = 0; i < dbLength; i++) {
            //获取字符
            char c = db.charAt(i);
            //判断
            if (c >= 'A' && c <= 'Z') {
                //遇到大写,就把全部的变小写
                sb.append("_").append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean checkIfNULL(Object entity){
        if(entity.getClass().getSimpleName().equals(REQUEST_MESSAGE)){
            //特殊类
            return entity == null || ((RequestMessage) entity).getVo() == null;
        }

        return entity == null;
    }

    public static boolean checkHasFile(int type){
        if(type == 3 || type == 4 || type == 5 || type == 10 || type == 12){
            return true;
        }
        return false;
    }

    /**
     * 获取指定位数的uuid
     */
    public static String getUUIDBits(int bits) {
        UUID uuid = UUID.randomUUID();
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        char[] out = new char[24];
        int tmp = 0, idx = 0;
        // 循环写法
        int bit = 0, bt1 = 8, bt2 = 8;
        int mask = 0x00, offsetm = 0, offsetl = 0;

        for(; bit < 16; bit += 3, idx += 4) {
            offsetm = 64 - (bit + 3) * 8;
            offsetl = 0;
            tmp = 0;

            if(bt1 > 3) {
                mask = (1 << 8 * 3) - 1;
            } else if(bt1 >= 0) {
                mask = (1 << 8 * bt1) - 1;
                bt2 -= 3 - bt1;
            } else {
                mask = (1 << 8 * ((bt2 > 3) ? 3 : bt2)) - 1;
                bt2 -= 3;
            }
            if(bt1 > 0) {
                bt1 -= 3;
                tmp = (int) ((offsetm < 0) ? msb : (msb >>> offsetm) & mask);
                if(bt1 < 0) {
                    tmp <<= Math.abs(offsetm);
                    mask = (1 << 8 * Math.abs(bt1)) - 1;
                }
            }
            if(offsetm < 0) {
                offsetl = 64 + offsetm;
                tmp |= ((offsetl < 0) ? lsb : (lsb >>> offsetl)) & mask;
            }

            if(bit == 15) {
                out[idx + 3] = alphabet[64];
                out[idx + 2] = alphabet[64];
                tmp <<= 4;
            } else {
                out[idx + 3] = alphabet[tmp & 0x3f];
                tmp >>= 6;
                out[idx + 2] = alphabet[tmp & 0x3f];
                tmp >>= 6;
            }
            out[idx + 1] = alphabet[tmp & 0x3f];
            tmp >>= 6;
            out[idx] = alphabet[tmp & 0x3f];
        }

        return new String(out, 0, bits);
    }

    /**
     * 采用URL Base64字符，即把“+/”换成“-_”
     */
    static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_=".toCharArray();

    /**
     * 判断是不是id或者除了int的其他是不是空
     * @return
     */
    public static boolean judgeField(Field field, Object value){
        boolean anInt = field.getType().getSimpleName().equals("int");
        String type = field.getType().getSimpleName();
        if((!anInt && value == null)
            || (field.getName().equals("id"))
            || (anInt && (int)value == 0)
            || field.getName().equals("serialVersionUID")
            || (type.equals("String") && ((String)value).trim().equals(""))
            || (value instanceof List && ((List<?>) value).size() == 0)
        ){
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        String uuidBits = getUUIDBits(8);
        System.out.println(uuidBits);
    }

    public static boolean isSerialVersionUID(String fieldName) {
        return fieldName.equals("serialVersionUID");
    }

    //判断是不是json字符串
    public static boolean isJSON2(String str) {
        boolean result = false;
        try {
            Object obj= JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result =false;
        }
        return result;
    }

    //判断是不是fileVo类型的
    public static boolean isFileVo(Object obj){
        return obj instanceof FileVo;
    }

    //判断一个数组类型是不是包含某个值
    public static boolean isContains(String[] list, String name){
        for (String s : list) {
            if(name.equals(s)){
                return true;
            }
        }
        return false;
    }

    //判断一个类是不是需要审核的类型
    public static boolean isApprove(int type){
        return type == AllClass.CompetitionManage.getValue();
    }

    public static String get16UUID(){
        // 1.开头两位，标识业务代码或机器代码（可变参数）
        String machineId = "cw";
        // 2.中间四位整数，标识日期
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        String dayTime = sdf.format(new Date());
        // 3.生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        // 4.可能为负数
        if(hashCode < 0){
            hashCode = -hashCode;
        }
        // 5.算法处理: 0-代表前面补充0; 10-代表长度为10; d-代表参数为正数型
        String value = machineId + dayTime + String.format("%010d", hashCode);
        return value;
    }

    //转化为vo
    public static<T> Queue<T> strToQueue(T str){
        Queue queue = JSON.parseObject((String) str, Queue.class);
        return queue;
    }

    //判断是不是VOResType
    public static boolean isVoResType(int type){
        return type == AllClass.CompetitionManage.getValue() || type == AllClass.EntranceInfo.getValue() ||
                type == AllClass.OneOrganization.getValue() || type == AllClass.SportsBaseMsg.getValue() ||
                type == AllClass.CoachManage.getValue() || type == AllClass.SecondOrganization.getValue();
    }

    //数据校验
    /**
     * 数据校验
     */
    public static<T> boolean bindData(T entity){
        //自主检测
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> validate = validator.validate(entity);
        if(validate.size() > 0){
            return true;
        }
        return false;
    }
}
