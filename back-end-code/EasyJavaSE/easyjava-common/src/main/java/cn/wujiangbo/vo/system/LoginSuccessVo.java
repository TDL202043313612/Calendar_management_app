package cn.wujiangbo.vo.system;

import cn.wujiangbo.dto.token.UserToken;
import lombok.Data;

/**
 * @desc 登录成功后，返回前端的对象
 */
@Data
public class LoginSuccessVo {

    private String token;//token
    private String refToken;//刷新token
    private UserToken userToken;//用户Token存的相关值
    private Long userId;//用户ID
}