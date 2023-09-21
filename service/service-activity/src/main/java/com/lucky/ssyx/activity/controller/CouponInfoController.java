package com.lucky.ssyx.activity.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.activity.service.CouponInfoService;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.activity.CouponInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2023-09-18
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
@CrossOrigin
public class CouponInfoController {

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("获取优惠卷信息分页列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                              @PathVariable Long page,
                              @ApiParam(name = "size", value = "每页记录数", required = true)
                              @PathVariable Long size){
        //创建分页对象,传入当前页和每页录数
        Page<CouponInfo> pageObj = new Page<>(page, size);
        //调用service层进行查询
        IPage<CouponInfo> couponInfoIPage = couponInfoService.selectPage(pageObj);
        return Result.ok(couponInfoIPage);
    }

    @ApiOperation("根据id获取优惠券信息")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        CouponInfo couponInfo = couponInfoService.getById(id);
        return Result.ok(couponInfo);
    }

    @ApiOperation("新增优惠卷活动")
    @PostMapping("/save")
    public Result save(@RequestBody CouponInfo couponInfo){
        couponInfoService.save(couponInfo);
        return Result.ok(null);
    }

    @ApiOperation("修改优惠卷活动")
    @PutMapping("update")
    public Result updateById(@RequestBody CouponInfo couponInfo){
        couponInfoService.updateById(couponInfo);
        return Result.ok(couponInfo);
    }

    @ApiOperation("删除优惠卷活动")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        couponInfoService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation("根据id列表删除优惠券活动")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        couponInfoService.removeByIds(idList);
        return Result.ok(null);
    }
}
















