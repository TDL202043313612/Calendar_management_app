package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyDept;
import cn.wujiangbo.dto.system.TreeSelect;
import cn.wujiangbo.query.system.EasyDeptQuery;
import cn.wujiangbo.result.JSONResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 */
public interface EasyDeptService extends IService<EasyDept> {

    //分页查询
    Page<EasyDept> selectMyPage(EasyDeptQuery query);

    List<EasyDept> selectDeptList(EasyDeptQuery dept);

    List<TreeSelect> buildDeptTreeSelect(List<EasyDept> depts);

    /**
     * 构建前端所需要树结构
     * @param depts 部门列表
     * @return 树结构列表
     */
    List<EasyDept> buildDeptTree(List<EasyDept> depts);

    String checkDeptNameUnique(EasyDept easyDept);

    int selectNormalChildrenDeptById(Long deptId);

    JSONResult updateDept(EasyDept dept);

    List<EasyDept> deptList(EasyDeptQuery query);

    JSONResult getDeptTreeData(EasyDeptQuery query);

    void removeBatchDept(Long id);
}