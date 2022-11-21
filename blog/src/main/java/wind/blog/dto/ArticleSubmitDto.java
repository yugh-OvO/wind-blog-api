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
     * 内容
     */
    private String content;

    /**
     * 状态(1-正常，2-禁用)
     */
    private Integer status;

}
