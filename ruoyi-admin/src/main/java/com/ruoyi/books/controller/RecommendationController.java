package com.ruoyi.books.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.books.domain.Recommendation;
import com.ruoyi.books.service.IRecommendationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 个性化推荐Controller
 * 
 * @author huanli
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/books/recommendation")
public class RecommendationController extends BaseController
{
    @Autowired
    private IRecommendationService recommendationService;

    /**
     * 查询个性化推荐列表
     */
    @PreAuthorize("@ss.hasPermi('books:recommendation:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        // 获取当前登录用户的ID
        Long userId = getLoginUser().getUserId(); // 假设框架提供了获取当前用户的方法
        startPage();
        List<Recommendation> list = recommendationService.selectRecommendationList(userId);
        return getDataTable(list);
    }



}
