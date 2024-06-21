package cn.wujiangbo.aspectj;

import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.domain.system.EasyOperLog;
import cn.wujiangbo.dto.token.UserToken;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.tools.SystemLogTools;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.ServletUtils;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.ip.IpUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 */
//这个@Aspect注释告诉Spring这是个切面类
@Aspect
@Component
@Slf4j
public class MyLogAspect {

    @Value("${system.recordOperationLog}")
    private boolean recordOperationLog;

    @Autowired
    private SystemLogTools systemLogTools;

    @Autowired
    private CommonTools commonTools;


    //为了记录方法的执行时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义一个切入点 我这里是从controller切入 不是从注解切入
     * 第一个星号：表示返回值的类型任意
     * cn.wujiangbo.controller：AOP所切的服务的包名
     * 后面..的含义：表示当前包及子包
     * 第二个星号：表示类名，*即所有类
     * .*(..)含义：表示任何方法名，括号表示参数，两个点表示任何参数类型
     */
    @Pointcut("execution(public * cn.wujiangbo.controller..*.*(..))")
    public void pointCut() {}

    @Before("pointCut()")
    public void beforMethod(JoinPoint point){
        startTime.set(System.currentTimeMillis());
    }


    /**
     * 返回通知, 在方法返回结果之后执行
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(myLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, MyLog myLog, Object jsonResult)
    {
        if(recordOperationLog){
            handleMyLog(joinPoint, myLog, null, jsonResult);
        }
    }

    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(myLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, MyLog myLog, Exception e)
    {
        if(recordOperationLog){
            handleMyLog(joinPoint, myLog, e, null);
        }
    }

    //处理日志
    protected void handleMyLog(final JoinPoint joinPoint, MyLog myLog, final Exception e, Object jsonResult)
    {
        try
        {
            // 获取当前的用户
            UserToken user = commonTools.getUser();
            // *========数据库日志=========*//
            EasyOperLog operMyLog = new EasyOperLog();
            operMyLog.setStatus(0);//操作状态（0正常 1异常）
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operMyLog.setOperIp(ip);
            operMyLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            if (user != null)
            {
                operMyLog.setOperUserId(user.getUserId());
            }
            if (e != null)
            {
                //发生异常了
                operMyLog.setStatus(1);
                operMyLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operMyLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operMyLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 设置耗时
            Long takeTime = System.currentTimeMillis() - startTime.get();
            operMyLog.setTakeTime(takeTime);
            operMyLog.setOperTime(DateUtils.getCurrentLocalDateTime());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, myLog, operMyLog, jsonResult);
            // 操作日志保存数据库
            systemLogTools.saveOperLog(operMyLog);
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param MyLog 日志
     * @param operMyLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, MyLog MyLog, EasyOperLog operMyLog, Object jsonResult) throws Exception
    {
        // 设置action动作
        operMyLog.setBusinessType(MyLog.businessType().getCode());
        // 设置标题
        operMyLog.setTitle(MyLog.title());
        // 设置操作人类别
        operMyLog.setOperatorType(MyLog.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (MyLog.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operMyLog);
        }
        // 是否需要保存response，参数和值
        if (MyLog.isSaveResponseData() && StringUtils.isNotNull(jsonResult))
        {
            operMyLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到MyLog中
     * @param operMyLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, EasyOperLog operMyLog) throws Exception
    {
        String requestMethod = operMyLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))
        {
            String params = argsArrayToString(joinPoint.getArgs());
            operMyLog.setOperParam(StringUtils.substring(params, 0, 2000));
        }
        else
        {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operMyLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (StringUtils.isNotNull(o) && !isFilterObject(o))
                {
                    try
                    {
                        Object jsonObj = JSON.toJSON(o);
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                        log.error("发生异常：{}", e);
                        e.printStackTrace();
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     * 
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
