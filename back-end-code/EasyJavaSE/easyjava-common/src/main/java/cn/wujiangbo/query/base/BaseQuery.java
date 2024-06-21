package cn.wujiangbo.query.base;

import lombok.Data;
import java.io.Serializable;

/**
 * 基础查询对象
 *
 */
@Data
public class BaseQuery implements Serializable {

    //学生姓名
    private String stuName;

    //标识位
    private String flag;

    //关键字
    private String keyword;
    //用户ID
    private Long userId;
    //所属部门ID
    private Long deptId;
    //有公共属性-分页
    private Integer current = 1; //当前页
    private Integer size = 10; //每页显示多少条
    private Long[] ids; //批量删除时，前端传来的主键ID数组集合（Long类型）
    private String[] idStrs; //批量删除时，前端传来的主键ID数组集合（String类型）

    private String queryTime; //查询时间

    private String beginTime; //开始时间
    private String endTime; //结束时间
}