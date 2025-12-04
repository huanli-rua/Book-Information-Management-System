package com.ruoyi.books.domain;

import com.ruoyi.rating.domain.UserRating;

import java.util.List;

/**
 * @author huanli
 * @date 2025/2/21 6:46
 */
public class Detail {

    private Books books;

    private UserRating userRating;

    private List<UserRating> commentList;

    public List<UserRating> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<UserRating> commentList) {
        this.commentList = commentList;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "books=" + books +
                ", userRating=" + userRating +
                ", commentList=" + commentList +
                '}';
    }
}
