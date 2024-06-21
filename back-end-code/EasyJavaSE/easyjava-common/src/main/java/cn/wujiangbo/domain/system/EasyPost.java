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
 * 岗位信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_post表对应的实体对象", description="岗位信息表")
@Table(name = "easy_post", comment = "岗位信息表")
public class EasyPost extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "岗位编码")
    @ApiModelProperty(value = "岗位编码")
    @TableField(value = "post_code")
    private String postCode;

    @Column(comment = "岗位名称")
    @ApiModelProperty(value = "岗位名称")
    @TableField(value = "post_name")
    private String postName;

    @Column(comment = "显示顺序")
    @ApiModelProperty(value = "显示顺序")
    @TableField(value = "post_sort")
    private Integer postSort;

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