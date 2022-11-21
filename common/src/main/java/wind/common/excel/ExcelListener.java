package wind.common.excel;

import com.alibaba.excel.read.listener.ReadListener;

/**
 * Excel 导入监听
 *
 * @author Yu Gaoheng
 */
public interface ExcelListener<T> extends ReadListener<T> {

    /**
     * 获取导入数据
     *
     * @return 导入数据
     */
    ExcelResult<T> getExcelResult();

}
