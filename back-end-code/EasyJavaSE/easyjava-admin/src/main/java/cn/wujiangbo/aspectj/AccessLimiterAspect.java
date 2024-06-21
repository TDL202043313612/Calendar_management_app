package cn.wujiangbo.aspectj;

import cn.wujiangbo.annotation.AccessLimiter;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * @description: 接口防刷处理
 *   思路如下：
 *      利用AOP，实现防刷逻辑。具体代码如下，通过Redis保存某个IP首次访问接口的时间，和访问次数，然后在限制时间内对访问次数进行累加，
 *      超过最大次数则抛出操作太频繁的异常，需要等待Redis的key过期之后才能再次访问该接口，达到接口防刷的效果
 */
@Aspect
@Component
@Slf4j
public class AccessLimiterAspect {

    private static final String LIMITING_KEY = "limiting:%s:%s";
    private static final String LIMITING_BEGINTIME = "beginTime";
    private static final String LIMITING_EXFREQUENCY = "exFrequency";

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(AccessLimiter)")
    public void pointcut(AccessLimiter AccessLimiter) {
    }

    @Around("pointcut(AccessLimiter)")
    public Object around(ProceedingJoinPoint pjp, AccessLimiter AccessLimiter) throws Throwable {
        //获取请求的ip和方法
        String ipAddress = IpUtils.getIpAddress();
        log.info("限流处理类，获取到IP={}", ipAddress);
        String methodName = pjp.getSignature().toLongString();

        //获取redis中周期内第一次访问方法的时间和已访问过接口的次数
        Long beginTimeLong = (Long) redisTemplate.opsForHash().get(String.format(LIMITING_KEY, ipAddress, methodName), LIMITING_BEGINTIME);
        Integer exFrequencyLong = (Integer) redisTemplate.opsForHash().get(String.format(LIMITING_KEY, ipAddress, methodName), LIMITING_EXFREQUENCY);
        long beginTime = beginTimeLong == null ? 0L : beginTimeLong;
        int exFrequency = exFrequencyLong == null ? 0 : exFrequencyLong;

        //当两次访问时间差超过限制时间时，记录最新访问时间作为一个访问周期内的首次访问时间，并设置访问次数为1
        if (System.currentTimeMillis() - beginTime > AccessLimiter.cycle()) {
            redisTemplate.opsForHash().put(String.format(LIMITING_KEY, ipAddress, methodName), LIMITING_BEGINTIME, System.currentTimeMillis());
            redisTemplate.opsForHash().put(String.format(LIMITING_KEY, ipAddress, methodName), LIMITING_EXFREQUENCY, 1);
            //设置key的过期时间
            redisTemplate.expire(String.format(LIMITING_KEY, ipAddress, methodName), AccessLimiter.expireTime(), TimeUnit.SECONDS);
            return pjp.proceed();
        } else {
            //如果该次访问与首次访问时间差在限制时间段内，则访问次数+1，并刷新key的过期时间
            if (exFrequency < AccessLimiter.frequency()) {
                redisTemplate.opsForHash().put(String.format(LIMITING_KEY, ipAddress, methodName), LIMITING_EXFREQUENCY, exFrequency + 1);
                redisTemplate.expire(String.format(LIMITING_KEY, ipAddress, methodName), AccessLimiter.expireTime(), TimeUnit.SECONDS);
                return pjp.proceed();
            } else {
                //限制时间内访问次数超过最大可访问次数，抛出异常
                throw new MyException(AccessLimiter.message());
            }
        }
    }

}