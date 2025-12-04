package com.ruoyi.books.service;

import java.util.List;
import com.ruoyi.books.domain.Recommendation;

/**
 * 个性化推荐Service接口
 * 
 * @author huanli
 * @date 2025-02-21
 */
public interface IRecommendationService 
{


    /**
     * 查询个性化推荐列表
     * 
     * @param userId 个性化推荐
     * @return 个性化推荐集合
     */
    public List<Recommendation> selectRecommendationList(Long userId);


}
