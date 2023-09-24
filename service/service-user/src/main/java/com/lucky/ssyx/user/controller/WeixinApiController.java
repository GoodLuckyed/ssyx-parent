package com.lucky.ssyx.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.lucky.ssyx.common.Auth.AuthContextHolder;
import com.lucky.ssyx.common.constant.RedisConst;
import com.lucky.ssyx.common.exception.SsyxException;
import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.common.result.ResultCodeEnum;
import com.lucky.ssyx.common.utils.JwtHelper;
import com.lucky.ssyx.enums.UserType;
import com.lucky.ssyx.model.user.User;
import com.lucky.ssyx.user.service.UserService;
import com.lucky.ssyx.user.utils.ConstantPropertiesUtil;
import com.lucky.ssyx.user.utils.HttpClientUtils;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import com.lucky.ssyx.vo.user.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lucky
 * @date 2023/9/23
 */
@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("微信小程序登录")
    @GetMapping("/wxLogin/{code}")
    public Result weiXinLogin(@PathVariable String code){
        //判断小程序请求微信服务回传的code(授权临时票据)是否为空
        if (StringUtils.isEmpty(code)){
            throw new SsyxException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        //获取小程序AppId和AppSecret
        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;
        //拼接微信接口服务请求的url
        StringBuffer tempUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        String url = String.format(tempUrl.toString(),wxOpenAppId,wxOpenAppSecret,code);

        //使用HttpClientUtils工具类发送请求
        String result = null;
        try {
             result = HttpClientUtils.get(url);
        } catch (Exception e) {
            throw new SsyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //将返回的结果转换成json数据
        JSONObject resultJson = JSONObject.parseObject(result);
        //获取会话密钥session_key和用户唯一标识openid
        String session_key = resultJson.getString("session_key");
        String openid = resultJson.getString("openid");

        //判断用户是否第一次登录->查询数据库user表
        User user = userService.getUserByOpenId(openid);
        if (user == null){
            user = new User();
            user.setOpenId(openid);
            user.setNickName(openid);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }

        //根据用户id获取提货点和社区团长信息
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressVoByUserId(user.getId());

        //根据JWT生成token
        String token = JwtHelper.createToken(user.getId(), user.getNickName());

        //获取用户信息放入redis缓存
        UserLoginVo userLoginVo = userService.getUserLoginVo(user.getId());
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX+user.getId(),userLoginVo,RedisConst.USERKEY_TIMEOUT, TimeUnit.DAYS);

        //将数据封装成map返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("token",token);
        map.put("leaderAddressVo",leaderAddressVo);
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation("更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }
}
















