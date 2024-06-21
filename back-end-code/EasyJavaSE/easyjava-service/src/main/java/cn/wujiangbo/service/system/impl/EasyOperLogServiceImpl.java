package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyOperLog;
import cn.wujiangbo.mapper.system.EasyOperLogMapper;
import cn.wujiangbo.query.system.EasyOperLogQuery;
import cn.wujiangbo.service.system.EasyOperLogService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyOperLogServiceImpl extends ServiceImpl<EasyOperLogMapper, EasyOperLog> implements EasyOperLogService {

    @Autowired
    private EasyOperLogMapper easyOperLogMapper;

    //查询列表
    @Override
    public Page<EasyOperLog> selectMyPage(EasyOperLogQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyOperLog> list = easyOperLogMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }
}