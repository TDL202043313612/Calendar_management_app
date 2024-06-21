package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @desc 字典类型表-查询对象
 *
 */
@Data
public class EasyDictTypeQuery extends BaseQuery{

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;
}