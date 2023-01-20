package com.gdut.boot.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.annotation.group.NotNeedAuth;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.CoachManage;
import com.gdut.boot.entity.EntranceExamMsg;
import com.gdut.boot.entity.EntranceInfo;
import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.service.ExcelService;
import com.gdut.boot.service.impl.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.util.BeanUtils.toDocSrc;
import static com.gdut.boot.util.BeanUtils.toImgSrc;
import static com.gdut.boot.util.FileUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/282:53
 */

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private ExcelService excelService;

    /**
     * @param response
     * @功能描述 下载文件:将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存
     */
    @RequestMapping("get")
    @NotNeedAuth
    public Msg downloadLocal(String url, HttpServletResponse response, HttpServletRequest request){
        InputStream inputStream = null;
        try {
            // 读到流中
            FileVo fileVo = split(url);
            String path = fileVo.getPath() + "/" + fileVo.getName();
            inputStream = new FileInputStream(path);// 文件的存放路径
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String filename = new File(path).getName();
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            return Msg.success();
        } catch (IOException e) {
            throw new BusinessException(Msg.fail("获取文件失败"));
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * excel上传接口
     * @param excelFile excel上传
     * @return
     */
    @ResponseBody
    @PostMapping("excelFileUpload")
    @AuthName(name = "excelFileUpload")
    public Msg uploadExcelFile(MultipartFile excelFile, int type, HttpServletRequest request) throws Exception {
        InputStream inputStream = excelFile.getInputStream();
        Msg msg = excelService.importOnInThree(inputStream, type);
        return msg;
    }


    /**
     * @param type 指想要下载的类名字
     * @param response
     * @功能描述 下载文件:将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存
     */
    @RequestMapping("download")
    @NotNeedAuth
    public Msg downLoadExcel(int type, HttpServletResponse response, HttpServletRequest request){
        InputStream inputStream = null;
        try {
            String path = excelService.getDownLoadPath(type);
            if(path == null){
                return Msg.fail("传入的类型不正确");
            }
            // 读到流中
            inputStream = new FileInputStream(path);// 文件的存放路径
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String filename = new File(path).getName();
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            return Msg.success("获取模板成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Msg.fail("下载模板失败");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * excel导出
     */
    @RequestMapping("exportByType")
    @AuthName(name = "exportByType")
    public void getExcelModule(int type, HttpServletResponse response, HttpServletRequest request){
        try {
            String fileName = excelService.getExcelOutportFileName(type);
            if(fileName == null){
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println(JSON.toJSONString(Msg.fail("类型异常")));
            }
            Object data = excelService.getDate(type);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            EasyExcel.write(response.getOutputStream(), reflectors.get(type).getType()).sheet("数据").doWrite((List<Object>)data);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println(JSON.toJSONString(Msg.fail("导出异常")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
