package cn.wujiangbo.service.websocket;

import lombok.Data;

/**
 * @description: socket消息发送实体类
 *
 */
@Data
public class PushMessage {

    /**
     * 当前登录用户ID
     */
    private Long userId;

    /**
     * 推送的标题
     */
    private String title;

    /**
     * 推送的内容
     */
    private String content;

    /**
     * 在线人数
     */
    private Integer onlineCount;

    /**
     * 是否需要提示框展示给用户
     */
    private Boolean show;

    public PushMessage() {
    }

    public PushMessage(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PushMessage(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}