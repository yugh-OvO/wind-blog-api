package wind.common.core.domain;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import wind.common.utils.StringUtils;
import wind.common.utils.sql.SqlUtil;

import java.io.Serializable;

/**
 * 分页查询实体类
 *
 * @author Yu Gaoheng
 */

@Data
public class PageQuery implements Serializable {

    public final static String ASC = "asc";
    public final static String DESC = "desc";
    public final static String ASCENDING = "ascending";
    public final static String DESCENDING = "descending";


    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;
    private static final long serialVersionUID = 1L;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 当前页数
     */
    private Integer current;
    /**
     * 排序列
     */
    private String orderByColumn;
    /**
     * 排序的方向desc或者asc
     */
    private String isAsc;

    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getCurrent(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        OrderItem orderItem = buildOrderItem();
        if (ObjectUtil.isNotNull(orderItem)) {
            page.addOrder(orderItem);
        }
        return page;
    }

    private OrderItem buildOrderItem() {
        // 兼容前端排序类型
        if (ASCENDING.equals(isAsc)) {
            isAsc = ASC;
        } else if (DESCENDING.equals(isAsc)) {
            isAsc = DESC;
        }
        if (StringUtils.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
            orderBy = StringUtils.toUnderScoreCase(orderBy);
            if (ASC.equals(isAsc)) {
                return OrderItem.asc(orderBy);
            } else if (DESC.equals(isAsc)) {
                return OrderItem.desc(orderBy);
            }
        }
        return null;
    }

}
