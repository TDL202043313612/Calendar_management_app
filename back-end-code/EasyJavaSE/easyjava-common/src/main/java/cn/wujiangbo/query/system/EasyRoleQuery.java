package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import lombok.Data;

/**
 * @desc 角色信息表-查询对象
 *
 */
@Data
public class EasyRoleQuery extends BaseQuery{

    private String roleName;
    private String status;
}