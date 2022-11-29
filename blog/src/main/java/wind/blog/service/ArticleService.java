package wind.blog.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wind.blog.dto.ArticleDto;
import wind.blog.dto.query.ArticleQueryDto;
import wind.blog.entity.Article;
import wind.blog.es.domain.ArticleEs;
import wind.blog.es.mapper.ArticleEsMapper;
import wind.blog.mapper.ArticleMapper;
import wind.blog.vo.ArticleVo;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.Paging;

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

    private final ArticleEsMapper esModel;

    public Paging<ArticleVo> list(ArticleQueryDto dto, PageQuery pageQuery) {
//        LambdaEsQueryWrapper<ArticleEs> wrapper = new LambdaEsQueryWrapper<>();
//        wrapper.like(ArticleEs::getTitle, "123");
//        System.out.println(esModel.selectList(wrapper));
        LambdaQueryWrapper<Article> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotEmpty(dto.getTitle()), Article::getContent, dto.getTitle());
        lqw.eq(ObjectUtil.isNotNull(dto.getStatus()), Article::getStatus, dto.getStatus());
        String[] createTime = dto.getCreateTimeRange();
        if (createTime != null && createTime.length == 2) {
            lqw.between(Article::getCreateTime, createTime[0], createTime[1] + " 23:59:59");
        }
        lqw.orderByDesc(Article::getId);
        Page<ArticleVo> result = model.selectVoPage(pageQuery.build(), lqw);
        return Paging.build(result);
    }

    public ArticleVo find(Integer id) {
        ArticleVo data = model.selectVoById(id);
        return data;
    }

    public Boolean create(ArticleDto req) {
        Article data = BeanUtil.toBean(req, Article.class);
        int result = model.insert(data);
        if (result > 0) {
            // 获取刚插入的数据
            Article lastOne = model.selectOne(Wrappers.lambdaQuery(Article.class).orderByDesc(Article::getId).last("limit 1"));
            ArticleEs esArticle = BeanUtil.toBean(lastOne, ArticleEs.class);
            esModel.insert(esArticle);
        }
        return result > 0;
    }

    public Boolean update(ArticleDto req) {
        Article data = BeanUtil.toBean(req, Article.class);
        ArticleEs esArticle = BeanUtil.toBean(req, ArticleEs.class);
        esModel.updateById(esArticle);
        return model.updateById(data) > 0;
    }

    public Boolean changeStatus(Integer id, Integer status) {
        Article update = new Article();
        update.setId(id);
        update.setStatus(status);
        return model.updateById(update) > 0;
    }

    public Boolean delete(Integer id) {
        esModel.deleteById(id);
        return model.deleteById(id) > 0;
    }

}
