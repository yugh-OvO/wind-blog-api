package wind.blog.es.domain;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wind.common.core.domain.BusinessEntity;

/**
 * 文章对象 article
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@IndexName(value = "article")
public class ArticleEs extends BusinessEntity {

    /**
     * 文章id
     */
    @IndexId(type = IdType.CUSTOMIZE)
    private Integer id;

    /**
     * 标题
     */
    @IndexField(value = "title")
    private String title;

    /**
     * 内容
     */
    @IndexField(value = "content", fieldType = FieldType.TEXT)
    private String content;

}
