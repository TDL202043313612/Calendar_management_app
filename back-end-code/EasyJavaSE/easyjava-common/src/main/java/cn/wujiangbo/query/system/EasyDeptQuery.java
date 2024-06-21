package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import lombok.Data;

/**
 * @desc 部门表-查询对象
 *
 */
@Data
public class EasyDeptQuery extends BaseQuery{

    private Long deptId;
    private Long parentId;
    private String deptName;
    private String status;//部门状态（0正常 1停用）
    private String delFlag;//删除标志（0代表存在 2代表删除）
}