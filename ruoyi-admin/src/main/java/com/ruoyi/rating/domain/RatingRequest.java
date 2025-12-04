package com.ruoyi.rating.domain;

/**
 * @author huanli
 * @date 2025/2/25 23:29
 */
public class RatingRequest {
    private Long userId;
    private Long bookIndex;
    private Double score;
    private String comment;

    // Getter 和 Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBookIndex() { return bookIndex; }
    public void setBookIndex(Long bookIndex) { this.bookIndex = bookIndex; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }


}
