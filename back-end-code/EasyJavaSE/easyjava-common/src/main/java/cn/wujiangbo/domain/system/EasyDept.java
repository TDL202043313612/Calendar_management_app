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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_dept表对应的实体对象", description="部门表")
@Table(name = "easy_dept", comment = "部门表")
public class EasyDept extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "父部门ID")
    @ApiModelProperty(value = "父部门ID")
    @TableField(value = "parent_id")
    private Long parentId;

    @Column(comment = "祖级列表(全路径)")
    @ApiModelProperty(value = "祖级列表(全路径)")
    @TableField(value = "ancestors")
    private String ancestors;

    @Column(comment = "部门名称")
    @ApiModelProperty(value = "部门名称")
    @TableField(value = "dept_name")
    private String deptName;

    @Column(comment = "显示顺序")
    @ApiModelProperty(value = "显示顺序")
    @TableField(value = "order_num")
    private Integer orderNum;

    @Column(comment = "负责人")
    @ApiModelProperty(value = "负责人")
    @TableField(value = "leader")
    private String leader;

    @Column(comment = "联系电话")
    @ApiModelProperty(value = "联系电话")
    @TableField(value = "phone")
    private String phone;

    @Column(comment = "邮箱")
    @ApiModelProperty(value = "邮箱")
    @TableField(value = "email")
    private String email;

    @Column(comment = "部门状态（0正常 1停用）")
    @ApiModelProperty(value = "部门状态（0正常 1停用）")
    @TableField(value = "status")
    private String status;

    @Column(comment = "删除标志（0代表存在 2代表删除）")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField(value = "del_flag")
    private String delFlag;

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

    /***********************************************************************************
    ***********************************************************************************/

    @ApiModelProperty(value = "子部门集合")
    @TableField(exist = false)
    private List<EasyDept> children = new ArrayList<>();

    @ApiModelProperty(value = "上级部门名称")
    @TableField(exist = false)
    private String parentName;

    @ApiModelProperty(value = "最后更新人名称")
    @TableField(exist = false)
    private String updateUserName;

}