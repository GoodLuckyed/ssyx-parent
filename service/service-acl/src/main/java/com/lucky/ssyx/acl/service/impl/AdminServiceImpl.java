package com.lucky.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.acl.mapper.AdminMapper;
import com.lucky.ssyx.acl.service.AdminService;
import com.lucky.ssyx.model.acl.Admin;
import com.lucky.ssyx.vo.acl.AdminQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 获取用户分页列表
     * @param pageObj
     * @param adminQueryVo
     * @return
     */
    @Override
    public IPage<Admin> selectPage(Page<Admin> pageObj, AdminQueryVo adminQueryVo) {
        String username = adminQueryVo.getUsername();
        String name = adminQueryVo.getName();
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(username),Admin::getUsername,username);
        wrapper.eq(!StringUtils.isEmpty(name),Admin::getName,name);
        Page<Admin> adminPage = adminMapper.selectPage(pageObj, wrapper);
        return adminPage;
    }
}
