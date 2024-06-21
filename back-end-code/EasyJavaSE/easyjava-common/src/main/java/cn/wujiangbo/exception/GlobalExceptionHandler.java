package cn.wujiangbo.exception;

import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.result.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.security.SignatureException;

/**
 * 全局异常处理工具类
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(MyException.class)
    public JSONResult MyException(MyException e){
        return JSONResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 文件上传超过大小
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult methodMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        log.error("文件上传超过大小-异常：{}", e);
        return JSONResult.error("上传文件太大啦...");
    }

    /**
     * 算术异常
     */
    @ExceptionHandler(ArithmeticException.class)
    public JSONResult methodArithmeticException(ArithmeticException e){
        log.error("算术异常：{}", e);
        return JSONResult.error("发生算术异常");
    }

    /**
     * Redis连接异常
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    public JSONResult methodRedisConnectionFailureException(RedisConnectionFailureException e){
        log.error("Redis连接异常：{}", e);
        return JSONResult.error("Redis连接异常");
    }

    /**
     * JWT解析异常
     */
    @ExceptionHandler(SignatureException.class)
    public JSONResult methodSignatureException(SignatureException e){
        return JSONResult.error(ErrorCode.ERROR_CODE_1026);
    }

    /**
     * 如果发生上面没有捕获的异常，那么统一走这个异常捕获，相当于是最大的一个范围
     */
    @ExceptionHandler(Exception.class)
    public JSONResult exceptionHandler(Exception e){
        e.printStackTrace();
        return JSONResult.error("系统繁忙，请稍后再试...");
    }
}