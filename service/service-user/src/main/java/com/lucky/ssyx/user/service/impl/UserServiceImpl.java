package com.lucky.ssyx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.enums.UserType;
import com.lucky.ssyx.user.mapper.LeaderMapper;
import com.lucky.ssyx.user.mapper.UserDeliveryMapper;
import com.lucky.ssyx.user.mapper.UserMapper;
import com.lucky.ssyx.model.user.Leader;
import com.lucky.ssyx.model.user.User;
import com.lucky.ssyx.model.user.UserDelivery;
import com.lucky.ssyx.user.service.UserService;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import com.lucky.ssyx.vo.user.UserLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lucky
 * @date 2023/9/23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDeliveryMapper userDeliveryMapper;

    @Autowired
    private LeaderMapper leaderMapper;

    /**
     * 通过openId获取用户信息
     *
     * @param openid
     */
    @Override
    public User getUserByOpenId(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenId, openid);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    /**
     * 根据用户id获取提货点和社区团长信息
     *
     * @param userId
     * @return
     */
    @Override
    public LeaderAddressVo getLeaderAddressVoByUserId(Long userId) {
        LambdaQueryWrapper<UserDelivery> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDelivery::getUserId, userId);
        wrapper.eq(UserDelivery::getIsDefault, 1);
        UserDelivery userDelivery = userDeliveryMapper.selectOne(wrapper);
        if (userDelivery == null) {
            return null;
        }
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());
        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtils.copyProperties(leader, leaderAddressVo);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    /**
     * 获取登录的用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserLoginVo getUserLoginVo(Long userId) {
        User user = userMapper.selectById(userId);
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(userId);
        userLoginVo.setOpenId(user.getOpenId());
        userLoginVo.setNickName(user.getNickName());
        userLoginVo.setIsNew(user.getIsNew());
        userLoginVo.setPhotoUrl(user.getPhotoUrl());
        //获取当前社区团长id与对应的仓库id
        if (user.getUserType() == UserType.LEADER){
            LambdaQueryWrapper<UserDelivery> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserDelivery::getUserId, userId);
            wrapper.eq(UserDelivery::getIsDefault, 1);
            UserDelivery userDelivery = userDeliveryMapper.selectOne(wrapper);
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
            userLoginVo.setWareId(userDelivery.getWareId());
        }
        return userLoginVo;
    }
}














