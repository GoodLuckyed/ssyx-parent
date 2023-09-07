package com.lucky.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.acl.mapper.RoleMapper;
import com.lucky.ssyx.acl.service.AdminRoleService;
import com.lucky.ssyx.model.acl.AdminRole;
import com.lucky.ssyx.model.acl.Role;
import com.lucky.ssyx.acl.service.RoleService;
import com.lucky.ssyx.vo.acl.RoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lucky
 * @date 2023/8/28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleService adminRoleService;

    /**'
     * 获取角色分页列表
     * @param pageObj
     * @param roleQueryVo
     * @return
     */
    @Override
    public IPage<Role> selectPage(Page<Role> pageObj, RoleQueryVo roleQueryVo) {
        String roleName = roleQueryVo.getRoleName();
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(roleName)){
            wrapper.like(Role::getRoleName,roleName);
        }
        Page<Role> rolePage = roleMapper.selectPage(pageObj, wrapper);
        return rolePage;
    }

    /**
     * 根据用户获取角色数据
     * @param adminId
     * @return
     */
    @Override
    public Map<String, Object> findRoleByUserId(Long adminId) {
        //获取所有的角色
        List<Role> roleList = roleMapper.selectList(null);

        //根据用户的id查询用户拥有的角色
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId,adminId);
        List<AdminRole> adminRoles = adminRoleService.list(wrapper);

        //从上面返回的集合中取出角色id
        List<Long> adminRoleList = adminRoles.stream()
                .map(item -> item.getRoleId())
                .collect(Collectors.toList());

        // 创建list集合 用户储存用户角色
        List<Role> assignRoleList = new ArrayList<>();

        //遍历所有角色列表 判断是否包含已分配的角色id
        for (Role role : roleList) {
            if (adminRoleList.contains(role.getId())){
                assignRoleList.add(role);
            }
        }
        //将角色列表和用户分配的角色封装成map返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("allRolesList",roleList);
        map.put("assignRoles",assignRoleList);
        return map;
    }

    /**
     * 根据用户分配角色
     * @param adminId
     * @param roleId
     */
    @Override
    public void saveAdminRole(Long adminId, Long[] roleId) {
        //先删除用户的角色信息
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId,adminId);
        adminRoleService.remove(wrapper);

        List<AdminRole> list = new ArrayList<>();
        //为用户添加角色
        for (Long rid : roleId) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(rid);
            list.add(adminRole);
        }
        //保存到用户角色表
        adminRoleService.saveBatch(list);
    }
}
