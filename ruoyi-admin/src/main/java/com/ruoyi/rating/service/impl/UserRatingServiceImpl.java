package com.ruoyi.rating.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rating.mapper.UserRatingMapper;
import com.ruoyi.rating.domain.UserRating;
import com.ruoyi.rating.service.IUserRatingService;

/**
 * 评分Service业务层处理
 * 
 * @author huanli
 * @date 2025-02-18
 */
@Service
public class UserRatingServiceImpl implements IUserRatingService 
{
    @Autowired
    private UserRatingMapper userRatingMapper;

    /**
     * 查询评分
     * 
     * @param userId 评分主键
     * @return 评分
     */
    @Override
    public UserRating selectUserRatingByUserId(Long userId)
    {
        return userRatingMapper.selectUserRatingByUserId(userId);
    }

    /**
     * 查询评分列表
     * 
     * @param userRating 评分
     * @return 评分
     */
    @Override
    public List<UserRating> selectUserRatingList(UserRating userRating)
    {
        return userRatingMapper.selectUserRatingList(userRating);
    }

    /**
     * 新增评分
     * 
     * @param userRating 评分
     * @return 结果
     */
    @Override
    public int insertUserRating(UserRating userRating)
    {
        return userRatingMapper.insertUserRating(userRating);
    }

    /**
     * 修改评分
     * 
     * @param userRating 评分
     * @return 结果
     */
    @Override
    public int updateUserRating(UserRating userRating)
    {
        return userRatingMapper.updateUserRating(userRating);
    }

    /**
     * 批量删除评分
     * 
     * @param userIds 需要删除的评分主键
     * @return 结果
     */
    @Override
    public int deleteUserRatingByUserIds(Long[] userIds)
    {
        return userRatingMapper.deleteUserRatingByUserIds(userIds);
    }

    /**
     * 删除评分信息
     * 
     * @param userId 评分主键
     * @return 结果
     */
    @Override
    public int deleteUserRatingByUserId(Long userId)
    {
        return userRatingMapper.deleteUserRatingByUserId(userId);
    }
}
