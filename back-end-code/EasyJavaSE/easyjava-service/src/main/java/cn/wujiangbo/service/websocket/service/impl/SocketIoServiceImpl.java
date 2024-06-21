package cn.wujiangbo.service.websocket.service.impl;

import cn.wujiangbo.service.websocket.PushMessage;
import cn.wujiangbo.service.websocket.service.SocketIoService;
import cn.wujiangbo.utils.RedisCache;
import cn.wujiangbo.utils.StringUtils;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: socket的实现类
 *
 */
@Service(value = "socketIOService")
@Slf4j
public class SocketIoServiceImpl implements SocketIoService {

    /**
     * 用来存已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    /**
     * socketIo的对象
     */
    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RedisCache redisCache;

    /**
     * 功能描述：当前的service被初始化的时候执行以下的方法
     */
    @PostConstruct
    private void autoStartUp() {
        clientMap.clear();
        start();
        log.info("websocket init finish");
    }

    /**
     * 功能描述：当我们的系统停止的时候关闭我们的socketIo
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            /**
             * 此处实现我们的socket的连接的用户的逻辑，此处我前端传的是 userId 这个参数，大家可以根据自己的情况来定义入参
             */
            String userId = getParamsByClient(client).get("userId").get(0);
            if(StringUtils.isNotNull(userId) && !"null".equals(userId)){
                log.info("用户[{}]上线啦", userId);
                clientMap.put(userId, client);
                sendOnlineMessage();
            }
        });

        // 监听客户端断开连接(直接关闭浏览器)
        socketIOServer.addDisconnectListener(client -> {
            List<String> list = getParamsByClient(client).get("userId");
            if(!CollectionUtils.isEmpty(list)){
                for (String userId : list) {
                    if (StringUtils.isNotNull(userId) && !"null".equals(userId)) {
                        log.info("用户[{}]下线啦", userId);
                        clientMap.remove(userId);
                        client.disconnect();
                        sendOnlineMessage();
                    }
                }
            }
        });

        /**
         * 监听前端某用户下线操作（点击退出操作）
         */
        socketIOServer.addEventListener("user_login_out", PushMessage.class, (client, data, ackSender) -> {
            String userId = data.getUserId() == null ? "" : String.valueOf(data.getUserId());
            if (data.getUserId() != null) {
                redisCache.deleteObject(userId);
                log.info("用户[{}]下线啦", userId);
                clientMap.remove(userId);
                client.disconnect();
                sendOnlineMessage();
            }
        });
        socketIOServer.start();
    }

    //群发消息，告知所有客户端在线人数
    public void sendOnlineMessage(){
        PushMessage msg = new PushMessage();
        msg.setShow(false);
        msg.setOnlineCount(getOnlineUserCount());
        pushMessageToAllUser(SocketIoService.PUSH_ONLINE_COUNT_EVENT, msg);
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    //获取在线用户数量
    @Override
    public int getOnlineUserCount() {
        return clientMap.size();
    }

    //获取在线用户ID集合
    @Override
    public List<Long> getOnlineUserIdList() {
        List<Long> userList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(clientMap)){
            for(String userId: clientMap.keySet()){
                userList.add(Long.valueOf(userId));
            }
        }
        return userList;
    }

    /**
     * 发送消息到某个用户
     * @param pushMessage 发送消息的实体
     */
    @Override
    public void pushMessageToUser(PushMessage pushMessage) {
        sendMessage(clientMap.get(String.valueOf(pushMessage.getUserId())), PUSH_SYSTEM_MESSAGE_EVENT, pushMessage);
    }

    //发送消息
    public void sendMessage(SocketIOClient clent, String event, PushMessage pushMessage){
        if(clent != null){
            clent.sendEvent(event, pushMessage);
        }
    }

    /**
     * 群发消息到所有在线用户
     * @param pushMessage 发送消息的实体
     * @param event 不同事件
     */
    @Override
    public void pushMessageToAllUser(String event, PushMessage pushMessage) {
        for(SocketIOClient clent: clientMap.values()){
            sendMessage(clent, event, pushMessage);
        }
    }

    /**
     * 强制某用户下线
     */
    @Override
    public void logout(String event, String userId) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setUserId(Long.valueOf(userId));
        sendMessage(clientMap.get(userId), event, pushMessage);
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     */
    private Map<String, List<String>> getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        return params;
    }
}