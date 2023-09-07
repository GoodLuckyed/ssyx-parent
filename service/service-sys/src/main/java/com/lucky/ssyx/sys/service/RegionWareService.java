package com.lucky.ssyx.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lucky.ssyx.model.sys.RegionWare;
import com.lucky.ssyx.vo.sys.RegionWareQueryVo;

/**
* @author lucky
* @description 针对表【region_ware(城市仓库关联表)】的数据库操作Service
* @createDate 2023-08-31 14:36:36
*/
public interface RegionWareService extends IService<RegionWare> {

    /**
     * 获取开通区域列表
     * @param pageObj
     * @param regionWareQueryVo
     */
    IPage<RegionWare> selectPage(Page<RegionWare> pageObj, RegionWareQueryVo regionWareQueryVo);

    /**
     * 新增开通区域
     * @param regionWare
     */
    void saveRegionWare(RegionWare regionWare);

    /**
     * 取消开通区域
     * @param id
     * @param status
     */
    void cancelRegionWare(Long id, Integer status);
}
