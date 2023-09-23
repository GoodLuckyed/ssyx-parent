package com.lucky.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.acl.mapper.RolePermissionMapper;
import com.lucky.ssyx.acl.service.PermissionService;
import com.lucky.ssyx.acl.service.RolePermissionService;
import com.lucky.ssyx.model.acl.Permission;
import com.lucky.ssyx.model.acl.RolePermission;
import com.lucky.ssyx.user.utils.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lucky
 * @date 2023/8/30
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 查看某个角色的权限列表
     *
     * @param roleId
     */
    @Override
    public Map<String, Object> assignByRoleId(Long roleId) {
        //查询所有的权限菜单列表
        List<Permission> allPermissionList = permissionService.list();
        List<Permission> buildPermissionList = PermissionHelper.buildPermission(allPermissionList);
        //查询某个角色的权限列表
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(wrapper);

        //从上面的集合中取出权限id
        List<Long> permissionIdList = rolePermissionList.stream().
                map(item -> item.getPermissionId())
                .collect(Collectors.toList());

        //封装角色已分配的权限菜单
        for (Permission permission : buildPermissionList) {
            if (permission.getPid() == 0) {
                if (permissionIdList.contains(permission.getId())) {
                    permission.setSelect(true);
                }
                this.setPermissionChildren(permission.getChildren(), permissionIdList);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("allPermissions", buildPermissionList);
        return map;
    }

    /**
     * 递归设置select的值
     *
     * @param childrenList
     * @param permissionIdList
     */
    private void setPermissionChildren(List<Permission> childrenList, List<Long> permissionIdList) {
        for (Permission permission : childrenList) {
            if (permissionIdList.contains(permission.getId())) {
                permission.setSelect(true);
            }
            this.setPermissionChildren(permission.getChildren(), permissionIdList);
        }
    }

    /**
     * 为角色分配权限菜单
     *
     * @param roleId
     * @param permissionId
     */
    @Override
    public void doAssign(Long roleId, Long[] permissionId) {
        //删除已为角色分配的权限菜单
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);

        List<RolePermission> list = new ArrayList<>();
        for (Long id : permissionId) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(id);
            list.add(rolePermission);
        }
        this.saveBatch(list);
    }
}
