package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.query.system.EasyRoleQuery;
import cn.wujiangbo.result.JSONResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 */
public interface EasyRoleService extends IService<EasyRole> {

    //分页查询
    Page<EasyRole> selectMyPage(EasyRoleQuery query);

    /**
     * 查询所有角色
     * @return 角色列表
     */
    List<EasyRole> selectRoleAll();

    String checkRoleNameUnique(EasyRole easyRole);

    JSONResult insertRole(EasyRole easyRole);

    JSONResult updateRole(EasyRole easyRole);

    JSONResult updateRoleStatus(EasyRole role);

    JSONResult deleteRole(Long[] ids);

    void checkRoleAllowed(Long id);

    Set<String> getRolePermission(Long userId);
}