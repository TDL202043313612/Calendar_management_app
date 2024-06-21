package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyLoginLog;
import cn.wujiangbo.mapper.system.EasyLoginLogMapper;
import cn.wujiangbo.query.system.EasyLoginLogQuery;
import cn.wujiangbo.service.system.EasyLoginLogService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyLoginLogServiceImpl extends ServiceImpl<EasyLoginLogMapper, EasyLoginLog> implements EasyLoginLogService {

    @Autowired
    private EasyLoginLogMapper easyLoginLogMapper;

    //查询列表
    @Override
    public Page<EasyLoginLog> selectMyPage(EasyLoginLogQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyLoginLog> list = easyLoginLogMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }
}