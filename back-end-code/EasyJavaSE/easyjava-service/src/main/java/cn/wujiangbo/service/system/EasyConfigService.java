package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyConfig;
import cn.wujiangbo.query.system.EasyConfigQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

/**
 * <p>
 * 系统参数配置表 服务类
 * </p>
 *
 */
public interface EasyConfigService extends IService<EasyConfig> {

    //分页查询
    Page<EasyConfig> selectMyPage(EasyConfigQuery query);

    Map<String, String> getConfigValueMap();
}