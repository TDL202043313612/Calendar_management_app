package cn.wujiangbo.domain.system;

import cn.wujiangbo.domain.base.BaseDomain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 字典类型表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("easy_dict_type")
@ApiModel(value="easy_dict_type表对应的实体对象", description="字典类型表")
@Table(name = "easy_dict_type", comment = "字典类型表")
public class EasyDictType extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "字典名称")
    @ApiModelProperty(value = "字典名称")
    @TableField(value = "dict_name")
    private String dictName;

    @Column(comment = "字典类型")
    @ApiModelProperty(value = "字典类型")
    @TableField(value = "dict_type")
    private String dictType;

    @Column(comment = "状态（0正常 1停用）")
    @ApiModelProperty(value = "状态（0正常 1停用）")
    @TableField(value = "status")
    private String status;

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