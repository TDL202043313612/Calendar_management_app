package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasySlf4jLogging;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.system.EasySlf4jLoggingQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* Slf4j日志表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2023-06-20
*/
public interface EasySlf4jLoggingMapper extends BaseMapper<EasySlf4jLogging> {

    //查询分页列表数据
    List<EasySlf4jLogging> selectMySqlPage(Page<EasySlf4jLogging> page, @Param("query") EasySlf4jLoggingQuery query);

    void deleteLog();
}