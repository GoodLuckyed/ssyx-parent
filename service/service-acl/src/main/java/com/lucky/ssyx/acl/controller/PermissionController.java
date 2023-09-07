package com.lucky.ssyx.acl.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lucky.ssyx.acl.service.PermissionService;
import com.lucky.ssyx.acl.service.RolePermissionService;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/30
 */
@RestController
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理接口")
@CrossOrigin //跨域
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @ApiOperation(value = "获取权限菜单")
    @GetMapping
    public Result queryAllPermission(){
        List<Permission> permissionList = permissionService.queryAllPermission();
        return Result.ok(permissionList);
    }

    @ApiOperation(value = "新增权限菜单")
    @PostMapping("save")
    public Result save(@RequestBody Permission permission){
        boolean b = permissionService.save(permission);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation(value = "修改权限菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody Permission permission){
        boolean b = permissionService.updateById(permission);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation(value = "递归删除权限菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = permissionService.removeChildById(id);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("查看某个角色的权限列表")
    @GetMapping("/toAssign/{roleId}")
    public Result assignByRoleId(@PathVariable Long roleId){
        Map<String, Object> map = rolePermissionService.assignByRoleId(roleId);
        return Result.ok(map.get("allPermissions"));
    }

    @ApiOperation("为角色分配权限菜单")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long roleId,@RequestParam Long[] permissionId){
        rolePermissionService.doAssign(roleId,permissionId);
        return Result.ok(null);
    }
}



