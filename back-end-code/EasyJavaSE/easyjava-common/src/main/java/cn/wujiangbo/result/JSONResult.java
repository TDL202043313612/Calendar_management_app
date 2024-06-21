package cn.wujiangbo.result;

import cn.wujiangbo.constants.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台返回前端统一结果对象
 *
 */
@Data
@ApiModel(value = "响应数据封装类")
public class JSONResult implements Serializable {

    @ApiModelProperty(value = "响应状态描述（true:成功;false:失败;）")
    private boolean success = true;

    @ApiModelProperty(value = "响应描述")
    private String message = ErrorCode.SYSTEM_SUCCESS.getMessage();

    //成功统一返回0000，其余编码全部是错误码
    @ApiModelProperty(value = "响应状态码")
    private String code = ErrorCode.SYSTEM_SUCCESS.getCode();

    //是否展示提示信息（默认不展示）
    @ApiModelProperty(value = "是否展示提示信息")
    private boolean showMessage = false;

    //返回的数据
    @ApiModelProperty(value = "响应数据")
    private Object data;

    //创建当前实例
    public static JSONResult success() {
        return new JSONResult();
    }

    //创建当前实例
    public static JSONResult success(String message) {
        JSONResult instance = new JSONResult();
        instance.setMessage(message);
        return instance;
    }
    public static JSONResult successData(String data) {
        JSONResult instance = new JSONResult();
        instance.setData(data);
        return instance;
    }

    //创建当前实例
    public static JSONResult success(boolean showMessage) {
        JSONResult instance = new JSONResult();
        instance.setShowMessage(showMessage);
        return instance;
    }

    //创建当前实例
    public static JSONResult success(boolean showMessage, String message) {
        JSONResult instance = new JSONResult();
        instance.setShowMessage(showMessage);
        instance.setMessage(message);
        return instance;
    }

    //创建当前实例
    public static JSONResult success(Object obj) {
        JSONResult instance = new JSONResult();
        instance.setData(obj);
        return instance;
    }

    //创建当前实例
    public static JSONResult success(Object obj, boolean showMessage) {
        JSONResult instance = new JSONResult();
        instance.setData(obj);
        instance.setShowMessage(showMessage);
        return instance;
    }

    //创建当前实例
    public static JSONResult success(Object obj, boolean showMessage, String message) {
        JSONResult instance = new JSONResult();
        instance.setData(obj);
        instance.setMessage(message);
        instance.setShowMessage(showMessage);
        return instance;
    }

    //成功，但是返回不同消息代码
    public static JSONResult success(Object obj, String code) {
        JSONResult instance = new JSONResult();
        instance.setSuccess(true);
        instance.setCode(code);
        instance.setData(obj);
        return instance;
    }

    public static JSONResult success(String code, String message) {
        JSONResult instance = new JSONResult();
        instance.setSuccess(true);
        instance.setCode(code);
        instance.setMessage(message);
        return instance;
    }

    //创建当前实例
    public static JSONResult error() {
        JSONResult instance = new JSONResult();
        instance.setCode(ErrorCode.SYSTEM_ERROR.getCode());
        instance.setSuccess(false);
        instance.setMessage(ErrorCode.SYSTEM_ERROR.getMessage());
        return instance;
    }

    //创建当前实例
    public static JSONResult error(String message) {
        JSONResult instance = new JSONResult();
        instance.setCode(ErrorCode.SYSTEM_ERROR.getCode());
        instance.setSuccess(false);
        instance.setMessage(message);
        return instance;
    }

    public static JSONResult error(String message, Object obj) {
        JSONResult instance = new JSONResult();
        instance.setCode(ErrorCode.SYSTEM_ERROR.getCode());
        instance.setMessage(message);
        instance.setSuccess(false);
        instance.setData(obj);
        return instance;
    }

    public static JSONResult error(String code, String message) {
        JSONResult instance = new JSONResult();
        instance.setCode(code);
        instance.setMessage(message);
        instance.setSuccess(false);
        return instance;
    }

    //接收一个错误码的枚举
    public static JSONResult error(ErrorCode errorCode) {
        JSONResult instance = new JSONResult();
        instance.setMessage(errorCode.getMessage());
        instance.setSuccess(false);
        instance.setCode(errorCode.getCode());
        return instance;
    }
}
