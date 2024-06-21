package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyOperLog;
import cn.wujiangbo.query.system.EasyOperLogQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 操作日志记录 Mapper接口
* </p>
*
*/
public interface EasyOperLogMapper extends BaseMapper<EasyOperLog> {
    List<EasyOperLog> selectMyPage(Page<EasyOperLog> page, @Param("param") EasyOperLogQuery query);
}