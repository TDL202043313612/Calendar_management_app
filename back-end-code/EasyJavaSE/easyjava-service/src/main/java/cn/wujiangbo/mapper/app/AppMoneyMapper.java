package cn.wujiangbo.mapper.app;

import cn.wujiangbo.domain.app.AppMoney;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.app.AppMoneyQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 收入支出表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2024-04-30
*/
public interface AppMoneyMapper extends BaseMapper<AppMoney> {

    //查询分页列表数据
    List<AppMoney> selectMySqlPage(Page<AppMoney> page, @Param("query") AppMoneyQuery query);

    //导出Excel表格
    List<AppMoney> selectExportExcelData(@Param("list") List<Long> ids);
}