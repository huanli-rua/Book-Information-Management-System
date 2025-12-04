package com.ruoyi.books.controller;

import com.ruoyi.books.domain.Books;
import com.ruoyi.books.domain.Detail;
import com.ruoyi.books.service.IBooksService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rating.controller.MyRatingController;
import com.ruoyi.rating.domain.MyRating;
import com.ruoyi.rating.domain.RatingRequest;
import com.ruoyi.rating.domain.UserRating;
import com.ruoyi.rating.service.impl.MyRatingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 书籍信息Controller
 *
 * @author huanli
 * @date 2025-01-16
 */
@RestController
@RequestMapping("/books/books")
public class BooksController extends BaseController {
    @Autowired
    private IBooksService booksService;

    /**
     * 查询书籍信息列表
     */
    @PreAuthorize("@ss.hasPermi('books:books:list')")
    @GetMapping("/list")
    public TableDataInfo list(Books books) {
        startPage();
        List<Books> list = booksService.selectBooksList(books);
        return getDataTable(list);
    }

    /**
     * 导出书籍信息列表
     */
    @PreAuthorize("@ss.hasPermi('books:books:export')")
    @Log(title = "书籍信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Books books) {
        List<Books> list = booksService.selectBooksList(books);
        ExcelUtil<Books> util = new ExcelUtil<Books>(Books.class);
        util.exportExcel(response, list, "书籍信息数据");
    }

    /**
     * 获取书籍信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('books:books:query')")
    @GetMapping(value = "/{bookIndex}")
    public AjaxResult getInfo(@PathVariable("bookIndex") Long bookIndex) {
        return success(booksService.selectBooksByBookIndex(bookIndex));
    }

    /**
     * 新增书籍信息
     */
    @PreAuthorize("@ss.hasPermi('books:books:add')")
    @Log(title = "书籍信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Books books) {
        return toAjax(booksService.insertBooks(books));
    }

    /**
     * 修改书籍信息
     */
    @PreAuthorize("@ss.hasPermi('books:books:edit')")
    @Log(title = "书籍信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Books books) {
        return toAjax(booksService.updateBooks(books));
    }

    /**
     * 删除书籍信息
     */
    @PreAuthorize("@ss.hasPermi('books:books:remove')")
    @Log(title = "书籍信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bookIndexs}")
    public AjaxResult remove(@PathVariable Long[] bookIndexs) {
        return toAjax(booksService.deleteBooksByBookIndexs(bookIndexs));
    }

    @Autowired
    private RestTemplate restTemplate;

    // 新增推荐接口（需配置若依白名单）
    @GetMapping("/recommend/{userId}")
    @ResponseBody
    public AjaxResult getRecommend(@PathVariable Long userId) {
        String pythonServiceUrl = "http://localhost:5000/recommend/" + userId;
        return AjaxResult.success(restTemplate.getForObject(pythonServiceUrl, Map.class));
    }

    // 新增评分接口
    @PostMapping("/submitRating")
    @PreAuthorize("@ss.hasPermi('books:rating:submit')")
    public AjaxResult submitRating(@RequestBody Map<String, Object> params) {

        Long bookIndex = Long.parseLong(params.get("bookIndex").toString());
        Double score = Double.parseDouble(params.get("score").toString());
        String comment = (String) params.get("comment");
        Long userId = getLoginUser().getUserId();

        String pythonServiceUrl = "http://localhost:5000/update";
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("userId", userId);
        requestParams.put("bookIndex", bookIndex);
        requestParams.put("score", score);
        requestParams.put("comment", comment);
        return AjaxResult.success(restTemplate.postForObject(pythonServiceUrl, requestParams, Map.class));
    }

    // 新增书籍详情接口
    @PreAuthorize("@ss.hasPermi('books:books:detail')")
    @GetMapping("/detail/{bookIndex}")
    public AjaxResult getBookDetail(@PathVariable("bookIndex") Long bookId) {
        Long userId = getLoginUser().getUserId();
        Detail detail = booksService.selectDetailByBookIndex(bookId, userId);
        return AjaxResult.success(detail);
    }
    //评论列表
    @PreAuthorize("@ss.hasPermi('books:comment:list')")
    @GetMapping("/allComments/{bookIndex}")
    public AjaxResult getAllComments(@PathVariable Long bookIndex) {
        List<UserRating> comments = booksService.selectAllCommentsByBookIndex(bookIndex);
        return AjaxResult.success(comments);
    }

}
