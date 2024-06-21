package cn.wujiangbo.mapper.app;

import cn.wujiangbo.domain.app.AppNews;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.app.AppNewsQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 新闻信息表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2024-04-30
*/
public interface AppNewsMapper extends BaseMapper<AppNews> {

    //查询分页列表数据
    List<AppNews> selectMySqlPage(Page<AppNews> page, @Param("query") AppNewsQuery query);

    //导出Excel表格
    List<AppNews> selectExportExcelData(@Param("list") List<Long> ids);
}