package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.constants.UserConstants;
import cn.wujiangbo.domain.system.EasyDept;
import cn.wujiangbo.dto.system.TreeSelect;
import cn.wujiangbo.mapper.system.EasyDeptMapper;
import cn.wujiangbo.query.system.EasyDeptQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.EasyDeptService;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.uuid.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyDeptServiceImpl extends ServiceImpl<EasyDeptMapper, EasyDept> implements EasyDeptService
{

    @Autowired
    private EasyDeptMapper easyDeptMapper;

    @Autowired
    private CommonTools commonTools;

    //查询列表
    @Override
    public Page<EasyDept> selectMyPage(EasyDeptQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyDept> list = easyDeptMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public List<EasyDept> selectDeptList(EasyDeptQuery dept) {
        return easyDeptMapper.selectDeptList(dept);
    }

    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<EasyDept> depts) {
        List<EasyDept> deptTrees = commonTools.getDeptTreesData();
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<EasyDept> buildDeptTree(List<EasyDept> depts)
    {
        List<EasyDept> returnList = new ArrayList<EasyDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (EasyDept dept : depts)
        {
            tempList.add(dept.getId());
        }
        for (EasyDept dept : depts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    @Override
    public String checkDeptNameUnique(EasyDept easyDept) {
        Long deptId = StringUtils.isNull(easyDept.getId()) ? -1L : easyDept.getId();
        EasyDept info = easyDeptMapper.checkDeptNameUnique(easyDept.getDeptName(), easyDept.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != deptId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return easyDeptMapper.selectNormalChildrenDeptById(deptId);
    }

    @Override
    public JSONResult updateDept(EasyDept dept) {
        EasyDept newParentDept = easyDeptMapper.selectDeptById(dept.getParentId());
        EasyDept oldDept = easyDeptMapper.selectDeptById(dept.getId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getId(), newAncestors, oldAncestors);
        }
        easyDeptMapper.updateById(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return JSONResult.success(true);
    }

    @Override
    public List<EasyDept> deptList(EasyDeptQuery query) {
        return easyDeptMapper.deptList(query);
    }

    @Override
    public JSONResult getDeptTreeData(EasyDeptQuery query) {
        //status：部门状态（0正常 1停用）
        QueryWrapper<EasyDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        List<EasyDept> deptList = easyDeptMapper.selectList(queryWrapper);
        return JSONResult.success(handleList(deptList));
    }

    //该部门下所有员工和子部门数据将全部被删除
    @Override
    public void removeBatchDept(Long id) {
        easyDeptMapper.removeBatchDept(id);
    }

    public List<EasyDept> handleList(List<EasyDept> menuList){
        //装一级分类
        List<EasyDept> firstCourseTypes = new ArrayList<>();
        //把所有分类存到一个Map中
        Map<Long, EasyDept> allCourseTypesMaps = new HashMap<>(menuList.size());
        for (EasyDept obj : menuList) {
            allCourseTypesMaps.put(obj.getId(), obj);
        }
        for (EasyDept obj : menuList) {
            if(obj.getParentId() == null || obj.getParentId().longValue() == 0){
                //说明是一级分类
                firstCourseTypes.add(obj);
            }
            else{
                //说明是非一级分类，肯定有父分类
                EasyDept parentType = allCourseTypesMaps.get(obj.getParentId());
                parentType.getChildren().add(obj);
            }
        }
        return firstCourseTypes;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(EasyDept dept)
    {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        easyDeptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<EasyDept> children = easyDeptMapper.selectChildrenDeptById(deptId);
        for (EasyDept child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            easyDeptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<EasyDept> list, EasyDept t)
    {
        // 得到子节点列表
        List<EasyDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (EasyDept tChild : childList)
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
    private boolean hasChild(List<EasyDept> list, EasyDept t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 得到子节点列表
     */
    private List<EasyDept> getChildList(List<EasyDept> list, EasyDept t)
    {
        List<EasyDept> tlist = new ArrayList<EasyDept>();
        Iterator<EasyDept> it = list.iterator();
        while (it.hasNext())
        {
            EasyDept n = it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }
}