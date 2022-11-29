package wind.common.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author Yu Gaoheng
 */

@Data
@NoArgsConstructor
public class Paging<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> list;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public Paging(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public static <T> Paging<T> build(IPage<T> page) {
        Paging<T> rspData = new Paging<>();
        rspData.setList(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    public static <T> Paging<T> build(List<T> list) {
        Paging<T> rspData = new Paging<>();
        rspData.setList(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    public static <T> Paging<T> build() {
        Paging<T> rspData = new Paging<>();
        return rspData;
    }

}
