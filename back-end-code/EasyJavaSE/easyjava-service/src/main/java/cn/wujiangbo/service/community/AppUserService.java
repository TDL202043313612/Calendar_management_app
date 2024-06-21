package cn.wujiangbo.service.community;

import cn.wujiangbo.domain.community.AppUser;
import cn.wujiangbo.mapper.community.AppUserMapper;
import cn.wujiangbo.query.community.AppUserQuery;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 社区APP用户信息表 服务实现类
 * </p>
 *
 * @author bobo(weixin:javabobo0513)
 * @since 2024-03-29
 */
@Transactional
@Service
@Slf4j
public class AppUserService extends ServiceImpl<AppUserMapper, AppUser>{

    @Resource
    private AppUserMapper appUserMapper;

    //查询分页列表数据
    public Page<AppUser> selectMyPage(AppUserQuery query) {
        QueryWrapper<AppUser> wrapper = new QueryWrapper<>();
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
    public Page<AppUser> selectMySqlPage(AppUserQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<AppUser> list = appUserMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }

    //导出Excel表格
    public List<AppUser> selectExportExcelData(AppUserQuery query) {
        return appUserMapper.selectExportExcelData(Arrays.asList(query.getIds()));
    }
}