package com.ruoyi.rating.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 我的评分对象 user_reviews_dataset
 * 
 * @author huanli
 * @date 2025-02-22
 */
public class MyRating extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户编号 */
    private Long userId;

    /** 书籍编号 */
    private Long bookIndex;

    /** 我的评分 */
    @Excel(name = "评分")
    private Long score;

    private String bookName;

    private String author;

    //评分
    private Double rating;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
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
            .append("bookName", getBookName())
            .append("author", getAuthor())
            .append("rating", getRating())
            .toString();
    }
}
