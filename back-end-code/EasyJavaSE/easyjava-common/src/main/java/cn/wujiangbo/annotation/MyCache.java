package cn.wujiangbo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>自定义添加缓存注解</p>
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCache {

    String cacheNames() default "";

    String key() default "";

    //缓存过期时间，默认是永不过期(单位：秒)
    int time() default -1;

    //缓存过期时间单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}