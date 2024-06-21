package cn.wujiangbo.domain.app;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.wujiangbo.domain.base.BaseDomain;
import cn.wujiangbo.annotation.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收入支出表
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="app_money 表对应的实体对象", description="收入支出表")
@Table(name = "app_money", comment = "收入支出表")
public class AppMoney extends BaseDomain implements Serializable {

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

    @ApiModelProperty(value = "记账金额")
    @TableField(value = "money_num")
    @Column(comment = "记账金额")
    @Excel(name = "记账金额")
    private BigDecimal moneyNum;

    @ApiModelProperty(value = "账目类型(支出;收入)")
    @TableField(value = "money_status")
    @Column(comment = "账目类型(支出;收入)")
    @Excel(name = "账目类型(支出;收入)")
    private String moneyStatus;

    @ApiModelProperty(value = "记账描述")
    @TableField(value = "money_desc")
    @Column(comment = "记账描述")
    @Excel(name = "记账描述")
    private String moneyDesc;
}