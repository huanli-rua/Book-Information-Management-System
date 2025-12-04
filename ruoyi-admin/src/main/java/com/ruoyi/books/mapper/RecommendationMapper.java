package com.ruoyi.books.mapper;

import java.util.List;
import com.ruoyi.books.domain.Recommendation;

/**
 * 个性化推荐Mapper接口
 * 
 * @author huanli
 * @date 2025-02-21
 */
public interface RecommendationMapper 
{


    /**
     * 查询个性化推荐列表
     * 
     * @param userId 个性化推荐的用户ID
     * @return 个性化推荐集合，返回一个包含个性化推荐对象的列表
     */
    public List<Recommendation> selectRecommendationList(Long userId);


}
