package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyResourceLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.system.EasyResourceLinkQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 资源链接表 Mapper接口
* </p>
*
*/
public interface EasyResourceLinkMapper extends BaseMapper<EasyResourceLink> {

    //查询分页列表数据
    List<EasyResourceLink> selectMySqlPage(Page<EasyResourceLink> page, @Param("query") EasyResourceLinkQuery query);
}