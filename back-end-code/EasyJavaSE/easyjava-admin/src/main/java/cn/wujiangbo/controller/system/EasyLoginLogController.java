package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyLoginLogService;
import cn.wujiangbo.domain.system.EasyLoginLog;
import cn.wujiangbo.query.system.EasyLoginLogQuery;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 系统访问记录 API接口
 *
 */
@RestController
@RequestMapping("/easyLoginLog")
public class EasyLoginLogController extends BaseController{

    @Autowired
    public EasyLoginLogService easyLoginLogService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 删除【系统访问记录】信息
     * @date 2022-06-07
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除[系统访问记录]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyLoginLog:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyLoginLogQuery query){
        //删除数据库记录
        easyLoginLogService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【系统访问记录】信息
    * @date 2022-06-07
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[系统访问记录]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyLoginLogService.getById(id));
    }


    /**
    * 查看【系统访问记录】所有数据（不分页）
    * @date 2022-06-07
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询[系统访问记录]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        List<EasyLoginLog> list = easyLoginLogService.list(null);
        return JSONResult.success(list);
    }

    /**
     * 分页查询【系统访问记录】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-06-07
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【系统访问记录】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyLoginLog:pagelist")
    public JSONResult pagelist(@RequestBody EasyLoginLogQuery query)
    {
        Page<EasyLoginLog> page = easyLoginLogService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}