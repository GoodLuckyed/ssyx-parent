package com.lucky.ssyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.acl.Role;
import com.lucky.ssyx.vo.acl.RoleQueryVo;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/28
 */
public interface RoleService extends IService<Role> {
    /**
     * 获取角色分页列表
     * @param pageObj
     * @param roleQueryVo
     * @return
     */
    IPage<Role> selectPage(Page<Role> pageObj, RoleQueryVo roleQueryVo);

    /**
     * 根据用户获取角色数据
     * @param adminId
     * @return
     */
    Map<String,Object> findRoleByUserId(Long adminId);

    /**
     * 根据用户分配角色
     * @param adminId
     * @param roleId
     */
    void saveAdminRole(Long adminId,Long[] roleId);
}
