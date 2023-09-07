package com.lucky.ssyx.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lucky.ssyx.model.sys.Region;
import com.lucky.ssyx.sys.service.RegionService;
import com.lucky.ssyx.sys.mapper.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lucky
* @description 针对表【region(地区表)】的数据库操作Service实现
* @createDate 2023-08-31 14:33:27
*/
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService{

    @Autowired
    private RegionMapper regionMapper;

    /**
     * 根据关键字获取区域列表
     * @param keyword
     * @return
     */
    @Override
    public List<Region> findRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Region::getName,keyword);
        List<Region> regionList = regionMapper.selectList(wrapper);
        return regionList;
    }
}




