package com.ruoyi.books.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.books.mapper.RecommendationMapper;
import com.ruoyi.books.domain.Recommendation;
import com.ruoyi.books.service.IRecommendationService;

/**
 * 个性化推荐Service业务层处理
 * 
 * @author huanli
 * @date 2025-02-21
 */
@Service
public class RecommendationServiceImpl implements IRecommendationService 
{
    @Autowired
    private RecommendationMapper recommendationMapper;



    /**
     * 查询个性化推荐列表
     * 
     * @param userId 个性化推荐
     * @return 个性化推荐
     */
    @Override
    public List<Recommendation> selectRecommendationList(Long  userId)
    {
        return recommendationMapper.selectRecommendationList(userId);
    }








}
