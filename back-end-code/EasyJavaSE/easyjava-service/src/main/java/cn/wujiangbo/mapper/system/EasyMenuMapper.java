package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyMenu;
import cn.wujiangbo.query.system.EasyMenuQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>
* 菜单权限表 Mapper接口
* </p>
*
*/
public interface EasyMenuMapper extends BaseMapper<EasyMenu> {

    List<EasyMenu> selectMyPage(Page<EasyMenu> page, @Param("param") EasyMenuQuery query);

    List<EasyMenu> selectMenuTreeByUserId(Long userId);

    List<String> selectMenuPermsByUserId(Long userId);

    List<EasyMenu> selectMenuListByUserId(EasyMenu menu);

    List<Integer> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    EasyMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

    int hasChildByMenuId(Long menuId);

    void deleteRoleMenu(Long menuId);

    List<String> selectAllPerms();

}