package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @desc 岗位信息表-查询对象
 *
 */
@Data
public class EasyPostQuery extends BaseQuery{

    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;
}