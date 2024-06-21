package cn.wujiangbo.controller.app;

import cn.wujiangbo.domain.app.AppMemo;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.service.app.AppMemoService;
import cn.wujiangbo.query.app.AppMemoQuery;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.utils.DateUtils;
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
 * @desc 备忘录表 API接口
 * @author bobo(weixin:javabobo0513)
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/appMemo")
public class AppMemoController extends BaseController{

    @Resource
    public AppMemoService appMemoService;

    /**
     * 新增数据到【备忘录表】
     * @date 2024-04-30
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "appMemo:save")
    @MyLog(title = "新增数据到【备忘录表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody AppMemo appMemo){
        appMemo.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appMemo.setCreateUserId(getUserId());
        appMemo.setCreateUserName(getUserName());
        appMemo.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appMemo.setUpdateUserId(getUserId());
        appMemo.setUpdateUserName(getUserName());
        appMemoService.save(appMemo);
        return JSONResult.success(true);
    }

    /**
    * 删除【备忘录表】数据
    * @date 2024-04-30
    */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【备忘录表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "appMemo:deleteBatch")
    public JSONResult batchDelete(@RequestBody AppMemoQuery query){
        //删除数据库数据
        appMemoService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 修改【备忘录表】表数据
     * @date 2024-04-30
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改【备忘录表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "appMemo:update")
    public JSONResult update(@RequestBody AppMemo appMemo){
        appMemo.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appMemo.setUpdateUserId(getUserId());
        appMemo.setUpdateUserName(getUserName());
        appMemoService.updateById(appMemo);
        return JSONResult.success(true);
    }

    /**
     * 查询【备忘录表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2024-04-30
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【备忘录表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "appMemo:pagelist")
    public JSONResult pagelist(@RequestBody AppMemoQuery query){
        Page<AppMemo> page = appMemoService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 查询【备忘录表】数据（不分页）
    * @date 2024-04-30
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【备忘录表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<AppMemo> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppMemo> list = appMemoService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
    * 根据ID查询【备忘录表】数据
    * @date 2024-04-30
    */
    @GetMapping(value = "/getDataById/{id}")
    @MyLog(title = "根据ID查询【备忘录表】数据", businessType = BusinessType.QUERY)
    public JSONResult getDataById(@PathVariable("id")Long id){
        return JSONResult.success(appMemoService.getById(id));
    }

    /**
     * 根据 QueryWrapper 对象查询【备忘录表】数据
     * @date 2024-04-30
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<AppMemo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        AppMemo one = appMemoService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /**
     * 【备忘录表】数据导出Excel
    */
    @PostMapping(value="/exportExcelFile")
    @CheckPermission(per = "appMemo:exportExcelFile")
    @MyLog(title = "【备忘录表】数据导出Excel", businessType = BusinessType.EXPORT)
    public void exportExcelFile(HttpServletResponse response, AppMemoQuery query){
        List<AppMemo> list = appMemoService.selectExportExcelData(query);
        ExcelUtil<AppMemo> util = new ExcelUtil<>(AppMemo.class);
        util.exportExcel(response, list, "备忘录表");
    }

    /**
     * xxx操作
    */
    @PostMapping(value="/xxxHandle")
    @MyLog(title = "xxxxxxxxxx", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "xxxxxx:xxxxxx")
    public JSONResult xxxHandle(@RequestBody AppMemo appMemo){
        AppMemo obj = appMemoService.getById(appMemo.getId());
        if(obj == null){
            throw new MyException(ErrorCode.COMMON_CODE_2001);
        }
        //做其他操作
        appMemoService.updateById(obj);
        return JSONResult.success(true);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/

    /**
     * APP-查询登录人的所有备忘录
     */
    @GetMapping(value = "/getAllMemoList")
    public JSONResult getAllMemoList(){
        QueryWrapper<AppMemo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user_id", getAppUserId());
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppMemo> list = appMemoService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * APP-新增备忘录
     */
    @PostMapping(value="/addMemo")
    public JSONResult addMemo(@RequestBody AppMemo appMemo){
        appMemo.setCreateUserId(getAppUserId());
        appMemo.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appMemoService.save(appMemo);
        return JSONResult.success(true);
    }

    /**
     * APP-删除备忘录
     */
    @GetMapping(value="/deleteMemoById/{id}")
    public JSONResult addMemo(@PathVariable("id") Long id){
        appMemoService.removeById(id);
        return JSONResult.success(true);
    }

    /**
     * APP-查询详情
     */
    @GetMapping(value="/getMemoById/{id}")
    public JSONResult getMemoById(@PathVariable("id") Long id){
        AppMemo one = appMemoService.getById(id);
        return JSONResult.success(one);
    }

}