package wind.common.core.domain;

import lombok.Data;

import java.util.List;

/**
 * Tree基类
 *
 * @author Yu Gaoheng
 */

@Data
public class OptionEntity<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String label;

    /**
     * ID
     */
    private Integer value;

    private Integer parentId;

    /**
     * 子集合
     */
    private List<T> children;

}
