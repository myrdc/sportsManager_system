package com.gdut.boot.constance.cache;

import com.gdut.boot.entity.*;
import com.gdut.boot.handler.generalHandler.Handler;
import com.gdut.boot.handler.generalHandler.*;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.service.*;
import com.gdut.boot.util.BeanUtils;

import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;

import static com.gdut.boot.constance.common.Common.INVOKE_LINK;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/270:15
 */

public class Cache {

    //执行链缓存
    public static final Map<String, Handler> HANDLERS = new HashMap<>();
    //数字和sevice类对应
    public static final Map<Integer, Class> SERVICE_CLASS = new HashMap<>();
    //数字和Invoker对应
    public static final Map<Integer, MyReflector> reflectors = new HashMap<>();
    //接口和名字对应
    public static final Map<Integer,  String> interfaceName = new HashMap<>();
    //类和名字对应
    public static final Map<String,  String> className = new HashMap<>();



    public static void LoadMap() throws Exception {
        //设置请求执行链
        HANDLERS.put(INVOKE_LINK, new Handler.Builder<Handler>()
                .addHandler(new PermissionHandler())
                .addHandler(new VerifyHandler())
                .addHandler(new GetHandler())
                .addHandler(new PostHandler())
                .addHandler(new DeleteHandler())
                .addHandler(new PutHandler())
                .build());

        //设置service_class对应名字
        SERVICE_CLASS.put(1, AccountService.class);
        SERVICE_CLASS.put(2, AttendanceManegeService.class);
        SERVICE_CLASS.put(3, CoachManageService.class);
        SERVICE_CLASS.put(4, CompetitionManageService.class);
        SERVICE_CLASS.put(5, OneOrganizationService.class);
        SERVICE_CLASS.put(6, PermissionService.class);
        SERVICE_CLASS.put(10, SportsBaseMsgService.class);
        SERVICE_CLASS.put(11, UserPermissionService.class);
        SERVICE_CLASS.put(12, ApproveService.class);
        SERVICE_CLASS.put(13, NoticeService.class);
        SERVICE_CLASS.put(14, EntranceExamMsgService.class);
        SERVICE_CLASS.put(15, EntranceInfoService.class);
        SERVICE_CLASS.put(16, SportCompetitionService.class);
        SERVICE_CLASS.put(17, SecondOrganizationService.class);
        SERVICE_CLASS.put(18, SportsTeamService.class);

        //反射器
        reflectors.put(1, new MyReflector(Account.class));
        reflectors.put(2, new MyReflector(AttendanceManege.class));
        reflectors.put(3, new MyReflector(CoachManage.class));
        reflectors.put(4, new MyReflector(CompetitionManage.class));
        reflectors.put(5, new MyReflector(OneOrganization.class));
        reflectors.put(6, new MyReflector(Permission.class));
        reflectors.put(10,new MyReflector(SportsBaseMsg.class));
        reflectors.put(11,new MyReflector(UserPermission.class));
        reflectors.put(12,new MyReflector(Approve.class));
        reflectors.put(13,new MyReflector(Notice.class));
        reflectors.put(14,new MyReflector(EntranceExamMsg.class));
        reflectors.put(15,new MyReflector(EntranceInfo.class));
        reflectors.put(16,new MyReflector(SportCompetition.class));
        reflectors.put(17,new MyReflector(SecondOrganization.class));
        reflectors.put(18,new MyReflector(SportsTeam.class));

        interfaceName.put(1, "获取账号信息");
        interfaceName.put(2, "添加账号信息");
        interfaceName.put(3, "修改账号信息");
        interfaceName.put(4, "删除账号信息");
        interfaceName.put(5, "获取审核信息");
        interfaceName.put(6, "添加审核信息");
        interfaceName.put(7, "再次上传审核信息");
        interfaceName.put(8, "修改审核信息");
        interfaceName.put(9, "删除审核信息");
        interfaceName.put(10, "批量删除审核信息");
        interfaceName.put(11, "管理员通过审核");
        interfaceName.put(12, "撤销提交的审核信息");
        interfaceName.put(13, "管理员审核不通过");
        interfaceName.put(14, "通过教练获取考勤信息");
        interfaceName.put(15, "获取考勤信息");
        interfaceName.put(16, "添加考勤信息");
        interfaceName.put(17, "删除考勤信息");
        interfaceName.put(18, "修改考勤信息");
        interfaceName.put(19, "批量删除考勤信息");
        interfaceName.put(20, "获取教练信息");
        interfaceName.put(21, "添加教练信息");
        interfaceName.put(22, "修改教练信息");
        interfaceName.put(23, "删除教练信息");
        interfaceName.put(24, "批量删除教练信息");
        interfaceName.put(25, "测试");
        interfaceName.put(26, "获取教练下面的学生");
        interfaceName.put(27, "获取教练所属的一级组织");
        interfaceName.put(28, "测试");
        interfaceName.put(29, "获取比赛信息");
        interfaceName.put(30, "添加比赛信息");
        interfaceName.put(31, "修改比赛信息");
        interfaceName.put(32, "删除比赛信息");
        interfaceName.put(33, "批量删除比赛信息");
        interfaceName.put(44, "excel批量导入");
        interfaceName.put(45, "下载excel导入模板");
        interfaceName.put(46, "导出excel");
        interfaceName.put(47, "获取通知信息");
        interfaceName.put(48, "添加通知信息");
        interfaceName.put(49, "修改通知信息");
        interfaceName.put(50, "删除通知信息");
        interfaceName.put(51, "批量删除通知信息");
        interfaceName.put(52, "通过id获取账号信息");
        interfaceName.put(53, "获取项目信息");
        interfaceName.put(54, "添加项目信息");
        interfaceName.put(55, "修改项目信息");
        interfaceName.put(56, "删除项目信息");
        interfaceName.put(57, "批量删除账号信息");
        interfaceName.put(62, "获取运动队信息");
        interfaceName.put(63, "添加运动队信息");
        interfaceName.put(64, "修改运动队信息");
        interfaceName.put(65, "删除运动队信息");
        interfaceName.put(66, "批量删除运动队信息");
        interfaceName.put(67, "查询运动队下面的运动员");
        interfaceName.put(68, "查询运动队下面的教练");
        interfaceName.put(69, "获取个人比赛信息");
        interfaceName.put(70, "添加个人比赛信息");
        interfaceName.put(71, "修改个人比赛信息");
        interfaceName.put(72, "删除个人比赛信息");
        interfaceName.put(73, "批量删除个人比赛信息");
        interfaceName.put(74, "获取个人信息");
        interfaceName.put(75, "添加个人信息");
        interfaceName.put(76, "修改个人信息");
        interfaceName.put(77, "删除个人信息");
        interfaceName.put(78, "批量删除个人信息");
        interfaceName.put(79, "设置用户权限");
        interfaceName.put(80, "获取所有人的权限");
        interfaceName.put(81, "根据工号获取权限");
        interfaceName.put(82, "添加新的用户权限");
        interfaceName.put(83, "登出");
        interfaceName.put(84, "获取所有一级二级组织以及运动员");
        interfaceName.put(85, "教练获取所有参与的比赛信息");

        className.put("AccountController", "账号管理");
        className.put("ApproveController", "审核管理");
        className.put("AttendanceManegeController", "考勤管理");
        className.put("CoachController", "教练管理");
        className.put("CompetitionManageController", "比赛管理");
        className.put("FileController", "文件管理");
        className.put("ImgController", "图片管理");
        className.put("NoticeController", "通知管理");
        className.put("OneOrganizationController", "项目管理");
        className.put("PermissionController", "权限管理");
        className.put("SecondOrganizationController", "运动队管理");
        className.put("SportCompetitionController", "个人比赛管理");
        className.put("SportsBaseMsgController", "个人信息管理");
        className.put("SportsTeamController", "运动队管理");


    }




}
