package cn.wujiangbo.mapper.community;

import cn.wujiangbo.domain.community.AppUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.wujiangbo.query.community.AppUserQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 社区APP用户信息表 Mapper接口
* </p>
*
* @author bobo(weixin:javabobo0513)
* @since 2024-03-29
*/
public interface AppUserMapper extends BaseMapper<AppUser> {

    //查询分页列表数据
    List<AppUser> selectMySqlPage(Page<AppUser> page, @Param("query") AppUserQuery query);

    //导出Excel表格
    List<AppUser> selectExportExcelData(@Param("list") List<Long> ids);
}