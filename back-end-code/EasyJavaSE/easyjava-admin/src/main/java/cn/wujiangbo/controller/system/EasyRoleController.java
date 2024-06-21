package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyRoleService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.constants.UserConstants;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.query.system.EasyRoleQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @desc 角色信息表 API接口
 *
 */
@RestController
@RequestMapping("/easyRole")
public class EasyRoleController extends BaseController{

    @Autowired
    public EasyRoleService easyRoleService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 新增角色信息表
     * @date 2022-05-27
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyRole:add")
    @MyLog(title = "新增角色信息表", businessType = BusinessType.INSERT)
    public JSONResult save(@Validated @RequestBody EasyRole easyRole){
        //验证角色名称是否已存在
        if (UserConstants.NOT_UNIQUE.equals(easyRoleService.checkRoleNameUnique(easyRole)))
        {
            throw new MyException(ErrorCode.ERROR_CODE_1034);
        }
        easyRole.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyRole.setCreateUserId(getUserId());
        easyRole.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyRole.setUpdateUserId(getUserId());
        return easyRoleService.insertRole(easyRole);
    }

    /**
     * 修改角色信息表
     * @date 2022-05-27
     */
    @PostMapping(value="/update")
    @CheckPermission(per = "easyRole:update")
    @MyLog(title = "修改角色信息表", businessType = BusinessType.UPDATE)
    public JSONResult update(@Validated @RequestBody EasyRole easyRole){
        //验证角色名称是否已存在
        if (UserConstants.NOT_UNIQUE.equals(easyRoleService.checkRoleNameUnique(easyRole)))
        {
            throw new MyException(ErrorCode.ERROR_CODE_1034);
        }
        easyRole.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyRole.setUpdateUserId(getUserId());
        return easyRoleService.updateRole(easyRole);
    }

    /**
     * 状态修改
     */
    @MyLog(title = "修改角色状态", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @CheckPermission(per = "easyRole:changeStatus")
    public JSONResult changeStatus(@RequestBody EasyRole role) {
        role.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        role.setUpdateUserId(getUserId());
        //校验当前角色是否允许被操作
        easyRoleService.checkRoleAllowed(role.getId());
        return easyRoleService.updateRoleStatus(role);
    }

    /**
     * 删除[角色信息表]信息
    * @date 2022-05-27
    */
    @DeleteMapping(value="/{roleIds}")
    @CheckPermission(per = "easyRole:delete")
    @MyLog(title = "删除[角色信息表]信息", businessType = BusinessType.DELETE)
    public JSONResult deleteRole(@PathVariable("roleIds") Long[] ids){
        return easyRoleService.deleteRole(ids);
    }

    /**
    * 根据ID查询【角色信息表】信息
    * @date 2022-05-27
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[角色信息表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyRoleService.getById(id));
    }


    /**
    * 查看【角色信息表】所有数据（不分页）
    * @date 2022-05-27
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询[角色信息表]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        List<EasyRole> list = easyRoleService.list(null);
        return JSONResult.success(list);
    }

    /**
     * 分页查询【角色信息表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-27
     */
    @PostMapping(value = "/pagelist")
    @CheckPermission(per = "easyRole:pagelist")
    @MyLog(title = "分页查询【角色信息表】数据", businessType = BusinessType.QUERY)
    public JSONResult pagelist(@RequestBody EasyRoleQuery query)
    {
        Page<EasyRole> page = easyRoleService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}