package cn.wujiangbo.domain.base;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 */
@Data
public class BaseDomain implements Serializable {

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String depName;

    @ApiModelProperty(value = "文件在OSS中的存储路径")
    @TableField(exist = false)
    private String ossPath;
}