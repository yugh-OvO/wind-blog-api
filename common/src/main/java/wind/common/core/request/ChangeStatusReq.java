package wind.common.core.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改状态请求
 *
 * @author Yu Gaoheng
 * @date 2022-10-24
 */
@Data
public class ChangeStatusReq {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "状态不能为空")
    private Integer status;

}
