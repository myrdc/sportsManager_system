package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.ResourceName;
import com.gdut.boot.bean.ResultTwo;
import com.gdut.boot.entity.*;
import com.gdut.boot.service.*;
import com.gdut.boot.mapper.PermissionMapper;
import com.gdut.boot.util.JwtUtil;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.gdut.boot.constance.cache.Cache.className;
import static com.gdut.boot.constance.cache.Cache.interfaceName;
import static com.gdut.boot.constance.common.RedisConstance.*;
import static com.gdut.boot.constance.permission.Permission.isValid;

/**
 *
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private ResourcesMiddleService resourcesMiddleService;

    @Autowired
    private ClassInterfacesService classInterfacesService;

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private CoachManageService coachManageService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Msg getPermission(String number, HttpServletRequest request) {
        //首先判断redis里面有没有这个token，如果有，直接返回
        UserPermissionVo uPV = null;
        String token = null;
        try {
            //否则走下面的流程，第一次进入或者说token过期了进入这里面
            final List<UserPermission> userNumber = userPermissionService.list(new QueryWrapper<UserPermission>().eq("user_number", number));
            if(userNumber.size() == 0){
                return Msg.fail("用户还没有录入系统中");
            }
            //一个人可能有多个权限
            uPV = new UserPermissionVo();
            uPV.setUserNumber(number);
            HashMap<String, List<ResourceName>> classInfMap = new HashMap();
            //权限
            List<String> personPermissions = new ArrayList<>();
            for (UserPermission permission : userNumber) {
                //获取权限id
                Integer permissionId = permission.getPermissionId();
                personPermissions.add(com.gdut.boot.constance.permission.Permission.getValue(permissionId));
                List<ResourcesMiddle> resources = resourcesMiddleService.list(new QueryWrapper<ResourcesMiddle>().eq("permission_id", permissionId));
                //找到所有的资源id
                Set<Integer> ids = resources.stream().map(ResourcesMiddle::getClassIntId).collect(Collectors.toSet());
                //然后到数据库中找出所有的权限
                List<ClassInterfaces> allInterfaces = classInterfacesService.listByIds(ids);
                for (ClassInterfaces allInterface : allInterfaces) {
                    if(classInfMap.containsKey(className.get(allInterface.getClazz()))){
                        //包含了
                        List<ResourceName> resourceNames = classInfMap.get(className.get(allInterface.getClazz()));
                        resourceNames.add(new ResourceName(allInterface.getClazzInterface(), interfaceName.get(allInterface.getId())));
                        classInfMap.put(className.get(allInterface.getClazz()), resourceNames);
                    }else{
                        //没有包含
                        List<ResourceName> list = new ArrayList<>();
                        list.add(new ResourceName(allInterface.getClazzInterface(), interfaceName.get(allInterface.getId())));
                        classInfMap.put(className.get(allInterface.getClazz()), list);
                    }
                }
            }
            uPV.setResourceMiddles(classInfMap);
            uPV.setPermission(personPermissions);
            if(personPermissions.contains("在校运动员") || personPermissions.contains("离校运动员")){
                //找出运动员id
                List<SportsBaseMsg> nums = sportsBaseMsgService.list(new QueryWrapper<SportsBaseMsg>().eq("number", number));
                if(!nums.isEmpty()){
                    uPV.setId(nums.get(0).getId());
                }

            }else if(personPermissions.contains("教练") || personPermissions.contains("主教练")){
                List<CoachManage> coachManages = coachManageService.list(new QueryWrapper<CoachManage>().eq("number", number));
                if(!coachManages.isEmpty()){
                    uPV.setId(coachManages.get(0).getId());
                }
            }
            //设置token, token只是对number进行token
            token = JwtUtil.getToken(number);
            //存储到userHolder里面
            redisUtils.set(redis_login + token, JSONObject.toJSONString(uPV), 24 * 60 * 60);
            //下面找出所有资源
            return Msg.success("获取权限成功", uPV, token);
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail("获取权限失败");
        }
    }

    @Override
    public Msg setAuth(String number, String auName) {
        //1.判断number是否存在
        List<UserPermission> users = userPermissionService.query().eq("user_number", number).list();
        if(users.isEmpty()){
            return Msg.fail("没有找到对应的用户");
        }
        //2.修改权限
        //2.1 判断权限是不是存在
        if(!isValid(auName)){
            return Msg.fail("权限名字错误");
        }
        //2.2 更新
        boolean update = userPermissionService.update()
                .set("permission_id", com.gdut.boot.constance.permission.Permission.getType(auName))
                .eq("user_number", number).update();
        if(update == false){
            return Msg.success("更新失败");
        }
        //3.redis失效, 需要重新登陆
        //redisUtils.delKey(redis_login + number);
        return Msg.success("修改权限成功");
    }

    @Override
    public Msg getAllNumAuth(Long pn, Long size) {
        //1.找出所有的
        Page<UserPermission> page = new Page<>(pn, size);
        List<UserPermission> records = userPermissionService.page(page).getRecords();
        if(records.isEmpty()){
            return Msg.success("没有数据");
        }
        //2.然后转换成中文
        List<ResultTwo> res = new ArrayList<>();
        records.forEach(e->{
            res.add(new ResultTwo(e.getId(), e.getUserNumber(), com.gdut.boot.constance.permission.Permission.getValue(e.getPermissionId())));
        });
        page.setRecords(null);
        return Msg.success("获取成功", res, page);
    }

    @Override
    public Msg getByNumber(String number) {
        //1.找出所有的
        List<UserPermission> records = userPermissionService.query().eq("user_number", number).list();
        if(records.isEmpty()){
            return Msg.success("没有数据");
        }
        //2.然后转换成中文
        ResultTwo res = new ResultTwo(records.get(0).getId(), number, com.gdut.boot.constance.permission.Permission.getValue(records.get(0).getPermissionId()));
        return Msg.success("获取成功", res);
    }

    @Override
    public Msg postSingle(ResultTwo resultTwo) {
        if(resultTwo.getResName() == null || resultTwo.getNumber() == null){
            return Msg.fail("输入的信息不能为空");
        }
        int type = com.gdut.boot.constance.permission.Permission.getType(resultTwo.getResName());
        if(type == -1){
            return Msg.fail("权限名字错误");
        }
        String number = resultTwo.getNumber();
        UserPermission userPermission = new UserPermission();
        userPermission.setPermissionId(type);
        userPermission.setUserNumber(number);
        List<UserPermission> list = userPermissionService.query().eq("user_number", number).eq("permission_id", type).list();
        if(!list.isEmpty()){
            return Msg.fail("已经有相同的权限存在了");
        }
        //查询出这个用户的所有权限，如果是
        boolean save = userPermissionService.save(userPermission);
        if(!save){
            return Msg.fail("添加失败，服务器异常");
        }
        //新添加的不需要redis失效
        return Msg.success("添加成功");
    }

    @Override
    public Msg getByToken(String token, HttpServletRequest request) {
        Object res = redisUtils.get(redis_login + token);
        if(res == null){
            return Msg.fail("token已经过期了");
        }
        if(redisUtils.get(redis_black + token) != null){
            return Msg.fail("请重新获取token");
        }
        UserPermissionVo uPV = JSONObject.parseObject((String) res, UserPermissionVo.class);
        List<String> personPermissions = uPV.getPermission();
        if(personPermissions.contains("在校运动员") || personPermissions.contains("离校运动员")){
            //找出运动员id
            List<SportsBaseMsg> nums = sportsBaseMsgService.list(new QueryWrapper<SportsBaseMsg>().eq("number", uPV.getUserNumber()));
            if(!nums.isEmpty()){
                uPV.setId(nums.get(0).getId());
            }

        }else if(personPermissions.contains("教练") || personPermissions.contains("主教练")){
            List<CoachManage> coachManages = coachManageService.list(new QueryWrapper<CoachManage>().eq("number", uPV.getUserNumber()));
            if(!coachManages.isEmpty()){
                uPV.setId(coachManages.get(0).getId());
            }
        }
        return Msg.success("获取成功", uPV);
    }
}




