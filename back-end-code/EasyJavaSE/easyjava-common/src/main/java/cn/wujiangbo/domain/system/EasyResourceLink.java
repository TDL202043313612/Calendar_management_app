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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源链接表
 * </p>
 *
 */
@Data
@ApiModel(value="easy_resource_link 表对应的实体对象", description="资源链接表")
@Table(name = "easy_resource_link", comment = "资源链接表")
public class EasyResourceLink extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @IsAutoIncrement
    @TableId(value = "id", type = IdType.AUTO)
    @Column(comment = "主键ID")
    private Long id;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(value = "create_user_name")
    @Column(comment = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    @Column(comment = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(value = "update_user_name")
    @Column(comment = "更新人姓名")
    private String updateUserName;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    @Column(comment = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注1")
    @TableField(value = "bak1")
    @Column(comment = "备注1")
    private String bak1;

    @ApiModelProperty(value = "备注2")
    @TableField(value = "bak2")
    @Column(comment = "备注2")
    private String bak2;

    @ApiModelProperty(value = "备注3")
    @TableField(value = "bak3")
    @Column(comment = "备注3")
    private String bak3;

    @ApiModelProperty(value = "资源名称")
    @TableField(value = "res_name")
    @Column(comment = "资源名称")
    private String resName;

    @ApiModelProperty(value = "资源简介描述")
    @TableField(value = "res_intro")
    @Column(comment = "资源简介描述")
    private String resIntro;

    @ApiModelProperty(value = "资源链接地址")
    @TableField(value = "res_link")
    @Column(comment = "资源链接地址")
    private String resLink;

    @ApiModelProperty(value = "资源状态（0：正常；1：停用）")
    @TableField(value = "status")
    @Column(comment = "资源状态（0：正常；1：停用）")
    private Integer status;

    @ApiModelProperty(value = "点击量")
    @TableField(value = "click_count")
    @Column(comment = "点击量")
    private Integer clickCount;

    @ApiModelProperty(value = "封面地址")
    @TableField(value = "photo_path")
    @Column(comment = "封面地址")
    private String photoPath;
}