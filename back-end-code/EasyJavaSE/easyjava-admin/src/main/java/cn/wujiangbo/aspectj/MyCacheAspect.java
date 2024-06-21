package cn.wujiangbo.aspectj;

import cn.wujiangbo.annotation.MyCache;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>切面类：实现缓存添加功能</p>
 *
 */
@Aspect
@Component
@Slf4j
public class MyCacheAspect {

    @Resource
    private RedisCache redisCache;

    //定义切点
    @Pointcut("@annotation(myCache)")
    public void pointCut(MyCache myCache){
    }

    //声明一个环绕通知
    @Around("pointCut(myCache)")
    public Object around(ProceedingJoinPoint joinPoint, MyCache myCache) {
        String cacheNames = myCache.cacheNames();
        String key = myCache.key();
        int time = myCache.time();
        TimeUnit timeUnit = myCache.timeUnit();
        /**
         * 思路：
         *      1、首先根据key查询Redis中是否存在对应缓存
         *      2、如果存在，就直接返回前端，不会进入目标接口执行业务代码了
         *      3、如果不存在，就进入目标接口执行业务代码，然后再讲返回数据存一份到Redis中即可
         */
        //获取目标接口全路径
        String methodPath = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        //封装存Redis的key
        String redisKey = new StringBuilder(cacheNames).append(":").append(key).toString();
        //开始判断Redis是否存在缓存
        Object result ;
        if (redisCache.exists(redisKey)){
            log.info("访问接口：{}，数据来源于Redis缓存", methodPath);
            return redisCache.getCacheObject(redisKey);
        }
        try {
            //继续执行调用目标方法，执行业务代码
            result = joinPoint.proceed();
            //执行完业务代码后拿到接口返回值result，将其存入Redis进行缓存即可
            redisCache.setCacheObject(redisKey, result, time, timeUnit);
            log.info("访问接口：{}，数据来源于接口，同时成功存入Redis缓存", methodPath);
        } catch (Throwable e) {
            throw new MyException("缓存数据存入Redis时异常：" + e.getLocalizedMessage());
        }
        return result;
    }
}