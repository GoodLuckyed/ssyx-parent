package com.lucky.ssyx.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.user.User;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import com.lucky.ssyx.vo.user.UserLoginVo;

/**
 * @author lucky
 * @date 2023/9/23
 */
public interface UserService extends IService<User> {

    /**
     * 通过openId获取用户信息
     * @param openid
     */
    User getUserByOpenId(String openid);

    /**
     * 根据用户id获取提货点和社区团长信息
     * @param userId
     * @return
     */
    LeaderAddressVo getLeaderAddressVoByUserId(Long userId);

    /**
     * 获取登录的用户信息
     * @param userId
     * @return
     */
    UserLoginVo getUserLoginVo(Long userId);
}
