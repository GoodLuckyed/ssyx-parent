package com.lucky.ssyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.acl.Admin;
import com.lucky.ssyx.vo.acl.AdminQueryVo;

import java.util.Map;

/**
 * @author lucky
 * @date 2023/8/28
 */
public interface AdminService extends IService<Admin> {
    /**
     * 获取用户分页列表
     * @param pageObj
     * @param adminQueryVo
     * @return
     */
    IPage<Admin> selectPage(Page<Admin> pageObj, AdminQueryVo adminQueryVo);

}
