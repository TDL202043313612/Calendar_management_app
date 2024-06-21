package cn.wujiangbo.service.websocket.service;

import cn.wujiangbo.service.websocket.PushMessage;
import java.util.List;

/**
 */
public interface SocketIoService {

    /**
     * 推送系统消息的事件
     */
    String PUSH_SYSTEM_MESSAGE_EVENT = "push_system_message_event";

    /**
     * 推送系统在线人数的事件
     */
    String PUSH_ONLINE_COUNT_EVENT = "push_online_count_event";

    /**
     * 强退某用户事件
     */
    String PUSH_LOGINOUT_EVENT = "push_loginout_event";

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 获取在线用户数量
     */
    int getOnlineUserCount();

    /**
     * 获取在线用户ID集合
     */
    List<Long> getOnlineUserIdList();

    /**
     * 发送消息到某个用户
     */
    void pushMessageToUser(PushMessage pushMessage);

    /**
     * 群发消息到所有在线用户
     */
    void pushMessageToAllUser(String event, PushMessage pushMessage);

    /**
     * 强制某用户下线
     */
    void logout(String event, String userId);
}