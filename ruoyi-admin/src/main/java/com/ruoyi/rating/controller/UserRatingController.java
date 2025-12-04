package com.ruoyi.rating.controller;

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
import com.ruoyi.rating.domain.UserRating;
import com.ruoyi.rating.service.IUserRatingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 评分Controller
 * 
 * @author huanli
 * @date 2025-02-18
 */
@RestController
@RequestMapping("/UserRating/UserRating")
public class UserRatingController extends BaseController
{
    @Autowired
    private IUserRatingService userRatingService;

    /**
     * 查询评分列表
     */
    @PreAuthorize("@ss.hasPermi('UserRating:UserRating:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserRating userRating)
    {
        startPage();
        List<UserRating> list = userRatingService.selectUserRatingList(userRating);
        return getDataTable(list);
    }

    /**
     * 导出评分列表
     */
    @PreAuthorize("@ss.hasPermi('UserRating:UserRating:export')")
    @Log(title = "评分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserRating userRating)
    {
        List<UserRating> list = userRatingService.selectUserRatingList(userRating);
        ExcelUtil<UserRating> util = new ExcelUtil<UserRating>(UserRating.class);
        util.exportExcel(response, list, "评分数据");
    }

    /**
     * 获取评分详细信息
     */
    @PreAuthorize("@ss.hasPermi('UserRating:UserRating:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(userRatingService.selectUserRatingByUserId(userId));
    }

    /**
     * 新增评分
     */
    @PreAuthorize("@ss.hasPermi('UserRating:UserRating:add')")
    @Log(title = "评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserRating userRating)
    {
        return toAjax(userRatingService.insertUserRating(userRating));
    }

    /**
     * 修改评分
     */
    @PreAuthorize("@ss.hasPermi('UserRating:UserRating:edit')")
    @Log(title = "评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserRating userRating)
    {
        return toAjax(userRatingService.updateUserRating(userRating));
    }

    /**
     * 删除评分
     */
    @PreAuthorize("@ss.hasPermi('UserRating:UserRating:remove')")
    @Log(title = "评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userRatingService.deleteUserRatingByUserIds(userIds));
    }
}
