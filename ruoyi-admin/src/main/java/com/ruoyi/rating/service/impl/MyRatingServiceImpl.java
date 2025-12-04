package com.ruoyi.rating.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rating.mapper.MyRatingMapper;
import com.ruoyi.rating.domain.MyRating;
import com.ruoyi.rating.service.IMyRatingService;

/**
 * 我的评分Service业务层处理
 * 
 * @author huanli
 * @date 2025-02-22
 */
@Service
public class MyRatingServiceImpl implements IMyRatingService 
{
    @Autowired
    private MyRatingMapper myRatingMapper;



    /**
     * 查询我的评分列表
     * 
     * @param userId 我的评分
     * @return 我的评分
     */
    @Override
    public List<MyRating> selectMyRatingList(Long userId)
    {
        return myRatingMapper.selectMyRatingList(userId);
    }

    /**
     * 新增我的评分
     * 
     * @param myRating 我的评分
     * @return 结果
     */
    @Override
    public int insertMyRating(MyRating myRating)
    {
        return myRatingMapper.insertMyRating(myRating);
    }


    /**
     * 删除我的评分信息
     * 
     * @param userId 我的评分主键
     * @return 结果
     */
    @Override
    public int deleteMyRatingByUserId(Long userId, Long bookIndex)
    {
        return myRatingMapper.deleteMyRatingByUserId(userId,bookIndex);
    }
}
