package cn.wujiangbo.dto.system;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * 部门用户树模型
 *
 */
@Data
@ToString
public class DeptUserTreeDto implements Serializable {

    private String id;
    private String parentId;
    private String name;
    // 前端数据能否被选中，1代表可以，0代表在tree上不能被选中
    private Integer type;
    private Integer orderNum;
}