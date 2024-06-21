package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasySlf4jLogging;
import cn.wujiangbo.mapper.system.EasySlf4jLoggingMapper;
import cn.wujiangbo.query.system.EasySlf4jLoggingQuery;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * Slf4j日志表 服务实现类
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2023-06-20
 */
@Transactional
@Service
@Slf4j
public class EasySlf4jLoggingService extends ServiceImpl<EasySlf4jLoggingMapper, EasySlf4jLogging>{

    @Resource
    private EasySlf4jLoggingMapper easySlf4jLoggingMapper;

    //查询分页列表数据
    public Page<EasySlf4jLogging> selectMyPage(EasySlf4jLoggingQuery query) {
        QueryWrapper<EasySlf4jLogging> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(query.getBeginTime())) {
            wrapper.ge("create_time", query.getBeginTime());
        }
        if (StringUtils.isNotEmpty(query.getEndTime())) {
            wrapper.le("create_time", query.getEndTime());
        }
        if (StringUtils.isNotEmpty(query.getKeyword())) {
            //wrapper.and(
            //    i -> i.like("user_name", query.getKeyword())
            //         .or().like("login_name", query.getKeyword())
            //);
        }
        //排序
        wrapper.orderByDesc("id");
        Page page = PageTools.getInstance().getPage(query);
        return super.page(page, wrapper);
    }

    //查询分页列表数据(自己写SQL)
    public Page<EasySlf4jLogging> selectMySqlPage(EasySlf4jLoggingQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasySlf4jLogging> list = easySlf4jLoggingMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }

    //利用线程池技术异步保存日志入库
    @Async("BoboExecutor")
    public void asyncSave(EasySlf4jLogging log) {
        easySlf4jLoggingMapper.insert(log);
    }
}