package wind.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import wind.common.annotation.ExcelDictFormat;

import java.util.Date;

/**
 * 操作日志记录表 operation_log
 *
 * @author Yu Gaoheng
 */

@Data
@TableName("sys_operation_log")
public class OperationLogVO {

    /**
     * 日志主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 操作模块
     */
    private String title;
    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @ExcelDictFormat(dictType = "sys_operation_type")
    private Integer businessType;
    /**
     * 业务类型数组
     */
    @TableField(exist = false)
    private Integer[] businessTypes;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @ExcelDictFormat(readConverterExp = "0=其它,1=后台用户,2=手机端用户")
    private Integer operatorType;
    /**
     * 操作人员
     */
    private String name;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 请求url
     */
    private String url;
    /**
     * 操作地址
     */
    private String ip;
    /**
     * 操作地点
     */
    private String location;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 返回参数
     */
    private String jsonResult;
    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;
    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 操作时间
     */
    private Date operationTime;

    @TableField(exist = false)
    private String[] operationTimeRange;

}
