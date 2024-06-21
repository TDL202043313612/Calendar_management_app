package cn.wujiangbo.service.system;

import cn.wujiangbo.domain.system.EasyImgResource;
import cn.wujiangbo.query.system.EasyImgResourceQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * <p>
 * 图片资源信息表 服务类
 * </p>
 *
 */
public interface EasyImgResourceService extends IService<EasyImgResource> {

    //分页查询
    Page<EasyImgResource> selectMyPage(EasyImgResourceQuery query);

    //新增多个资源到数据库
    String insertMultiResource(List<MultipartFile> files, Long userId, String funcFlag);

    //根据ID删除所有资源
    void deleteById(Long id, String tableName, String folderName);
}