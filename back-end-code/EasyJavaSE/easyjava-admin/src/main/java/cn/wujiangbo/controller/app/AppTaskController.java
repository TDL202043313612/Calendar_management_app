package cn.wujiangbo.controller.app;

import cn.wujiangbo.domain.app.AppTask;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.service.app.AppTaskService;
import cn.wujiangbo.query.app.AppTaskQuery;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.poi.ExcelUtil;
import cn.wujiangbo.constants.ErrorCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 日常任务表 API接口
 * @author bobo(weixin:javabobo0513)
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/appTask")
public class AppTaskController extends BaseController{

    @Resource
    public AppTaskService appTaskService;

    /**
     * 新增数据到【日常任务表】
     * @date 2024-04-30
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "appTask:save")
    @MyLog(title = "新增数据到【日常任务表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody AppTask appTask){
        appTask.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appTask.setCreateUserId(getUserId());
        appTask.setCreateUserName(getUserName());
        appTask.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appTask.setUpdateUserId(getUserId());
        appTask.setUpdateUserName(getUserName());
        appTaskService.save(appTask);
        return JSONResult.success(true);
    }

    /**
    * 删除【日常任务表】数据
    * @date 2024-04-30
    */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【日常任务表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "appTask:deleteBatch")
    public JSONResult batchDelete(@RequestBody AppTaskQuery query){
        //删除数据库数据
        appTaskService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 修改【日常任务表】表数据
     * @date 2024-04-30
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改【日常任务表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "appTask:update")
    public JSONResult update(@RequestBody AppTask appTask){
        appTask.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appTask.setUpdateUserId(getUserId());
        appTask.setUpdateUserName(getUserName());
        appTaskService.updateById(appTask);
        return JSONResult.success(true);
    }

    /**
     * 查询【日常任务表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2024-04-30
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【日常任务表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "appTask:pagelist")
    public JSONResult pagelist(@RequestBody AppTaskQuery query){
        Page<AppTask> page = appTaskService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 查询【日常任务表】数据（不分页）
    * @date 2024-04-30
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【日常任务表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<AppTask> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppTask> list = appTaskService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
    * 根据ID查询【日常任务表】数据
    * @date 2024-04-30
    */
    @GetMapping(value = "/getDataById/{id}")
    @MyLog(title = "根据ID查询【日常任务表】数据", businessType = BusinessType.QUERY)
    public JSONResult getDataById(@PathVariable("id")Long id){
        return JSONResult.success(appTaskService.getById(id));
    }

    /**
     * 根据 QueryWrapper 对象查询【日常任务表】数据
     * @date 2024-04-30
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<AppTask> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        AppTask one = appTaskService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /**
     * 【日常任务表】数据导出Excel
    */
    @PostMapping(value="/exportExcelFile")
    @CheckPermission(per = "appTask:exportExcelFile")
    @MyLog(title = "【日常任务表】数据导出Excel", businessType = BusinessType.EXPORT)
    public void exportExcelFile(HttpServletResponse response, AppTaskQuery query){
        List<AppTask> list = appTaskService.selectExportExcelData(query);
        ExcelUtil<AppTask> util = new ExcelUtil<>(AppTask.class);
        util.exportExcel(response, list, "日常任务表");
    }

    /**
     * xxx操作
    */
    @PostMapping(value="/xxxHandle")
    @MyLog(title = "xxxxxxxxxx", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "xxxxxx:xxxxxx")
    public JSONResult xxxHandle(@RequestBody AppTask appTask){
        AppTask obj = appTaskService.getById(appTask.getId());
        if(obj == null){
            throw new MyException(ErrorCode.COMMON_CODE_2001);
        }
        //做其他操作
        appTaskService.updateById(obj);
        return JSONResult.success(true);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/

    /**
     * APP-查询登录人所有日常任务数据
     */
    @GetMapping(value = "/getAllTaskList")
    public JSONResult getAllTaskList(){
        QueryWrapper<AppTask> queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user_id", getAppUserId());
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppTask> list = appTaskService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * APP-新增日常任务
     */
    @PostMapping(value="/addTask")
    public JSONResult addTask(@RequestBody AppTask appTask){
        if(!MyTools.hasLength(appTask.getTaskTitle())){
            throw new MyException("任务标题不能为空！");
        }
        if(!MyTools.hasLength(appTask.getTaskContent())){
            throw new MyException("任务内容不能为空！");
        }
        if(!MyTools.hasLength(appTask.getTaskStatus())){
            throw new MyException("任务状态不能为空！");
        }
        appTask.setCreateUserId(getAppUserId());
        appTask.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appTaskService.save(appTask);
        return JSONResult.success(true);
    }

    /**
     * APP-编辑日常任务
     */
    @PostMapping(value="/updateTask")
    public JSONResult updateTask(@RequestBody AppTask appTask){
        if(appTask.getId() == null){
            throw new MyException("任务ID不能为空！");
        }
        if(!MyTools.hasLength(appTask.getTaskTitle())){
            throw new MyException("任务标题不能为空！");
        }
        if(!MyTools.hasLength(appTask.getTaskContent())){
            throw new MyException("任务内容不能为空！");
        }
        if(!MyTools.hasLength(appTask.getTaskStatus())){
            throw new MyException("任务状态不能为空！");
        }
        appTask.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appTaskService.updateById(appTask);
        return JSONResult.success(true);
    }

    /**
     * APP-根据ID查询任务详情
     */
    @GetMapping(value="/getTaskById/{id}")
    public JSONResult getTaskById(@PathVariable("id") Long id){
        if(id == null){
            throw new MyException("ID不能为空！");
        }
        AppTask one = appTaskService.getById(id);
        if(one == null){
            throw new MyException("任务信息不存在！");
        }
        return JSONResult.success(one);
    }

    /**
     * APP-删除日常任务
     */
    @GetMapping(value="/deleteTask/{id}")
    public JSONResult deleteTask(@PathVariable("id") Long id){
        if(id == null){
            throw new MyException("ID不能为空！");
        }
        AppTask one = appTaskService.getById(id);
        if(one == null){
            throw new MyException("任务信息不存在！");
        }
        appTaskService.removeById(id);
        return JSONResult.success(true);
    }

    /**
     * APP-修改任务状态(待办/已办)
     */
    @GetMapping(value="/updateTaskStatus/{id}")
    public JSONResult updateTaskStatus(@PathVariable("id") Long id){
        if(id == null){
            throw new MyException("ID不能为空！");
        }
        AppTask one = appTaskService.getById(id);
        if(one == null){
            throw new MyException("任务信息不存在！");
        }
        one.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        one.setTaskStatus("待办".equals(one.getTaskStatus()) ? "已办" : "待办");
        appTaskService.updateById(one);
        return JSONResult.success(true);
    }
}