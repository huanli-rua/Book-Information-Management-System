package com.ruoyi.rating.mapper;

import java.util.List;
import com.ruoyi.rating.domain.UserRating;

/**
 * 评分Mapper接口
 * 
 * @author huanli
 * @date 2025-02-18
 */
public interface UserRatingMapper 
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
     * 删除评分
     * 
     * @param userId 评分主键
     * @return 结果
     */
    public int deleteUserRatingByUserId(Long userId);

    /**
     * 批量删除评分
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRatingByUserIds(Long[] userIds);
}
