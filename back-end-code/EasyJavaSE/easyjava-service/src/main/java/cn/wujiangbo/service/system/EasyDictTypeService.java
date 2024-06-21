package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyDictType;
import cn.wujiangbo.query.system.EasyDictTypeQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 */
public interface EasyDictTypeService extends IService<EasyDictType> {

    //分页查询
    Page<EasyDictType> selectMyPage(EasyDictTypeQuery query);

    List<EasyDictType> selectDictTypeAll();
}