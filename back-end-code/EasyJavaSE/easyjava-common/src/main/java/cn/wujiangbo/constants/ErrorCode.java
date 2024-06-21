package cn.wujiangbo.constants;

/**
 * @desc 系统错误码
 */
public enum ErrorCode {

    SYSTEM_SUCCESS("0000", "操作成功！"),

    //公用错误信息
    COMMON_CODE_2001("2001", "入参有误！"),

    //自定义错误信息
    LOGIN_PASSWORD_ERROR("1001", "登录密码错误！"),
    USERNAME_EXIST("1002", "登录账号已存在，请重新输入！"),
    OSS_ENABLE_FASLE("1003", "文件服务器太贵了，买不起，先委屈下用默认头像哈！"),
    USERNAME_NOT_EXIST("1004", "账号不存在！"),
    USERNAME_NO_USEING("1005", "账号已被停用！"),
    TOKEN_EXPIRE("1006", "认证信息已过期，请重新登录！"),
    NO_PERMISSION("1007", "您没有权限进行此操作！"),
    LOGIN_CODE_EXPIRE("1008", "图片验证码已过期，请刷新！"),
    LOGIN_CODE_ERROR("1009", "图片验证码输入错误，请重新输入！"),
    ERROR_CODE_1010("1010", "员工编号异常！"),
    ERROR_CODE_1011("1011", "获取服务器监控信息发生异常！"),
    ERROR_CODE_1020("1020", "内容过长，请重新编辑！"),
    ERROR_CODE_1021("1021", "令牌无效，请重新登录！"),
    ERROR_CODE_1022("1022", "邮箱已存在，请更换！"),
    ERROR_CODE_1026("1026", "令牌错误"),
    ERROR_CODE_1027("1027", "手机号已存在，请更换！"),
    ERROR_CODE_1028("1028", "不能删除自己哦！"),
    ERROR_CODE_1029("1029", "不允许操作超级管理员用户！"),
    ERROR_CODE_1030("1030", "菜单名称已存在，请更换！"),
    ERROR_CODE_1031("1031", "地址必须以http(s)://开头，请更换！"),
    ERROR_CODE_1032("1032", "上级菜单不能选择自己！"),
    ERROR_CODE_1033("1033", "存在子菜单,不允许删除！"),
    ERROR_CODE_1034("1034", "角色名称已存在，请更换！"),
    ERROR_CODE_1035("1035", "不允许操作超级管理员角色！"),
    ERROR_CODE_1036("1036", "部门名称已存在，请更换！"),
    ERROR_CODE_1037("1037", "上级部门不能是自己，请更换！"),
    ERROR_CODE_1038("1038", "该部门包含未停用的子部门！"),
    ERROR_CODE_1039("1039", "文件上传至OSS异常！"),
    ERROR_CODE_1040("1040", "上传内容不能为空！"),
    ERROR_CODE_1041("1041", "接收人不能为空哦！"),
    ERROR_CODE_1042("1042", "旧密码不能为空哦！"),
    ERROR_CODE_1043("1043", "新密码不能为空哦！"),
    ERROR_CODE_1044("1044", "旧密码错误！"),
    ERROR_CODE_1051("1051", "糟糕，有他人登录同一账号，把你挤下线了！"),
    ERROR_CODE_1052("1052", "资源编号不能为空！"),


    SYSTEM_ERROR("9999", "系统发生异常，请稍后再试！");

    //错误码
    private String code;

    //错误信息
    private String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}