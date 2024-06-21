package cn.wujiangbo.aspectj;

import cn.wujiangbo.annotation.MyCacheEvict;
import cn.wujiangbo.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * <p>切面类：实现缓存删除功能</p>
 *
 */
@Aspect
@Component
@Slf4j
public class MyCacheEvictAspect {

    @Resource
    private RedisCache redisCache;

    //定义切点
    @Pointcut("@annotation(myCache)")
    public void pointCut(MyCacheEvict myCache){
    }

    //声明一个环绕通知
    @Around("pointCut(myCache)")
    public Object around(ProceedingJoinPoint joinPoint, MyCacheEvict myCache) {
        String cacheNames = myCache.cacheNames();
        String key = myCache.key();
        /**
         * 思路：
         *      1、执行目标接口之前，就根据key去Redis删除一次缓存
         *      2、目标接口代码执行完毕之后，再根据key去Redis删除一次缓存
         */
        //获取目标接口全路径
        String methodPath = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        //封装存Redis的key
        String redisKey = new StringBuilder(cacheNames).append(":").append(key).toString();
        Object result;
        //删除缓存
        redisCache.deleteObject(redisKey);
        try {
            //继续执行调用目标方法，执行业务代码
            result = joinPoint.proceed();
            //删除缓存
            redisCache.deleteObject(redisKey);
            log.info("访问接口：{}，删除缓存成功", methodPath);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}