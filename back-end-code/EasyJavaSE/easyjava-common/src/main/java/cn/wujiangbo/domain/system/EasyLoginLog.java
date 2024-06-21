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
 * 系统访问记录
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_login_log表对应的实体对象", description="系统访问记录")
@Table(name = "easy_login_log", comment = "系统登录日志表")
public class EasyLoginLog extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "登录账号")
    @ApiModelProperty(value = "登录账号")
    @TableField(value = "user_name")
    private String userName;

    @Column(comment = "登录IP地址")
    @ApiModelProperty(value = "登录IP地址")
    @TableField(value = "ipaddr")
    private String ipaddr;

    @Column(comment = "登录地点")
    @ApiModelProperty(value = "登录地点")
    @TableField(value = "login_location")
    private String loginLocation;

    @Column(comment = "浏览器类型")
    @ApiModelProperty(value = "浏览器类型")
    @TableField(value = "browser")
    private String browser;

    @Column(comment = "操作系统")
    @ApiModelProperty(value = "操作系统")
    @TableField(value = "os")
    private String os;

    @Column(comment = "提示消息")
    @ApiModelProperty(value = "提示消息")
    @TableField(value = "msg")
    private String msg;

    @Column(comment = "访问时间")
    @ApiModelProperty(value = "访问时间")
    @TableField(value = "login_time")
    private LocalDateTime loginTime;
}