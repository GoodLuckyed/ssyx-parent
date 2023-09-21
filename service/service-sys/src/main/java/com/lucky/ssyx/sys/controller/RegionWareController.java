package com.lucky.ssyx.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.sys.RegionWare;
import com.lucky.ssyx.sys.service.RegionWareService;
import com.lucky.ssyx.vo.sys.RegionWareQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lucky
 * @date 2023/8/31
 */
@Api(tags = "开通区域接口")
@RestController
@RequestMapping(value="/admin/sys/regionWare")
public class RegionWareController {

    @Autowired
    private RegionWareService regionWareService;

    @ApiOperation("获取开通区域列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                                  @PathVariable Long page,
                              @ApiParam(name = "size", value = "每页记录数", required = true)
                                  @PathVariable Long size,
                              @ApiParam(name = "regionWareQueryVo", value = "查询对象", required = false)
                              RegionWareQueryVo regionWareQueryVo){
        //创建分页对象,传入当前页和每页条数
        Page<RegionWare> pageObj = new Page<>(page,size);
        //调用service层查询
        IPage<RegionWare> regionWareIPage = regionWareService.selectPage(pageObj, regionWareQueryVo);
        return Result.ok(regionWareIPage);
    }

    @ApiOperation("新增开通区域")
    @PostMapping("/save")
    public Result save(@RequestBody RegionWare regionWare){
        regionWareService.saveRegionWare(regionWare);
        return Result.ok(null);
    }

    @ApiOperation("删除开通区域")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean b = regionWareService.removeById(id);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("取消开通区域")
    @PostMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status){
        regionWareService.cancelRegionWare(id,status);
        return Result.ok(null);
    }
}
