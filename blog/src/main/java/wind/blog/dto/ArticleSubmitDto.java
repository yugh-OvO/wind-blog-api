package wind.blog.dto;

import lombok.Data;
import wind.common.core.validate.EditGroup;

import javax.validation.constraints.NotNull;

/**
 * 用户业务对象 users
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */

@Data
public class ArticleSubmitDto {

    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

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
     * 分类id
     */
    private Integer categoryId;

    /**
     * 封面图
     */
    private String cover;

}
