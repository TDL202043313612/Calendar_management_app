package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyMessage;
import cn.wujiangbo.query.system.EasyMessageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统消息表 服务类
 * </p>
 *
 */
public interface EasyMessageService extends IService<EasyMessage> {

    //分页查询
    Page<EasyMessage> selectMyPage(EasyMessageQuery query);

    void updateStatus(Long userId);

    void updateMessageStatus(Long id);
}