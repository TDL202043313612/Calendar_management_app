package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.constants.UserConstants;
import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.domain.system.EasyRoleMenu;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.mapper.system.EasyRoleMapper;
import cn.wujiangbo.query.system.EasyRoleQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.EasyRoleService;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyRoleServiceImpl extends ServiceImpl<EasyRoleMapper, EasyRole> implements EasyRoleService {

    @Autowired
    private EasyRoleMapper easyRoleMapper;

    //查询列表
    @Override
    public Page<EasyRole> selectMyPage(EasyRoleQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyRole> list = easyRoleMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public List<EasyRole> selectRoleAll() {
        return easyRoleMapper.selectRoleAll();
    }

    /**
     * 校验角色名称是否唯一
     */
    @Override
    public String checkRoleNameUnique(EasyRole easyRole) {
        Long roleId = StringUtils.isNull(easyRole.getId()) ? -1L : easyRole.getId();
        EasyRole info = easyRoleMapper.checkRoleNameUnique(easyRole.getRoleName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增角色
     */
    @Override
    @Transactional
    public JSONResult insertRole(EasyRole easyRole) {
        // 新增角色信息
        easyRoleMapper.insert(easyRole);
        //新增角色菜单关联信息
        insertRoleMenu(easyRole);
        return JSONResult.success(true);
    }

    /**
     * 修改角色
     */
    @Override
    @Transactional
    public JSONResult updateRole(EasyRole easyRole) {
        // 修改角色信息
        easyRoleMapper.updateById(easyRole);
        // 删除角色与菜单关联
        easyRoleMapper.deleteRoleMenuByRoleId(easyRole.getId());
        insertRoleMenu(easyRole);
        return JSONResult.success(true);
    }

    @Override
    public JSONResult updateRoleStatus(EasyRole role) {
        easyRoleMapper.updateById(role);
        return JSONResult.success(true);
    }

    /**
     * 批量删除角色信息
     */
    @Override
    @Transactional
    public JSONResult deleteRole(Long[] roleIds) {
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(roleId);
        }
        // 删除角色与菜单关联
        easyRoleMapper.deleteRoleMenu(roleIds);
        easyRoleMapper.deleteRoleByIds(roleIds);
        return JSONResult.success(true);
    }

    @Override
    public void checkRoleAllowed(Long id){
        if(MyTools.isAdminRole(id)){
            throw new MyException(ErrorCode.ERROR_CODE_1035);
        }
    }

    @Override
    public Set<String> getRolePermission(Long userId) {
        List<EasyRole> perms = easyRoleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (EasyRole perm : perms)
        {
            if (StringUtils.isNotNull(perm) && perm.getRoleKey() != null)
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;

    }

    /**
     * 新增角色菜单信息
     */
    private void insertRoleMenu(EasyRole role) {
        // 新增用户与角色管理
        List<EasyRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds())
        {
            EasyRoleMenu rm = new EasyRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            easyRoleMapper.batchRoleMenu(list);
        }
    }
}