package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasySystemMsg;
import cn.wujiangbo.query.system.EasySystemMsgQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 公告信息表 Mapper接口
* </p>
*
*/
public interface EasySystemMsgMapper extends BaseMapper<EasySystemMsg> {
}