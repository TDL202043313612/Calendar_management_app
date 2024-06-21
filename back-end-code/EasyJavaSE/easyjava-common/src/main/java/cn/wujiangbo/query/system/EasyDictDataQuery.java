package cn.wujiangbo.query.system;

import cn.wujiangbo.query.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @desc 字典数据表-查询对象
 *
 */
@Data
public class EasyDictDataQuery extends BaseQuery{

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "字典类型")
    private String dictType;
}