package cn.wujiangbo.controller.system;

import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.dto.system.ServerInfo;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * 监控相关API
 *
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 服务器监控
     */
    @GetMapping("/server")
    public JSONResult getInfo()
    {
        ServerInfo server = new ServerInfo();
        try {
            server.copyTo();
        } catch (Exception e) {
            throw new MyException(ErrorCode.ERROR_CODE_1011);
        }
        return JSONResult.success(server);
    }

    /**
     * Redis缓存监控
     */
    @GetMapping("/redisCache")
    public JSONResult getRedisCache(){
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return JSONResult.success(result);
    }
}