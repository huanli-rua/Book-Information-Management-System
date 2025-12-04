package com.ruoyi.books.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 个性化推荐对象 recommendation
 * 
 * @author huanli
 * @date 2025-02-21
 */
public class Recommendation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户编号 */
    private Long userId;

    /** 书籍编号 */
    @Excel(name = "书籍编号")
    private Long bookIndex;

    /** 预测分数 */
    private Double predictedScore;

    private String bookName;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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
    public void setPredictedScore(Double predictedScore) 
    {
        this.predictedScore = predictedScore;
    }

    public Double getPredictedScore() 
    {
        return predictedScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("bookIndex", getBookIndex())
            .append("predictedScore", getPredictedScore())
                .append("bookName", getBookName())
            .toString();
    }
}
