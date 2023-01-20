package com.gdut.boot.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.annotation.group.NotNeedAuth;
import com.gdut.boot.bean.DeleteFile;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.CoachManage;
import com.gdut.boot.entity.EntranceExamMsg;
import com.gdut.boot.entity.EntranceInfo;
import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.reflect.invoker.ServiceInvoker;
import com.gdut.boot.service.*;
import com.gdut.boot.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static com.gdut.boot.constance.cache.Cache.SERVICE_CLASS;
import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.constance.common.Common.UPDATE;
import static com.gdut.boot.constance.common.Common.WRAPPER;
import static com.gdut.boot.util.BeanUtils.toDocSrc;
import static com.gdut.boot.util.BeanUtils.toImgSrc;
import static com.gdut.boot.util.CommonUtil.fieldNameReverseToDBName;
import static com.gdut.boot.util.FileUtils.*;
import static org.springframework.util.ResourceUtils.getFile;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 处理图片的类
 * @verdion
 * @date 2022/1/2717:29
 */

@RestController
@RequestMapping("/api/img")
public class ImgController {

    @Autowired
    private ImgService imgService;

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private EntranceInfoService entranceInfoService;

    @Autowired
    private EntranceExamMsgService entranceExamMsgService;

    @Autowired
    private CoachManageService coachManageService ;


    /**
     * 获取图片的方法
     *
     * @param name     前端传过来一个url，用name接收就可以了
     * @param response
     */
    @RequestMapping("get")
    @NotNeedAuth
    public void getImg(@RequestParam("path") String name, HttpServletResponse response) {
        try {
            if (!isImg(name)) {
                throw new BusinessException(Msg.fail("文件类型错误"));
            }
            String format = ImgType(name);
            ImageIO.write(ImageIO.read(getFile(name)), format, response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException(Msg.fail("读取文件发生异常"));
        }

    }

    /**
     * 上传图片的方法
     */
    /**
     * @param id   主键id
     * @param type 类型
     * @param img  文件，有可能是多个
     * @return
     */
    @PutMapping()
    @NotNeedAuth
    public Msg postImg(int id, int type, MultipartFile img) {
        //存入linux
        if(img == null || img.getSize() > 1024*1024){
            return Msg.fail("文件不能为空");
        }
        FileVo upload = upload(img);
        String src = null;
        Object resource = null;
        //生成图片或文件请求路径
        if (isImg(img.getResource().getFilename())) {
            src = toImgSrc(upload.getPath());
        } else {
            src = toDocSrc(upload.getPath());
        }
        if (type == AllClass.SportsBaseMsg.getValue()) {
            QueryWrapper<SportsBaseMsg> var1 = new QueryWrapper<>();
            var1.eq("id", id);
            List<SportsBaseMsg> list = sportsBaseMsgService.list(var1);
            if(list.size() == 0){
                var1 = null;
                return Msg.fail("还没有插入基本数据，不能上传图片或文件");
            }
            resource = list.get(0);
            UpdateWrapper<SportsBaseMsg> eq = new UpdateWrapper<SportsBaseMsg>().eq("id", id).set("picture", src);
            sportsBaseMsgService.update(eq);
        }
        if (type == AllClass.EntranceInfo.getValue()) {
            QueryWrapper<EntranceInfo> var2 = new QueryWrapper<>();
            var2.eq("id", id);
            List<EntranceInfo> list = entranceInfoService.list(var2);
            if(list.size() == 0){
                var2 = null;
                return Msg.fail("还没有插入基本数据，不能上传图片或文件");
            }
            resource = list.get(0);
            UpdateWrapper<EntranceInfo> eq = new UpdateWrapper<EntranceInfo>().eq("id", id).set("student_card", src);
            entranceInfoService.update(eq);
        }
        if(type == AllClass.EntranceExamMsg.getValue()){
            QueryWrapper<EntranceExamMsg> var3 = new QueryWrapper<>();
            var3.eq("id", id);
            List<EntranceExamMsg> list = entranceExamMsgService.list(var3);
            if(list.size() == 0){
                var3 = null;
                return Msg.fail("还没有插入基本数据，不能上传图片或文件");
            }
            resource = list.get(0);
            UpdateWrapper<EntranceExamMsg> eq = new UpdateWrapper<EntranceExamMsg>().eq("id", id).set("id_card", src);
            entranceExamMsgService.update(eq);
        }
        if(type == AllClass.CoachManage.getValue()){
            QueryWrapper<CoachManage> var4 = new QueryWrapper<>();
            var4.eq("id", id);
            List<CoachManage> list = coachManageService.list(var4);
            if(list.size() == 0){
                var4 = null;
                return Msg.fail("还没有插入基本数据，不能上传图片或文件");
            }
            resource = list.get(0);
            UpdateWrapper<CoachManage> eq = new UpdateWrapper<CoachManage>().eq("id", id).set("card_picture", src);
            coachManageService.update(eq);
        }
        FileUtils.deleteFile(resource);
        return Msg.success("插入成功", src);
    }

    /**
     * 批量上传图片的方法
     */
    /**
     * @param id   主键id
     * @param type 类型
     * @param imgs  文件，多个
     * @return
     */
    @PutMapping("list")
    @NotNeedAuth
    public Msg postListImg(int id, int type, List<MultipartFile> imgs) {
        //存入linux
        ArrayList<String> srcs = new ArrayList();
        for (MultipartFile img : imgs) {

            if(img == null || img.getSize() > 1024*1024){
                return Msg.fail("文件不能为空");
            }
            FileVo upload = upload(img);
            String src = null;
            Object resource = null;
            //生成图片或文件请求路径
            if (isImg(img.getResource().getFilename())) {
                src = toImgSrc(upload.getPath());
            } else {
                src = toDocSrc(upload.getPath());
            }
            if (type == AllClass.SportsBaseMsg.getValue()) {
                QueryWrapper<SportsBaseMsg> var1 = new QueryWrapper<>();
                var1.eq("id", id);
                List<SportsBaseMsg> list = sportsBaseMsgService.list(var1);
                if(list.size() == 0){
                    var1 = null;
                    return Msg.fail("还没有插入基本数据，不能上传图片或文件");
                }
                resource = list.get(0);
                UpdateWrapper<SportsBaseMsg> eq = new UpdateWrapper<SportsBaseMsg>().eq("id", id).set("picture", src);
                sportsBaseMsgService.update(eq);
            }
            if (type == AllClass.EntranceInfo.getValue()) {
                QueryWrapper<EntranceInfo> var2 = new QueryWrapper<>();
                var2.eq("id", id);
                List<EntranceInfo> list = entranceInfoService.list(var2);
                if(list.size() == 0){
                    var2 = null;
                    return Msg.fail("还没有插入基本数据，不能上传图片或文件");
                }
                resource = list.get(0);
                UpdateWrapper<EntranceInfo> eq = new UpdateWrapper<EntranceInfo>().eq("id", id).set("student_card", src);
                entranceInfoService.update(eq);
            }
            if(type == AllClass.EntranceExamMsg.getValue()){
                QueryWrapper<EntranceExamMsg> var3 = new QueryWrapper<>();
                var3.eq("id", id);
                List<EntranceExamMsg> list = entranceExamMsgService.list(var3);
                if(list.size() == 0){
                    var3 = null;
                    return Msg.fail("还没有插入基本数据，不能上传图片或文件");
                }
                resource = list.get(0);
                UpdateWrapper<EntranceExamMsg> eq = new UpdateWrapper<EntranceExamMsg>().eq("id", id).set("id_card", src);
                entranceExamMsgService.update(eq);
            }
            if(type == AllClass.CoachManage.getValue()){
                QueryWrapper<CoachManage> var4 = new QueryWrapper<>();
                var4.eq("id", id);
                List<CoachManage> list = coachManageService.list(var4);
                if(list.size() == 0){
                    var4 = null;
                    return Msg.fail("还没有插入基本数据，不能上传图片或文件");
                }
                resource = list.get(0);
                UpdateWrapper<CoachManage> eq = new UpdateWrapper<CoachManage>().eq("id", id).set("card_picture", src);
                coachManageService.update(eq);
            }
            srcs.add(src);
            FileUtils.deleteFile(resource);
        }
        return Msg.success("插入成功").setData(srcs);
    }

    /**
     *
     * @param deleteFile 删除的内容
     * @return
     */
    @DeleteMapping("delete")
    @NotNeedAuth
    public Msg delete(@RequestBody DeleteFile deleteFile) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取原来的
        boolean delete = FileUtils.delete(deleteFile.getPath());
        Queue<String> queue = JSON.parseObject(deleteFile.getResource(), Queue.class);
        if(queue.size() == 0){
            //删除数据库的数据
            return Msg.success("删除成功");
        }
        queue.remove(deleteFile.getPath());
        UpdateWrapper updateWrapper = new UpdateWrapper();
        if(queue.size() == 0){
            updateWrapper.set(fieldNameReverseToDBName(deleteFile.getName()), null);
        }else{
            updateWrapper.set(fieldNameReverseToDBName(deleteFile.getName()), JSON.toJSONString(queue));
        }
        updateWrapper.eq("id", deleteFile.getId());
        //更新数据库
        final ServiceInvoker serviceInvoker = reflectors.get(deleteFile.getType()).getServiceInvoker();
        serviceInvoker.getType().getMethod(UPDATE, WRAPPER).invoke(serviceInvoker.getServiceObject(), updateWrapper);
        if(delete == true){
            //删除文件
            return Msg.success("删除成功");
        }
        return Msg.fail("删除失败");
    }
}
