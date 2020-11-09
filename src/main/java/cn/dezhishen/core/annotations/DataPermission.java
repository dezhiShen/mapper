package cn.dezhishen.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解,用于标定字段和提供对应的权限标识信息
 *
 * @author
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {
    /**
     * 注解字段对应的权限的名称
     *
     * @return
     */
    String field();

    /**
     * 权限的类型,List,Item
     *
     * @return
     */
    ParamType paramType() default ParamType.Item;
}
