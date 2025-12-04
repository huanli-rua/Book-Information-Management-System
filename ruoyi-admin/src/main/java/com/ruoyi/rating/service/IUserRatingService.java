package com.ruoyi.rating.service;

import java.util.List;
import com.ruoyi.rating.domain.UserRating;

/**
 * 评分Service接口
 * 
 * @author huanli
 * @date 2025-02-18
 */
public interface IUserRatingService 
{
    /**
     * 查询评分
     * 
     * @param userId 评分主键
     * @return 评分
     */
    public UserRating selectUserRatingByUserId(Long userId);

    /**
     * 查询评分列表
     * 
     * @param userRating 评分
     * @return 评分集合
     */
    public List<UserRating> selectUserRatingList(UserRating userRating);

    /**
     * 新增评分
     * 
     * @param userRating 评分
     * @return 结果
     */
    public int insertUserRating(UserRating userRating);

    /**
     * 修改评分
     * 
     * @param userRating 评分
     * @return 结果
     */
    public int updateUserRating(UserRating userRating);

    /**
     * 批量删除评分
     * 
     * @param userIds 需要删除的评分主键集合
     * @return 结果
     */
    public int deleteUserRatingByUserIds(Long[] userIds);

    /**
     * 删除评分信息
     * 
     * @param userId 评分主键
     * @return 结果
     */
    public int deleteUserRatingByUserId(Long userId);
}
