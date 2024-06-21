package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasySystemMsg;
import cn.wujiangbo.mapper.system.EasySystemMsgMapper;
import cn.wujiangbo.query.system.EasySystemMsgQuery;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公告信息表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasySystemMsgService extends ServiceImpl<EasySystemMsgMapper, EasySystemMsg>{

    @Autowired
    private EasySystemMsgMapper easySystemMsgMapper;

    //查询分页列表数据
    public Page<EasySystemMsg> selectMyPage(EasySystemMsgQuery query) {
        QueryWrapper<EasySystemMsg> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(query.getBeginTime())) {
            wrapper.ge("create_time", query.getBeginTime());
        }
        if (StringUtils.isNotEmpty(query.getEndTime())) {
            wrapper.le("create_time", query.getEndTime());
        }
        if (StringUtils.isNotEmpty(query.getKeyword())) {
            wrapper.and(i -> i.like("id", query.getKeyword())
                   .or().like("create_time", query.getKeyword())
            );
        }
        //排序
        wrapper.orderByDesc("create_time").orderByDesc("id");
        Page page = PageTools.getInstance().getPage(query);
        return super.page(page, wrapper);
    }
}