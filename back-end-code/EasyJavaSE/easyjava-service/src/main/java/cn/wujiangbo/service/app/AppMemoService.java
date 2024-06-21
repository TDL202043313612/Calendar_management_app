package cn.wujiangbo.service.app;

import cn.wujiangbo.domain.app.AppMemo;
import cn.wujiangbo.mapper.app.AppMemoMapper;
import cn.wujiangbo.query.app.AppMemoQuery;
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
 * 备忘录表 服务实现类
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Transactional
@Service
@Slf4j
public class AppMemoService extends ServiceImpl<AppMemoMapper, AppMemo>{

    @Resource
    private AppMemoMapper appMemoMapper;

    //查询分页列表数据
    public Page<AppMemo> selectMyPage(AppMemoQuery query) {
        QueryWrapper<AppMemo> wrapper = new QueryWrapper<>();
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
    public Page<AppMemo> selectMySqlPage(AppMemoQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<AppMemo> list = appMemoMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }

    //导出Excel表格
    public List<AppMemo> selectExportExcelData(AppMemoQuery query) {
        return appMemoMapper.selectExportExcelData(Arrays.asList(query.getIds()));
    }
}