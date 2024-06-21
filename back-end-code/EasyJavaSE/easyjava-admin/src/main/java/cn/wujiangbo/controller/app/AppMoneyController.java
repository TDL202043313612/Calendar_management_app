package cn.wujiangbo.controller.app;

import cn.wujiangbo.domain.app.AppMoney;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.service.app.AppMoneyService;
import cn.wujiangbo.query.app.AppMoneyQuery;
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
 * @desc 收入支出表 API接口
 * @author bobo(weixin:javabobo0513)
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/appMoney")
public class AppMoneyController extends BaseController{

    @Resource
    public AppMoneyService appMoneyService;

    /**
     * 新增数据到【收入支出表】
     * @date 2024-04-30
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "appMoney:save")
    @MyLog(title = "新增数据到【收入支出表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody AppMoney appMoney){
        appMoney.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appMoney.setCreateUserId(getUserId());
        appMoney.setCreateUserName(getUserName());
        appMoney.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appMoney.setUpdateUserId(getUserId());
        appMoney.setUpdateUserName(getUserName());
        appMoneyService.save(appMoney);
        return JSONResult.success(true);
    }

    /**
    * 删除【收入支出表】数据
    * @date 2024-04-30
    */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【收入支出表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "appMoney:deleteBatch")
    public JSONResult batchDelete(@RequestBody AppMoneyQuery query){
        //删除数据库数据
        appMoneyService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 修改【收入支出表】表数据
     * @date 2024-04-30
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改【收入支出表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "appMoney:update")
    public JSONResult update(@RequestBody AppMoney appMoney){
        appMoney.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appMoney.setUpdateUserId(getUserId());
        appMoney.setUpdateUserName(getUserName());
        appMoneyService.updateById(appMoney);
        return JSONResult.success(true);
    }

    /**
     * 查询【收入支出表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2024-04-30
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【收入支出表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "appMoney:pagelist")
    public JSONResult pagelist(@RequestBody AppMoneyQuery query){
        Page<AppMoney> page = appMoneyService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 查询【收入支出表】数据（不分页）
    * @date 2024-04-30
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【收入支出表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<AppMoney> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppMoney> list = appMoneyService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
    * 根据ID查询【收入支出表】数据
    * @date 2024-04-30
    */
    @GetMapping(value = "/getDataById/{id}")
    @MyLog(title = "根据ID查询【收入支出表】数据", businessType = BusinessType.QUERY)
    public JSONResult getDataById(@PathVariable("id")Long id){
        return JSONResult.success(appMoneyService.getById(id));
    }

    /**
     * 根据 QueryWrapper 对象查询【收入支出表】数据
     * @date 2024-04-30
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<AppMoney> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        AppMoney one = appMoneyService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /**
     * 【收入支出表】数据导出Excel
    */
    @PostMapping(value="/exportExcelFile")
    @CheckPermission(per = "appMoney:exportExcelFile")
    @MyLog(title = "【收入支出表】数据导出Excel", businessType = BusinessType.EXPORT)
    public void exportExcelFile(HttpServletResponse response, AppMoneyQuery query){
        List<AppMoney> list = appMoneyService.selectExportExcelData(query);
        ExcelUtil<AppMoney> util = new ExcelUtil<>(AppMoney.class);
        util.exportExcel(response, list, "收入支出表");
    }

    /**
     * xxx操作
    */
    @PostMapping(value="/xxxHandle")
    @MyLog(title = "xxxxxxxxxx", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "xxxxxx:xxxxxx")
    public JSONResult xxxHandle(@RequestBody AppMoney appMoney){
        AppMoney obj = appMoneyService.getById(appMoney.getId());
        if(obj == null){
            throw new MyException(ErrorCode.COMMON_CODE_2001);
        }
        //做其他操作
        appMoneyService.updateById(obj);
        return JSONResult.success(true);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/

    /**
     * APP-查询登录人所有记账数据(包含开支或收入)
     */
    @GetMapping(value = "/getAllMoneyData")
    public JSONResult getAllMoneyData(){
        QueryWrapper<AppMoney> queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user_id", getAppUserId());
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppMoney> list = appMoneyService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * APP-记账(包含开支或收入)
     */
    @PostMapping(value="/addMoney")
    public JSONResult addMoney(@RequestBody AppMoney appMoney){
        if(!MyTools.hasLength(appMoney.getMoneyStatus())){
            throw new MyException("账目类型必选！");
        }
        if(!MyTools.hasLength(appMoney.getMoneyDesc())){
            throw new MyException("记账描述必填！");
        }
        if(appMoney.getMoneyNum() == null){
            throw new MyException("账目金额必填！");
        }
        appMoney.setCreateUserId(getAppUserId());
        appMoney.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appMoneyService.save(appMoney);
        return JSONResult.success(true);
    }

    /**
     * APP-删除记账(包含开支或收入)
     */
    @GetMapping(value="/deleteMoney/{id}")
    public JSONResult deleteMoney(@PathVariable("id") Long id){
        if(id == null){
            throw new MyException("入参ID不能为空！");
        }
        AppMoney obj = appMoneyService.getById(id);
        if(obj == null){
            throw new MyException("信息不存在！");
        }
        appMoneyService.removeById(id);
        return JSONResult.success(true);
    }
}