package cn.wujiangbo.controller.system;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.tools.SystemConfigTool;
import cn.wujiangbo.utils.RedisCache;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 获取图片验证码
 *
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private SystemConfigTool systemConfigTool;

    /**
     * 生成验证码(easy-captcha)
     */
    @GetMapping("/getImageEasyCode/{uuid}")
    public JSONResult getImageEasyCode(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        //是否校验登录图片验证码：0：校验；1：不校验
        String captchaOnOff = systemConfigTool.getConfigValueByKey(Constants.SYSTEM_CONFIG_KEY.DEFAULT_CAPTCHAONOFF);
        Map<String, String> map = new HashMap<>();
        if ("1".equals(captchaOnOff)) {
            map.put("captchaOnOff", captchaOnOff);
            return JSONResult.success(map);
        }

        // 保存验证码信息
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        //设置验证码的类型（可不设置，使用默认值）
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String code = specCaptcha.text().toLowerCase();

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        map.put("captchaOnOff", "0");
        map.put("img", specCaptcha.toBase64());
        return JSONResult.success(map);
    }

    /**
     * 门户网站-生成验证码
     */
    @GetMapping("/captchaImageYmsd/{uuid}")
    public JSONResult captchaImageYmsd(@PathVariable("uuid") String uuid) throws IOException
    {
        //是否校验登录图片验证码：0：校验；1：不校验
        String captchaOnOff = systemConfigTool.getConfigValueByKey(Constants.SYSTEM_CONFIG_KEY.DEFAULT_CAPTCHAONOFF);
        Map<String, String> map = new HashMap<>();
        if ("1".equals(captchaOnOff))
        {
            map.put("flag", captchaOnOff);
            return JSONResult.success(map);
        }

        // 保存验证码信息
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        //设置验证码的类型（可不设置，使用默认值）
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String code = specCaptcha.text().toLowerCase();

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        map.put("flag", captchaOnOff);
        map.put("img", specCaptcha.toBase64());
        return JSONResult.success(map);
    }

}