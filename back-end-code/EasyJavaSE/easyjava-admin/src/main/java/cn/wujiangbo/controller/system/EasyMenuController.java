package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyMenuService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyMenu;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc 菜单权限表 API接口
 *
 */
@RestController
@RequestMapping("/easyMenu")
public class EasyMenuController extends BaseController{

    @Autowired
    public EasyMenuService easyMenuService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 获取菜单下拉树列表（查询所有菜单信息）
     */
    @GetMapping("/treeselect")
    public JSONResult treeselect(EasyMenu menu)
    {
        List<EasyMenu> menus = easyMenuService.selectAllMenuList(menu, getUserId(), getSuperAdmin());
        return JSONResult.success(easyMenuService.buildMenuTreeSelect(menus));
    }

    /**
     * 获取菜单下拉树列表（查询当前用户有权限看到的菜单下拉列表）
     */
    @GetMapping("/treeselectByUserId")
    public JSONResult treeselectByUserId(EasyMenu menu)
    {
        List<EasyMenu> menus = easyMenuService.selectMenuList(menu, getUserId(), getSuperAdmin());
        return JSONResult.success(easyMenuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public JSONResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<EasyMenu> menus = easyMenuService.selectAllMenuList(new EasyMenu(), getUserId(), getSuperAdmin());
        Map<String, Object> map = new HashMap<>();
        map.put("checkedKeys", easyMenuService.selectMenuListByRoleId(roleId));
        map.put("menus", easyMenuService.buildMenuTreeSelect(menus));
        return JSONResult.success(map);
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/getRouters")
    public JSONResult getRouters()
    {
        List<EasyMenu> menus = easyMenuService.selectMenuTreeByUserId(getUserId(), getSuperAdmin());
        return JSONResult.success(easyMenuService.buildMenus(menus));
    }

    /**
     * 新增菜单
     * @date 2022-05-23
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyMenu:save")
    @MyLog(title = "新增菜单", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyMenu easyMenu){
        easyMenu.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyMenu.setCreateUserId(getUserId());
        easyMenu.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyMenu.setUpdateUserId(getUserId());
        return easyMenuService.saveMenu(easyMenu);
    }

    /**
     * 修改菜单权限表
     * @date 2022-05-23
     */
    @PostMapping(value="/update")
    @CheckPermission(per = "easyMenu:update")
    @MyLog(title = "修改菜单表", businessType = BusinessType.UPDATE)
    public JSONResult update(@RequestBody EasyMenu easyMenu){
        easyMenu.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyMenu.setUpdateUserId(getUserId());
        return easyMenuService.updateMenu(easyMenu);
    }

    /**
    * 根据ID删除【菜单权限表】信息
    * @date 2022-05-23
    */
    @DeleteMapping(value="/{menuId}")
    @CheckPermission(per = "easyMenu:delete")
    @MyLog(title = "删除[菜单表]信息", businessType = BusinessType.DELETE)
    public JSONResult delete(@PathVariable("menuId") Long menuId){
        if (easyMenuService.hasChildByMenuId(menuId))
        {
            return JSONResult.error(ErrorCode.ERROR_CODE_1033);
        }
        return easyMenuService.deleteMenuById(menuId);
    }

    /**
    * 根据菜单编号获取详细信息
    * @date 2022-05-23
    */
    @GetMapping(value = "/{menuId}")
    public JSONResult get(@PathVariable("menuId")Long menuId)
    {
        return JSONResult.success(easyMenuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单列表（菜单管理页面的树形列表数据）
     */
    @PostMapping(value = "/list")
    @MyLog(title = "分页查询【菜单表】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyMenu:list")
    public JSONResult list(@RequestBody EasyMenu menu)
    {
        List<EasyMenu> menus = easyMenuService.selectAllMenuList(menu, getUserId(), getSuperAdmin());
        return JSONResult.success(menus);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}