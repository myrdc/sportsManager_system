package com.gdut.boot.constance.common;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.exception.BusinessException;

import java.io.File;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 文件常量
 * @verdion
 * @date 2022/1/2717:54
 */

public class FileConstance {

    //文件
    public static final String[] IMG_SUFFIX =
            {".JPG", ".jpg", ".tiff", ".bmp", ".BMP", ".pcx", ".PCX", ".gif", ".GIF", ".WBMP", ".png", ".PNG", ".raw", ".RAW", ".JPEG", ".pnm", ".PNM", ".tif", ".TIF", ".TIFF", ".wbmp", ".jpeg", ".webp"};

    //图片存储路径
//    public static final String IMG_PATH = "/usr/local/nginx/file/sportsFile/img/";          //jj服务器
    public static final String IMG_PATH = "/file/sports/img/";
    //public static final String IMG_PATH = "/opt/docker-v/nginx/html/static/sport/file/img/";             //学校服务器
    //public static final String IMG_PATH = "D:/研发项目/体育馆预约系统/sports/img/";
    //public static final String IMG_PATH = "C:\\Users\\Administrator\\Desktop\\sport\\sports-manager\\img\\";

    //文件存储路径
//    public static final String DOC_PATH = "/usr/local/nginx/file/sportsFile/doc/";          //jj服务器
    public static final String DOC_PATH = "/file/sports/doc/";
    //public static final String DOC_PATH = "/opt/docker-v/nginx/html/static/sport/file/doc/";             //学校服务器
    //public static final String DOC_PATH = "D:/研发项目/体育馆预约系统/sports/doc/";
    //public static final String DOC_PATH = "C:\\Users\\Administrator\\Desktop\\sport\\sports-manager\\file\\";

    //临时文件存储路径
    public static final String TMP_PATH = "/file/sports/tmp/";

    //获取图片请求路径
    public static final String IMG_REQ = "/sports/api/img/get?path=";

    //获取文件请求路径
    public static final String FILE_REQ = "/sports/api/file/get?path=";

    /**
     * excel存放路径
     */
    public static final String EXCEL_OUTPUT_SPORTS = "/file/excel/sport/运动员导入模板.xlsx";
    public static final String EXCEL_OUTPUT_COACHS = "/file/excel/sport/教练导入模板.xlsx";
    public static final String EXCEL_OUTPUT_COMPETITIONS = "/file/excel/sport/比赛信息导入模板.xlsx";

            //jj服务器
//    public static final String EXCEL_OUTPUT_SPORTS = "/usr/local/nginx/file/sportsFile/excel/运动员导入模板.xlsx";
//    public static final String EXCEL_OUTPUT_COACHS = "/usr/local/nginx/file/sportsFile/excel/教练导入模板.xlsx";
//    public static final String EXCEL_OUTPUT_COMPETITIONS = "/usr/local/nginx/file/sportsFile/excel/比赛信息导入模板.xlsx";

    //public static final String EXCEL_OUTPUT_SPORTS = "/opt/sports/sportsManager/excel/运动员导入模板.xlsx";            //学校服务器
    //public static final String EXCEL_OUTPUT_COACHS = "/opt/sports/sportsManager/excel/教练导入模板.xlsx";             //学校服务器
    //public static final String EXCEL_OUTPUT_COMPETITIONS = "/opt/sports/sportsManager/excel/比赛信息导入模板.xlsx";     //学校服务器

}
