package com.gdut.boot.util;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gdut.boot.vo.res.UserPermissionVo;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.gdut.boot.util.PasswordUtil.encryptAES;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/7/18 10:05
 */

public class JwtUtil {

    private static final Calendar instance = Calendar.getInstance();

    //签名
    private static final String SING = "token!#@123";

    /**
     * 生成token header.payload.sing
     */
    public static String getToken(String map){
        //7天过期
        instance.add(Calendar.DATE, 7); //默认7天过期
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("payload", map);    //payload
        //过期时间和签名
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SING));
        //token令牌
        return token;
    }

    /**
     * 验证token 合法
     */
    public static void verifyToken(String token){
        JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    public static String getToken(String number, String pre) throws Exception {
        String token = encryptAES(number);
        return token;
    }

    /**
     * 获取token信息方法,如果错误就直接抛异常
     */
    public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }

    /**
     * 获取payload信息
     * @param token 待拿取信息的token
     * @param key token中payload的key
     */
    public static String getPayloadByKey(String token,String key){
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return jwt.getClaim(key).asString();
    }

    //生成Token
    public static<T> String getToken(Map<String,T> map){
        //得到当前天的1天后
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v.toString());
        });
        //header默认设置了
        String token = builder
                //设置过期时间
                .withExpiresAt(instance.getTime())
                //设置签名(需要一个秘钥SIGN)
                .sign(Algorithm.HMAC256(SING));
        return token;
    }

}
