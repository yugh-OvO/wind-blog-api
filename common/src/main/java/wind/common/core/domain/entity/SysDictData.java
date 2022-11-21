package wind.common.core.domain.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wind.common.annotation.ExcelDictFormat;
import wind.common.constant.UserConstants;
import wind.common.convert.ExcelDictConvert;
import wind.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典数据表 sys_dict_data
 *
 * @author Yu Gaoheng
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@ExcelIgnoreUnannotated
public class SysDictData extends BaseEntity {

    /**
     * 字典排序
     */
    @ExcelProperty(value = "字典排序")
    private Integer sort;

    /**
     * 字典标签
     */
    @ExcelProperty(value = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    private String label;

    /**
     * 字典键值
     */
    @ExcelProperty(value = "字典键值")
    private Integer value;

    /**
     * 字典类型
     */
    @ExcelProperty(value = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    private String type;

    /**
     * 样式属性（其他样式扩展）
     */
    @Size(max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 是否默认（1-是 2-否）
     */
    @ExcelProperty(value = "是否默认", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_yes_no")
    private Integer isDefault;

    /**
     * 状态（1-正常 2-停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public boolean getDefault() {
        return UserConstants.YES.equals(this.isDefault);
    }

}
