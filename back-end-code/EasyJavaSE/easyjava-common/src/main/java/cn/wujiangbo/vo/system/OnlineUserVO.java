package cn.wujiangbo.vo.system;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 在线用户信息
 *
 */
@Data
public class OnlineUserVO implements Serializable {

    private Long userId;
    private String userName;
    private String nickName;
    private String phone;
    private String email;
    private String loginIp;
    private Date loginDate;
}
