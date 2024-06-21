package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyDictData;
import cn.wujiangbo.query.system.EasyDictDataQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 */
public interface EasyDictDataService extends IService<EasyDictData> {

    //分页查询
    Page<EasyDictData> selectMyPage(EasyDictDataQuery query);

    List<EasyDictData> selectDictDataByType(String dictType);
}