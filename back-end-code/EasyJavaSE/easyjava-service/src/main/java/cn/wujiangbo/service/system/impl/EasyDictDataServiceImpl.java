package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.domain.system.EasyDictData;
import cn.wujiangbo.mapper.system.EasyDictDataMapper;
import cn.wujiangbo.query.system.EasyDictDataQuery;
import cn.wujiangbo.service.system.EasyDictDataService;
import cn.wujiangbo.utils.PageTools;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyDictDataServiceImpl extends ServiceImpl<EasyDictDataMapper, EasyDictData> implements EasyDictDataService {

    @Autowired
    private EasyDictDataMapper easyDictDataMapper;

    //查询列表
    @Override
    public Page<EasyDictData> selectMyPage(EasyDictDataQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyDictData> list = easyDictDataMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<EasyDictData> selectDictDataByType(String dictType) {
        List<EasyDictData> dictDataList = easyDictDataMapper.selectDictDataByType(dictType);
        return dictDataList;
    }
}