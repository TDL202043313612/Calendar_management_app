package cn.wujiangbo.service.websocket;

import cn.wujiangbo.service.websocket.service.SocketIoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>WebSocket工具类</p>
 *
 */
@Component
public class WebSocketUtils {

    @Autowired
    private SocketIoService socketIoService;

    /**
     * 推送实时消息给某个在线用户
     * @param userId 用户ID
     * @param msgContent 消息内容
     */
    public void pushMessageToSingleUser(Long userId, String msgContent){
        socketIoService.pushMessageToUser(new PushMessage(userId, "", msgContent));
    }

    /**
     * 推送实时消息给所有在线用户
     * @param msgContent 消息内容
     */
    public void pushMessageToAllUser(String msgContent){
        socketIoService.pushMessageToAllUser(SocketIoService.PUSH_SYSTEM_MESSAGE_EVENT, new PushMessage("", msgContent));
    }

}