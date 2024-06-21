package cn.wujiangbo.service.app;

import cn.wujiangbo.domain.app.AppMoney;
import cn.wujiangbo.mapper.app.AppMoneyMapper;
import cn.wujiangbo.query.app.AppMoneyQuery;
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
 * 收入支出表 服务实现类
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Transactional
@Service
@Slf4j
public class AppMoneyService extends ServiceImpl<AppMoneyMapper, AppMoney>{

    @Resource
    private AppMoneyMapper appMoneyMapper;

    //查询分页列表数据
    public Page<AppMoney> selectMyPage(AppMoneyQuery query) {
        QueryWrapper<AppMoney> wrapper = new QueryWrapper<>();
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
    public Page<AppMoney> selectMySqlPage(AppMoneyQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<AppMoney> list = appMoneyMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }

    //导出Excel表格
    public List<AppMoney> selectExportExcelData(AppMoneyQuery query) {
        return appMoneyMapper.selectExportExcelData(Arrays.asList(query.getIds()));
    }
}