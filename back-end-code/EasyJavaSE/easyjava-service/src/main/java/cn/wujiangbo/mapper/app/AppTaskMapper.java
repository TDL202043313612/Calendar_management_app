package cn.wujiangbo.mapper.app;

import cn.wujiangbo.domain.app.AppTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.app.AppTaskQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 日常任务表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2024-04-30
*/
public interface AppTaskMapper extends BaseMapper<AppTask> {

    //查询分页列表数据
    List<AppTask> selectMySqlPage(Page<AppTask> page, @Param("query") AppTaskQuery query);

    //导出Excel表格
    List<AppTask> selectExportExcelData(@Param("list") List<Long> ids);
}