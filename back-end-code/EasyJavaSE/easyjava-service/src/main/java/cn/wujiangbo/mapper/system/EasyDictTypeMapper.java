package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyDictType;
import cn.wujiangbo.query.system.EasyDictTypeQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 字典类型表 Mapper接口
* </p>
*
*/
public interface EasyDictTypeMapper extends BaseMapper<EasyDictType> {
    List<EasyDictType> selectMyPage(Page<EasyDictType> page, @Param("param") EasyDictTypeQuery query);
}