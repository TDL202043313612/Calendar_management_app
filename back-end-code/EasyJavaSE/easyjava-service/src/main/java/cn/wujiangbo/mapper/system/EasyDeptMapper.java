package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyDept;
import cn.wujiangbo.dto.system.DeptUserDto;
import cn.wujiangbo.query.system.EasyDeptQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 部门表 Mapper接口
* </p>
*
*/
public interface EasyDeptMapper extends BaseMapper<EasyDept> {

    List<EasyDept> selectMyPage(Page<EasyDept> page, @Param("param") EasyDeptQuery query);

    List<EasyDept> selectDeptList(EasyDeptQuery dept);

    EasyDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    int selectNormalChildrenDeptById(Long deptId);

    EasyDept selectDeptById(Long parentId);

    List<EasyDept> selectChildrenDeptById(Long deptId);

    void updateDeptChildren(@Param("depts") List<EasyDept> depts);

    void updateDeptStatusNormal(Long[] deptIds);

    List<EasyDept> deptList(EasyDeptQuery query);

    List<DeptUserDto> selectDeptUserList();

    void removeBatchDept(Long id);
}