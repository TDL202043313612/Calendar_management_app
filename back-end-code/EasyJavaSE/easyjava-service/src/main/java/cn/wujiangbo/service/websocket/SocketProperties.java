package cn.wujiangbo.service.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: socketio在yml配置文件中的配置值
 *
 */
@Component
@ConfigurationProperties(prefix = "socketio")
@Data
public class SocketProperties {

    private String host;

    private Integer port;

    private Integer bossCount;

    private Integer workCount;

    private Boolean allowCustomRequests;

    private Integer upgradeTimeout;

    private Integer pingTimeout;

    private Integer pingInterval;
}