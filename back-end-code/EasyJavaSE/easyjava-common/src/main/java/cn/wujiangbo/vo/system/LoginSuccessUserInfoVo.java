package cn.wujiangbo.vo.system;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @description: 封装用户信息返回前端
 *
 */
@Data
public class LoginSuccessUserInfoVo {

    private Long id;//主键ID
    private String avatar;//头像地址
    private String sex;//性别
    private String nickName;//真实名称
    private String userName;//登录账号
    private String empNo;//员工编号
    private String loginIp;//上次登录IP
    private LocalDateTime loginDate;//上次登录时间
}