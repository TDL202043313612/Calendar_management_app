package cn.wujiangbo.domain.community;

import cn.wujiangbo.annotation.excel.Excel;
import cn.wujiangbo.domain.base.BaseDomain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 社区APP用户信息表
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-03-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="app_user 表对应的实体对象", description="社区APP用户信息表")
@Table(name = "app_user", comment = "社区APP用户信息表")
public class AppUser extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @IsAutoIncrement
    @TableId(value = "id", type = IdType.AUTO)
    @Column(comment = "主键ID")
    @Excel(name = "主键ID")
    private Long id;

    @ApiModelProperty(value = "提交人ID")
    @TableField(value = "create_user_id")
    @Column(comment = "提交人ID")
    @Excel(name = "提交人ID")
    private Long createUserId;

    @ApiModelProperty(value = "提交人")
    @TableField(value = "create_user_name")
    @Column(comment = "提交人")
    @Excel(name = "提交人")
    private String createUserName;

    @ApiModelProperty(value = "提交时间")
    @TableField(value = "create_time")
    @Column(comment = "提交时间")
    @Excel(name = "提交时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "update_user_id")
    @Column(comment = "更新人ID")
    @Excel(name = "更新人ID")
    private Long updateUserId;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(value = "update_user_name")
    @Column(comment = "更新人姓名")
    @Excel(name = "更新人姓名")
    private String updateUserName;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    @Column(comment = "更新时间")
    @Excel(name = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "登录账号")
    @TableField(value = "login_name")
    @Column(comment = "登录账号")
    @Excel(name = "登录账号")
    private String loginName;

    @ApiModelProperty(value = "登录密码")
    @TableField(value = "login_pass")
    @Column(comment = "登录密码")
    @Excel(name = "登录密码")
    private String loginPass;

    @ApiModelProperty(value = "昵称")
    @TableField(value = "nick_name")
    @Column(comment = "昵称")
    @Excel(name = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    @TableField(value = "user_img")
    @Column(comment = "用户头像")
    @Excel(name = "用户头像")
    private String userImg;

    @ApiModelProperty(value = "用户性别（0：男；1：女）")
    @TableField(value = "user_sex")
    @Column(comment = "用户性别（0：男；1：女）")
    @Excel(name = "用户性别（0：男；1：女）")
    private Integer userSex;

    @ApiModelProperty(value = "用户手机号")
    @TableField(value = "user_phone")
    @Column(comment = "用户手机号")
    @Excel(name = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "用户状态（1：正常；2：限制登录）")
    @TableField(value = "user_status")
    @Column(comment = "用户状态（1：正常；2：限制登录）")
    @Excel(name = "用户状态（1：正常；2：限制登录）")
    private Integer userStatus;

    @ApiModelProperty(value = "限制登录原因")
    @TableField(value = "user_error")
    @Column(comment = "限制登录原因")
    @Excel(name = "限制登录原因")
    private String userError;

    @ApiModelProperty(value = "用户个人简介")
    @TableField(value = "user_brief")
    @Column(comment = "用户个人简介")
    @Excel(name = "用户个人简介")
    private String userBrief;

    @ApiModelProperty(value = "最后一次登录时间")
    @TableField(value = "last_login_time")
    @Column(comment = "最后一次登录时间")
    @Excel(name = "最后一次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "注册时间")
    @TableField(value = "register_time")
    @Column(comment = "注册时间")
    @Excel(name = "注册时间")
    private LocalDateTime registerTime;

    @ApiModelProperty(value = "毕业院校")
    @TableField(value = "user_school")
    @Column(comment = "毕业院校")
    @Excel(name = "毕业院校")
    private String userSchool;

    @ApiModelProperty(value = "专业")
    @TableField(value = "user_major")
    @Column(comment = "专业")
    @Excel(name = "专业")
    private String userMajor;

    @ApiModelProperty(value = "账户余额")
    @TableField(value = "user_money")
    @Column(comment = "账户余额")
    @Excel(name = "账户余额")
    private BigDecimal userMoney;
}