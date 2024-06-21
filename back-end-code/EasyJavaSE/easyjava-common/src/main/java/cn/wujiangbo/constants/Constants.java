package cn.wujiangbo.constants;

/**
 * 常量类
 *
 */
public class Constants {

    /**
     * Base64图片数据的头信息
     */
    public static final String BASE64_HEAD = "data:img/jpg;base64,";

    /**
     * Base64图片数据的头信息
     */
    public static final String BASE64_HEAD_PNG = "data:image/png;base64,";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * APP用户登录 redis key
     */
    public static final String LOGIN_TOKEN_KEY_APP = "login_app_tokens:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌在Redis中保存时长
     */
    public static final int TOKEN_REDIS_ALIVE_TIME = 2;

    /**
     * 登录图片验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 注册图片验证码 redis key
     */
    public static final String REGISTER_CODE_KEY = "register_codes:";

    /**
     * 登录图片验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 5;

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "cn.wujiangbo" };

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.ruoyi.common.utils.file" };

    //阿里OSS服务存放文件夹名称
    public static class OSS{
        public static final String DEFAULT_NAME = "default";//默认文件存放目录
        public static final String USER_IMAGE = "userImage";//存放用户头像
        public static final String QUILL_IMAGE = "easyjava/quill_image";//富文本编辑框上传的图片
        public static final String RESOURCE_LINK_IMG = "easyjava/HarmonyOSAppTest";

        //资源链接的默认封面照
        public static final String RESOURCE_LINK_DEFAULT_IMG = "https://easyjava.oss-cn-chengdu.aliyuncs.com/resourceLink/resourceLink.png";
        public static final String RESOURCE_LINK_DEFAULT_IMG_NAME = "resourceLink/resourceLink.png";
    }

    //阿里OSS服务存放文件夹名称-学工部
    public static class OSS_xuegongbu{
        public static final String TANHUAPINGJU = "ymsd/tanhuapingju";//谈话凭据
    }

    //数据库表名
    public static class table{
        public static final String TABLE_0001 = "ym_happy";//源码时代-快乐瞬间表
    }

    //数据库各种状态对应值
    public static class STATUS{
        public static final String STATUS_NORMAL = "0";//正常
        public static final String STATUS_STOP = "1";//停用
    }

    //数据字典类型字符串标识
    public static class DICT_TYPE{
        public static final String SYS_USER_SEX = "sys_user_sex";//性别
        public static final String SYS_STATUS = "sys_status";//状态
        public static final String USER_NATION = "user_nation";//民族
        public static final String USER_NATIONALITY = "user_nationality";//国籍
        public static final String USER_BLOOD_TYPE = "user_blood_type";//血型
        public static final String USER_EDUCATION = "user_education";//学历
        public static final String USER_MARRIAGE = "user_marriage";//婚姻
        public static final String USER_POLITICAL = "user_political";//政治面貌
        public static final String USER_MAJOR = "user_major";//专业
        public static final String USER_POSITION = "user_position";//职位
    }

    //Cacheable注解里需要用到的常量
    public static class CACHEABLE{
        public static final String CACHENAMES = "easyjava";
        public static final String KEY_SYSTEM_CONFIG = "systemConfig";
        public static final String KEY_ALL_PERMISSION_LIST = "permissionList";
        public static final String KEY_ALL_PERMISSION_LIST_NO_BUTTON = "permissionList_NoButton";
        public static final String KEY_ALL_DICT_INFO = "dictinfo";
        public static final String KEY_DEPT_TREE = "deptinfo";
        public static final String DICT_INFO_KEY = CACHENAMES + ":" + KEY_ALL_DICT_INFO;
    }

    //系统配置的key值
    public static class SYSTEM_CONFIG_KEY{
        public static final String DEFAULT_LOGIN_PASSWORD = "sys.user.initPassword";
        public static final String DEFAULT_COMPANY_NAME = "sys.companyName";
        public static final String DEFAULT_SYSTEM_NAME = "sys.systemName";
        public static final String DEFAULT_COMPANY_LOGO = "sys.companyLogo";
        public static final String DEFAULT_CAPTCHAONOFF = "sys.captchaOnOff";//0：校验；1：不校验
        public static final String STU_REGISTER_FLAG = "stuRegisterFlag";//是否开放学员注册接口(0：开放；1：不开放)
    }

    //RabbitMQ相关常量
    public static class MQ{
        public static final String EXCHANGE_EASYJAVA_TOPIC = "exchange_easyjava_topic";//通配符交换机名称
        public static final String QUEUE_EMAIL = "easyjava_queue_email";//邮件队列名称
        public static final String ROUTINGKEY_QUEUE_EMAIL = "#.email";//邮件队列的路由键
    }

    //常量
    public static class YMSD{
        public static final String READ = "0";//消息状态（0已读 1未读）
        public static final String UN_READ = "1";//消息状态（0已读 1未读）
    }

}