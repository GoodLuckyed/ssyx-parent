package com.lucky.ssyx.user;

import com.lucky.ssyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lucky
 * @date 2023/9/24
 */
@FeignClient(value = "service-user")
public interface UserFeginClient {

    /**
     * 获取提货点地址信息
     * @param userId
     * @return
     */
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable Long userId);
}