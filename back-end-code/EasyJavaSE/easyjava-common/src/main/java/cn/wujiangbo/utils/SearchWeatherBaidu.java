package cn.wujiangbo.utils;

import cn.wujiangbo.exception.MyException;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>调用百度接口获取天气信息工具类</p>
 *
 */
public class SearchWeatherBaidu {

    public static String URL = "https://api.map.baidu.com/weather/v1/?";

    public String requestGetAK(Map<String, String> param) {
        try {
            StringBuffer queryString = new StringBuffer();
            queryString.append(URL);
            for (Map.Entry<?, ?> pair : param.entrySet()) {
                queryString.append(pair.getKey() + "=");
                //    第一种方式使用的 jdk 自带的转码方式  第二种方式使用的 spring 的转码方法 两种均可
                //    queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8").replace("+", "%20") + "&");
                queryString.append(UriUtils.encode((String) pair.getValue(), "UTF-8") + "&");
            }
            if (queryString.length() > 0) {
                queryString.deleteCharAt(queryString.length() - 1);
            }

            java.net.URL url = new URL(queryString.toString());
            System.out.println(queryString.toString());
            URLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();

            InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            isr.close();
            System.out.println("调用百度接口返回天气结果：" + buffer.toString());
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new MyException("调用百度获取天气异常！");
    }
}
