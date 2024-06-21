package cn.wujiangbo.mapper.app;

import cn.wujiangbo.domain.app.AppLinkman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.app.AppLinkmanQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 联系人表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2024-04-30
*/
public interface AppLinkmanMapper extends BaseMapper<AppLinkman> {

    //查询分页列表数据
    List<AppLinkman> selectMySqlPage(Page<AppLinkman> page, @Param("query") AppLinkmanQuery query);

    //导出Excel表格
    List<AppLinkman> selectExportExcelData(@Param("list") List<Long> ids);
}