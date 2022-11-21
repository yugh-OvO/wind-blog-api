package wind.common.excel;

import java.util.List;

/**
 * excel返回对象
 *
 * @author Yu Gaoheng
 */
public interface ExcelResult<T> {

    /**
     * 对象列表
     *
     * @return 对象列表
     */
    List<T> getList();

    /**
     * 错误列表
     *
     * @return 错误列表
     */
    List<String> getErrorList();

    /**
     * 导入回执
     *
     * @return 导入回执
     */
    String getAnalysis();
}
