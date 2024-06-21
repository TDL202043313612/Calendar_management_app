package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyLoginLog;
import cn.wujiangbo.query.system.EasyLoginLogQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 */
public interface EasyLoginLogService extends IService<EasyLoginLog> {

    //分页查询
    Page<EasyLoginLog> selectMyPage(EasyLoginLogQuery query);
}