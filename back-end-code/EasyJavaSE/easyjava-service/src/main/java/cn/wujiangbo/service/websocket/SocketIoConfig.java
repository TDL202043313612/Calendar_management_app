package cn.wujiangbo.service.websocket;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: socketIo的配置类
 *
 */
@Configuration
public class SocketIoConfig {

    @Autowired
    private SocketProperties socketProperties;

    /**
     * 以下配置在上面的application.yml中已经注明
     * @return 实例化socketIo的服务对象
     */
    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);//解决SOCKET服务端重启"Address already in use"异常
        socketConfig.setTcpKeepAlive(false);
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(socketProperties.getHost());
        config.setPort(socketProperties.getPort());
        config.setBossThreads(socketProperties.getBossCount());
        config.setWorkerThreads(socketProperties.getWorkCount());
        config.setAllowCustomRequests(socketProperties.getAllowCustomRequests());
        config.setUpgradeTimeout(socketProperties.getUpgradeTimeout());
        config.setPingTimeout(socketProperties.getPingTimeout());
        config.setPingInterval(socketProperties.getPingInterval());
        return new SocketIOServer(config);
    }
}