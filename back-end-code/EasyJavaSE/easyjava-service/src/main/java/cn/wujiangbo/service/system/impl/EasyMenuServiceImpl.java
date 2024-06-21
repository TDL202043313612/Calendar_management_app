package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.constants.UserConstants;
import cn.wujiangbo.domain.system.EasyMenu;
import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.dto.system.TreeSelect;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.mapper.system.EasyMenuMapper;
import cn.wujiangbo.mapper.system.EasyRoleMapper;
import cn.wujiangbo.query.system.EasyMenuQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.EasyMenuService;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.vo.system.MetaVo;
import cn.wujiangbo.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyMenuServiceImpl extends ServiceImpl<EasyMenuMapper, EasyMenu> implements EasyMenuService {

    @Autowired
    private EasyMenuMapper easyMenuMapper;

    @Autowired
    private EasyMenuService easyMenuService;

    @Autowired
    private EasyRoleMapper easyRoleMapper;

    //查询列表
    @Override
    public Page<EasyMenu> selectMyPage(EasyMenuQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyMenu> list = easyMenuMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public List<EasyMenu> selectMenuTreeByUserId(Long userId, boolean isAdmin) {
        /**
         * 如果是超级管理员的话，就查询所有菜单即可
         * 否则就需要根据配置的角色信息去查询对应的菜单了
         */
        List<EasyMenu> list = new ArrayList<>();
        if(isAdmin){
            //是超级管理员，查询所有菜单信息即可
            QueryWrapper<EasyMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", "0");//菜单状态（0正常 1停用）
            queryWrapper.eq("visible", "0");//菜单状态（0显示 1隐藏）
            queryWrapper.orderByAsc("parent_id");
            queryWrapper.orderByAsc("order_num");
            list = easyMenuMapper.selectList(queryWrapper);
        } else {
            list = easyMenuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPermsTemp(list);
    }

    //封装菜单信息
    public List<EasyMenu> getChildPermsTemp(List<EasyMenu> allMenus) {

        //装一级分类
        List<EasyMenu> firstMenus = new ArrayList<>();

        //把所有分类存到一个HashMap中
        HashMap<Long, EasyMenu> allMenusMaps = new HashMap<>(allMenus.size());
        for (EasyMenu obj : allMenus) {
            allMenusMaps.put(obj.getId(), obj);
        }

        for (EasyMenu menu : allMenus) {
            if(menu.getParentId().longValue() == 0){
                //查找一级分类
                firstMenus.add(menu);
            }
            else{
                //非一级分类，肯定有父分类
                EasyMenu parentType = allMenusMaps.get(menu.getParentId().longValue());
                parentType.getChildren().add(menu);
            }
        }
        return firstMenus;
    }



    /**
     * 根据父节点的ID获取所有子节点
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<EasyMenu> getChildPerms(List<EasyMenu> list, int parentId)
    {
        List<EasyMenu> returnList = new ArrayList<EasyMenu>();
        for (Iterator<EasyMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            EasyMenu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * @param list
     * @param t
     */
    private void recursionFn(List<EasyMenu> list, EasyMenu t)
    {
        // 得到子节点列表
        List<EasyMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (EasyMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<EasyMenu> list, EasyMenu t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 得到子节点列表
     */
    private List<EasyMenu> getChildList(List<EasyMenu> list, EasyMenu t)
    {
        List<EasyMenu> tlist = new ArrayList<EasyMenu>();
        Iterator<EasyMenu> it = list.iterator();
        while (it.hasNext())
        {
            EasyMenu n = it.next();
            if (n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    @Override
    public List<RouterVo> buildMenus(List<EasyMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (EasyMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            //是否缓存（0缓存 1不缓存）
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), 1 == menu.getIsCache().intValue(), menu.getPath()));
            List<EasyMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), 1 == menu.getIsCache().intValue(), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
            {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public Set<String> getMenuPermission(Long userId, boolean isAdmin) {
        List<String> perms = new ArrayList<>();
        if(isAdmin){
            //超级管理员，则查询所有权限字符串即可
            perms = easyMenuMapper.selectAllPerms();
        }else{
            perms = easyMenuMapper.selectMenuPermsByUserId(userId);
        }
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<EasyMenu> selectMenuList(EasyMenu menu, Long userId, boolean isAdmin) {
        List<EasyMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (isAdmin)
        {
            menuList = easyMenuMapper.selectMenuListByUserId(menu);
        }
        else
        {
            menu.setUserId(userId);
            menuList = easyMenuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    @Override
    public List<EasyMenu> selectMenuList(Long userId, boolean isAdmin) {
        return selectMenuList(new EasyMenu(), userId, isAdmin);
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<EasyMenu> menus) {
        List<EasyMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询菜单树信息
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Integer> selectMenuListByRoleId(Long roleId) {
        EasyRole role = easyRoleMapper.selectRoleById(roleId);
        return easyMenuMapper.selectMenuListByRoleId(roleId, role.getMenuCheckStrictly());
    }

    @Override
    public EasyMenu selectMenuById(Long menuId) {
        return easyMenuMapper.selectById(menuId);
    }

    //新增菜单
    @Override
    public JSONResult saveMenu(EasyMenu easyMenu) {
        //校验菜单名称是否已存在
        Long menuId = StringUtils.isNull(easyMenu.getId()) ? -1L : easyMenu.getId();
        EasyMenu menu = easyMenuMapper.checkMenuNameUnique(easyMenu.getMenuName(), easyMenu.getParentId());
        if(StringUtils.isNotNull(menu) && menu.getId().longValue() != menuId.longValue()){
            throw new MyException(ErrorCode.ERROR_CODE_1030);
        }
        //校验外链地址格式
        if (StringUtils.isNotNull(menu) && UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            throw new MyException(ErrorCode.ERROR_CODE_1031);
        }

        //菜单信息入库
        easyMenuMapper.insert(easyMenu);

        /**
         * 新增菜单下面CRUD按钮菜单信息
         */
        //菜单类型（M目录 C菜单 F按钮）
        if("C".equals(easyMenu.getMenuType())){
            //获取到【路由地址】
            saveButton(easyMenu.getId(), easyMenu.getPath());
        }

        //返回结果集
        return JSONResult.success(true);
    }

    //新增按钮菜单到数据库
    public void saveButton(Long menuId, String path){
        List<EasyMenu> list = new ArrayList<>();

        //新增菜单
        EasyMenu menuAdd = new EasyMenu();
        menuAdd.setParentId(menuId);
        menuAdd.setMenuType("F");
        menuAdd.setMenuName("新增");
        menuAdd.setOrderNum(1);
        menuAdd.setPerms(path + ":save");
        list.add(menuAdd);

        //删除菜单
        EasyMenu menuDelete = new EasyMenu();
        menuDelete.setParentId(menuId);
        menuDelete.setMenuType("F");
        menuDelete.setMenuName("删除");
        menuDelete.setOrderNum(2);
        menuDelete.setPerms(path + ":deleteBatch");
        list.add(menuDelete);

        //修改菜单
        EasyMenu menuUpdate = new EasyMenu();
        menuUpdate.setParentId(menuId);
        menuUpdate.setMenuType("F");
        menuUpdate.setMenuName("修改");
        menuUpdate.setOrderNum(3);
        menuUpdate.setPerms(path + ":update");
        list.add(menuUpdate);

        //分页查询菜单
        EasyMenu menuPageList = new EasyMenu();
        menuPageList.setParentId(menuId);
        menuPageList.setMenuType("F");
        menuPageList.setMenuName("分页列表");
        menuPageList.setOrderNum(4);
        menuPageList.setPerms(path + ":pagelist");
        list.add(menuPageList);

        //导出Excel表
        EasyMenu menuExportExcel = new EasyMenu();
        menuExportExcel.setParentId(menuId);
        menuExportExcel.setMenuType("F");
        menuExportExcel.setMenuName("导出Excel");
        menuExportExcel.setOrderNum(5);
        menuExportExcel.setPerms(path + ":exportExcelFile");
        list.add(menuExportExcel);

        //批量新增
        easyMenuService.saveBatch(list);
    }

    //修改菜单
    @Override
    public JSONResult updateMenu(EasyMenu easyMenu) {
        //校验菜单名称是否已存在
        Long menuId = StringUtils.isNull(easyMenu.getId()) ? -1L : easyMenu.getId();
        EasyMenu menu = easyMenuMapper.checkMenuNameUnique(easyMenu.getMenuName(), easyMenu.getParentId());
        if(StringUtils.isNotNull(menu) && menu.getId().longValue() != menuId.longValue()){
            throw new MyException(ErrorCode.ERROR_CODE_1030);
        }
        //校验外链地址格式
        if (StringUtils.isNotNull(menu) && UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            throw new MyException(ErrorCode.ERROR_CODE_1031);
        }
        //上级菜单不能选择自己
        if (StringUtils.isNotNull(menu) && menu.getId().equals(menu.getParentId()))
        {
            throw new MyException(ErrorCode.ERROR_CODE_1032);
        }

        //修改数据库菜单信息
        easyMenuMapper.updateById(easyMenu);

        //返回结果集
        return JSONResult.success(true);
    }

    /**
     * 是否存在菜单子节点
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = easyMenuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    @Override
    @Transactional
    public JSONResult deleteMenuById(Long menuId) {
        easyMenuMapper.deleteById(menuId);
        //删除关联角色菜单关联表中的信息
        easyMenuMapper.deleteRoleMenu(menuId);
        return JSONResult.success(true);
    }

    @Override
    public List<EasyMenu> selectAllMenuList(EasyMenu menu, Long userId, boolean isAdmin) {
        return selectMenuList(new EasyMenu(), userId, isAdmin);
    }

    ////根据用户ID查询所拥有的所有权限字符串
    @Override
    public Set<String> selectMenuPermsByUserId(Long id, boolean isAdmin) {
        List<String> perms = new ArrayList<>();
        if(isAdmin){
            perms = easyMenuMapper.selectAllPerms();
        }else{
            perms = easyMenuMapper.selectMenuPermsByUserId(id);
        }
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    public List<EasyMenu> buildMenuTree(List<EasyMenu> menus)
    {
        List<EasyMenu> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (EasyMenu dept : menus)
        {
            tempList.add(dept.getId());
        }
        for (Iterator<EasyMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            EasyMenu menu = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }


    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(EasyMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            component = UserConstants.INNER_LINK;
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(EasyMenu menu)
    {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(EasyMenu menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(String.valueOf(menu.getIsFrame())))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS },
                new String[] { "", "" });
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(EasyMenu menu)
    {
        return (String.valueOf(menu.getIsFrame())).equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(EasyMenu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(EasyMenu menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && (String.valueOf(menu.getIsFrame())).equals(UserConstants.NO_FRAME);
    }
}