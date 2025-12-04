package com.ruoyi.rating.mapper;

import java.util.List;
import com.ruoyi.rating.domain.MyRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 我的评分Mapper接口
 * 
 * @author huanli
 * @date 2025-02-22
 */
@Mapper
public interface MyRatingMapper 
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
     * 删除我的评分
     * 
     * @param userId 我的评分主键
     * @return 结果
     */
    public int deleteMyRatingByUserId(@Param("userId") Long userId,   // 明确参数名称为userId
                                      @Param("bookIndex") Long bookIndex  // 明确参数名称为bookIndex
    );


}
