package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyOperLogService;
import cn.wujiangbo.domain.system.EasyOperLog;
import cn.wujiangbo.query.system.EasyOperLogQuery;
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
 * @desc 操作日志记录 API接口
 *
 */
@RestController
@RequestMapping("/easyOperLog")
public class EasyOperLogController extends BaseController{

    @Autowired
    public EasyOperLogService easyOperLogService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 删除【操作日志记录】信息
     * @date 2022-06-07
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除[操作日志记录]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyOperLog:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyOperLogQuery query){
        //删除数据库记录
        easyOperLogService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【操作日志记录】信息
    * @date 2022-06-07
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[操作日志记录]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyOperLogService.getById(id));
    }


    /**
    * 查看【操作日志记录】所有数据（不分页）
    * @date 2022-06-07
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询[操作日志记录]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        List<EasyOperLog> list = easyOperLogService.list(null);
        return JSONResult.success(list);
    }

    /**
     * 分页查询【操作日志记录】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-06-07
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【操作日志记录】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyOperLog:pagelist")
    public JSONResult pagelist(@RequestBody EasyOperLogQuery query)
    {
        Page<EasyOperLog> page = easyOperLogService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}