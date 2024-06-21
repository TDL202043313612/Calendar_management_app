package cn.wujiangbo.annotation;

import cn.wujiangbo.enums.DataSourceEnum;
import java.lang.annotation.*;

/**
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    DataSourceEnum value() default DataSourceEnum.DB1;
}