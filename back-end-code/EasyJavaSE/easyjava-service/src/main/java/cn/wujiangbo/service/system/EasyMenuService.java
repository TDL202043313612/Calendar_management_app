package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyMenu;
import cn.wujiangbo.dto.system.TreeSelect;
import cn.wujiangbo.query.system.EasyMenuQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 */
public interface EasyMenuService extends IService<EasyMenu> {

    //分页查询
    Page<EasyMenu> selectMyPage(EasyMenuQuery query);

    List<EasyMenu> selectMenuTreeByUserId(Long userId, boolean isAdmin);

    List<RouterVo> buildMenus(List<EasyMenu> menus);

    Set<String> getMenuPermission(Long userId, boolean isAdmin);

    List<EasyMenu> selectMenuList(EasyMenu menu, Long userId, boolean isAdmin);

    /**
     * 根据用户查询系统菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<EasyMenu> selectMenuList(Long userId, boolean isAdmin);

    List<TreeSelect> buildMenuTreeSelect(List<EasyMenu> menus);

    List<Integer> selectMenuListByRoleId(Long roleId);

    EasyMenu selectMenuById(Long menuId);

    JSONResult saveMenu(EasyMenu easyMenu);

    JSONResult updateMenu(EasyMenu easyMenu);

    boolean hasChildByMenuId(Long menuId);

    JSONResult deleteMenuById(Long menuId);

    List<EasyMenu> selectAllMenuList(EasyMenu menu, Long userId, boolean isAdmin);

    Set<String> selectMenuPermsByUserId(Long id, boolean isAdmin);
}