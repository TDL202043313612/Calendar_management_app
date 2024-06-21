package cn.wujiangbo.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 部门人员下拉树
 *
 */
public class UserTreeSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 是否可被选择 */
    private boolean isDisabled;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UserTreeSelect> children;

    public UserTreeSelect()
    {
    }

    public UserTreeSelect(DeptUserDto dept)
    {
        this.id = dept.getId();
        this.label = dept.getDeptName();
        this.isDisabled = "true".equals(dept.getIsDisabled());
        this.children = dept.getChildren().stream().map(UserTreeSelect::new).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public List<UserTreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<UserTreeSelect> children) {
        this.children = children;
    }
}