package com.lucky.ssyx.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.acl.Role;
import com.lucky.ssyx.acl.service.RoleService;
import com.lucky.ssyx.vo.acl.RoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lucky
 * @date 2023/8/28
 */
@RestController
@RequestMapping("/admin/acl/role")
@Api(tags = "角色接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("获取角色分页列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList( @ApiParam(name = "page", value = "当前页码", required = true)
                                   @PathVariable Long page,
                               @ApiParam(name = "size", value = "每页记录数", required = true)
                                   @PathVariable Long size,
                               @ApiParam(name = "roleQueryVo", value = "查询对象", required = false)
                                       RoleQueryVo roleQueryVo){
        //创建分页对象,传入当前页和每页录数
        Page<Role> pageObj = new Page<>(page,size);
        //调用service层进行查询
        IPage<Role> roleIPage = roleService.selectPage(pageObj, roleQueryVo);
        return Result.ok(roleIPage);
    }

    @ApiOperation("保存角色信息")
    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        boolean b = roleService.save(role);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("根据id获取角色信息")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Role role = roleService.getById(id);
        if (!StringUtils.isEmpty(role)) {
            return Result.ok(role);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("更新角色信息")
    @PutMapping("/update")
    public Result updateById(@RequestBody Role role){
        boolean b = roleService.updateById(role);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("根据id删除角色")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean b = roleService.removeById(id);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("批量删除多个角色")
    @DeleteMapping("/batchRemove")
    public Result removeRoles(@RequestBody List<Long> ids){
        boolean b = roleService.removeByIds(ids);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
}
