package cn.wujiangbo.domain.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.wujiangbo.domain.base.BaseDomain;
import cn.wujiangbo.annotation.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新闻信息表
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="app_news 表对应的实体对象", description="新闻信息表")
@Table(name = "app_news", comment = "新闻信息表")
public class AppNews extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @IsAutoIncrement
    @TableId(value = "id", type = IdType.AUTO)
    @Column(comment = "主键ID")
    @Excel(name = "主键ID")
    private Long id;

    @ApiModelProperty(value = "提交人ID")
    @TableField(value = "create_user_id")
    @Column(comment = "提交人ID")
    @Excel(name = "提交人ID")
    private Long createUserId;

    @ApiModelProperty(value = "提交人")
    @TableField(value = "create_user_name")
    @Column(comment = "提交人")
    @Excel(name = "提交人")
    private String createUserName;

    @ApiModelProperty(value = "提交时间")
    @TableField(value = "create_time")
    @Column(comment = "提交时间")
    @Excel(name = "提交时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "update_user_id")
    @Column(comment = "更新人ID")
    @Excel(name = "更新人ID")
    private Long updateUserId;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(value = "update_user_name")
    @Column(comment = "更新人姓名")
    @Excel(name = "更新人姓名")
    private String updateUserName;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    @Column(comment = "更新时间")
    @Excel(name = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "新闻标题")
    @TableField(value = "new_title")
    @Column(comment = "新闻标题")
    @Excel(name = "新闻标题")
    private String newTitle;

    @ApiModelProperty(value = "新闻内容")
    @TableField(value = "new_content")
    @Column(comment = "新闻内容")
    @Excel(name = "新闻内容")
    private String newContent;

    @ApiModelProperty(value = "新闻封面照")
    @TableField(value = "new_img")
    @Column(comment = "新闻封面照")
    @Excel(name = "新闻封面照")
    private String newImg;

    @ApiModelProperty(value = "新闻类型(热点;娱乐;游戏;体育;健康;军事)")
    @TableField(value = "new_type")
    @Column(comment = "新闻类型(热点;娱乐;游戏;体育;健康;军事)")
    @Excel(name = "新闻类型(热点;娱乐;游戏;体育;健康;军事)")
    private String newType;

    @ApiModelProperty(value = "新闻阅读量")
    @TableField(value = "new_read_count")
    @Column(comment = "新闻阅读量")
    @Excel(name = "新闻阅读量")
    private Integer newReadCount;

    @ApiModelProperty(value = "新闻作者")
    @TableField(value = "new_author")
    @Column(comment = "新闻作者")
    @Excel(name = "新闻作者")
    private String newAuthor;
}