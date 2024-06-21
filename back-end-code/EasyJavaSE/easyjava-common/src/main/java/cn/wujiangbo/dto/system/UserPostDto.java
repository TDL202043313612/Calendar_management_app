package cn.wujiangbo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 类描述
 *
 */
@Data
@ApiModel(value="用户岗位关联表对应实体对象", description="用户岗位关联表对应实体对象")
public class UserPostDto {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "岗位ID")
    private Long postId;
}
