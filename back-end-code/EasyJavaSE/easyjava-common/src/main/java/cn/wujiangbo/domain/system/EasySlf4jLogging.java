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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * Slf4j日志表
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2023-06-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="easy_slf4j_logging 表对应的实体对象", description="Slf4j日志表")
@Table(name = "easy_slf4j_logging", comment = "Slf4j日志表")
public class EasySlf4jLogging extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @IsAutoIncrement
    @TableId(value = "id", type = IdType.AUTO)
    @Column(comment = "主键ID")
    private Long id;

    @ApiModelProperty(value = "日志级别")
    @TableField(value = "log_level")
    @Column(comment = "日志级别")
    private String logLevel;

    @ApiModelProperty(value = "日志内容")
    @TableField(value = "log_content")
    @Column(comment = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "日志时间")
    @TableField(value = "log_time")
    @Column(comment = "日志时间")
    private LocalDateTime logTime;

    @ApiModelProperty(value = "日志类路径")
    @TableField(value = "log_class")
    @Column(comment = "日志类路径")
    private String logClass;

    @ApiModelProperty(value = "日志线程")
    @TableField(value = "log_thread")
    @Column(comment = "日志线程")
    private String logThread;

    @ApiModelProperty(value = "全局跟踪ID")
    @TableField(value = "track_id")
    @Column(comment = "全局跟踪ID")
    private String trackId;
}