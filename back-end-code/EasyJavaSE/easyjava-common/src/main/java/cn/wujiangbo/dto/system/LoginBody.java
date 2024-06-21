package cn.wujiangbo.dto.system;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 用户登录对象
 *
 */
@Data
@ToString
public class LoginBody {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
