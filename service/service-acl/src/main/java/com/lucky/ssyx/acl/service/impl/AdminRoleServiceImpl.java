package com.lucky.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.acl.mapper.AdminRoleMapper;
import com.lucky.ssyx.acl.service.AdminRoleService;
import com.lucky.ssyx.model.acl.AdminRole;
import org.springframework.stereotype.Service;

/**
 * @author lucky
 * @date 2023/8/29
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {
}
