package cn.wujiangbo.controller.app;

import cn.wujiangbo.domain.app.AppLinkman;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.service.app.AppLinkmanService;
import cn.wujiangbo.query.app.AppLinkmanQuery;
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
 * @desc 联系人表 API接口
 * @author bobo(weixin:javabobo0513)
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/appLinkman")
public class AppLinkmanController extends BaseController{

    @Resource
    public AppLinkmanService appLinkmanService;

    /**
     * 新增数据到【联系人表】
     * @date 2024-04-30
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "appLinkman:save")
    @MyLog(title = "新增数据到【联系人表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody AppLinkman appLinkman){
        appLinkman.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appLinkman.setCreateUserId(getUserId());
        appLinkman.setCreateUserName(getUserName());
        appLinkman.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appLinkman.setUpdateUserId(getUserId());
        appLinkman.setUpdateUserName(getUserName());
        appLinkmanService.save(appLinkman);
        return JSONResult.success(true);
    }

    /**
    * 删除【联系人表】数据
    * @date 2024-04-30
    */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【联系人表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "appLinkman:deleteBatch")
    public JSONResult batchDelete(@RequestBody AppLinkmanQuery query){
        //删除数据库数据
        appLinkmanService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 修改【联系人表】表数据
     * @date 2024-04-30
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改【联系人表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "appLinkman:update")
    public JSONResult update(@RequestBody AppLinkman appLinkman){
        appLinkman.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appLinkman.setUpdateUserId(getUserId());
        appLinkman.setUpdateUserName(getUserName());
        appLinkmanService.updateById(appLinkman);
        return JSONResult.success(true);
    }

    /**
     * 查询【联系人表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2024-04-30
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【联系人表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "appLinkman:pagelist")
    public JSONResult pagelist(@RequestBody AppLinkmanQuery query){
        Page<AppLinkman> page = appLinkmanService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 查询【联系人表】数据（不分页）
    * @date 2024-04-30
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【联系人表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<AppLinkman> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppLinkman> list = appLinkmanService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
    * 根据ID查询【联系人表】数据
    * @date 2024-04-30
    */
    @GetMapping(value = "/getDataById/{id}")
    @MyLog(title = "根据ID查询【联系人表】数据", businessType = BusinessType.QUERY)
    public JSONResult getDataById(@PathVariable("id")Long id){
        return JSONResult.success(appLinkmanService.getById(id));
    }

    /**
     * 根据 QueryWrapper 对象查询【联系人表】数据
     * @date 2024-04-30
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<AppLinkman> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        AppLinkman one = appLinkmanService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /**
     * 【联系人表】数据导出Excel
    */
    @PostMapping(value="/exportExcelFile")
    @CheckPermission(per = "appLinkman:exportExcelFile")
    @MyLog(title = "【联系人表】数据导出Excel", businessType = BusinessType.EXPORT)
    public void exportExcelFile(HttpServletResponse response, AppLinkmanQuery query){
        List<AppLinkman> list = appLinkmanService.selectExportExcelData(query);
        ExcelUtil<AppLinkman> util = new ExcelUtil<>(AppLinkman.class);
        util.exportExcel(response, list, "联系人表");
    }

    /**
     * xxx操作
    */
    @PostMapping(value="/xxxHandle")
    @MyLog(title = "xxxxxxxxxx", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "xxxxxx:xxxxxx")
    public JSONResult xxxHandle(@RequestBody AppLinkman appLinkman){
        AppLinkman obj = appLinkmanService.getById(appLinkman.getId());
        if(obj == null){
            throw new MyException(ErrorCode.COMMON_CODE_2001);
        }
        //做其他操作
        appLinkmanService.updateById(obj);
        return JSONResult.success(true);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/


    /**
     * APP-查询自己能看到的所有联系人数据
     */
    @GetMapping(value = "/getAllManList")
    public JSONResult getAllManList(){
        QueryWrapper<AppLinkman> queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user_id", getAppUserId());
        queryWrapper.orderByDesc("man_top");
        queryWrapper.orderByDesc("id");
        List<AppLinkman> list = appLinkmanService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * APP-新增联系人
     */
    @PostMapping(value="/addLinkMan")
    public JSONResult addLinkMan(@RequestBody AppLinkman appLinkman){
        appLinkman.setCreateUserId(getAppUserId());
        appLinkman.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appLinkmanService.save(appLinkman);
        return JSONResult.success(true);
    }

    /**
     * APP-删除联系人
     */
    @GetMapping(value="/deleteLinkMan/{id}")
    public JSONResult deleteLinkMan(@PathVariable("id") Long id){
        appLinkmanService.removeById(id);
        return JSONResult.success(true);
    }
}