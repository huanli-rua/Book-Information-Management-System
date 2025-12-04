package com.ruoyi.books.service;

import java.util.List;
import com.ruoyi.books.domain.Books;
import com.ruoyi.books.domain.Detail;
import com.ruoyi.rating.domain.UserRating;

/**
 * 书籍信息Service接口
 * 
 * @author huanli
 * @date 2025-01-16
 */
public interface IBooksService 
{
    /**
     * 查询书籍信息
     * 
     * @param bookIndex 书籍信息主键
     * @return 书籍信息
     */
    public Books selectBooksByBookIndex(Long bookIndex);

    /**
     * 查询书籍信息列表
     * 
     * @param books 书籍信息
     * @return 书籍信息集合
     */
    public List<Books> selectBooksList(Books books);

    /**
     * 新增书籍信息
     * 
     * @param books 书籍信息
     * @return 结果
     */
    public int insertBooks(Books books);

    /**
     * 修改书籍信息
     * 
     * @param books 书籍信息
     * @return 结果
     */
    public int updateBooks(Books books);

    /**
     * 批量删除书籍信息
     * 
     * @param bookIndexs 需要删除的书籍信息主键集合
     * @return 结果
     */
    public int deleteBooksByBookIndexs(Long[] bookIndexs);

    /**
     * 删除书籍信息信息
     * 
     * @param bookIndex 书籍信息主键
     * @return 结果
     */
    public int deleteBooksByBookIndex(Long bookIndex);

    Detail selectDetailByBookIndex(Long bookId, Long userId);

    List<UserRating> selectAllCommentsByBookIndex(Long bookIndex);
}
