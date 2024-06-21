package cn.wujiangbo.annotation;

import java.lang.annotation.*;

/**
 * 接口访问限制
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {

    /**
     * 从第一次访问接口的时间到cycle周期时间内，无法超过frequency次
     */
    int frequency() default 5;//频率

    /**
     * 周期时间,单位：毫秒
     * 默认周期时间为1分钟
     */
    long cycle() default 1 * 60 * 1000;

    /**
     * 返回的错误信息
     */
    String message() default "操作过于频繁，请稍后再试！";

    /**
     * key到期时间,单位：秒
     * 如果在cycle周期时间内超过frequency次，则默认5分钟内无法继续访问
     */
    long expireTime() default 5 * 60;
}