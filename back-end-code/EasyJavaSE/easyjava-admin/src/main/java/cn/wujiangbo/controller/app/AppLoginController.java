package cn.wujiangbo.controller.app;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.community.AppUser;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.community.AppUserService;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.RedisCache;
import cn.wujiangbo.utils.uuid.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @desc APP登录相关接口
 * @author bobo(weixin:javabobo0513)
 * @date 2024-03-29
 */
@RestController
@RequestMapping("/app/login")
public class AppLoginController extends BaseController{

    @Resource
    public AppUserService appUserService;

    @Resource
    public RedisCache redisCache;

    @Value("${easyjava.userDefaultAvatarPath}")
    public String userDefaultAvatarPath;

    /**
     * APP注册接口
     */
    @PostMapping(value="/register")
    public JSONResult register(@RequestBody AppUser appUser){
        if(!MyTools.hasLength(appUser.getUserPhone())){
            throw new MyException("账号不能为空！");
        }
        if(!MyTools.hasLength(appUser.getLoginPass())){
            throw new MyException("密码不能为空！");
        }

        //查询手机号是否被注册
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_phone", appUser.getUserPhone());
        AppUser one = appUserService.getOne(queryWrapper);
        if(one != null){
            throw new MyException("手机号已被注册，请更换！");
        }

        //字段赋值
        appUser.setLoginName(appUser.getUserPhone());//手机号作为登录账号
        appUser.setUserPhone(appUser.getUserPhone());
        appUser.setLoginPass(appUser.getLoginPass());//登录密码默认为123456
        appUser.setUserImg(userDefaultAvatarPath);//头像
        appUser.setNickName(MyTools.getNickName());//随机昵称
        appUser.setUserStatus(1);//用户状态（1：正常；2：限制登录）
        appUser.setCreateTime(DateUtils.getCurrentLocalDateTime());//创建时间
        appUser.setRegisterTime(DateUtils.getCurrentLocalDateTime());//账号注册时间
        appUser.setUserMoney(new BigDecimal(200));
        //数据入库
        appUserService.save(appUser);
        return JSONResult.success(true, "注册成功，快去登录吧！");
    }

    /**
     * APP登录接口
     */
    @PostMapping(value="/login")
    public JSONResult save(@RequestBody AppUser appUser){
        if(!MyTools.hasLength(appUser.getLoginName())){
            throw new MyException("登录账号不能为空！");
        }
        if(!MyTools.hasLength(appUser.getLoginPass())){
            throw new MyException("登录密码不能为空！");
        }
        //根据账号查询数据库用户信息
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", appUser.getLoginName());
        AppUser one = appUserService.getOne(queryWrapper);
        if(one == null){
            throw new MyException("登录账号不存在！");
        }
        //对比密码
        if(!one.getLoginPass().equals(appUser.getLoginPass())){
            throw new MyException("登录密码错误！");
        }
        //检查用户状态,用户状态（1：正常；2：限制登录）
        if(one.getUserStatus() == 2){
            throw new MyException("抱歉，您已被限制登录" + (MyTools.hasLength(one.getUserError()) ? "，原因：" + one.getUserError() : "！"));
        }
        //登录成功，记录用户最后登录时间等信息
        one.setLastLoginTime(DateUtils.getCurrentLocalDateTime());
        appUserService.updateById(one);

        //给前端颁发token
        String token = UUID.fastUUID().toString(true);
        //token存Redis,24小时过期
        redisCache.setCacheObject(Constants.LOGIN_TOKEN_KEY_APP + token, one.getId(), 2, TimeUnit.HOURS);
        System.out.println("token=" + token);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userId", one.getId());
        return JSONResult.success(map);
    }

}