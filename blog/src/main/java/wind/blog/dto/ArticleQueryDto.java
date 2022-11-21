package wind.blog.dto;

import lombok.Data;

/**
 * 用户业务对象 users
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */

@Data
public class ArticleQueryDto {

    /**
     * 问题内容
     */
    private String content;

    /**
     * 注册时间
     */
    private String[] createTimeRange;

    /**
     * 状态(1-正常，2-禁用)
     */
    private Integer status;

}
