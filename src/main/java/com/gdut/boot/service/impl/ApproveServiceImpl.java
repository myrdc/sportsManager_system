package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.*;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.filter.UserHolder;
import com.gdut.boot.service.*;
import com.gdut.boot.mapper.ApproveMapper;
import com.gdut.boot.util.JwtUtil;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.vo.ApproveVo;
import com.gdut.boot.vo.CompetitionManageVo;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.gdut.boot.constance.common.CompetitionCommons.*;
import static com.gdut.boot.constance.common.RedisConstance.redis_login;
import static com.gdut.boot.util.CommonUtil.get16UUID;
import static com.gdut.boot.util.TimeUtil.getTimeNow;

/**
 *
 */
@Service
public class ApproveServiceImpl extends ServiceImpl<ApproveMapper, Approve>
        implements ApproveService {

    @Autowired
    private ApproveService approveService;

    @Autowired
    private CompetitionManageService competitionManageService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SportCompetitionService sportCompetitionService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private SportsBaseMsgService sportsBaseMsgService;

    @Resource
    private CoachManageService coachManageService;

    @Resource
    private CoachCompetitionService coachCompetitionService;

    public UserPermissionVo getUser(){
        String token = request.getHeader("token");
        Object object = redisUtils.get(redis_login + token);
        if(object == null){
            throw new BusinessException(Msg.fail("登陆信息丢失, 请重新进入网站"));
        }
        UserPermissionVo user = JSONObject.parseObject((String) object, UserPermissionVo.class);
        return user;
    }


    @Override
    public void postApproves(int type, int entityId, RequestMessage message) {
        UserPermissionVo now = getUser();
        //审核人员
        List<String> approvePerson = ((CompetitionManageVo) message.getVo()).getApprovePerson();
        for (String person : approvePerson) {
            //判断审核人的权限
            //到这里应该就是管理员的了
            //生成审核的内容
            Approve approve = new Approve(0, type, entityId, person, APPROVE_STATE_UNDO, now.getUserNumber(), getTimeNow(), "有新的比赛请求等待审核", 1,
                    0, null, null, null,  null);   //被审核人的name
            boolean save = approveService.save(approve);
            if (save == false) {
                throw new BusinessException(Msg.fail("ApproveServiceImpl类postApproves方法添加审核出现异常"));
            }
            //发送通知
            Notice notice = new Notice(0, person, "有比赛信息上传的请求等待审核", now.getUserNumber() , UNREAD, entityId, type);
            noticeService.save(notice);
        }
    }

    /**
     * 手动添加审核信息 审核信息
     * @param approveVo
     * @return
     */
    @Override
    public Msg post(ApproveVo approveVo) {
        //1.获取审核的记录，查看这条记录是不是已经被审核过了

        saveToApprove(approveVo);
        return Msg.success("成功");
    }

    /**
     * 添加审核信息
     *
     * @param approveVo
     */
    private void saveToApprove(ApproveVo approveVo) {
        Approve approve = new Approve(0, approveVo.getType(),
                approveVo.getContentId(), null, approveVo.getState()
                , approveVo.getResourceNumber(), approveVo.getTime(), approveVo.getMessage(),
                approveVo.getBeforeOrAfter(), approveVo.getResult(), approveVo.getApproveTime(),
                null, null, "aaa");
        for (String number : approveVo.getNumber()) {
            approve.setNumber(number);
            approveService.save(approve);
        }
    }


    /**
     * 分管领导选择给部分负责人进行审核 TODO:检测账号合法性
     *
     * @param approveVo
     * @return
     */
    @Override
    public Msg next(ApproveVo approveVo) {
        if (approveVo.getType() == AllClass.CompetitionManage.getValue()) {
            Integer modifyId = approveVo.getContentId();
            //设置分管领导的修改状态为已修改
            UpdateWrapper<Approve> var1 = new UpdateWrapper();
            var1.eq("content_id", modifyId).eq("before_or_after", approveVo.getBeforeOrAfter())
                    .eq("type", approveVo.getType()).set("state", 1).set("approve_time", getTimeNow()).set("real_approve", "3120005071");
            approveService.update(var1);

            //设置传递给部门负责人
            List<String> number = approveVo.getNumber();
            String uuid16 = "sh" + get16UUID();
            for (String num : number) {
                Approve approve = new Approve(0, approveVo.getType(), approveVo.getContentId(), num
                        , 2, approveVo.getResourceNumber(), getTimeNow(),
                        approveVo.getMessage(), approveVo.getBeforeOrAfter(), 0
                        , approveVo.getApproveTime(), null, null, "aaa");
                boolean save = approveService.save(approve);
                if (save == false) {
                    return Msg.fail("ApproveServiceImpl类next方法添加审核出现异常");
                }
            }

            //修改比赛状态原来数据库状态为2等待
            Integer type = approveVo.getType();
            if (type == AllClass.CompetitionManage.getValue()) {
                UpdateWrapper<CompetitionManage> updateWrapper = new UpdateWrapper();
                Integer contentId = approveVo.getContentId();
                updateWrapper.eq("id", contentId).set("state", 2);
                boolean update = competitionManageService.update(updateWrapper);
                if (update == false) {
                    return Msg.fail("ApproveServiceImpl类next方法修改类型" + type + "出现异常");
                }
            }

            //发通知给被审核人
            //TODO:应该是从session里面获取当前分管领导的工号，这个方法只能分管领导发出
            Notice notice = new Notice(0, approveVo.getResourceNumber(), "分管领导审核成功，等待部门负责人进行审核",
                    "aaa", 2, approveVo.getContentId(), approveVo.getType());
            noticeService.save(notice);

            //发通知给下级领导
            for (String nextLeaderNumber : approveVo.getNumber()) {
                Notice nextLeaderNotice = new Notice(0, nextLeaderNumber, "有比赛信息上传的请求等待审核", "系统消息", 2,
                        approveVo.getContentId(), approveVo.getType());
                noticeService.save(nextLeaderNotice);
            }
            return Msg.success("成功");
        }
        return Msg.fail("不是要审核的类型");
    }

    @Override
    public Msg get(String number, int pn, int size) {
        return null;
    }

    /**
     * 分管领导和部门负责人完成审核
     *
     * @param approveVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg finish(ApproveVo approveVo, HttpServletRequest request) {
        //什么类型
        Integer type = approveVo.getType();
        //内容的id
        Integer contentId = approveVo.getContentId();
        //获取登陆的用户
        UserPermissionVo user = getUser();

        //1.处理比赛信息，给运动员也添加相关的信息
        if (type == AllClass.CompetitionManage.getValue()) {
            Msg msg = dealAPPWhereCom(contentId, user);
            if (msg.getCode() == 0) {
                return msg;
            }
        }

        //2.更新审核的原始状态
        UpdateWrapper<Approve> var = new UpdateWrapper<>();
        //已审核通过
        var.set("state", APPROVE_STATE_DO).set("result", APPROVE_SUCCESS).set("real_approve", user.getUserNumber()).set("approve_time", getTimeNow())
                .eq("content_id", contentId)
                .eq("type", approveVo.getType())
                .eq("before_or_after", approveVo.getBeforeOrAfter());
        boolean isSccess = approveService.update(var);
        if (!isSccess) {
            return Msg.fail("更新审核原数据失败");
        }

        //3.发送通知给被审核人
        Notice notice = new Notice(0, approveVo.getResourceNumber(), approveVo.getMessage(), user.getUserNumber(), UNREAD, approveVo.getContentId()
                , approveVo.getType());
        boolean save = noticeService.save(notice);
        if (!save) {
            return Msg.fail("发送通知给被审核者失败");
        }
        return Msg.success("审核完成");
    }

    public Msg dealAPPWhereCom(int contentId, UserPermissionVo user) {
        //首先判断这个内容有没有被审核过了
        QueryWrapper<CompetitionManage> var = new QueryWrapper<CompetitionManage>().eq("id", contentId);;
        List<CompetitionManage> list = competitionManageService.list(var);
        if (list.size() == 0) {
            return Msg.fail("审核的比赛信息信息不存在！！！");
        }
        if (!list.get(0).getState().equals(COMPETITION_APPROVE_WAIT)) {
            return Msg.fail("该信息已经被其他人审核过了，不需要再次审核");
        }

        //1.设置原来的数据为2已完成
        UpdateWrapper<CompetitionManage> var1 = new UpdateWrapper<>();
        var1.set("state", COMPETITION_APPROVE_FINISH);
        var1.eq("id", contentId);
        boolean update = competitionManageService.update(var1);
        if (!update) {
            return Msg.fail("更新比赛原数据失败");
        }
        //2、给运动员也添加上一条比赛信息
        String name = list.get(0).getName();
        //3、临时信息
        CompetitionManage temp = list.get(0);
        Queue<String> queue = JSON.parseObject(name, Queue.class);
        //通过名字去找工号出来
        List<SportsBaseMsg> names = sportsBaseMsgService.list(new QueryWrapper<SportsBaseMsg>().in("chinese_name", queue));
        if(names.isEmpty() || names.size() != queue.size()){
            return Msg.fail("参赛的选手在系统中没有账号");
        }
        Map<String, String> nameNumberMap = names.stream().collect(Collectors.toMap(SportsBaseMsg::getChineseName, SportsBaseMsg::getNumber, (k, v) -> v));
        for (String stuName : queue) {
            //添加个人的比赛记录
            SportCompetition sportCompetition = new SportCompetition(0, nameNumberMap.get(stuName),temp.getDesgination(), temp.getCompetitionTime(), temp.getId());
            sportCompetitionService.save(sportCompetition);
            //每个运动员也要发送通知
            Notice notice = new Notice(0, nameNumberMap.get(stuName),"有新的比赛记录,请到个人比赛查看",user.getUserNumber(), UNREAD, contentId, AllClass.CompetitionManage.getValue());
            noticeService.save(notice);
        }

        //4、给每个教练也插入一条比赛记录
        String coach = list.get(0).getCoach();
        Queue<String> parseObject = JSON.parseObject(coach, Queue.class);
        for (String coachs : parseObject) {
            //找出教练的id
            List<CoachManage> coachManages = coachManageService.query().eq("name", coachs).list();
            if(coachManages.size() != 0){
                coachCompetitionService.save(new CoachCompetition(0, coachManages.get(0).getNumber(), contentId));
            }
        }
        return Msg.success();
    }


    /**
     * 教练撤销申请, 那么修改状态为-1
     */
    @Override
    public Msg undo(ApproveVo approveVo) {
        //首先判断有没有通过审核，如果通过了就不能撤销
        int type = approveVo.getType();
        if (type == AllClass.CompetitionManage.getValue()) {
            return undoCM(approveVo);
        }
        return Msg.success();
    }

    /**
     * 管理员选择不通过
     *
     * @param approveVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg fail(ApproveVo approveVo, HttpServletRequest request) {
        //1.获取当前用户信息
        UserPermissionVo now = getUser();
        //2.首先设置比赛信息
        if (approveVo.getType() == AllClass.CompetitionManage.getValue()) {
            //2.1获取比赛id
            int contentId = approveVo.getContentId();
            //2.2获取比赛内容
            List<CompetitionManage> contents = competitionManageService.list(new QueryWrapper<CompetitionManage>().eq("id", contentId));
            if (contents.size() == 0) {
                return Msg.fail("查询不到比赛数据, 请检查是否被删除了");
            }
            CompetitionManage competitionManage = contents.get(0);
            if (!competitionManage.getState().equals(COMPETITION_APPROVE_WAIT)) {
                return Msg.fail("该信息已经被其他人审核过了，不需要再次审核");
            }
            //2.3更新比赛状态为未通过
            boolean update = competitionManageService.update(new UpdateWrapper<CompetitionManage>().set("state", COMPETITION_APPROVE_FAIL).eq("id", contentId));
            //2.4判断更新有没有成功
            if (update == false) {
                return Msg.fail();
            }
        }

        //2、其次更新审核信息
        approveService.update(new UpdateWrapper<Approve>()
                .set("state", APPROVE_STATE_DO).   //1.已审核
                        set("result", APPROVE_FAIL).   //2.不通过
                        set("approve_time", getTimeNow()).  //3.审核时间
                        eq("id", approveVo.getId()));   //通过id更新

        //3.最后发送通知给被审核的人
        Notice notice = new Notice(0, approveVo.getResourceNumber(), approveVo.getMessage(), now.getUserNumber(),
                UNREAD, approveVo.getContentId(), approveVo.getType());
        noticeService.save(notice);
        //4.返回结果
        return Msg.success("成功");
    }

    /**
     * 再一次提交审核的申请
     *
     * @param approveVo
     * @return
     */
    @Override
    public Msg postAgain(ApproveVo approveVo, List<String> approves) {
        //判断
        if (approveVo.getState() != -1 || approveVo.getState() != 2) {
            return Msg.fail("请求没有被驳回或者拒绝, 无法再次提交请求");
        }
        for (String approveNum : approves) {
            Approve approve = new Approve(0, approveVo.getType(),
                    approveVo.getContentId(), approveNum, 2
                    , approveVo.getResourceNumber(), getTimeNow(), null,
                    approveVo.getBeforeOrAfter(), 0, null,
                    null, null, "aaa");
            approveService.save(approve);

            //发通知
            //TODO:从session里面获取frompeople
            Notice notice = new Notice(0, approveVo.getResourceNumber(), approveVo.getMessage(), "aaa",
                    2, approveVo.getContentId(), approveVo.getType());
        }
        return Msg.success("成功");
    }

    /**
     * 比赛申请的撤销操作
     */
    private Msg undoCM(ApproveVo approveVo) {
        int contentId = approveVo.getContentId();
        List<CompetitionManage> lists = competitionManageService.list(new QueryWrapper<CompetitionManage>().eq("id", contentId));
        if (lists.size() == 0) {
            return Msg.fail("比赛消息不存在，不能撤销，请检查是不是删除了比赛消息");
        }
        if (lists.get(0).getState().equals("3") || lists.get(0).getState().equals("4")) {
            return Msg.fail("已经审核完成的不可以再撤销");
        }
        //1、把比赛消息改成-1状态
        UpdateWrapper<CompetitionManage> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", contentId);
        updateWrapper.set("state", -1); //-1表示撤销状态
        competitionManageService.update(updateWrapper);

        //2、修改审核消息为-1
        UpdateWrapper<Approve> upVar = new UpdateWrapper<>();
        upVar.eq("before_or_after", approveVo.getBeforeOrAfter())
                .eq("type", approveVo.getType())
                .eq("content_id", approveVo.getContentId())
                .set("result", -1);    //result设置为-1表示撤销了

        approveService.update(upVar);

        //3、发送通知
        List<String> leads = approveVo.getNumber();
        for (String coachMsg : leads) {
            if (!coachMsg.equals(approveVo.getResourceNumber())) {

            }
            //TODO:从session中获取
            Notice notice = new Notice(0, coachMsg,
                    "教练" + approveVo.getResourceNumber() + "撤销了id为" + contentId + "的比赛申请",
                    "aaa", 2, approveVo.getContentId(), approveVo.getType());
            noticeService.save(notice);
        }
        return Msg.success();
    }


}




