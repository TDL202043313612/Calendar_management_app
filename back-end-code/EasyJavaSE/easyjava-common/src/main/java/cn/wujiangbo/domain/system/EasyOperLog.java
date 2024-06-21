package cn.wujiangbo.domain.system;

import cn.wujiangbo.domain.base.BaseDomain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志记录
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_oper_log表对应的实体对象", description="操作日志表")
@Table(name = "easy_oper_log", comment = "操作日志表")
public class EasyOperLog extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "模块标题")
    @ApiModelProperty(value = "模块标题")
    @TableField(value = "title")
    private String title;

    @Column(comment = "操作类型（0其它 1新增 2修改 3删除 4查询）")
    @ApiModelProperty(value = "操作类型（0其它 1新增 2修改 3删除 4查询）")
    @TableField(value = "business_type")
    private Integer businessType;

    @Column(comment = "方法名称")
    @ApiModelProperty(value = "方法名称")
    @TableField(value = "method")
    private String method;

    @Column(comment = "请求方式")
    @ApiModelProperty(value = "请求方式")
    @TableField(value = "request_method")
    private String requestMethod;

    @Column(comment = "操作类别（0其它 1后台用户 2门户用户）")
    @ApiModelProperty(value = "操作类别（0其它 1后台用户 2门户用户）")
    @TableField(value = "operator_type")
    private Integer operatorType;

    @Column(comment = "操作人ID")
    @ApiModelProperty(value = "操作人ID")
    @TableField(value = "oper_user_id")
    private Long operUserId;

    @Column(comment = "请求URL")
    @ApiModelProperty(value = "请求URL")
    @TableField(value = "oper_url")
    private String operUrl;

    @Column(comment = "操作人IP地址")
    @ApiModelProperty(value = "操作人IP地址")
    @TableField(value = "oper_ip")
    private String operIp;

    @Column(comment = "操作地点")
    @ApiModelProperty(value = "操作地点")
    @TableField(value = "oper_location")
    private String operLocation;

    @Column(comment = "请求参数")
    @ApiModelProperty(value = "请求参数")
    @TableField(value = "oper_param")
    private String operParam;

    @Column(comment = "返回参数")
    @ApiModelProperty(value = "返回参数")
    @TableField(value = "json_result")
    private String jsonResult;

    @Column(comment = "操作状态（0正常 1异常）")
    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    @TableField(value = "status")
    private Integer status;

    @Column(comment = "错误消息")
    @ApiModelProperty(value = "错误消息")
    @TableField(value = "error_msg")
    private String errorMsg;

    @Column(comment = "操作时间")
    @ApiModelProperty(value = "操作时间")
    @TableField(value = "oper_time")
    private LocalDateTime operTime;

    @Column(comment = "方法执行耗时")
    @ApiModelProperty(value = "方法执行耗时")
    @TableField(value = "take_time")
    private Long takeTime;

    /***********************************************************************************
    ***********************************************************************************/

    @ApiModelProperty(value = "操作人")
    @TableField(exist = false)
    private String createUserName;

}