package com.lucky.ssyx.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.acl.RolePermission;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/30
 */
public interface RolePermissionService extends IService<RolePermission> {
    /**
     * 查看某个角色的权限列表
     * @param roleId
     */
    Map<String,Object> assignByRoleId(Long roleId);

    /**
     * 为角色分配权限菜单
     * @param roleId
     * @param permissionId
     */
    void doAssign(Long roleId, Long[] permissionId);
}
