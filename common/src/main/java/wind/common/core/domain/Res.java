package wind.common.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author Yu Gaoheng
 */
@Data
@NoArgsConstructor
public class Res<T> implements Serializable {
    /**
     * 成功
     */
    public static final int SUCCESS = 200;
    /**
     * 失败
     */
    public static final int FAIL = 500;
    private static final long serialVersionUID = 1L;
    private int code;

    private String msg;

    private T data;

    public static <T> Res<T> ok() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> Res<T> ok(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> Res<T> ok(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> Res<T> ok(String msg, T data) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Res<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> Res<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> Res<T> fail(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> Res<T> fail(String msg, T data) {
        return restResult(data, FAIL, msg);
    }

    public static <T> Res<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> Res<T> restResult(T data, int code, String msg) {
        Res<T> res = new Res<>();
        res.setCode(code);
        res.setData(data);
        res.setMsg(msg);
        return res;
    }

}
