package com.lucky.ssyx.common.Auth;

import com.lucky.ssyx.common.constant.RedisConst;
import com.lucky.ssyx.common.utils.JwtHelper;
import com.lucky.ssyx.vo.user.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lucky
 * @date 2023/9/24
 */

/**
 * 小程序登录拦截器
 */
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)){
            //使用JWT工具类获取用户的id
            Long userId = JwtHelper.getUserId(token);
            //根据userId从redis里获取用户信息
            UserLoginVo userLoginVo = (UserLoginVo) redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + userId);
            if (userLoginVo != null){
                //将用户信息放入ThreadLocal中
                AuthContextHolder.setUserId(userLoginVo.getUserId());
                AuthContextHolder.setWareId(userLoginVo.getWareId());
                AuthContextHolder.setUserLoginVo(userLoginVo);
            }
        }
        return true;
    }
}




