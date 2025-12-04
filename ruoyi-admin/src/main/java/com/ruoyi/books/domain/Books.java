package com.ruoyi.books.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 书籍信息对象 books_of_the_decade
 * 
 * @author huanli
 * @date 2025-01-16
 */
public class Books extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 书籍编号 */
    @Excel(name = "书籍编号")
    private Long bookIndex;

    /** 书籍名称 */
    @Excel(name = "书籍名称")
    private String bookName;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 评分 */
    @Excel(name = "评分")
    private Double rating;

    /** 投票数 */
    @Excel(name = "投票数")
    private Long numberOfVotes;

    /** 综合得分 */
    @Excel(name = "综合得分")
    private Long score;

    /** 我的评分 */
    @Excel(name ="我的评分")
    private Long myRating;

//    /** 评论 */
//    @Excel(name = "评论")
//    private String comment;
//
//    public Long getMyRating() {
//        return myRating;
//    }
//
//    public void setMyRating(Long myRating) {
//        this.myRating = myRating;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }

    public void setBookIndex(Long bookIndex)
    {
        this.bookIndex = bookIndex;
    }

    public Long getBookIndex() 
    {
        return bookIndex;
    }
    public void setBookName(String bookName) 
    {
        this.bookName = bookName;
    }

    public String getBookName() 
    {
        return bookName;
    }
    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getAuthor() 
    {
        return author;
    }
    public void setRating(Double rating) 
    {
        this.rating = rating;
    }

    public Double getRating() 
    {
        return rating;
    }
    public void setNumberOfVotes(Long numberOfVotes) 
    {
        this.numberOfVotes = numberOfVotes;
    }

    public Long getNumberOfVotes() 
    {
        return numberOfVotes;
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
            .append("bookIndex", getBookIndex())
            .append("bookName", getBookName())
            .append("author", getAuthor())
            .append("rating", getRating())
            .append("numberOfVotes", getNumberOfVotes())
            .append("score", getScore())

            .toString();
    }
}
