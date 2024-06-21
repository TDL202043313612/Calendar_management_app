package cn.wujiangbo.filter;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.extra.spring.SpringUtil;
import cn.wujiangbo.domain.system.EasySlf4jLogging;
import cn.wujiangbo.service.system.EasySlf4jLoggingService;
import cn.wujiangbo.utils.DateUtils;

/**
 * <p>Slf4j日志入库-过滤器</p>
 *
 */
public class LogFilter extends Filter<LoggingEvent> {

    public EasySlf4jLoggingService easySlf4jLoggingService = null;

    @Override
    public FilterReply decide(LoggingEvent event) {
        String loggerName = event.getLoggerName();
        if(loggerName.startsWith("cn.wujiangbo")){
            //项目本身的日志才会入库
            EasySlf4jLogging log = new EasySlf4jLogging();
            log.setLogTime(DateUtils.getCurrentLocalDateTime());
            log.setLogThread(event.getThreadName());
            log.setLogClass(loggerName);
            log.setLogLevel(event.getLevel().levelStr);
            log.setTrackId(event.getMDCPropertyMap().get("TRACE_ID"));
            log.setLogContent(event.getFormattedMessage());//日志内容

            //每次都是获取同一个EasySlf4jLoggingService对象
            easySlf4jLoggingService = SpringUtil.getBean(EasySlf4jLoggingService.class);
            //日志入库
            easySlf4jLoggingService.asyncSave(log);
            return FilterReply.ACCEPT;
        }else{
            //非项目本身的日志不会入库
            return FilterReply.DENY;
        }
    }
}