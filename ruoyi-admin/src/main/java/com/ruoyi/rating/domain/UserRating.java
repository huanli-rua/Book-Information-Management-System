package com.ruoyi.rating.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评分对象 user_reviews_dataset
 * 
 * @author huanli
 * @date 2025-02-18
 */
public class UserRating extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户编号 */
    private Long userId;

    /** 书籍编号 */
    private Long bookIndex;

    /** 评分 */
    @Excel(name = "评分")
    private Long score;

    /** 评论 */
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setBookIndex(Long bookIndex) 
    {
        this.bookIndex = bookIndex;
    }

    public Long getBookIndex() 
    {
        return bookIndex;
    }
    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("bookIndex", getBookIndex())
            .append("score", getScore())
            .append("comment", getComment())
            .toString();
    }
}
