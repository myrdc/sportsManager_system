package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.Img;
import com.gdut.boot.annotation.file.ImgList;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.service.HandlerPutService;
import com.gdut.boot.util.CommonUtil;
import com.gdut.boot.util.FileUtils;
import com.gdut.boot.util.ReflectUtil;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;

import static com.gdut.boot.constance.common.Common.*;
import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.util.BeanUtils.toDocSrc;
import static com.gdut.boot.util.BeanUtils.toImgSrc;
import static com.gdut.boot.util.CommonUtil.*;
import static com.gdut.boot.util.FileUtils.*;
import static com.gdut.boot.util.FileUtils.deleteFile;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2022/1/27 22:10
 */

@Service
public class HandlerPutServiceImpl implements HandlerPutService {

    /**
     * TODO:有时间可以使用回滚优化！！！！！这样exception就不用自己处理了 现在put只做了文件异常删除处理
     * 修改实体类，通过id
     *
     * @param requestMessage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg put(RequestMessage requestMessage) {
        //实体类类型
        int type = requestMessage.getType();
        //实体类，里面的id一定不为空
        Object vo = requestMessage.getVo();
        //修改数据库
        change(type, vo);
        return Msg.success("修改成功");
    }

    private void change(int type, Object entity) {
        //修改数据库
        Object source = changeInDB(type, entity);

        //修改文件：删除原来的文件
        try {
            changeInFile(source, entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除原来的文件
     *
     * @param source 数据库中原来的数据
     */
    private void changeInFile(Object source, Object entity) throws Exception {
        //根据传过来的entity找到有哪些文件被修改了，把原来的返回
        List<java.io.File> files = getFilesByProcess(source, entity);
        //统一删除
        if(files.isEmpty()){
            return;
        }
        FileUtils.deleteMulFile(files);
    }

    private List<java.io.File> getFilesByProcess(Object source, Object entity) throws Exception {
        List<java.io.File> files = new ArrayList<>();
        for (Field declaredField : source.getClass().getDeclaredFields()) {
            //不是file跳过
            if (!isFile(declaredField)) {
                continue;
            }
            //只需要file类型的
            //entity是vo, value是MultipartFile
            Object value = entity.getClass().getMethod(getGet(declaredField.getName())).invoke(entity);
            if (value != null) {
                //证明这个文件要被删除
                //获取原来的文件
                Object delete = source.getClass().getMethod(getGet(declaredField.getName())).invoke(source);
                if (isEmpty((String) delete)){
                    continue;
                }
                //判断是不是单文件
                if (declaredField.getAnnotation(Img.class) != null || declaredField.getAnnotation(File.class) != null) {
                    //单文件的
                    if (((MultipartFile)value).getOriginalFilename().equals((String)delete)) {
                        //判断如果原来是null或者原来的名字和后来的名字一样就不用删除，否则就需要删除
                        continue;
                    }
                }
                if (declaredField.getAnnotation(ImgList.class) != null || declaredField.getAnnotation(FileList.class) != null) {
                    //多文件的

                }

                String path = split(((String) delete)).getPath();
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    files.add(file);
                }
            }
        }
        return files;
    }


    /**
     * 从数据库中更新
     *
     * @param type
     * @param entity
     * @return
     */
    private Object changeInDB(int type, Object entity) {
        //修改数据库的内容，通过 id 删除
        try {
            //获取vo中的id
            int id = (int) entity.getClass().getMethod(getGet(ID))
                    .invoke(entity);
            //反射器
            MyReflector myReflector = reflectors.get(type);
            //service的类型
            Class service = myReflector.getServiceInvoker().getType();
            //service的对象
            Object serviceObject = myReflector.getServiceInvoker().getServiceObject();

            //通过数据库查询出原来的文件位置
            //创建wrapper
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            List result = (List) service.getMethod(LIST, WRAPPER)
                    .invoke(serviceObject, queryWrapper);
            if (result.size() == 0) {
                throw new BusinessException(Msg.fail("数据库没有找到对应的数据"), "changeInDB", null,
                        Arrays.asList(entity, type),
                        entity.getClass());
            }
            //创建wrapper, 使用wrapper更新
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper = fixWrapper(entity, updateWrapper, result.get(0));
            //重新获取
            result = (List) service.getMethod(LIST, WRAPPER)
                    .invoke(serviceObject, queryWrapper);
            //修改数据库
            service.getMethod(UPDATE, WRAPPER).invoke(serviceObject, updateWrapper);
            //返回更新后的结果
            return result.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 填充wrapper
     *
     * @param entity        实体类
     * @param updateWrapper 更新的wrapper
     * @return
     */
    private UpdateWrapper fixWrapper(Object entity, UpdateWrapper updateWrapper, Object resource) throws Exception {
        try {
            //往wrapper中填充要插入的数据
            for (Field field : entity.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                if (isSerialVersionUID(fieldName)) {
                    continue;
                }
                //通过get方法获取值
                Object value = entity.getClass().getMethod(getGet(field.getName())).invoke(entity);
                if (judgeField(field, value)) {
                    continue;
                }
                //填充
                //单个文件
                String after = null;
                if (field.getAnnotation(Img.class) != null || field.getAnnotation(File.class) != null) {
                    after = updateWithSingleFile((MultipartFile) value, updateWrapper, fieldName);
                    updateWrapper.set(fieldNameReverseToDBName(fieldName), after);
                }
                //多个文件
                else if (field.getAnnotation(ImgList.class) != null || field.getAnnotation(FileList.class) != null) {
                    //获取原来的文件
                    String resourceFile = (String) resource.getClass().getMethod(getGet(field.getName())).invoke(resource);
                    after = updateWithMuxFile((List<MultipartFile>) value, resourceFile);
                    updateWrapper.set(fieldNameReverseToDBName(fieldName), after);
                }
                //普通类型
                else {
                    //队列类型
                    if (value instanceof List) {
                        updateWrapper.set(fieldNameReverseToDBName(fieldName), JSON.toJSONString(value));
                        continue;
                    }
                    //其余类型
                    updateWrapper.set(fieldNameReverseToDBName(fieldName), value);
                }
            }
            return updateWrapper;
        } catch (Exception e) {
            List<java.io.File> file = getMuxFile(entity);
            FileUtils.deleteMulFile(file);
            throw new RuntimeException(e);
        }
    }

    private List<java.io.File> getMuxFile(Object entity) throws Exception {
        List<java.io.File> files = new ArrayList<>();
        for (Field declaredField : entity.getClass().getDeclaredFields()) {
            if (!isFile(declaredField)) {
                continue;
            }
            List<MultipartFile> value = (List<MultipartFile>) (entity.getClass().getMethod(getGet(declaredField.getName())).invoke(entity));
            if (value != null) {
                value.forEach(v -> {
                    try {
                        files.add(v.getResource().getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        return files;
    }

    private String updateWithMuxFile(List<MultipartFile> value, String resourceFile) {
        Queue<String> queue = null;
        if (resourceFile == null) {
            queue = new LinkedList<>();
        } else {
            //如果不是就用原来的
            queue = JSON.parseObject(resourceFile, Queue.class);
        }
        if(queue == null){
            queue = new LinkedList<>();
        }
        for (MultipartFile file : value) {
            //获取返回的文件路径和名字
            FileVo upload = upload(file);
            String url = null;
            if (isImg(file.getResource().getFilename())) {
                url = toImgSrc(upload.getPath());
            } else {
                url = toDocSrc(upload.getPath());
            }
            queue.add(url);
        }
        return JSON.toJSONString(queue);
    }

    /**
     * 保存图片或文件到数据库，返回wrapper
     *
     * @param value
     * @return
     */
    private String updateWithSingleFile(MultipartFile value, UpdateWrapper wrapper, String fieldName) {
        //获取返回的文件路径和名字
        FileVo upload = upload(value);
        String url = null;
        if (isImg(value.getResource().getFilename())) {
            url = toImgSrc(upload.getPath());
        } else {
            url = toDocSrc(upload.getPath());
        }
        return url;
    }
}
