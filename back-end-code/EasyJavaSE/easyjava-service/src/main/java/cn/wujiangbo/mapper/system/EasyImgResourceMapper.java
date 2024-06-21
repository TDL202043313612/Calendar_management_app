package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyImgResource;
import cn.wujiangbo.query.system.EasyImgResourceQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 图片资源信息表 Mapper接口
* </p>
*
*/
public interface EasyImgResourceMapper extends BaseMapper<EasyImgResource> {
    List<EasyImgResource> selectMyPage(Page<EasyImgResource> page, @Param("param") EasyImgResourceQuery query);

    List<EasyImgResource> selectById(@Param("id") Long id, @Param("tableName") String tableName);
}