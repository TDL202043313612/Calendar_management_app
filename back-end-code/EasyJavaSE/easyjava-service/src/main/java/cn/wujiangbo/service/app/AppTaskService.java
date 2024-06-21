package cn.wujiangbo.service.app;

import cn.wujiangbo.domain.app.AppTask;
import cn.wujiangbo.mapper.app.AppTaskMapper;
import cn.wujiangbo.query.app.AppTaskQuery;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 日常任务表 服务实现类
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Transactional
@Service
@Slf4j
public class AppTaskService extends ServiceImpl<AppTaskMapper, AppTask>{

    @Resource
    private AppTaskMapper appTaskMapper;

    //查询分页列表数据
    public Page<AppTask> selectMyPage(AppTaskQuery query) {
        QueryWrapper<AppTask> wrapper = new QueryWrapper<>();
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
    public Page<AppTask> selectMySqlPage(AppTaskQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<AppTask> list = appTaskMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }

    //导出Excel表格
    public List<AppTask> selectExportExcelData(AppTaskQuery query) {
        return appTaskMapper.selectExportExcelData(Arrays.asList(query.getIds()));
    }
}