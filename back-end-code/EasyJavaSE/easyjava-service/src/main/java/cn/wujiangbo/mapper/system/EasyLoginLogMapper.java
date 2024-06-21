package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyLoginLog;
import cn.wujiangbo.query.system.EasyLoginLogQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 系统访问记录 Mapper接口
* </p>
*
*/
public interface EasyLoginLogMapper extends BaseMapper<EasyLoginLog> {
    List<EasyLoginLog> selectMyPage(Page<EasyLoginLog> page, @Param("param") EasyLoginLogQuery query);
}