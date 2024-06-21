package cn.wujiangbo.dto.system;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Data
public class DeptUserDto {

    private String id;
    private String parentId;
    private String deptName;

    /** 是否可被选择 */
    private String isDisabled;

    private List<DeptUserDto> children = new ArrayList<>();
}