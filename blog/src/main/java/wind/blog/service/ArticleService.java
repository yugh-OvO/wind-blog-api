package wind.blog.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wind.blog.dto.ArticleQueryDto;
import wind.blog.dto.ArticleSubmitDto;
import wind.blog.mapper.ArticleMapper;
import wind.blog.model.Article;
import wind.blog.vo.ArticleVo;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 用户Service业务层处理
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleMapper model;

    public TableDataInfo<ArticleVo> list(ArticleQueryDto req, PageQuery pageQuery) {
        LambdaQueryWrapper<Article> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotEmpty(req.getContent()), Article::getContent, req.getContent());
        lqw.eq(ObjectUtil.isNotNull(req.getStatus()), Article::getStatus, req.getStatus());
        String[] createTime = req.getCreateTimeRange();
        if (createTime != null && createTime.length == 2) {
            lqw.between(Article::getCreateTime, createTime[0], createTime[1] + " 23:59:59");
        }
        lqw.orderByDesc(Article::getId);
        Page<ArticleVo> result = model.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    public ArticleVo find(Integer id) {
        ArticleVo data = model.selectVoById(id);
        data.setAnswersArray(JSONUtil.parseArray(data.getAnswers()));
        return data;
    }

    public Boolean create(ArticleSubmitDto req) {
        Article data = BeanUtil.toBean(req, Article.class);
        return model.insert(data) > 0;
    }

    public Boolean update(ArticleSubmitDto req) {
        Article data = BeanUtil.toBean(req, Article.class);
        return model.updateById(data) > 0;
    }

    public Boolean changeStatus(Integer id, Integer status) {
        Article update = new Article();
        update.setId(id);
        update.setStatus(status);
        return model.updateById(update) > 0;
    }

    public Boolean delete(Integer id) {
        return model.deleteById(id) > 0;
    }

    public List<ArticleVo> all() {
        LambdaQueryWrapper<Article> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Article::getId);
        List<ArticleVo> result = model.selectVoList(lqw);
        return result;
    }

}
