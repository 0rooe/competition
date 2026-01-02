package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.entity.SaixiangbaomingEntity;
import com.entity.view.SaixiangbaomingView;
import com.entity.XueshengEntity;
import com.service.SaixiangbaomingService;
import com.service.XueshengService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 赛项报名
 * 后端接口
 * 
 * @author
 * @email
 * @date 2021-03-26 17:35:20
 */
@RestController
@RequestMapping("/saixiangbaoming")
public class SaixiangbaomingController {
    @Autowired
    private SaixiangbaomingService saixiangbaomingService;

    @Autowired
    private XueshengService xueshengService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SaixiangbaomingEntity saixiangbaoming,
            HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        EntityWrapper<SaixiangbaomingEntity> ew = new EntityWrapper<SaixiangbaomingEntity>();

        if (tableName.equals("xuesheng")) {
            String xuehao = (String) request.getSession().getAttribute("username");
            List<SaixiangbaomingEntity> myEntries = saixiangbaomingService.selectList(
                    new EntityWrapper<SaixiangbaomingEntity>().eq("xuehao", xuehao));
            java.util.Set<String> myTeams = new java.util.HashSet<>();
            for (SaixiangbaomingEntity e : myEntries) {
                if (e.getTuanduimingcheng() != null && !e.getTuanduimingcheng().isEmpty()) {
                    myTeams.add(e.getTuanduimingcheng());
                }
            }
            if (myTeams.isEmpty()) {
                ew.eq("xuehao", xuehao);
            } else {
                ew.eq("xuehao", xuehao)
                        .or()
                        .in("tuanduimingcheng", myTeams);
            }
            ew.orderBy("tuanduimingcheng");
        }
        if (tableName.equals("jiaoshi")) {
            saixiangbaoming.setJiaoshigonghao((String) request.getSession().getAttribute("username"));
        }

        PageUtils page = saixiangbaomingService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, saixiangbaoming), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, SaixiangbaomingEntity saixiangbaoming,
            HttpServletRequest request) {
        EntityWrapper<SaixiangbaomingEntity> ew = new EntityWrapper<SaixiangbaomingEntity>();
        PageUtils page = saixiangbaomingService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, saixiangbaoming), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(SaixiangbaomingEntity saixiangbaoming) {
        EntityWrapper<SaixiangbaomingEntity> ew = new EntityWrapper<SaixiangbaomingEntity>();
        ew.allEq(MPUtil.allEQMapPre(saixiangbaoming, "saixiangbaoming"));
        return R.ok().put("data", saixiangbaomingService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SaixiangbaomingEntity saixiangbaoming) {
        EntityWrapper<SaixiangbaomingEntity> ew = new EntityWrapper<SaixiangbaomingEntity>();
        ew.allEq(MPUtil.allEQMapPre(saixiangbaoming, "saixiangbaoming"));
        SaixiangbaomingView saixiangbaomingView = saixiangbaomingService.selectView(ew);
        return R.ok("查询赛项报名成功").put("data", saixiangbaomingView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SaixiangbaomingEntity saixiangbaoming = saixiangbaomingService.selectById(id);
        return R.ok().put("data", saixiangbaoming);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        SaixiangbaomingEntity saixiangbaoming = saixiangbaomingService.selectById(id);
        return R.ok().put("data", saixiangbaoming);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SaixiangbaomingEntity saixiangbaoming, HttpServletRequest request) {
        saixiangbaoming.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(saixiangbaoming);
        saixiangbaomingService.insert(saixiangbaoming);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody SaixiangbaomingEntity saixiangbaoming, HttpServletRequest request) {
        saixiangbaoming.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(saixiangbaoming);

        if ("团队赛".equals(saixiangbaoming.getLeixing())) {
            if ("队长".equals(saixiangbaoming.getTuanduiRole())) {
                // Generate logic
                String code = getRandomString(6);
                saixiangbaoming.setInvitationCode(code);
            } else if ("队员".equals(saixiangbaoming.getTuanduiRole())) {
                // Member logic: validate code
                if (saixiangbaoming.getInvitationCode() == null || saixiangbaoming.getInvitationCode().isEmpty()) {
                    return R.error("请输入邀请码");
                }
                SaixiangbaomingEntity captain = saixiangbaomingService
                        .selectOne(new EntityWrapper<SaixiangbaomingEntity>()
                                .eq("invitation_code", saixiangbaoming.getInvitationCode()));

                if (captain == null) {
                    return R.error("无效的邀请码");
                }
                // Copy info from captain
                saixiangbaoming.setTuanduimingcheng(captain.getTuanduimingcheng());
                saixiangbaoming.setJiaoshigonghao(captain.getJiaoshigonghao());
                saixiangbaoming.setJiaoshixingming(captain.getJiaoshixingming());
            }
        }

        saixiangbaomingService.insert(saixiangbaoming);
        return R.ok().put("invitationCode", saixiangbaoming.getInvitationCode());
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        java.util.Random random = new java.util.Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SaixiangbaomingEntity saixiangbaoming, HttpServletRequest request) {
        // ValidatorUtils.validateEntity(saixiangbaoming);
        saixiangbaomingService.updateById(saixiangbaoming);// 全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        saixiangbaomingService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口
     */
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
            @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (map.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if (map.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        Wrapper<SaixiangbaomingEntity> wrapper = new EntityWrapper<SaixiangbaomingEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }

        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("xuesheng")) {
            wrapper.eq("xuehao", (String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("jiaoshi")) {
            wrapper.eq("jiaoshigonghao", (String) request.getSession().getAttribute("username"));
        }

        int count = saixiangbaomingService.selectCount(wrapper);
        return R.ok().put("count", count);
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws java.io.IOException {
        List<SaixiangbaomingEntity> list = saixiangbaomingService
                .selectList(new EntityWrapper<SaixiangbaomingEntity>());
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (list != null) {
            for (SaixiangbaomingEntity item : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("saixiangmingcheng", item.getSaixiangmingcheng());
                map.put("leixing", item.getLeixing());
                map.put("jibie", item.getJibie());
                map.put("baomingfeiyong", item.getBaomingfeiyong());
                map.put("baomingriqi", item.getBaomingriqi());
                map.put("xuehao", item.getXuehao());
                map.put("xingming", item.getXingming());
                // Transform sfsh values
                String sfsh = item.getSfsh();
                if ("是".equals(sfsh)) {
                    map.put("sfsh", "通过");
                } else if ("否".equals(sfsh)) {
                    map.put("sfsh", "未通过");
                } else {
                    map.put("sfsh", sfsh);
                }
                map.put("shhf", item.getShhf());
                // Add ispay field
                map.put("ispay", item.getIspay());
                // Add teacher fields
                map.put("jiaoshigonghao", item.getJiaoshigonghao());
                map.put("jiaoshixingming", item.getJiaoshixingming());
                map.put("tuanduimingcheng", item.getTuanduimingcheng());
                map.put("tuanduiRole", item.getTuanduiRole());
                map.put("invitationCode", item.getInvitationCode());
                mapList.add(map);
            }
        }
        String[] headers = { "赛项名称", "类型", "级别", "报名费用", "报名日期", "学号", "姓名", "审核状态", "审核回复", "是否支付", "教师工号", "教师姓名",
                "团队名称", "团队角色", "邀请码" };
        String[] columns = { "saixiangmingcheng", "leixing", "jibie", "baomingfeiyong", "baomingriqi", "xuehao",
                "xingming", "sfsh", "shhf", "ispay", "jiaoshigonghao", "jiaoshixingming", "tuanduimingcheng",
                "tuanduiRole", "invitationCode" };
        com.utils.ExcelUtils.export(response, mapList, headers, columns, "赛项报名列表");
    }

    /**
     * 邀请队员
     */
    @RequestMapping("/invite")
    public R invite(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String xuehao = (String) params.get("xuehao");
        String teamName = (String) params.get("teamName");
        String saixiangId = (String) params.get("saixiangId");

        // Check if student exists
        XueshengEntity student = xueshengService.selectOne(new EntityWrapper<XueshengEntity>().eq("xuehao", xuehao));
        if (student == null) {
            return R.error("未找到该学号的学生");
        }

        // Check if already registered
        SaixiangbaomingEntity existing = saixiangbaomingService.selectOne(new EntityWrapper<SaixiangbaomingEntity>()
                .eq("xuehao", xuehao)
                .eq("saixiangmingcheng", params.get("saixiangmingcheng")));

        if (existing != null) {
            return R.error("该学生已报名此赛项");
        }

        // Create registration
        SaixiangbaomingEntity baoming = new SaixiangbaomingEntity();
        // Copy necessary fields from Captain's registration or params
        // Ideally we should look up the captain's record to copy competition details,
        // but for now we assume params has enough info or we fetch valid info

        baoming.setXuehao(student.getXuehao());
        baoming.setXingming(student.getXingming());
        baoming.setTuanduimingcheng(teamName);
        baoming.setTuanduiRole("队员");

        // We need to fill in other fields like saixiangmingcheng, etc.
        // Let's assume the params passed contain the full registration object of the
        // captain to copy from
        baoming.setSaixiangmingcheng((String) params.get("saixiangmingcheng"));
        baoming.setLeixing((String) params.get("leixing"));
        baoming.setJibie((String) params.get("jibie"));
        baoming.setBaomingfeiyong((Integer) params.get("baomingfeiyong")); // Careful with Type casting
        baoming.setBaomingriqi(new java.util.Date());

        // Copy Teacher info
        baoming.setJiaoshigonghao((String) params.get("jiaoshigonghao"));
        baoming.setJiaoshixingming((String) params.get("jiaoshixingming"));

        baoming.setId(new java.util.Date().getTime());
        saixiangbaomingService.insert(baoming);

        return R.ok("邀请成功");
    }

}
