package cn.wujiangbo.mapper.app;

import cn.wujiangbo.domain.app.AppMemo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.app.AppMemoQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 备忘录表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2024-04-30
*/
public interface AppMemoMapper extends BaseMapper<AppMemo> {

    //查询分页列表数据
    List<AppMemo> selectMySqlPage(Page<AppMemo> page, @Param("query") AppMemoQuery query);

    //导出Excel表格
    List<AppMemo> selectExportExcelData(@Param("list") List<Long> ids);
}