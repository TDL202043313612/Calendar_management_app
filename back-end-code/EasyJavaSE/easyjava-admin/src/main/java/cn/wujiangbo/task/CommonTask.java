package cn.wujiangbo.task;

import cn.wujiangbo.mapper.system.EasySlf4jLoggingMapper;
import cn.wujiangbo.utils.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * <p>公用定时任务类</p>
 *
 */
@Component
@Slf4j
public class CommonTask {

    @Autowired
    private ConfigurableApplicationContext context;

    @Resource
    public EasySlf4jLoggingMapper easySlf4jLoggingMapper;

    @Value("${easyjava.outTime}")
    private String outTime;

    /**
     * 删除日志表数据
     * 删除6个月前的数据，每天晚上11:30执行一次，始终保留6个月内的数据
     */
    @Scheduled(cron = "0 30 23 ? * *")//每天晚上11:30触发
    //@Scheduled(cron = "*/10 * * * * ?")//每隔n秒执行一次
    public void task001() {
        log.info("--------【定时任务:删除数据库日志-每晚11:30触发一次】-------------执行开始，时间：{}", DateUtils.getCurrentDateString());
        easySlf4jLoggingMapper.deleteLog();
        log.info("--------【定时任务:删除数据库日志-每晚11:30触发一次】-------------执行结束，时间：{}", DateUtils.getCurrentDateString());
    }

    //24 * 60 * 60 * 1000 = 86400000    24小时
    @Scheduled(fixedRate = 86400000) //每隔24小时执行一次
    public void task002() {
        //校验是否过期
        LocalDateTime localDateTime = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://worldtimeapi.org/api/timezone/Asia/Shanghai";
            ResponseEntity<BeijingTime> response = restTemplate.getForEntity(url, BeijingTime.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                BeijingTime beijingTime = response.getBody();
                // 提取北京时间
                String dateTimeStr = beijingTime.getDatetime();
                // 将字符串的北京时间转换成 LocalDateTime 类型
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeStr);
                localDateTime = offsetDateTime.toLocalDateTime();
            } else {
                exitSystem();
            }
        }
        catch (Exception e){
            //如果没有外网，就只能用本地时间了
            localDateTime = LocalDateTime.now();
        }
        // 获取yml中的过期时间并转换成 LocalDateTime 类型
        OffsetDateTime offsetOutTime = OffsetDateTime.parse(outTime);
        LocalDateTime localOutTime = offsetOutTime.toLocalDateTime();
        // 比较北京时间(本地时间)和yml中设置的过期时间
        int result = localDateTime.compareTo(localOutTime);
        if (result > 0) {
            //过期了
            exitSystem();
        }
        else{
            //未过期
        }
    }

    //退出系统
    public void exitSystem(){
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }
}

@Data
class BeijingTime{
    private String client_ip;
    private String datetime;
    private String timezone;
    private String unixtime;
}