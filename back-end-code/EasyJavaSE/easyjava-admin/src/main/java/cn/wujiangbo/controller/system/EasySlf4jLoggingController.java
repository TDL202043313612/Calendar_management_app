package cn.wujiangbo.controller.system;

import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasySlf4jLogging;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.query.system.EasySlf4jLoggingQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.service.system.EasySlf4jLoggingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @desc Slf4j日志表 API接口
 * @author bobo(weixin:javabobo0513)
 * @date 2023-06-20
 */
@RestController
@RequestMapping("/easySlf4jLogging")
public class EasySlf4jLoggingController extends BaseController{

    @Resource
    public EasySlf4jLoggingService easySlf4jLoggingService;

    /**
     * 删除【Slf4j日志表】数据
     * @date 2023-06-20
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【Slf4j日志表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easySlf4jLogging:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasySlf4jLoggingQuery query){
        //删除数据库数据
        easySlf4jLoggingService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【Slf4j日志表】数据
    * @date 2023-06-20
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询【Slf4j日志表】数据", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(easySlf4jLoggingService.getById(id));
    }

    /**
    * 查询【Slf4j日志表】数据（不分页）
    * @date 2023-06-20
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【Slf4j日志表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<EasySlf4jLogging> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<EasySlf4jLogging> list = easySlf4jLoggingService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * 查询【Slf4j日志表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2023-06-20
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【Slf4j日志表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easySlf4jLogging:pagelist")
    public JSONResult pagelist(@RequestBody EasySlf4jLoggingQuery query){
        Page<EasySlf4jLogging> page = easySlf4jLoggingService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 根据 QueryWrapper 对象查询【Slf4j日志表】数据
    * @date 2023-06-20
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<EasySlf4jLogging> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        EasySlf4jLogging one = easySlf4jLoggingService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}