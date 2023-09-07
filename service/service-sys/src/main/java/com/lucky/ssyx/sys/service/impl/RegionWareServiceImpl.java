package com.lucky.ssyx.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.ssyx.common.exception.SsyxException;
import com.lucky.ssyx.common.result.ResultCodeEnum;
import com.lucky.ssyx.model.sys.RegionWare;
import com.lucky.ssyx.sys.service.RegionWareService;
import com.lucky.ssyx.sys.mapper.RegionWareMapper;
import com.lucky.ssyx.vo.sys.RegionWareQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author lucky
 * @description 针对表【region_ware(城市仓库关联表)】的数据库操作Service实现
 * @createDate 2023-08-31 14:36:36
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Autowired
    private RegionWareMapper regionWareMapper;

    @Override
    public IPage<RegionWare> selectPage(Page<RegionWare> pageObj, RegionWareQueryVo regionWareQueryVo) {
        //获取查询对象
        String keyword = regionWareQueryVo.getKeyword();
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        //判断查询对象是否为空
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like(RegionWare::getRegionName, keyword)
                    .or().like(RegionWare::getWareName, keyword);
        }
        //查询
        Page<RegionWare> regionWarePage = regionWareMapper.selectPage(pageObj, wrapper);
        return regionWarePage;
    }

    /**
     * 新增开通区域
     * @param regionWare
     */
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        //判断区域是否开通
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionWare::getRegionId,regionWare.getRegionId());
        Integer count = regionWareMapper.selectCount(wrapper);
        if (count > 0){
            throw new SsyxException(ResultCodeEnum.REGION_OPEN);
        }
        regionWareMapper.insert(regionWare);
    }

    /**
     * 取消开通区域
     * @param id
     * @param status
     */
    @Override
    public void cancelRegionWare(Long id, Integer status) {
        RegionWare regionWare = regionWareMapper.selectById(id);
        regionWare.setStatus(status);
        regionWareMapper.updateById(regionWare);
    }
}





