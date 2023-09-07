package com.lucky.ssyx.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.acl.Permission;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/30
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取权限菜单
     * @return
     */
    List<Permission> queryAllPermission();

    /**
     * 递归删除权限菜单
     * @param id
     * @return
     */
    boolean removeChildById(Long id);

}
