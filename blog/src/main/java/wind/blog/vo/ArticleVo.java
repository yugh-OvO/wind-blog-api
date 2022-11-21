package wind.blog.vo;

import cn.hutool.json.JSONArray;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * id
     */
    private Integer id;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 答案json
     */
    private JSONArray answersArray;

    private String answers;

    /**
     * 状态(1-正常，2-禁用)
     */
    @JsonIgnore
    private Integer status;

    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    private String createBy;

    @JsonIgnore
    private String updateBy;

}
