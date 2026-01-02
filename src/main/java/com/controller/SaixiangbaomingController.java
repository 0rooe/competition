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

import com.service.SaixiangbaomingService;
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

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SaixiangbaomingEntity saixiangbaoming,
            HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("xuesheng")) {
            saixiangbaoming.setXuehao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<SaixiangbaomingEntity> ew = new EntityWrapper<SaixiangbaomingEntity>();
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
        saixiangbaomingService.insert(saixiangbaoming);
        return R.ok();
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
                mapList.add(map);
            }
        }
        // Update headers and columns
        String[] headers = { "赛项名称", "类型", "级别", "报名费用", "报名日期", "学号", "姓名", "审核状态", "审核回复", "是否支付" };
        String[] columns = { "saixiangmingcheng", "leixing", "jibie", "baomingfeiyong", "baomingriqi", "xuehao",
                "xingming", "sfsh", "shhf", "ispay" };
        com.utils.ExcelUtils.export(response, mapList, headers, columns, "赛项报名列表");
    }

}
