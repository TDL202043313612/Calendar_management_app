package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyOperLog;
import cn.wujiangbo.query.system.EasyOperLogQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 */
public interface EasyOperLogService extends IService<EasyOperLog> {

    //分页查询
    Page<EasyOperLog> selectMyPage(EasyOperLogQuery query);
}