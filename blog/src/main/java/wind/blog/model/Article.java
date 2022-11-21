package wind.blog.model;

import cn.easyes.annotation.IndexName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wind.common.core.domain.BaseEntity;

/**
 * 用户对象 member
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
@IndexName("article")
public class Article extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 问题编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态(1-正常，2-禁用)
     */
    private Integer status;

    /**
     * 是否置顶(1-是，2-否)
     */
    private Integer isTop;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 封面图
     */
    private String cover;

}
