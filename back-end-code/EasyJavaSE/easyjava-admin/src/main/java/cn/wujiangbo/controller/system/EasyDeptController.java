package cn.wujiangbo.controller.system;

import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.constants.UserConstants;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyDept;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.query.system.EasyDeptQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.service.system.EasyDeptService;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc 部门表 API接口
 *
 */
@RestController
@RequestMapping("/easyDept")
public class EasyDeptController extends BaseController {

    @Autowired
    public EasyDeptService easyDeptService;

    @Autowired
    public EasyUserMapper easyUserMapper;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public JSONResult treeselect(EasyDeptQuery deptQuery) {
        List<EasyDept> deptList = easyDeptService.selectDeptList(deptQuery);
        return JSONResult.success(easyDeptService.buildDeptTreeSelect(deptList));
    }

    /**
     * 查询部门列表（排除节点）
     */
    @GetMapping("/list/exclude/{deptId}")
    public JSONResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<EasyDept> depts = easyDeptService.selectDeptList(new EasyDeptQuery());
        Iterator<EasyDept> it = depts.iterator();
        while (it.hasNext()) {
            EasyDept d = it.next();
            if (d.getId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return JSONResult.success(depts);
    }

    /**
     * 新增部门表
     *
     * @date 2022-05-26
     */
    @PostMapping(value = "/save")
    @MyLog(title = "新增部门表", businessType = BusinessType.INSERT)
    @CheckPermission(per = "easyDept:save")
    public JSONResult save(@RequestBody EasyDept easyDept) {
        if (UserConstants.NOT_UNIQUE.equals(easyDeptService.checkDeptNameUnique(easyDept))) {
            throw new MyException(ErrorCode.ERROR_CODE_1036);
        }
        easyDept.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyDept.setCreateUserId(getUserId());
        easyDept.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyDept.setUpdateUserId(getUserId());

        //部门数据入库
        easyDeptService.save(easyDept);

        //更新 ancestors 祖籍列表字段值
        Long parentId = easyDept.getParentId();
        EasyDept parentDept = easyDeptService.getById(parentId);
        String parentPath = parentDept.getAncestors();
        if("0".equalsIgnoreCase(parentPath)){
            parentPath = "" + parentId + "." + easyDept.getId();
        }else{
            parentPath = parentPath + "." + easyDept.getId();
        }
        easyDept.setAncestors(parentPath);
        //执行更新操作
        easyDeptService.updateById(easyDept);

        return JSONResult.success(true);
    }

    /**
     * 修改部门表
     *
     * @date 2022-05-26
     */
    @PostMapping(value = "/update")
    @CheckPermission(per = "easyDept:update")
    @MyLog(title = "修改部门表", businessType = BusinessType.UPDATE)
    public JSONResult update(@RequestBody EasyDept dept) {
        Long deptId = dept.getId();
        if (UserConstants.NOT_UNIQUE.equals(easyDeptService.checkDeptNameUnique(dept))) {
            throw new MyException(ErrorCode.ERROR_CODE_1036);
        } else if (dept.getParentId().equals(deptId)) {
            throw new MyException(ErrorCode.ERROR_CODE_1037);
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && easyDeptService.selectNormalChildrenDeptById(deptId) > 0) {
            throw new MyException(ErrorCode.ERROR_CODE_1038);
        }
        dept.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        dept.setUpdateUserId(getUserId());
        return easyDeptService.updateDept(dept);
    }

    /**
     * 根据ID删除【部门表】信息
     *
     * @date 2022-05-26
     */
    @DeleteMapping(value = "/{id}")
    @CheckPermission(per = "easyDept:delete")
    @MyLog(title = "单条删除[部门表]信息", businessType = BusinessType.DELETE)
    @Transactional
    public JSONResult delete(@PathVariable("id") Long id) {

        //查询出要删除的部门及所有子部门的ID集合
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("ancestors", id);
        List<EasyDept> list = easyDeptService.list(wrapper);
        List<Long> deptIds = list.stream().map(EasyDept::getId).collect(Collectors.toList());

        //该部门下所有员工和子部门数据将全部被删除
        easyDeptService.removeByIds(deptIds);

        //然后再删除部门下的所有员工
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.in("dept_id", deptIds);
        easyUserMapper.delete(userWrapper);

        return JSONResult.success(true);
    }

    /**
     * 批量删除【部门表】信息
     * @date 2022-05-26
     */
    @PostMapping(value = "/batchDelete")
    @MyLog(title = "批量删除[部门表]信息", businessType = BusinessType.DELETE)
    public JSONResult batchDelete(@RequestBody EasyDeptQuery query) {
        //删除数据库记录
        easyDeptService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 获取部门树数据
     */
    @PostMapping("/getDeptTreeData")
    public JSONResult getDeptTreeData(@RequestBody EasyDeptQuery query){
        return easyDeptService.getDeptTreeData(query);
    }

    /**
     * 根据ID查询【部门表】信息
     * @date 2022-05-26
     */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[部门表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id") Long id) {
        return JSONResult.success(easyDeptService.getById(id));
    }


    /**
     * 部门管理-页面获取部门树形数据
     */
    @PostMapping(value = "/list")
    @MyLog(title = "查询[部门表]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(@RequestBody EasyDeptQuery query) {
        List<EasyDept> list = easyDeptService.deptList(query);
        return JSONResult.success(list);
    }

    /**
     * 分页查询【部门表】数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-26
     */
    @PostMapping(value = "/pagelist")
    @CheckPermission(per = "easyDept:pagelist")
    @MyLog(title = "分页查询【部门表】数据", businessType = BusinessType.QUERY)
    public JSONResult pagelist(@RequestBody EasyDeptQuery query) {
        Page<EasyDept> page = easyDeptService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
     以上代码是自动生成的
     自己写的代码放在下面
     ***********************************************************************************/
}