package cn.wujiangbo.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解校验权限
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {

    /**
     * 权限字符串
     */
    String per() default "";
}