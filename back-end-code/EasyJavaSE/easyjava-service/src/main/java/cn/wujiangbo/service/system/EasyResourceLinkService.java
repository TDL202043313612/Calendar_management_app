package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyResourceLink;
import cn.wujiangbo.mapper.system.EasyResourceLinkMapper;
import cn.wujiangbo.query.system.EasyResourceLinkQuery;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 资源链接表 服务实现类
 * </p>
 *
 */
@Transactional
@Service
@Slf4j
public class EasyResourceLinkService extends ServiceImpl<EasyResourceLinkMapper, EasyResourceLink>{

    @Autowired
    private EasyResourceLinkMapper easyResourceLinkMapper;

    //查询分页列表数据
    public Page<EasyResourceLink> selectMyPage(EasyResourceLinkQuery query) {
        QueryWrapper<EasyResourceLink> wrapper = new QueryWrapper<>();
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
    public Page<EasyResourceLink> selectMySqlPage(EasyResourceLinkQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyResourceLink> list = easyResourceLinkMapper.selectMySqlPage(page, query);
        return page.setRecords(list);
    }
}