package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyPost;
import cn.wujiangbo.query.system.EasyPostQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 岗位信息表 Mapper接口
* </p>
*
*/
public interface EasyPostMapper extends BaseMapper<EasyPost> {
    List<EasyPost> selectMyPage(Page<EasyPost> page, @Param("param") EasyPostQuery query);

    List<Long> selectPostListByUserId(Long userId);

    List<EasyPost> selectPostsByUserId(Long userId);
}