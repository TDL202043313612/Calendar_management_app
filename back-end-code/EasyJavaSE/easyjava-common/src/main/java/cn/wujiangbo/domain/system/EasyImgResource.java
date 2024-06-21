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
 * 图片资源信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_img_resource 表对应的实体对象", description="图片资源信息表")
@Table(name = "easy_img_resource", comment = "图片资源信息表")
public class EasyImgResource extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @IsAutoIncrement
    @TableId(value = "id", type = IdType.AUTO)
    @Column(comment = "主键ID")
    private Long id;

    @ApiModelProperty(value = "创建用户ID")
    @TableField(value = "create_user_id")
    @Column(comment = "创建用户ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    @Column(comment = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "update_user_id")
    @Column(comment = "更新人ID")
    private Long updateUserId;

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

    @ApiModelProperty(value = "资源编号")
    @TableField(value = "busi_no")
    @Column(comment = "资源编号")
    private String busiNo;

    @ApiModelProperty(value = "功能标识（与业务场景有关，自由指定）")
    @TableField(value = "func_flag")
    @Column(comment = "功能标识（与业务场景有关，自由指定）")
    private String funcFlag;

    @ApiModelProperty(value = "资源类型")
    @TableField(value = "res_type")
    @Column(comment = "资源类型")
    private String resType;

    @ApiModelProperty(value = "资源文件名")
    @TableField(value = "res_name")
    @Column(comment = "资源文件名")
    private String resName;

    @ApiModelProperty(value = "资源全路径")
    @TableField(value = "res_path")
    @Column(comment = "资源全路径")
    private String resPath;

    @ApiModelProperty(value = "资源大小（单位：字节）")
    @TableField(value = "res_size")
    @Column(comment = "资源大小（单位：字节）")
    private Long resSize;

    @ApiModelProperty(value = "资源后缀")
    @TableField(value = "res_suffix")
    @Column(comment = "资源后缀")
    private String resSuffix;
}