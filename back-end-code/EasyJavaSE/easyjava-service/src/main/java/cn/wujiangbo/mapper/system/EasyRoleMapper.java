package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.domain.system.EasyRoleMenu;
import cn.wujiangbo.query.system.EasyRoleQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 角色信息表 Mapper接口
* </p>
*
*/
public interface EasyRoleMapper extends BaseMapper<EasyRole> {

    List<EasyRole> selectMyPage(Page<EasyRole> page, @Param("param") EasyRoleQuery query);

    List<EasyRole> selectRoleAll();

    EasyRole selectRoleById(Long roleId);

    EasyRole checkRoleNameUnique(String roleName);

    void batchRoleMenu(List<EasyRoleMenu> list);

    //通过角色ID删除角色和菜单关联
    void deleteRoleMenuByRoleId(Long id);

    void deleteRoleMenu(Long[] roleIds);

    void deleteRoleByIds(Long[] roleIds);

    List<EasyRole> selectRolePermissionByUserId(Long userId);

    List<EasyRole> selectRolesByUserId(Long userId);
}