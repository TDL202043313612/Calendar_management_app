package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyMessage;
import cn.wujiangbo.mapper.system.EasyMessageMapper;
import cn.wujiangbo.query.system.EasyMessageQuery;
import cn.wujiangbo.service.system.EasyMessageService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统消息表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyMessageServiceImpl extends ServiceImpl<EasyMessageMapper, EasyMessage> implements EasyMessageService {

    @Autowired
    private EasyMessageMapper easyMessageMapper;

    //查询列表
    @Override
    public Page<EasyMessage> selectMyPage(EasyMessageQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyMessage> list = easyMessageMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public void updateStatus(Long userId) {
        easyMessageMapper.updateStatus(userId);
    }

    @Override
    public void updateMessageStatus(Long id) {
        easyMessageMapper.updateMessageStatus(id);
    }
}