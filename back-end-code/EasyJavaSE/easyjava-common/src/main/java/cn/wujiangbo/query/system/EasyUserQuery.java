package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import lombok.Data;

/**
 * @desc 用户信息表-查询对象
 *
 */
@Data
public class EasyUserQuery extends BaseQuery{

    private String userName;
    private String phonenumber;
    private String status;
    private Long deptId;
}