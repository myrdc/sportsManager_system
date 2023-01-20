package com.gdut.boot.util;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.Img;
import com.gdut.boot.annotation.file.ImgList;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static com.gdut.boot.constance.common.RedisConstance.*;
import static com.gdut.boot.constance.common.FileConstance.*;
import static com.gdut.boot.util.CommonUtil.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 处理文件的类工具类
 * @verdion
 * @date 2022/1/2717:31
 */

public class FileUtils {

    public static final String IMGPATH = "/";


    //检测是不是图片
    public static boolean isImg(String fileName){
        for (String imgSuffix : IMG_SUFFIX) {
            if(fileName.endsWith(imgSuffix)){
                return true;
            }
        }
        return false;
    }

    //获取图片的类型
    public static String ImgType(String imgName){
        String[] split = imgName.split("\\.");
        return split[split.length - 1];
    }

    //删除file
    public static void deleteMulFile(List<File> files) {
        for (File file : files) {
            redisAddFile(file.getPath());
        }
    }

    //文件获取
    public File getFile(String name, String month){
        //上传文件的时候防止都放在一个文件夹中，使用一个月创建一个文件夹的形式
        String all = IMG_PATH + month + "/" + name;
        File file = new File(all);
        if(!file.exists()){
            throw new BusinessException(Msg.fail("图片没有了，可能是存储图片发生异常"));
        }
        return file;
    }

    /**
     * 文件删除
     * @param path 路径
     * @return
     */
    public static boolean delete(String path){
        //获取get方法中的结果
        if(isJSON2(path)){
            Queue<String> queue = JSON.parseObject(path, Queue.class);
            for (String var : queue) {
                //添加到redis里面
                redisAddFile(dealSymbol(var));
            }
            return true;
        }else{
            redisAddFile(path);
        }
        return true;
    }

    /**
     * @param path
     * @return
     */
    public static String dealSymbol(String path) {
        return split(path).getPath();
    }

    /*** 解决文件名乱码和分目录管理（按年月日管理）
     * @param file: 文件
     * @throws ServletException
     * @throws IOException
     */
    public static FileVo upload(MultipartFile file){
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            String fileName =  System.currentTimeMillis() + file.getResource().getFilename();

            //创建path,获取文件保存在服务器的路径
            String path = null;
            if(isImg(file.getResource().getFilename())){
                path = IMG_PATH;
            }else{
                path = DOC_PATH;
            }

            //获取当前系统事件
            Calendar now = Calendar.getInstance();
            //获取年月日
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            path = path  + year + "/" + month;

            File dirFile = new File(path);
            if(!dirFile.exists()){
                //如果不存在就创建
                dirFile.mkdirs();
            }

            //开始读取
            outputStream = new FileOutputStream(new File(path, fileName));
            inputStream = file.getInputStream();

            int count;
            byte[] buffer = new byte[128];
            while((count = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, count);
            }
            FileVo fileVo = new FileVo();
            //fileName没什么用
            fileVo.setName(fileName);
            fileVo.setPath(path + "/" + fileName);
            return fileVo;
        } catch (IOException e) {
            throw new BusinessException(Msg.fail("上传文件发生异常"));
        } finally {
            try {
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean singleDelete(String path){
        File file = new File(path);
        if(!file.exists()){
            return true;
        }
        return file.delete();
    }

    public static FileVo split(String url) {
//        String[] split = url.split("\\?");
//        //名字和路径
//        String path = split[1];
//        String realPath = path.split("=")[1];
//        String[] var = realPath.split("/");
//        String fileName = var[var.length-1];
//        return new FileVo(null, fileName, realPath);
        String[] var = url.split("/");
        return new FileVo(null, var[var.length-1], url);
    }

    /**
     * 删除文件
     * @param entity 数据库中查出原来的值
     * other: 前端传过来的值
     */
    public static void deleteFile(Object entity) {
        if(entity == null){
            return;
        }
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        //实体类
        try {
            for (Field field : declaredFields) {
                String fieldName = field.getName();
                if(isSerialVersionUID(fieldName)){
                    continue;
                }
                //获取get方法中的结果
                Object result = ReflectUtil.getMethod(entity.getClass(), getGet(fieldName), null)
                        .invoke(entity);
                if(result == null){
                    continue;
                }
                //判断是不是文件，此时的result是String类型的路径
                if(field.getAnnotation(Img.class) != null || field.getAnnotation(com.gdut.boot.annotation.file.File.class) != null){
                    //删除文件
                    deleteSingleFile((String)result);
                }else if(field.getAnnotation(ImgList.class) != null || field.getAnnotation(FileList.class) != null){
                    //删除多个文件
                    deleteMuxFile((String)result);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(Msg.fail("删除实体类异常"), e);
        }
    }

    /**
     * 删除多个文件
     * @param result json数据类型
     */
    private static void deleteMuxFile(String result) {
        //判断
        if(StrUtil.isBlank(result)){
            return;
        }
        Queue<String> queue = JSON.parseObject(result, Queue.class);
        for (String path : queue) {
            deleteSingleFile(path);
        }
    }

    /**
     * 删除单个文件
     * @param result 路径
     */
    private static void deleteSingleFile(String result) {
        if(StrUtil.isBlank(result)){
            return;
        }
        FileVo split = split(result);
        FileUtils.delete(split.getPath());
    }

    public static  boolean isFile(Field declaredField) {
        return (declaredField.getAnnotation(Img.class)) != null
                || (declaredField.getAnnotation(com.gdut.boot.annotation.file.File.class) != null)
                || (declaredField.getAnnotation(ImgList.class) != null)
                || (declaredField.getAnnotation(FileList.class) != null);

    }

    public static void redisAddFile(String filePath){
        final RedisUtils redis = BeanUtils.getBean(RedisUtils.class);
        final Object redisObj = redis.get(redis_prefix + redisFile + redis_file_suffix);
        List list = null;
        if(redisObj == null){
            list = new ArrayList<>();
        }else{
            list = (List) redisObj;
        }
        if(!list.contains(filePath)){
            list.add(filePath);
        }
        redis.set(redis_prefix + redisFile + redis_file_suffix, list);
    }

    public static void redisDeleteFile(){
        final RedisUtils redis = BeanUtils.getBean(RedisUtils.class);
        final Object redisObj = redis.get(redis_prefix + redisFile + redis_file_suffix);
        if(redisObj != null){
            List files = (List) redisObj;
            for (Object file : files) {
                singleDelete((String)file);
            }
        }
        redis.delKey(redis_prefix + redisFile + redis_file_suffix);
    }
}
