package com.lucky.ssyx.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.ssyx.acl.service.AdminService;
import com.lucky.ssyx.acl.service.RoleService;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.common.utils.MD5;
import com.lucky.ssyx.model.acl.Admin;
import com.lucky.ssyx.vo.acl.AdminQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/28
 */
@RestController
@RequestMapping("/admin/acl/user")
@Api(tags = "用户接口")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;

    @ApiOperation("获取用户分页列表")
    @GetMapping("/{page}/{size}")
    public Result getPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                                   @PathVariable Long page,
                               @ApiParam(name = "size", value = "每页记录数", required = true)
                                   @PathVariable Long size,
                               @ApiParam(name = "userQueryVo", value = "查询对象", required = false)
                                       AdminQueryVo adminQueryVo){
        //创建分页对象,传入当前页和每页录数
        Page<Admin> pageObj = new Page<>(page,size);
        //调用service层进行查询
        IPage<Admin> adminIPage = adminService.selectPage(pageObj, adminQueryVo);
        return Result.ok(adminIPage);
    }

    @ApiOperation("根据id获取用户信息")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        if (!StringUtils.isEmpty(admin)){
            return Result.ok(admin);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("保存用户信息")
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin){
        // 对密码进行加密
        String password = admin.getPassword();
        String newPassword = MD5.encrypt(password);
        admin.setPassword(newPassword);
        boolean b = adminService.save(admin);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin){
        boolean b = adminService.updateById(admin);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("根据id删除用户")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean b = adminService.removeById(id);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("批量删除多个用户")
    @DeleteMapping("/batchRemove")
    public Result removeRoles(@RequestBody List<Long> ids){
        boolean b = adminService.removeByIds(ids);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{adminId}")
    public Result getRoles(@PathVariable Long adminId){
        Map<String, Object> map = roleService.findRoleByUserId(adminId);
        return Result.ok(map);
    }

    @ApiOperation("根据用户分配角色")
    @PostMapping("/doAssign")
    public Result assignRole(@RequestParam Long adminId,@RequestParam Long[] roleId){
        roleService.saveAdminRole(adminId,roleId);
        return Result.ok(null);
    }
}
