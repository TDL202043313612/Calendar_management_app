package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import lombok.Data;

/**
 * @desc Slf4j日志表-查询对象
 * @author bobo(weixin:javabobo0513)
 * @since 2023-06-20
 */
@Data
public class EasySlf4jLoggingQuery extends BaseQuery{

    private String trackId;//日志全局跟踪ID
    private String logLevel;//日志级别
    private String className;//类名
}