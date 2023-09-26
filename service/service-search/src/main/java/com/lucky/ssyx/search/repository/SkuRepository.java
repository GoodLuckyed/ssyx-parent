package com.lucky.ssyx.search.repository;

import com.lucky.ssyx.model.search.SkuEs;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lucky
 * @date 2023/9/10
 */
@Mapper
public interface SkuRepository extends ElasticsearchRepository<SkuEs,Long> {

    /**
     * 获取爆款商品
     * @param pageable
     * @return
     */
    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);

    /**
     * 根据categoryId和wareId查询商品
     * @param categoryId
     * @param wareId
     * @param pageable
     * @return
     */
    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);

    /**
     * 根据keyword和wareId查询商品
     * @param keyword
     * @param wareId
     * @param pageable
     * @return
     */
    Page<SkuEs> findByKeywordAndWareId(String keyword, Long wareId, Pageable pageable);
}
