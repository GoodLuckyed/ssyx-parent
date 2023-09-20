package com.lucky.ssyx.activity.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.service.ActivityInfoService;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.activity.ActivityInfo;
import com.lucky.ssyx.model.activity.ActivityRule;
import com.lucky.ssyx.model.product.SkuInfo;
import com.lucky.ssyx.vo.activity.ActivityRuleVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
@RestController
@RequestMapping("/admin/activity/activityInfo")
@CrossOrigin
public class ActivityInfoController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation("获取营销活动信息分页列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                                  @PathVariable Long page,
                              @ApiParam(name = "size", value = "每页记录数", required = true)
                                  @PathVariable Long size){
        //创建分页对象,传入当前页和每页录数
        Page<ActivityInfo> pageObj = new Page<>(page, size);
        //调用service层进行查询
        IPage<ActivityInfo> activityInfoIPage = activityInfoService.selectPage(pageObj);
        return Result.ok(activityInfoIPage);
    }

    @ApiOperation("根据id获取营销活动信息")
    @GetMapping("/get/{activityId}")
    public Result get(@PathVariable Long activityId){
        ActivityInfo activityInfo = activityInfoService.getById(activityId);
        activityInfo.setActivityTypeString(activityInfo.getActivityType().getComment());
        return Result.ok(activityInfo);
    }

    @ApiOperation("新增营销活动")
    @PostMapping("/save")
    public Result save(@RequestBody ActivityInfo activityInfo){
        activityInfoService.save(activityInfo);
        return Result.ok(null);
    }


    @ApiOperation("修改营销活动")
    @PutMapping("/update")
    public Result updateById(@RequestBody ActivityInfo activityInfo) {
        activityInfoService.updateById(activityInfo);
        return Result.ok(null);
    }

    @ApiOperation("删除营销活动")
    @DeleteMapping("/remove/{activityId}")
    public Result remove(@PathVariable Long activityId) {
        activityInfoService.removeById(activityId);
        return Result.ok(null);
    }

    @ApiOperation("根据id列表删除营销活动")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        activityInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation("根据营销活动id查询营销规则数据")
    @GetMapping("/findActivityRuleList/{id}")
    public Result findActivityRuleList(@PathVariable Long id){
        Map<String, Object> activityRuleMap = activityInfoService.findActivityRuleList(id);
        return Result.ok(activityRuleMap);
    }

    @ApiOperation("新增营销活动规则")
    @PostMapping("/saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo){
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok(null);
    }

    @ApiOperation("根据关键字获取sku列表")
    @GetMapping("/findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable String keyword){
        List<SkuInfo> skuInfoList = activityInfoService.findSkuInfoByKeyword(keyword);
        return Result.ok(skuInfoList);
    }
}























