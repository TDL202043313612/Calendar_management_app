package cn.wujiangbo.service.app;

import cn.wujiangbo.domain.app.AppLinkman;
import cn.wujiangbo.mapper.app.AppLinkmanMapper;
import cn.wujiangbo.query.app.AppLinkmanQuery;
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
 * 联系人表 服务实现类
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Transactional
@Service
@Slf4j
public class AppLinkmanService extends ServiceImpl<AppLinkmanMapper, AppLinkman>{

    @Resource
    private AppLinkmanMapper appLinkmanMapper;

    //查询分页列表数据
    public Page<AppLinkman> selectMyPage(AppLinkmanQuery query) {
        QueryWrapper<AppLinkman> wrapper = new QueryWrapper<>();
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
    public Page<AppLinkman> selectMySqlPage(AppLinkmanQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<AppLinkman> list = appLinkmanMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }

    //导出Excel表格
    public List<AppLinkman> selectExportExcelData(AppLinkmanQuery query) {
        return appLinkmanMapper.selectExportExcelData(Arrays.asList(query.getIds()));
    }
}