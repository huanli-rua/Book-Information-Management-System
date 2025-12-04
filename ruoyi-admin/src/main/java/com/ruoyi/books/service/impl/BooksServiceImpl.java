package com.ruoyi.books.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ruoyi.books.domain.Detail;
import com.ruoyi.rating.domain.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.books.mapper.BooksMapper;
import com.ruoyi.books.domain.Books;
import com.ruoyi.books.service.IBooksService;

/**
 * 书籍信息Service业务层处理
 * 
 * @author huanli
 * @date 2025-01-16
 */
@Service
public class BooksServiceImpl implements IBooksService 
{
    @Autowired
    private BooksMapper booksMapper;

    /**
     * 查询书籍信息
     * 
     * @param bookIndex 书籍信息主键
     * @return 书籍信息
     */
    @Override
    public Books selectBooksByBookIndex(Long bookIndex)
    {
        return booksMapper.selectBooksByBookIndex(bookIndex);
    }

    /**
     * 查询书籍信息列表
     * 
     * @param books 书籍信息
     * @return 书籍信息
     */
    @Override
    public List<Books> selectBooksList(Books books)
    {
        return booksMapper.selectBooksList(books);
    }

    /**
     * 新增书籍信息
     * 
     * @param books 书籍信息
     * @return 结果
     */
    @Override
    public int insertBooks(Books books)
    {
        return booksMapper.insertBooks(books);
    }

    /**
     * 修改书籍信息
     * 
     * @param books 书籍信息
     * @return 结果
     */
    @Override
    public int updateBooks(Books books)
    {
        return booksMapper.updateBooks(books);
    }

    /**
     * 批量删除书籍信息
     * 
     * @param bookIndexs 需要删除的书籍信息主键
     * @return 结果
     */
    @Override
    public int deleteBooksByBookIndexs(Long[] bookIndexs)
    {
        return booksMapper.deleteBooksByBookIndexs(bookIndexs);
    }

    /**
     * 删除书籍信息信息
     * 
     * @param bookIndex 书籍信息主键
     * @return 结果
     */
    @Override
    public int deleteBooksByBookIndex(Long bookIndex)
    {
        return booksMapper.deleteBooksByBookIndex(bookIndex);
    }

    @Override
    public Detail selectDetailByBookIndex(Long bookId, Long userId) {
        // 获取书籍详情和当前用户评分
        Detail detail = booksMapper.selectDetailByBookIndex(bookId, userId);

        // 关键修复1：确保评论列表不为null
        List<UserRating> allComments = Optional.ofNullable(booksMapper.selectAllCommentsByBookIndex(bookId))
                .orElse(new ArrayList<>());

        // 提取当前用户评论并置顶
        UserRating currentUserRating = detail.getUserRating();
        if (currentUserRating != null) {
            // 关键修复2：添加空值检查
            allComments.removeIf(ur ->
                    ur != null &&
                            ur.getUserId() != null &&
                            ur.getUserId().equals(userId)
            );
            allComments.add(0, currentUserRating);
        }

        detail.setCommentList(allComments);
        return detail;
    }

    @Override
    public List<UserRating> selectAllCommentsByBookIndex(Long bookIndex) {
        return booksMapper.selectAllCommentsByBookIndex(bookIndex);
    }


}
