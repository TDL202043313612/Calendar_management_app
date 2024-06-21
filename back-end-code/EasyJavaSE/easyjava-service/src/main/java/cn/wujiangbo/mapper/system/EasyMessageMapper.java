package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyMessage;
import cn.wujiangbo.query.system.EasyMessageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 系统消息表 Mapper接口
* </p>
*
*/
public interface EasyMessageMapper extends BaseMapper<EasyMessage> {
    List<EasyMessage> selectMyPage(Page<EasyMessage> page, @Param("param") EasyMessageQuery query);

    void updateStatus(Long userId);

    void updateMessageStatus(Long id);
}