package com.lucky.ssyx.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.ssyx.model.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lucky
 * @date 2023/9/23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
