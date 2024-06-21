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
 * 公告信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_system_msg 表对应的实体对象", description="公告信息表")
@Table(name = "easy_system_msg", comment = "公告信息表")
public class EasySystemMsg extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @IsAutoIncrement
    @TableId(value = "id", type = IdType.AUTO)
    @Column(comment = "主键ID")
    private Long id;

    @ApiModelProperty(value = "消息创建时间")
    @TableField(value = "create_time")
    @Column(comment = "消息创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "消息发布者工号")
    @TableField(value = "publish_empno")
    @Column(comment = "消息发布者工号")
    private String publishEmpno;

    @ApiModelProperty(value = "消息发布者名称")
    @TableField(value = "publish_name")
    @Column(comment = "消息发布者名称")
    private String publishName;

    @ApiModelProperty(value = "消息标题")
    @TableField(value = "msg_title")
    @Column(comment = "消息标题")
    private String msgTitle;

    @ApiModelProperty(value = "消息内容")
    @TableField(value = "msg_content")
    @Column(comment = "消息内容")
    private String msgContent;

    @ApiModelProperty(value = "消息级别（1：非常紧急；2：紧急；3：一般）")
    @TableField(value = "msg_level")
    @Column(comment = "消息级别（1：非常紧急；2：紧急；3：一般）")
    private Integer msgLevel;

    @ApiModelProperty(value = "接收人ID（关联easy_user表主键）")
    @TableField(value = "receive_user_id")
    @Column(comment = "接收人ID（关联easy_user表主键）")
    private Long receiveUserId;

    @ApiModelProperty(value = "接收人名称")
    @TableField(value = "receive_user_name")
    @Column(comment = "接收人名称")
    private String receiveUserName;

    @ApiModelProperty(value = "消息状态（0：已读；1：未读）")
    @TableField(value = "msg_status")
    @Column(comment = "消息状态（0：已读；1：未读）")
    private String msgStatus;
}