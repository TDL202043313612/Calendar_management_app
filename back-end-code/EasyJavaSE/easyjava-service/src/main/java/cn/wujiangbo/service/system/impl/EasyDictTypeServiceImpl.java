package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyDictType;
import cn.wujiangbo.mapper.system.EasyDictTypeMapper;
import cn.wujiangbo.query.system.EasyDictTypeQuery;
import cn.wujiangbo.service.system.EasyDictTypeService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyDictTypeServiceImpl extends ServiceImpl<EasyDictTypeMapper, EasyDictType> implements EasyDictTypeService {

    @Autowired
    private EasyDictTypeMapper easyDictTypeMapper;

    //查询列表
    @Override
    public Page<EasyDictType> selectMyPage(EasyDictTypeQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyDictType> list = easyDictTypeMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public List<EasyDictType> selectDictTypeAll() {
        return easyDictTypeMapper.selectList(null);
    }
}