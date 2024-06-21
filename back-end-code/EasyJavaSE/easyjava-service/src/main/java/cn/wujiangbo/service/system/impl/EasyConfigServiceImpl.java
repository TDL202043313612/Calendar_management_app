package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyConfig;
import cn.wujiangbo.mapper.system.EasyConfigMapper;
import cn.wujiangbo.query.system.EasyConfigQuery;
import cn.wujiangbo.service.system.EasyConfigService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统参数配置表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyConfigServiceImpl extends ServiceImpl<EasyConfigMapper, EasyConfig> implements EasyConfigService {

    @Autowired
    private EasyConfigMapper easyConfigMapper;

    //查询列表
    @Override
    public Page<EasyConfig> selectMyPage(EasyConfigQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyConfig> list = easyConfigMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public Map<String, String> getConfigValueMap() {
        List<EasyConfig> sysConfigList = easyConfigMapper.selectList(null);
        Map<String, String> map = new HashMap<>();
        for(int i=0; i<sysConfigList.size(); i++){
            EasyConfig sysConfig = sysConfigList.get(i);
            map.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
        }
        return map;
    }
}