package com.gdut.boot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.OneOrgSecond;
import com.gdut.boot.bean.OneOrgSecondStu;
import com.gdut.boot.bean.SecondOrgStu;
import com.gdut.boot.constance.cache.Cache;
import com.gdut.boot.entity.*;
import com.gdut.boot.mapper.*;
import com.gdut.boot.service.*;
import com.gdut.boot.vo.CoachManageVo;
import com.gdut.boot.vo.res.CoachManageResVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gdut.boot.util.CommonUtil.strToQueue;
import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;

/**
 *
 */
@Service
@Slf4j
public class CoachManageServiceImpl extends ServiceImpl<CoachManageMapper, CoachManage>
    implements CoachManageService{

    @Autowired
    private CoachManageMapper coachManageMapper;

    @Autowired
    private CoachStuMapper coachStuMapper;

    @Autowired
    private SeOrganizationStudentMapper seOrganizationStudentMapper;

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private SportsBaseMsgMapper sportsBaseMsgMapper;

    @Autowired
    private SecondOrganizationMapper secondOrganizationMapper;

    @Autowired
    private SeOrganizationCoachService seOrganizationCoachService;

    @Autowired
    private OneOrganizationMapper oneOrganizationMapper;

    @Autowired
    private OneOrganizationService oneOrganizationService;

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @Autowired
    private CoachStuService coachStuService;


    @Override
    public Object toVo(CoachManage record) {
        CoachManageResVo coachManageResVo = new CoachManageResVo();
        coachManageResVo.setId(record.getId());
        coachManageResVo.setNumber(record.getNumber());
        coachManageResVo.setIdCardNumber(record.getIdCardNumber());
        coachManageResVo.setCardPicture(record.getCardPicture());
        coachManageResVo.setName(record.getName());
        coachManageResVo.setPhone(record.getPhone());
        coachManageResVo.setGender(record.getGender());
        coachManageResVo.setProfessionName(record.getProfessionName());

        Queue<Queue> queue = new LinkedList<>();
        final String projectGroup = record.getProjectGroup();
        final Queue<String> qs = JSON.parseObject(projectGroup, Queue.class);
        for (String q : qs) {
            final String[] split = q.split(":");
            if(split.length == 1){
                final List<SecondOrganization> secondOrganizations = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("one_org", split[0]));
                for (SecondOrganization secondOrganization : secondOrganizations) {
                    Queue<String> queues = new LinkedList<>();
                    queues.add(split[0]);
                    queues.add(secondOrganization.getName());
                    queue.add(queues);
                }
            }else{
                Queue<String> queues = new LinkedList<>();
                queues.add(split[0]);
                queues.add(split[1]);
                queue.add(queues);
            }
        }
        coachManageResVo.setProjectGroup(queue);
        return coachManageResVo;
    }

    /**
     * 绑定教练和二级组织以及学生之间的关系
     * @param coachManage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg dealWithOrgAndCoach(CoachManageVo coachManage) {
        final List<String> projectGroup = coachManage.getProjectGroup();    //教练所在组别，也就是二级组织
        final Integer coachManageId = coachManage.getId();
        //添加教练和二级组织的关系
        //首先找出所有二级组织的id
        //篮球:篮球甲组,足球:足球甲组,乒乓球
        Set<Integer> set = new HashSet<>();
        for (String pGs : projectGroup) {
            if(StrUtil.isBlank(pGs)){
                continue;
            }
            String[] split = pGs.split(":");
            if(split.length == 1){
                //选了全部
                final List<SecondOrganization> secondOrganizations = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("one_org", split[0]));
                if(secondOrganizations.isEmpty()){
                    //没有二级组织
                    continue;
                }
                final Set<Integer> ids = secondOrganizations.stream().map(SecondOrganization -> SecondOrganization.getId()).collect(Collectors.toSet());
                set.addAll(ids);
            }else{
                //选了一个
                final List<SecondOrganization> list = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", split[1]).eq("one_org", split[0]));
                if(list.isEmpty()){
                    return Msg.fail("二级组织错误");
                }
                set.add(list.get(0).getId());
            }
        }
        if(set.isEmpty()){
            return Msg.fail("没有对应的二级组织");
        }

        coachManageMapper.insertByGroupAndCoachId(set, coachManageId);
        //然后添加教练和学生的关系
        //既然是新增的，那原来的教练应该就没有吧
        final List<CoachStu> all = coachStuMapper.selectList(new QueryWrapper<CoachStu>().eq("coach_id", coachManage.getId()));
        if(!all.isEmpty()){
            //不应该有这种情况的
            return Msg.fail();
        }
        //找出组织里面的学生
        //通过set找出所有学生
        final List<SeOrganizationStudent> seOrganizationStudents = seOrganizationStudentMapper.selectList(new QueryWrapper<SeOrganizationStudent>().in("second_organization", set));
        if(seOrganizationStudents.isEmpty()){
            //组织没有学生，直接返回
            return Msg.success();
        }
        //有学生，就插入
        final Set<Integer> stuIds = seOrganizationStudents.stream().map(SeOrganizationStudent::getStudentId).collect(Collectors.toSet());
        coachStuMapper.insertByStuList(stuIds, coachManage.getId());
        return Msg.success();
    }

    //获取这个教练下面的学生
    @Override
    public Msg getStuByCoachId(String number) {
        Integer coachId = getIdByNumber(number);
        final List<CoachStu> coachStus = coachStuMapper.selectList(new QueryWrapper<CoachStu>().eq("coach_id", coachId));
        Set<Integer> stu = new HashSet<>();
        for (CoachStu coachStu : coachStus) {
            stu.add(coachStu.getStuId());
        }
        if(stu.isEmpty()){
            return Msg.success("该教练下面没有运动员");
        }
        final List<SportsBaseMsg> sportsBaseMsgs = sportsBaseMsgService.listByIds(stu);
        return Msg.success("成功", sportsBaseMsgs);
    }

    //根据教练id获取这个教练所在的一级组织和二级组织
    @Override
    public Msg getOneOrgById(String number) {
        Integer coachId = getIdByNumber(number);
        OneOrgSecond res = new OneOrgSecond();
        //找出教练的所有的二级组织
        final List<SeOrganizationCoach> coaches = seOrganizationCoachService.list(new QueryWrapper<SeOrganizationCoach>().eq("coach_id", coachId));
        if(coaches.isEmpty()){
            return Msg.fail("没有加入二级组织");
        }
        Set<Integer> set = new HashSet<>();
        for (SeOrganizationCoach coach : coaches) {
            set.add(coach.getSecondOrganization());
        }
        final List<SecondOrganization> secondOrganizations = secondOrganizationMapper.selectByIds(set);
        HashMap<String, LinkedList<SecondOrganization>> map = new HashMap<>();
        //一级组织名字 ==> 二级组织list
        for (SecondOrganization secondOrganization : secondOrganizations) {
            final String oneOrg = secondOrganization.getOneOrg();//一级组织名字
            final LinkedList<SecondOrganization> orDefault = map.getOrDefault(oneOrg, new LinkedList<>());
            orDefault.add(secondOrganization);
            map.put(oneOrg, orDefault);
        }
        //下面找出这些一级组织
        if(!map.isEmpty()){
            List<OneOrganization> oneOrganizations = oneOrganizationMapper.listByNames(map.keySet());
            if(oneOrganizations.isEmpty()){
                return Msg.fail("没有一级组织，是不是被删除了");
            }
            Set<String> isExit = new HashSet<>();
            for (OneOrganization oneOrganization : oneOrganizations) {
                //遍历所有一级组织，找到对应的二级组织
                if(!isExit.contains(oneOrganization.getName())){
                    res.getOneOrganizations().add(oneOrganization);
                    res.getSecondOrganizations().addAll(map.get(oneOrganization.getName()));
                    isExit.add(oneOrganization.getName());
                }
            }
            return Msg.success("成功", res);
        }
        return Msg.fail("没有一级组织");
    }

    @Override
    public boolean judgeIsUnExit(CoachManageVo coachManageVo) {
        List<String> projectGroup = coachManageVo.getProjectGroup();
        log.debug("一级组织数组{}" ,projectGroup);
        if(projectGroup == null){
            //如果为空，没有传入
            return false;
        }
        for (String single : projectGroup) {
            if(StrUtil.isBlank(single)){
                continue;
            }
            final String[] split = single.split(":");
            //首先判断一级组织
            List<OneOrganization> oneOrganizations = oneOrganizationService.list(new QueryWrapper<OneOrganization>().eq("name", split[0]));
            if(oneOrganizations.isEmpty()){
                //一级不存在
                return true;
            }
            //判断二级
            if(split.length > 1){
                //如果长度只有1，证明全选了，否则就是只选了一个
                final List<SecondOrganization> secondOrganizations = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", split[1]).eq("one_org", split[0]));
                if(secondOrganizations.isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    //批量插入
    @Override
    public void insertCoachManage(List<CoachManage> beans) {
        coachManageMapper.insertBatchSomeColumn(beans);
    }

    @Override
    public Msg dealWithExcelBean(List<Object> beans) {
        for (Object bean : beans) {
            return dealWithOrgAndCoachII((CoachManage)bean);
        }
        return null;
    }

    @Override
    public Msg dealBeanBefore(List<Object> beans) {
        for (Object bean : beans) {
            CoachManage coachManage = (CoachManage) bean;
            //判断教练是否重复了
            final List<CoachManage> coachManages = coachManageMapper.selectList(new QueryWrapper<CoachManage>().eq("number", coachManage.getNumber()));
            if(!coachManages.isEmpty()){
                return Msg.fail("教练工号" + coachManage.getNumber() +"重复");
            }
            coachManage.setProjectGroup(JSON.toJSONString(coachManage.getProjectGroup().split(",")));
        }
        return Msg.success();
    }

    /**
     * 传入教练id返回所有的一级组织和对应的运动员
     * @param number
     * @return
     */
    @Override
    public Msg getAllStuAndOrg(String number) {
        Integer coachId = getIdByNumber(number);
        List<CoachManage> coas = query().eq("id", coachId).list();
        if(coas.size() == 0){
            return Msg.fail("找不到对应的教练");
        }
        //1、首先找出这个教练所属的二级组织
        List<SecondOrganization> allSes = secondOrganizationMapper.selectByCoachId(coachId);
        if(allSes.size() == 0){
            return Msg.fail("没有加入任何二级组织");
        }
        //2、然后把这些二级组织按一级组织分类
        Map<String, List<SecondOrganization>> oneToTwo = allSes.stream().collect(Collectors.groupingBy(SecondOrganization::getOneOrg));
        //3、最后找到所有的运动员
        //3.1 遍历所有的一级组织
        List<OneOrgSecondStu> res = new ArrayList<>();
        for (String oneOrg : oneToTwo.keySet()) {
            OneOrgSecondStu add = new OneOrgSecondStu(oneOrg, null);
            List<SecondOrgStu> secondOrganizations = new ArrayList<>();
            //然后遍历一级组织下的二级组织
            List<SecondOrganization> allSeconds = oneToTwo.get(oneOrg);
            //3.2 遍历所有二级组织
            for (SecondOrganization allSecond : allSeconds) {
                //找出这个二级组织下的所有运动员
                Integer id = allSecond.getId();
                List<SportsBaseMsg> sportsBaseMsgs = sportsBaseMsgMapper.selectBySeId(id);
                secondOrganizations.add(new SecondOrgStu(allSecond.getName(), id, sportsBaseMsgs));
            }
            add.setSecondOrganizations(secondOrganizations);
            res.add(add);
        }
        return Msg.success("获取成功", res);
    }

    @Override
    public void dealIfPutOrg(CoachManage resource, CoachManageVo after) {
        List<String> projectGroup = after.getProjectGroup();
        boolean isSame = judgeIfSame(resource.getProjectGroup(), projectGroup);
        if(isSame == false){
            //开始修改
            //删除原来的关系
            Integer id = resource.getId();
            //1.删除coach和stu的关系
            coachStuService.remove(new QueryWrapper<CoachStu>().eq("coach_id", id));
            //2.删除coach和组织的关系
            seOrganizationCoachService.remove(new QueryWrapper<SeOrganizationCoach>().eq("coach_id", id));
            //3.添加新的关系
            dealWithOrgAndCoach(after);
        }
    }

    /**
     * 判断教练前后组别有变化吗
     * @param after 后来的组别
     * @return
     */
    private boolean judgeIfSame(String before, List<String> after) {
        if(before == null && (after != null || after.size() != 0)){
            return false;
        }
        if(before == null && (after == null || after.size() == 0)){
            return true;
        }
        Queue queue = JSON.parseObject(before, Queue.class);
        return isListEqual(queue, after);
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

    public Integer getIdByNumber(String number){
        CoachManage one = query().eq("number", number).one();
        if(one == null){
            return -1;
        }
        return one.getId();
    }

    //导入excel的时候处理
    private Msg dealWithOrgAndCoachII(CoachManage coachManage) {
        String group = coachManage.getProjectGroup();
        Queue<String> ps = JSON.parseObject(group, Queue.class);
//        String[] ps = group.split(",");

        final Integer coachManageId = coachManage.getId();
        //添加教练和二级组织的关系
        //首先找出所有二级组织的id
        Set<Integer> set = new HashSet<>();
        for (String pGs : ps) {
            String[] split = pGs.split(":");
            if(split.length == 1){
                //选了全部
                final List<SecondOrganization> secondOrganizations = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("one_org", split[0]));
                if(secondOrganizations.isEmpty()){
                    //没有二级组织
                    break;
                }
                final Set<Integer> ids = secondOrganizations.stream().map(SecondOrganization -> SecondOrganization.getId()).collect(Collectors.toSet());
                set.addAll(ids);
            }else{
                //选了一个
                final List<SecondOrganization> list = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", split[1]).eq("one_org", split[0]));
                if(list.isEmpty()){
                    return Msg.success();
                }
                set.add(list.get(0).getId());
            }
        }

        if(!set.isEmpty()){
            coachManageMapper.insertByGroupAndCoachId(set, coachManageId);
        }
        //然后添加教练和学生的关系
        //既然是新增的，那原来的教练应该就没有吧
        final List<CoachStu> all = coachStuMapper.selectList(new QueryWrapper<CoachStu>().eq("coach_id", coachManage.getId()));
        if(!all.isEmpty()){
            //不应该有这种情况的
            return Msg.success();
        }
        //找出组织里面的学生
        //通过set找出所有学生
        final List<SeOrganizationStudent> seOrganizationStudents = seOrganizationStudentMapper.selectList(new QueryWrapper<SeOrganizationStudent>().in("second_organization", set));
        if(seOrganizationStudents.isEmpty()){
            //组织没有学生，直接返回
            return Msg.success();
        }
        //有学生，就插入
        final Set<Integer> stuIds = seOrganizationStudents.stream().map(SeOrganizationStudent::getStudentId).collect(Collectors.toSet());
        coachStuMapper.insertByStuList(stuIds, coachManage.getId());
        coachManage.setProjectGroup(JSON.toJSONString(ps));
        return Msg.success();
    }
}




