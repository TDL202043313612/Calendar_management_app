package cn.wujiangbo.domain.app;

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
 * 联系人表
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="app_linkman 表对应的实体对象", description="联系人表")
@Table(name = "app_linkman", comment = "联系人表")
public class AppLinkman extends BaseDomain implements Serializable {

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

    @ApiModelProperty(value = "联系人名称")
    @TableField(value = "man_name")
    @Column(comment = "联系人名称")
    @Excel(name = "联系人名称")
    private String manName;

    @ApiModelProperty(value = "联系人电话")
    @TableField(value = "man_phone")
    @Column(comment = "联系人电话")
    @Excel(name = "联系人电话")
    private String manPhone;

    @ApiModelProperty(value = "是否置顶(置顶;不置顶;)")
    @TableField(value = "man_top")
    @Column(comment = "是否置顶(置顶;不置顶;)")
    @Excel(name = "是否置顶(置顶;不置顶;)")
    private String manTop;
}