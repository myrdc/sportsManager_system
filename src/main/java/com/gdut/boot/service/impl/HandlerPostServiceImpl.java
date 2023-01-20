package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.Img;
import com.gdut.boot.annotation.file.ImgList;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.permission.Permission;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.handler.reflect.invoker.MySetFieldInvoker;
import com.gdut.boot.service.ApproveService;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.HandlerPostService;
import com.gdut.boot.util.FileUtils;
import com.gdut.boot.util.JwtUtil;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.util.ReflectUtil;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.xmlbeans.SchemaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.callback.Callback;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.gdut.boot.constance.common.Common.ID;
import static com.gdut.boot.constance.common.Common.SAVE;
import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.constance.common.RedisConstance.redis_login;
import static com.gdut.boot.util.BeanUtils.*;
import static com.gdut.boot.util.CommonUtil.*;
import static com.gdut.boot.util.CommonUtil.isJSON2;
import static com.gdut.boot.util.FileUtils.isImg;
import static com.gdut.boot.util.FileUtils.upload;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2719:42
 */

@Service
public class HandlerPostServiceImpl implements HandlerPostService {

    @Autowired
    private ApproveService approveService;

    @Autowired
    private CompetitionManageService competitionManageService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg save(RequestMessage requestMessage) {
        try {
            //首先获取反射器
            MyReflector reflector = reflectors.get(requestMessage.getType());
            //检查类是不是为空
            if (checkIfNULL(requestMessage)) {
                return Msg.fail("传入的类为空, 出大问题");
            }
            //对类中的参数进行处理
            requestMessage = dealWithImgAndFile(requestMessage, reflector);
            //插入数据库
            boolean result = insertIntoDB(requestMessage);
            if (result == true) {
                return Msg.success("插入成功",  requestMessage.getEntity() instanceof FileVo ?
                        ((FileVo) requestMessage.getEntity()).getBean() : requestMessage.getEntity());
            }
            return Msg.fail("插入失败");
        } catch (Exception e) {
            if (requestMessage.getEntity() instanceof FileVo) {
                //如果是文件类型的，此时这个bean里面有文件，需要删除文件
                FileUtils.delete(((FileVo) requestMessage.getEntity()).getPath());
            }
            throw new RuntimeException(e);
        }
    }

    private boolean insertIntoDB(RequestMessage message) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //判断此时这个bean里面是不是有文件
        Object entity = message.getEntity() instanceof FileVo ?
                ((FileVo) message.getEntity()).getBean() : message.getEntity();

        int type = message.getType();
        //获取反射器
        final MyReflector myReflector = reflectors.get(type);
        //获取对应的service 和 bean
        Class serverClass = myReflector.getServiceInvoker().getType();
        Object serviceEn = myReflector.getServiceInvoker().getServiceObject();

        //插入数据库
        Object invoke = serverClass.getMethod(SAVE, Object.class)
                .invoke(serviceEn, entity);

        //判断是不是审核类型，如果是需要进行审核,现在不需要审核了,判断如果是教练或者运动员提交的还是需要申请
        if(isApprove(type)){
            //判断是管理员还是运动员, 首先获取权限对象
            String token = message.getRequest().getHeader("token");
            String uPv = (String)redisUtils.get(redis_login + token);
            if(uPv == null){
                throw new BusinessException(Msg.fail("登陆信息丢失"));
            }
            UserPermissionVo loginUser = JSONObject.parseObject(uPv, UserPermissionVo.class);
            //获取id
            List<String> permission = loginUser.getPermission();
            if(permission.contains(Permission.manager.name()) || permission.contains(Permission.superManager.name())){
                //如果是管理员，那么不用审核了，直接通过
                competitionManageService.dealWithExcelBean(Arrays.asList(entity));
            }else{
                //如果是教练或者学生，就需要发送审核请求
                int id = (int)entity.getClass().getMethod(getGet(ID))
                        .invoke(entity);
                approveService.postApproves(type, id, message);
            }
        }
        return (Boolean) invoke;
    }

    /**
     * 对图片和文件类进行处理 从 Vo 转化成 bean
     *
     * @param requestMessage 参数类型
     * @return
     */
    @Override
    public RequestMessage dealWithImgAndFile(RequestMessage requestMessage, MyReflector reflector) throws Exception {
        //实体类
        Class entity = reflector.getType();
        //生成bean对象
        Object bean = classForBean(entity.getSimpleName());
        //转化成Vo类型
        for (Field field : entity.getDeclaredFields()) {
            //获取属性的名称
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID")) {
                continue;
            }
            //获取get方法中的结果
            Object result = ReflectUtil.getMethod(classForVo(entity.getSimpleName()), getGet(fieldName), null)
                    .invoke(requestMessage.getVo());
            if(result == null){
                continue;
            }
            //注解
            if (field.getAnnotation(Img.class) != null || field.getAnnotation(File.class) != null) {
                //参数是单个文件, 处理单个图片
                bean = dealWithSingleFile((MultipartFile) result, bean, field);
            } else if (field.getAnnotation(ImgList.class) != null || field.getAnnotation(FileList.class) != null) {
                //参数是多个图片或者多个文件
                bean = dealWithMuxFile((List<MultipartFile>) result, bean, field);
            } else {
                /**
                 设置正常属性
                 处理Vo
                 */
                if (isFileVo(bean)) {
                    /**
                     判断是不是list类型的，list需要转json
                     */
                    if(result instanceof List){
                        ((MySetFieldInvoker)reflector.getSetInvoker(fieldName))
                                .invoke(((FileVo) bean).getBean(), new Object[]{JSON.toJSONString(result)});
                        continue;
                    }
                        /*
                            带文件的
                        */
                    ((MySetFieldInvoker)reflector.getSetInvoker(fieldName))
                            .invoke(((FileVo) bean).getBean(), new Object[]{result});
                } else {
                         /*
                            不带文件的
                        */
                    /**
                      判断是不是list类型的，list需要转json
                     */
                    if(result instanceof List){
                        ((MySetFieldInvoker)reflector.getSetInvoker(fieldName)).invoke(bean, new Object[]{JSON.toJSONString(result)});
                        continue;
                    }
                    ((MySetFieldInvoker)reflector.getSetInvoker(fieldName)).invoke(bean, new Object[]{result});;
                }

            }
        }
        requestMessage.setEntity(bean);
        return requestMessage;
    }

    /**
     * 处理多文件
     * @param result
     * @param bean
     * @param field
     */
    private Object dealWithMuxFile(List<MultipartFile> result, Object bean, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Queue<String> srcs = new LinkedList<>();
        for (MultipartFile var : result) {
            //存入linux
            FileVo upload = upload(var);
            String src = null;
            //生成图片请求路径
            if (isImg(var.getResource().getFilename())) {
                src = toImgSrc(upload.getPath());
            } else {
                src = toDocSrc(upload.getPath());
            }
            //设置属性
            srcs.add(src);
        }

        //是不是FileVo类型的
        if(isFileVo(bean)){
            //前面已经处理过一次文件了,这次进来把路径添加到上一次的文件路径末尾处
            ((FileVo) bean).getBean().getClass().getMethod(getSet(field.getName()), field.getType())
                    .invoke(((FileVo) bean).getBean(), JSON.toJSONString(srcs));
            Queue queue = JSON.parseObject(((FileVo) bean).getPath(), Queue.class);
            for (String src : srcs) {
                queue.add(src);
            }
            ((FileVo) bean).setPath(JSON.toJSONString(queue));
        }else{
            //第一次进来，bean还不是fileVo类型
            //设置属性
            bean.getClass().getMethod(getSet(field.getName()), field.getType())
                    .invoke(bean, JSON.toJSONString(srcs));
            FileVo fileVo = new FileVo();
            fileVo.setBean(bean);
            fileVo.setPath(JSON.toJSONString(srcs));
            return fileVo;
        }
        return bean;
    }

    /**
     * 处理单个文件src，把文件名字转化为文件路径存储 <img src=''></img>
     *
     * @param result
     * @param bean   实体类
     * @return
     */
    private Object dealWithSingleFile(MultipartFile result, Object bean, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String filename = result.getResource().getFilename();
        //存入linux
        FileVo upload = upload(result);
        String src = null;
        //生成图片或文件请求路径
        if (isImg(result.getResource().getFilename())) {
            src = toImgSrc(upload.getPath());
        } else {
            src = toDocSrc(upload.getPath());
        }
        //设置属性
        if(isFileVo(bean)){
            //证明前面已经存过一次文件了
            ((FileVo)bean).getBean().getClass().getMethod(getSet(field.getName()), field.getType())
                    .invoke(((FileVo)bean).getBean(), src);
            String path = ((FileVo) bean).getPath();
            Queue queue = JSON.parseObject(path, Queue.class);
            queue.add(src);
            ((FileVo)bean).setPath(JSON.toJSONString(queue));
            return bean;
        }else{
            //第一次进来，没有fileVo
            //设置文件请求路径
            Queue queue = new LinkedList();
            queue.add(src);
            bean.getClass().getMethod(getSet(field.getName()), field.getType())
                    .invoke(bean, src);
            //设置文件路径
            upload.setBean(bean);
            //返回
            upload.setPath(JSON.toJSONString(queue));
            return upload;
        }
    }
}
