package cn.wujiangbo.tools;

import cn.wujiangbo.domain.system.EasyLoginLog;
import cn.wujiangbo.domain.system.EasyOperLog;
import cn.wujiangbo.service.system.EasyLoginLogService;
import cn.wujiangbo.service.system.EasyOperLogService;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.ServletUtils;
import cn.wujiangbo.utils.ip.AddressUtils;
import cn.wujiangbo.utils.ip.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 记录日志工具类
 *
 */
@Component
public class SystemLogTools {

    @Autowired
    private EasyLoginLogService easyLoginLogService;

    @Autowired
    private EasyOperLogService easyOperLogService;

    //是否记录用户的操作日志到数据库
    @Value("${system.recordOperationLog}")
    private boolean recordOperationLog;

    /**
     * 保存登录/注销日志到数据库
     */
    public void saveLoginLog(String username, String desc){
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        // 封装对象
        EasyLoginLog logininfor = new EasyLoginLog();
        logininfor.setUserName(username);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        logininfor.setBrowser(MyTools.getClientInfo().get("browser"));
        logininfor.setOs(MyTools.getClientInfo().get("os"));
        logininfor.setMsg(desc);
        logininfor.setLoginTime(DateUtils.getCurrentLocalDateTime());

        //数据入库
        easyLoginLogService.save(logininfor);

        //RabbitMQ发送邮件
        //rabbitTemplate.convertAndSend(Constants.RabbitMQ.SYSTEM_LOGINLOG_EXCHANGE, Constants.RabbitMQ.SYSTEM_LOGINLOG_ROUTING_KEY, JSONObject.toJSONString(logininfor));
    }

    /**
     * 保存操作日志到数据库
     */
    public void saveOperLog(final EasyOperLog operLog){
        if(recordOperationLog){
            operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
            operLog.setOperTime(DateUtils.getCurrentLocalDateTime());

            //数据入库
            easyOperLogService.save(operLog);

            //RabbitMQ发送邮件
            //rabbitTemplate.convertAndSend(Constants.RabbitMQ.SYSTEM_LOG_EXCHANGE, Constants.RabbitMQ.SYSTEM_LOG_ROUTING_KEY, JSONObject.toJSONString(operLog));
        }
    }
}