package wind.blog.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wind.common.core.domain.BusinessEntity;

import java.util.Date;


/**
 * 题库视图对象
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ExcelIgnoreUnannotated
public class ArticleVo extends BusinessEntity {

    /**
     * 标题
     */
    private String title;

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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}
