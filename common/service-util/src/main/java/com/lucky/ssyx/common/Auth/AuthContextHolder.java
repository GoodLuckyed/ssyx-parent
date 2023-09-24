package com.lucky.ssyx.common.Auth;

/**
 * @author lucky
 * @date 2023/9/24
 */

import com.lucky.ssyx.vo.user.UserLoginVo;

/**
 * 获取登录用户信息类
 */
public class AuthContextHolder {

    //登录用户id
    private static ThreadLocal<Long> userId = new ThreadLocal<>();
    //仓库id
    private static ThreadLocal<Long> wareId = new ThreadLocal<>();
    //用户信息对象
    private static ThreadLocal<UserLoginVo> userLoginVo = new ThreadLocal<>();

    public static void setUserId(Long _userId){
        userId.set(_userId);
    }
    public static Long getUserId(){
        return userId.get();
    }
    public static void setWareId(Long _wareId){
        wareId.set(_wareId);
    }
    public static Long getWareId(){
        return wareId.get();
    }
    public static void setUserLoginVo(UserLoginVo _userLoginVo) {
        userLoginVo.set(_userLoginVo);
    }
    public static UserLoginVo getUserLoginVo(){
        return userLoginVo.get();
    }
}




