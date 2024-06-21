package cn.wujiangbo.interceptor;

import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.dto.token.UserToken;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.RedisCache;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>权限校验拦截器</p>
 *
 */
@Component
@Slf4j
public class CheckPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CommonTools commonTools;

    /**
     * 前置处理
     * 该方法将在请求处理之前进行调用
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        //记录方法执行时间-起始时间
        request.setAttribute("startTime", System.currentTimeMillis());

        //判断：如果拦截到的资源不是方法，那就直接返回true放行，不需要走后面的逻辑判断
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //指定到这里，说明请求的是方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //获取方法对象
        Method method = handlerMethod.getMethod();
        //获取方法上面的 CheckIdempotent 注解对象
        CheckPermission methodAnnotation = method.getAnnotation(CheckPermission.class);
        if (methodAnnotation != null) {
            //获取权限字符串
            String per = methodAnnotation.per();
            if(StringUtils.isNotBlank(per)){
                /**
                 * 说明需要校验权限，拥有该权限字符串，才能访问该方法
                 */
                UserToken userToken = commonTools.getUser();
                System.out.println("用户：" + userToken.getNickName() + "，正在访问资源：" + request.getRequestURI() + "   time:" + DateUtils.getCurrentDateString());
                System.out.println("");
                //先获取到当前用户所拥有的权限字符串集合
                Set<String> permissionsSet = userToken.getPermissions();
                //开始判断
                if(!permissionsSet.contains(per)){
                    //无权限访问，抛错
                    throw new MyException(ErrorCode.NO_PERMISSION);
                }
                else{
                    //有权限访问的话，Redis的key值续期2小时
                    String header = request.getHeader(Constants.TOKEN);
                    redisCache.setCacheObject(header, JSONObject.toJSONString(userToken), Constants.TOKEN_REDIS_ALIVE_TIME, TimeUnit.HOURS);
                }
            }
        }
        //必须返回true，否则会拦截掉所有请求，不会执行controller方法中的内容了
        return true;
    }

    /**
     * 后置处理
     * 当前请求进行处理之后，也就是Controller 方法调用之后执行，但是它会在DispatcherServlet 进行视图返回渲染之前被调用
     * 注意：preHandle方法返回true时，才会执行此方法
     */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            String methodPath = method.getDeclaringClass().getName()+"."+method.getName();
//            long startTime = (long) request.getAttribute("startTime");
//            request.removeAttribute("startTime");
//            //打印一下耗时情况
//            System.out.println("【" + methodPath + "】接口被调用完毕，耗时： " + (System.currentTimeMillis() - startTime) + "毫秒");
//        }
//    }

    /**
     * 完成请求后调用
     * 该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行，这个方法的主要作用是用于进行资源清理工作的。
     * 注意：preHandle方法返回true时，才会执行此方法
     */
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        log.info("资源释放/清理工作-执行完毕");
//    }
}