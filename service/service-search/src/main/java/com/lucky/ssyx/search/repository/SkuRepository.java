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
}
