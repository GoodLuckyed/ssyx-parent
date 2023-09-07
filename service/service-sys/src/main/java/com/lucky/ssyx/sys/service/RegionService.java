package com.lucky.ssyx.sys.service;

import com.lucky.ssyx.model.sys.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lucky
* @description 针对表【region(地区表)】的数据库操作Service
* @createDate 2023-08-31 14:33:27
*/
public interface RegionService extends IService<Region> {

    /**
     * 根据关键字获取区域列表
     * @param keyword
     */
    List<Region> findRegionByKeyword(String keyword);
}
