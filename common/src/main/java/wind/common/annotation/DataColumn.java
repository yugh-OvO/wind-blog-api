package wind.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限
 * <p>
 * 一个注解只能对应一个模板
 *
 * @author Yu Gaoheng
 * @version 3.5.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataColumn {

    /**
     * 占位符关键字
     */
    String[] key() default "departmentName";

    /**
     * 占位符替换值
     */
    String[] value() default "department_id";

}
