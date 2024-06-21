package cn.wujiangbo.dto.token;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @desc desc
 */
@Data
public class UserToken {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 员工编号
     */
    private String empNo;

    /**
     * 用户登录账号
     */
    private String userName;

    /**
     * 用户真实姓名
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * Token 时间戳
     */
    private String timeStamp;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色ID集合
     */
    private Long[] roleId;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 是否是超级管理员
     */
    private boolean superAdmin = false;

    /**
     * 权限列表
     */
    private Set<String> permissions;
}
