package com.ruoyi.rating.service;

import java.util.List;
import com.ruoyi.rating.domain.MyRating;

/**
 * 我的评分Service接口
 * 
 * @author huanli
 * @date 2025-02-22
 */
public interface IMyRatingService 
{


    /**
     * 查询我的评分列表
     * 
     * @param userId 我的评分
     * @return 我的评分集合
     */
    public List<MyRating> selectMyRatingList(Long userId);

    /**
     * 新增我的评分
     *
     * @param myRating 我的评分
     * @return 结果
     */
    public int insertMyRating(MyRating myRating);



    /**
     * 删除我的评分信息
     * 
     * @param userId 我的评分主键
     * @return 结果
     */
    public int deleteMyRatingByUserId(Long userId,Long bookIndex);
}
