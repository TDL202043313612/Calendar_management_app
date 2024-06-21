package cn.wujiangbo.domain.system;

import lombok.Data;

/**
 * @description: 角色和菜单关联
 */
@Data
public class EasyRoleMenu {

    /** 角色ID */
    private Long roleId;

    /** 菜单ID */
    private Long menuId;
}
