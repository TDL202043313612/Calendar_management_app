package cn.wujiangbo.domain.system;

import cn.wujiangbo.annotation.excel.Excel;
import cn.wujiangbo.annotation.excel.Excel.ColumnType;
import cn.wujiangbo.annotation.excel.Excel.Type;
import cn.wujiangbo.annotation.excel.Excels;
import cn.wujiangbo.domain.base.BaseDomain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="easy_user表对应的实体对象", description="用户信息表")
@Table(name = "easy_user", comment = "用户信息表")
public class EasyUser extends BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    @IsAutoIncrement
    @Column(comment = "用户ID")
    private Long id;

    @ApiModelProperty(value = "部门ID")
    @TableField(value = "dept_id")
    @Column(comment = "部门ID")
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    @ApiModelProperty(value = "登录账号")
    @TableField(value = "user_name")
    @NotBlank(message = "登录账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    @Excel(name = "登录账号")
    @Column(comment = "登录账号")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    @TableField(value = "nick_name")
    @Excel(name = "用户昵称")
    @Column(comment = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型（00系统用户）")
    @TableField(value = "user_type")
    @Excel(name = "用户类型", readConverterExp = "00=系统用户")
    @Column(comment = "用户类型（00系统用户）")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    @TableField(value = "email")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Excel(name = "用户邮箱")
    @Column(comment = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    @TableField(value = "phonenumber")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    @Excel(name = "手机号")
    @Column(comment = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    @TableField(value = "sex")
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @Column(comment = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    @TableField(value = "avatar")
    @Column(comment = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "登录密码")
    @TableField(value = "password")
    @Column(comment = "登录密码")
    private String password;

    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    @TableField(value = "status")
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    @Column(comment = "帐号状态（0正常 1停用）")
    private String status;

    @Column(comment = "删除标志（0代表存在 2代表删除）")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField(value = "del_flag")
    private String delFlag;

    @Column(comment = "最后登录IP")
    @ApiModelProperty(value = "最后登录IP")
    @TableField(value = "login_ip")
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    @Column(comment = "最后登录时间")
    @ApiModelProperty(value = "最后登录时间")
    @TableField(value = "login_date")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private LocalDateTime loginDate;

    @Column(comment = "创建者ID")
    @ApiModelProperty(value = "创建者ID")
    @TableField(value = "create_user_id")
    private Long createUserId;

    @Column(comment = "创建时间")
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Column(comment = "更新者ID")
    @ApiModelProperty(value = "更新者ID")
    @TableField(value = "update_user_id")
    private Long updateUserId;

    @Column(comment = "更新时间")
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Column(comment = "备注")
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    @Column(comment = "员工编号")
    @ApiModelProperty(value = "员工编号")
    @TableField(value = "emp_no")
    @Excel(name = "员工编号", type = Type.EXPORT)
    private String empNo;

    /***********************************************************************************
     * 上面是自动生成代码
     * 下面是手动新增代码
    ***********************************************************************************/

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createUserName;

    @ApiModelProperty(value = "更新人名称")
    @TableField(exist = false)
    private String updateUserName;

    @ApiModelProperty(value = "岗位名称")
    @TableField(exist = false)
    private String postName;

    @ApiModelProperty(value = "所属部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "角色对象集合")
    @TableField(exist = false)
    private List<EasyRole> roles;

    @ApiModelProperty(value = "部门对象")
    @TableField(exist = false)
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private EasyDept dept;

    @ApiModelProperty(value = "角色组")
    @TableField(exist = false)
    private Long[] roleIds;

    @ApiModelProperty(value = "岗位组")
    @TableField(exist = false)
    private Long[] postIds;

    @ApiModelProperty(value = "旧密码")
    @TableField(exist = false)
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @TableField(exist = false)
    private String newPassword;

    /** 角色ID */
    @TableField(exist = false)
    private Long roleId;

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    //ID等于12的用户是超级管理员
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 12L == userId;
    }

    public EasyUser(){}

    public EasyUser(Long id) {
        this.id = id;
    }
}