package wind.blog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wind.blog.dto.ArticleDto;
import wind.blog.dto.query.ArticleQueryDto;
import wind.blog.service.ArticleService;
import wind.blog.vo.ArticleVo;
import wind.common.annotation.Log;
import wind.common.annotation.RepeatSubmit;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.Result;
import wind.common.core.domain.dto.ChangeStatusDto;
import wind.common.core.page.Paging;
import wind.common.core.validate.AddGroup;
import wind.common.core.validate.EditGroup;
import wind.common.enums.BusinessType;

import javax.validation.constraints.NotNull;

/**
 * 文章
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/blog/article")
public class ArticleController extends BaseController {

    private final ArticleService service;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("articleList")
    @GetMapping("/list")
    public Result<Paging<ArticleVo>> list(ArticleQueryDto bo, PageQuery pageQuery) {
        return Result.ok(service.list(bo, pageQuery));
    }

    /**
     * 获取用户详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("articleList")
    @GetMapping("/find")
    public Result<ArticleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @RequestParam Integer id) {
        return Result.ok(service.find(id));
    }

    @SaCheckPermission("articleList")
    @RepeatSubmit()
    @Log(title = "文章管理", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public Result<Void> create(@Validated(AddGroup.class) @RequestBody ArticleDto dto) {
        return toAjax(service.create(dto) ? 1 : 0);
    }

    @SaCheckPermission("articleList")
    @RepeatSubmit()
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ArticleDto dto) {
        return toAjax(service.update(dto) ? 1 : 0);
    }

    @SaCheckPermission("articleList")
    @PutMapping("/changeStatus")
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    public Result<Void> updateStatus(@RequestBody ChangeStatusDto dto) {
        Boolean result = service.changeStatus(dto.getId(), dto.getStatus());
        return toAjax(result ? 1 : 0);
    }

    @SaCheckPermission("articleList")
    @Log(title = "文章管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Integer id) {
        return toAjax(service.delete(id) ? 1 : 0);
    }

}
