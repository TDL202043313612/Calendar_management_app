package cn.wujiangbo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 类描述
 *
 */
@Data
@ApiModel(value="用户角色关联表对应实体对象", description="用户角色关联表对应实体对象")
public class UserRoleDto {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    public UserRoleDto() {
    }

    public UserRoleDto(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
