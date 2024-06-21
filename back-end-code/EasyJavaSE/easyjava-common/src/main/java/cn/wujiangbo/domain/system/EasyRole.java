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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_role表对应的实体对象", description="角色信息表")
@Table(name = "easy_role", comment = "角色信息表")
public class EasyRole extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "角色名称")
    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    @TableField(value = "role_name")
    private String roleName;

    @Column(comment = "角色权限字符串")
    @ApiModelProperty(value = "角色权限字符串")
    @TableField(value = "role_key")
    private String roleKey;

    @Column(comment = "显示顺序")
    @ApiModelProperty(value = "显示顺序")
    @TableField(value = "role_sort")
    private Integer roleSort;

    @Column(comment = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    @ApiModelProperty(value = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    @TableField(value = "data_scope")
    private String dataScope;

    @Column(comment = "菜单树选择项是否关联显示")
    @ApiModelProperty(value = "菜单树选择项是否关联显示")
    @TableField(value = "menu_check_strictly")
    private Boolean menuCheckStrictly;

    @Column(comment = "部门树选择项是否关联显示")
    @ApiModelProperty(value = "部门树选择项是否关联显示")
    @TableField(value = "dept_check_strictly")
    private Boolean deptCheckStrictly;

    @Column(comment = "角色状态（0正常 1停用）")
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
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

    @Column(comment = "备注")
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;


    /***********************************************************************************
    ***********************************************************************************/

    @ApiModelProperty(value = "菜单组")
    @TableField(exist = false)
    private Long[] menuIds;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(exist = false)
    private String updateUserName;

}