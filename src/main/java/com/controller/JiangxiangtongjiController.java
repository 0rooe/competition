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

import com.entity.JiangxiangtongjiEntity;
import com.entity.view.JiangxiangtongjiView;

import com.service.JiangxiangtongjiService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 奖项统计
 * 后端接口
 * 
 * @author
 * @email
 * @date 2021-03-26 17:35:20
 */
@RestController
@RequestMapping("/jiangxiangtongji")
public class JiangxiangtongjiController {
    @Autowired
    private JiangxiangtongjiService jiangxiangtongjiService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, JiangxiangtongjiEntity jiangxiangtongji,
            HttpServletRequest request) {
        EntityWrapper<JiangxiangtongjiEntity> ew = new EntityWrapper<JiangxiangtongjiEntity>();
        PageUtils page = jiangxiangtongjiService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiangxiangtongji), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, JiangxiangtongjiEntity jiangxiangtongji,
            HttpServletRequest request) {
        EntityWrapper<JiangxiangtongjiEntity> ew = new EntityWrapper<JiangxiangtongjiEntity>();
        PageUtils page = jiangxiangtongjiService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiangxiangtongji), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(JiangxiangtongjiEntity jiangxiangtongji) {
        EntityWrapper<JiangxiangtongjiEntity> ew = new EntityWrapper<JiangxiangtongjiEntity>();
        ew.allEq(MPUtil.allEQMapPre(jiangxiangtongji, "jiangxiangtongji"));
        return R.ok().put("data", jiangxiangtongjiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiangxiangtongjiEntity jiangxiangtongji) {
        EntityWrapper<JiangxiangtongjiEntity> ew = new EntityWrapper<JiangxiangtongjiEntity>();
        ew.allEq(MPUtil.allEQMapPre(jiangxiangtongji, "jiangxiangtongji"));
        JiangxiangtongjiView jiangxiangtongjiView = jiangxiangtongjiService.selectView(ew);
        return R.ok("查询奖项统计成功").put("data", jiangxiangtongjiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        JiangxiangtongjiEntity jiangxiangtongji = jiangxiangtongjiService.selectById(id);
        return R.ok().put("data", jiangxiangtongji);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        JiangxiangtongjiEntity jiangxiangtongji = jiangxiangtongjiService.selectById(id);
        return R.ok().put("data", jiangxiangtongji);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JiangxiangtongjiEntity jiangxiangtongji, HttpServletRequest request) {
        jiangxiangtongji.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(jiangxiangtongji);
        jiangxiangtongjiService.insert(jiangxiangtongji);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JiangxiangtongjiEntity jiangxiangtongji, HttpServletRequest request) {
        jiangxiangtongji.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(jiangxiangtongji);
        jiangxiangtongjiService.insert(jiangxiangtongji);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JiangxiangtongjiEntity jiangxiangtongji, HttpServletRequest request) {
        // ValidatorUtils.validateEntity(jiangxiangtongji);
        jiangxiangtongjiService.updateById(jiangxiangtongji);// 全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        jiangxiangtongjiService.deleteBatchIds(Arrays.asList(ids));
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

        Wrapper<JiangxiangtongjiEntity> wrapper = new EntityWrapper<JiangxiangtongjiEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }

        int count = jiangxiangtongjiService.selectCount(wrapper);
        return R.ok().put("count", count);
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityWrapper<JiangxiangtongjiEntity> ew = new EntityWrapper<JiangxiangtongjiEntity>();
        List<JiangxiangtongjiEntity> list = jiangxiangtongjiService.selectList(ew);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (JiangxiangtongjiEntity entity : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("yuanxiaomingcheng", entity.getYuanxiaomingcheng());
            map.put("jiangxiangmingcheng", entity.getJiangxiangmingcheng());
            map.put("leixing", entity.getLeixing());
            map.put("zongshu", entity.getZongshu());
            resultList.add(map);
        }

        String[] headers = { "院校名称", "比赛名称", "奖项等级", "数量" };
        String[] columns = { "yuanxiaomingcheng", "jiangxiangmingcheng", "leixing", "zongshu" };

        com.utils.ExcelUtils.export(response, resultList, headers, columns, "奖项统计列表");
    }

}
