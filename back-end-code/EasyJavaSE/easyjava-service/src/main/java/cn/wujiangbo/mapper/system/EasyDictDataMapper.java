package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyDictData;
import cn.wujiangbo.query.system.EasyDictDataQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 字典数据表 Mapper接口
* </p>
*
*/
public interface EasyDictDataMapper extends BaseMapper<EasyDictData> {
    List<EasyDictData> selectMyPage(Page<EasyDictData> page, @Param("param") EasyDictDataQuery query);

    List<EasyDictData> selectDictDataByType(String dictType);

    void deleteDictDataByTypeIds(List<Long> ids);
}