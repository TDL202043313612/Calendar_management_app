package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyPost;
import cn.wujiangbo.query.system.EasyPostQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 岗位信息表 服务类
 * </p>
 *
 */
public interface EasyPostService extends IService<EasyPost> {

    //分页查询
    Page<EasyPost> selectMyPage(EasyPostQuery query);

    List<EasyPost> selectPostAll();

    List<Long> selectPostListByUserId(Long userId);
}