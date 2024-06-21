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
 * 系统消息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_message表对应的实体对象", description="系统消息表")
@Table(name = "easy_message", comment = "系统消息表")
public class EasyMessage extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    @Column(comment = "主键ID")
    private Long id;

    @Column(comment = "创建人ID")
    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "create_user_id")
    private Long createUserId;

    @Column(comment = "发布人姓名")
    @ApiModelProperty(value = "发布人姓名")
    @TableField(value = "create_user_name")
    private String createUserName;

    @Column(comment = "创建时间")
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Column(comment = "消息标题")
    @ApiModelProperty(value = "消息标题")
    @TableField(value = "title")
    private String title;

    @Column(comment = "接收人ID")
    @ApiModelProperty(value = "接收人ID")
    @TableField(value = "receive_user_id")
    private Long receiveUserId;

    @Column(comment = "消息状态（0已读 1未读）")
    @ApiModelProperty(value = "消息状态（0已读 1未读）")
    @TableField(value = "msg_status")
    private String msgStatus;

    @Column(comment = "消息内容")
    @ApiModelProperty(value = "消息内容")
    @TableField(value = "msg_content")
    private String msgContent;

    @ApiModelProperty(value = "多选接收人ID集合")
    @TableField(exist = false)
    private Long[] receiveUserIds;

    @ApiModelProperty(value = "接收人名称")
    @TableField(exist = false)
    private String receiveUserName;
}