package com.gdut.boot;

import cn.hutool.core.lang.hash.Hash;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gdut.boot.controller.CompetitionManageController;
import com.gdut.boot.entity.ClassInterfaces;
import com.gdut.boot.entity.CompetitionManage;
import com.gdut.boot.service.ClassInterfacesService;
import com.gdut.boot.service.ResourcesMiddleService;
import com.gdut.boot.vo.CompetitionManageVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

import static com.gdut.boot.util.JwtUtil.getToken;
import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;

@SpringBootTest
class SportsManagerSystemApplicationTests {

    @Resource
    private ClassInterfacesService classInterfacesService;
    private ResourcesMiddleService resourcesMiddleService;
    private CompetitionManageController competitionManageController;

    @Test
    //获取令牌
    void contextLoads() {

//        String name = "[\"/file/sports/img/2022/4/165046440438520220211-051925.jpg\",\"/file/sports/img/2022/4/165046440439520220211-052829.jpg\",\"/file/sports/img/2022/4/165046447552120220211-052829.jpg\"]";
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.SECOND, 40);  //20秒过期
//
//        HashMap<String, Object> map = new HashMap<>();
//        String token = JWT.create()
//                .withHeader(map)   //head
//                .withClaim("userId", 12)        //payload
//                .withClaim("username", "JLH")        //payload
//                .withExpiresAt(instance.getTime())          //过期时间
//                .sign(Algorithm.HMAC256("rdcyyds"));//设置签名
//
//        System.out.println(token);
    }

    //验证令牌
    @Test
    public void test(){
        //生成jwt验证对象
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("rdcyyds")).build();
//
//        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTgxMTQ0ODAsInVzZXJJZCI6MTIsInVzZXJuYW1lIjoiSkxIIn0.UxNdQd1ZjrjqcrpJl5VHmkP_Lcnr3XCeyR3040PsPKg");
//        Claim userId = verify.getClaim("userId");
//        Claim username = verify.getClaim("username");
//
//        int userIdS = userId.asInt();
//        String usernameS = username.asString();
//        System.out.println(userIdS + ", " + usernameS);
    }

    @Test
    public void test1(){
//        String[] strings = new String[2];
//        strings[0] = "篮球,篮球甲组";
//        strings[1] = "足球,足球甲组";
//        System.out.println(strings);
    }

    @Test
    public void RefreshDBAndMap(){
//        HashMap<String, String> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("number", "666666");
//        String token = getToken(objectObjectHashMap);
//        System.out.println("管理员: " + token);
//
//        objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("number", "888888");
//        token = getToken(objectObjectHashMap);
//        System.out.println("教练: " + token);
//
//        objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("number", "777777");
//        token = getToken(objectObjectHashMap);
//        System.out.println("在校运动员: " + token);
//
//        objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("number", "999999");
//        token = getToken(objectObjectHashMap);
//        System.out.println("离校运动员: " + token);
//
//        objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("number", "555555");
//        token = getToken(objectObjectHashMap);
//        System.out.println("主教练: " + token);
//
//        objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("number", "444444");
//        token = getToken(objectObjectHashMap);
//        System.out.println("超级管理员" + token);

    }


    @Test
    public void TestDBAndMap(){
        System.out.println(JSON.toJSONString(new String[]{"篮球:篮球甲组"}));
    }



    public boolean isListEqual(Queue l0, List l1){
        if (l0 == l1)
            return true;
        if (l0 == null && l1 == null)
            return true;
        if (l0 == null || l1 == null)
            return false;
        if (l0.size() != l1.size())
            return false;
        if (isEqualCollection(l0 , l1) && l0.containsAll(l1) && l1.containsAll(l0)){
            return true;
        }
        return false;
    }

}
