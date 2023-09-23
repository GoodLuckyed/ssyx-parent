package com.lucky.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.acl.mapper.PermissionMapper;
import com.lucky.ssyx.acl.service.PermissionService;
import com.lucky.ssyx.model.acl.Permission;
import com.lucky.ssyx.user.utils.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @date 2023/8/30
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;


    /**
     * 获取权限菜单
     * @return
     */
    @Override
    public List<Permission> queryAllPermission() {
        //查询所有权限菜单
        List<Permission> allPermissionList = permissionMapper.selectList(null);

        //把权限菜单数据构建成树形结构数据
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    /**
     * 递归删除权限菜单
     * @param id
     * @return
     */
    @Override
    public boolean removeChildById(Long id) {
        ArrayList<Long> idList = new ArrayList<>();
        this.selectChildListById(id, idList);
        idList.add(id);
        permissionMapper.deleteBatchIds(idList);
        return true;
    }

    /**
     * 递归获取子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(Long id, ArrayList<Long> idList) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid,id);
        List<Permission> childrenPermissionList = permissionMapper.selectList(wrapper);

        childrenPermissionList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }
}







