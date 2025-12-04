package com.ruoyi.books.mapper;

import java.util.List;
import com.ruoyi.books.domain.Books;
import com.ruoyi.books.domain.Detail;
import com.ruoyi.rating.domain.UserRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 书籍信息Mapper接口
 * 
 * @author huanli
 * @date 2025-01-16
 */
@Mapper
public interface BooksMapper 
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
     * 删除书籍信息
     * 
     * @param bookIndex 书籍信息主键
     * @return 结果
     */
    public int deleteBooksByBookIndex(Long bookIndex);

    /**
     * 批量删除书籍信息
     * 
     * @param bookIndexs 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBooksByBookIndexs(Long[] bookIndexs);


    public  Detail selectDetailByBookIndex(@Param("bookId") Long bookId, @Param("userId") Long userId);

    List<UserRating> selectAllCommentsByBookIndex(@Param("bookId") Long bookId);

}
