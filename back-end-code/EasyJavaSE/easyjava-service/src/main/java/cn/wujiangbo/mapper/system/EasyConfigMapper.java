package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyConfig;
import cn.wujiangbo.query.system.EasyConfigQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 系统参数配置表 Mapper接口
* </p>
*
*/
public interface EasyConfigMapper extends BaseMapper<EasyConfig> {
    List<EasyConfig> selectMyPage(Page<EasyConfig> page, @Param("param") EasyConfigQuery query);
}