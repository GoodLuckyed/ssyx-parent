package com.lucky.ssyx.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.model.sys.Ware;
import com.lucky.ssyx.sys.service.WareService;
import com.lucky.ssyx.sys.mapper.WareMapper;
import org.springframework.stereotype.Service;

/**
* @author lucky
* @description 针对表【ware(仓库表)】的数据库操作Service实现
* @createDate 2023-08-31 14:36:54
*/
@Service
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware>
    implements WareService{

}




