package com.lucky.ssyx.user.api;

import com.lucky.ssyx.model.user.User;
import com.lucky.ssyx.user.service.UserService;
import com.lucky.ssyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lucky
 * @date 2023/9/24
 */
@RestController
@RequestMapping("/api/user/leader")
public class LeaderApiController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取提货点地址信息")
    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable Long userId){
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressVoByUserId(userId);
        return leaderAddressVo;
    }
}
