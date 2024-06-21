package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyPost;
import cn.wujiangbo.mapper.system.EasyPostMapper;
import cn.wujiangbo.query.system.EasyPostQuery;
import cn.wujiangbo.service.system.EasyPostService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 岗位信息表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyPostServiceImpl extends ServiceImpl<EasyPostMapper, EasyPost> implements EasyPostService {

    @Autowired
    private EasyPostMapper easyPostMapper;

    //查询列表
    @Override
    public Page<EasyPost> selectMyPage(EasyPostQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyPost> list = easyPostMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public List<EasyPost> selectPostAll() {
        QueryWrapper<EasyPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");//状态（0正常 1停用）
        return easyPostMapper.selectList(queryWrapper);
    }

    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return easyPostMapper.selectPostListByUserId(userId);
    }
}