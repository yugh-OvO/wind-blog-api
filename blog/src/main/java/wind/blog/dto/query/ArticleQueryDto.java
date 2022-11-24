package wind.blog.dto.query;

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
     * 标题
     */
    private String title;

    /**
     * 注册时间
     */
    private String[] createTimeRange;

    /**
     * 状态(1-正常，2-禁用)
     */
    private Integer status;

}
