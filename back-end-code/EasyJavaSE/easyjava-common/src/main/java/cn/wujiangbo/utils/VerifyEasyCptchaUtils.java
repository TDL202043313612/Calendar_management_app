package cn.wujiangbo.utils;

import com.wf.captcha.ArithmeticCaptcha;

import java.util.HashMap;
import java.util.Map;

/**
 * 算术运算验证码
 *
 */
public class VerifyEasyCptchaUtils {

    /**
     * 功能说明 获取数字运算的相关参数（base64图片+运算结果）对象
     *
     * @param width     生成的图片宽度
     * @param heigh     生成的图片高度
     * @param numLength 几位数的运算
     * @return Map<String, String>。有2个key，resultData:运算结果；img:base64的转码图片
     */
    public static Map<String, String> getImageEasyCaptcha(int width, int heigh, int numLength) {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(width, heigh);
        // 几位数运算，默认是两位
        captcha.setLen(numLength);
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("resultData", new Double(Double.parseDouble(captcha.text())).intValue() + "");
        } catch (Exception e) {
            map.put("resultData", captcha.text());
        }
        map.put("img", captcha.toBase64());
        return map;
    }
}