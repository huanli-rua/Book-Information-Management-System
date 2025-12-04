package com.ruoyi.rating.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.rating.domain.RatingRequest;
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
import com.ruoyi.rating.domain.MyRating;
import com.ruoyi.rating.service.IMyRatingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 我的评分Controller
 * 
 * @author huanli
 * @date 2025-02-22
 */
@RestController
@RequestMapping("/UserRating/MyRating")
public class MyRatingController extends BaseController
{
    @Autowired
    private IMyRatingService myRatingService;


    /**
     * 查询我的评分列表
     */
    @PreAuthorize("@ss.hasPermi('UserRating:MyRating:list')")
    @GetMapping("/list")
    public TableDataInfo list()
    {
        Long userId = getLoginUser().getUserId();
        startPage();
        List<MyRating> list = myRatingService.selectMyRatingList(userId);
        return getDataTable(list);
    }





    /**
     * 新增我的评分
     */
    @PreAuthorize("@ss.hasPermi('UserRating:MyRating:add')")
    @Log(title = "我的评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MyRating myRating)
    {
        return toAjax(myRatingService.insertMyRating(myRating));
    }



    /**
     * 删除我的评分
     */
    @PreAuthorize("@ss.hasPermi('UserRating:MyRating:remove')")
    @Log(title = "我的评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bookIndex}")
    public AjaxResult remove(@PathVariable Long bookIndex)
    {
        Long userId = getLoginUser().getUserId();
        return toAjax(myRatingService.deleteMyRatingByUserId(userId,bookIndex));
    }
}
