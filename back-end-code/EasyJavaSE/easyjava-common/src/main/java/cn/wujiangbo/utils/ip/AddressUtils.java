package cn.wujiangbo.utils.ip;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.dto.system.SystemConfigDto;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取地址类
 *
 */
@Slf4j
public class AddressUtils
{
    // IP地址查询
    public static final String IP_URL = "http://api.map.baidu.com/location/ip?ak=yWKY5FVP34gGT3N7fRfMOVeP9yZwZWA4&ip=%s&coor=bd09ll";

    // 未知地址
    public static final String UNKNOWN = "未知地址";

    /**
     * 根据IP地址获取地理位置
     * @param ip
     * @return
     */
    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
        if (SystemConfigDto.isAddressEnabled())
        {
            try
            {
                String responseStr = HttpUtils.sendGet(String.format(IP_URL, ip), Constants.GBK);
                if (StringUtils.isEmpty(responseStr))
                {
                    log.error("根据IP获取地理位置异常：{}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(responseStr);
                String content = obj.getString("content");
                JSONObject contentObj = JSONObject.parseObject(content);
                String addressStr = contentObj.getString("address");
                log.info("根据IP：[{}]查询到的地址为：[{}]", ip, addressStr);
                return addressStr;
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常：{}", e);
            }
        }
        return address;
    }
}