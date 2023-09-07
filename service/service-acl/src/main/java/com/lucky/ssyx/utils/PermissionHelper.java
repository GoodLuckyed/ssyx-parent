package com.lucky.ssyx.utils;

import com.lucky.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @date 2023/8/30
 */

/**
 * 把权限菜单数据构建成树形结构数据
 */
public class PermissionHelper {

    //使用递归方法建权限菜单
    public static List<Permission> buildPermission(List<Permission> allPermissionList){
        List<Permission> trees = new ArrayList<>();
        for (Permission permission : allPermissionList) {
            if (permission.getPid() == 0){
                permission.setLevel(1);
                trees.add(findChildren(permission,allPermissionList));
            }
        }
        return trees;
    }

    //递归查找子节点
    private static Permission findChildren(Permission permission,List<Permission> allPermissionList) {
        permission.setChildren(new ArrayList<Permission>());
        for (Permission it : allPermissionList) {
            if (permission.getId().longValue() == it.getPid().longValue()){
                int level = permission.getLevel() + 1;
                it.setLevel(level);
                permission.getChildren().add(findChildren(it,allPermissionList));
            }
        }
        return permission;
    }
}
