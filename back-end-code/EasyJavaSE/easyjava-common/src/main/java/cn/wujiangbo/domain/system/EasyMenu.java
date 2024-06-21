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
 * 菜单权限表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_menu表对应的实体对象", description="菜单权限表")
@Table(name = "easy_menu", comment = "菜单权限表")
public class EasyMenu extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "菜单名称")
    @ApiModelProperty(value = "菜单名称")
    @TableField(value = "menu_name")
    private String menuName;

    @Column(comment = "父菜单ID")
    @ApiModelProperty(value = "父菜单ID")
    @TableField(value = "parent_id")
    private Long parentId;

    @Column(comment = "显示顺序")
    @ApiModelProperty(value = "显示顺序")
    @TableField(value = "order_num")
    private Integer orderNum;

    @Column(comment = "路由地址")
    @ApiModelProperty(value = "路由地址")
    @TableField(value = "path")
    private String path;

    @Column(comment = "组件路径")
    @ApiModelProperty(value = "组件路径")
    @TableField(value = "component")
    private String component;

    @Column(comment = "路由参数")
    @ApiModelProperty(value = "路由参数")
    @TableField(value = "query")
    private String query;

    @Column(comment = "是否为外链（0是 1否）")
    @ApiModelProperty(value = "是否为外链（0是 1否）")
    @TableField(value = "is_frame")
    private Integer isFrame;

    @Column(comment = "是否缓存（0缓存 1不缓存）")
    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    @TableField(value = "is_cache")
    private Integer isCache;

    @Column(comment = "菜单类型（M目录 C菜单 F按钮）")
    @ApiModelProperty(value = "菜单类型（M目录 C菜单 F按钮）")
    @TableField(value = "menu_type")
    private String menuType;

    @Column(comment = "菜单状态（0显示 1隐藏）")
    @ApiModelProperty(value = "菜单状态（0显示 1隐藏）")
    @TableField(value = "visible")
    private String visible;

    @Column(comment = "菜单状态（0正常 1停用）")
    @ApiModelProperty(value = "菜单状态（0正常 1停用）")
    @TableField(value = "status")
    private String status;

    @Column(comment = "权限标识")
    @ApiModelProperty(value = "权限标识")
    @TableField(value = "perms")
    private String perms;

    @Column(comment = "菜单图标")
    @ApiModelProperty(value = "菜单图标")
    @TableField(value = "icon")
    private String icon;

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

    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    private String updateUserName;

    //子菜单
    @TableField(exist = false)
    private List<EasyMenu> children = new ArrayList<>();

}