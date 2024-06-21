package cn.wujiangbo.controller.system;

import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.dto.system.LoginBody;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.EasyUserService;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.tools.SystemLogTools;
import cn.wujiangbo.utils.RedisCache;
import cn.wujiangbo.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 登录API
 *
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class SysLoginController extends BaseController {

    @Autowired
    private EasyUserService userService;

    @Autowired
    private SystemLogTools systemLog;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CommonTools commonTools;

    /**
     * 后台系统登录接口
     */
    @PostMapping("/login")
    @MyLog(title = "登录系统", businessType = BusinessType.OTHER)
    public JSONResult login(HttpServletRequest request, @RequestBody LoginBody loginBody)
    {
        //验证码校验
        commonTools.checkImgCode(loginBody.getUuid(), loginBody.getCode());

        //图片验证码校验正确，开始校验密码，然后生成令牌
        return userService.login(request, loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
    }

    /**
     * 刷新权限
     */
    @PostMapping("/refreshPermission")
    @MyLog(title = "刷新权限", businessType = BusinessType.OTHER)
    public JSONResult refreshPermission(HttpServletRequest request)
    {
        return userService.refreshPermission(request, getUserId());
    }

    /**
     * 退出
     */
    @GetMapping("/loginOut")
    @MyLog(title = "退出系统", businessType = BusinessType.OTHER)
    public JSONResult loginOut()
    {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            String token = request.getHeader(Constants.TOKEN);
            //删除Redis中的用户信息
            redisCache.deleteObject(token);
            systemLog.saveLoginLog(getUserName(), "退出系统");
        }catch (Exception e){
            e.printStackTrace();
        }
        return JSONResult.success();
    }

    /**
     * 获取登录页面所需信息
     */
    @GetMapping("/getLoginPageInfo")
    public JSONResult getLoginPageInfo()
    {
        return userService.getLoginPageInfo();
    }

}
