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
 * 系统参数配置表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_config表对应的实体对象", description="系统参数配置表")
@Table(name = "easy_config", comment = "系统参数配置表")
public class EasyConfig extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "参数名称")
    @ApiModelProperty(value = "参数名称")
    @TableField(value = "config_name")
    private String configName;

    @Column(comment = "参数键名")
    @ApiModelProperty(value = "参数键名")
    @TableField(value = "config_key")
    private String configKey;

    @Column(comment = "参数键值")
    @ApiModelProperty(value = "参数键值")
    @TableField(value = "config_value")
    private String configValue;

    @Column(comment = "系统内置（Y是 N否）")
    @ApiModelProperty(value = "系统内置（Y是 N否）")
    @TableField(value = "config_type")
    private String configType;

    @Column(comment = "创建者ID")
    @ApiModelProperty(value = "创建者ID")
    @TableField(value = "create_user_id")
    private Long createUserId;

    @Column(comment = "创建时间")
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Column(comment = "更新者ID")
    @ApiModelProperty(value = "更新者ID")
    @TableField(value = "update_user_id")
    private Long updateUserId;

    @Column(comment = "更新时间")
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Column(comment = "备注")
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    /***********************************************************************************
     ***********************************************************************************/

    @ApiModelProperty(value = "最后更新人姓名")
    @TableField(exist = false)
    private String updateUserName;
}