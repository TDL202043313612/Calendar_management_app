package cn.wujiangbo.controller.base;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.dto.token.UserToken;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.RedisCache;
import cn.wujiangbo.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 */
@Component
public class BaseController {

    @Autowired
    public CommonTools commonTools;

    @Autowired
    private RedisCache redisCache;

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 获取后台系统当前登录用户信息
     *
     * @return
     */
    public UserToken getUser() {
        return commonTools.getUser();
    }

    /**
     * 获取当前登录账号是否是超级管理员
     *
     * @return
     */
    public boolean getSuperAdmin() {
        return getUser().isSuperAdmin();
    }

    /**
     * 获取登录人的主键ID
     *
     * @return
     */
    public Long getUserId() {
        return getUser().getUserId();
    }

    /**
     * 获取APP用户ID
     *
     * @return
     */
    public Long getAppUserId() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            String headerToken = request.getHeader("token");
            if (MyTools.hasLength(headerToken)) {
                Long userId = redisCache.getCacheObject(Constants.LOGIN_TOKEN_KEY_APP + headerToken);
                return userId;
            } else {
                throw new MyException(ErrorCode.NO_PERMISSION);
            }
        }
        throw new MyException("HttpServletRequest对象为空！");
    }

    /**
     * 获取登录人的真实姓名
     *
     * @return
     */
    public String getUserName() {
        return getUser().getNickName();
    }

    /**
     * 获取登录人的员工编号
     *
     * @return
     */
    public String getEmpNo() {
        return getUser().getEmpNo();
    }

}