package cn.wujiangbo.tools;

import cn.wujiangbo.service.system.EasyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: SystemConfigTool
 *
 */
@Component
public class SystemConfigTool {

    @Autowired
    private EasyConfigService easyConfigService;

    /**
     * 根据key获取系统配置值
     * @param key
     * @return
     */
    public String getConfigValueByKey(String key){
        Map<String, String> map = easyConfigService.getConfigValueMap();
        return map.get(key);
    }
}
