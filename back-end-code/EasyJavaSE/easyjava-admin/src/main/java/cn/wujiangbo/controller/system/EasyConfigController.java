package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyConfigService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyConfig;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.query.system.EasyConfigQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 系统参数配置表 API接口
 */
@RestController
@RequestMapping("/easyConfig")
public class EasyConfigController extends BaseController{

    @Autowired
    public EasyConfigService easyConfigService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 新增系统参数配置表
     * @date 2022-05-27
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyConfig:add")
    @MyLog(title = "新增系统参数配置表", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyConfig easyConfig){
        easyConfig.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyConfig.setCreateUserId(getUserId());
        easyConfig.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyConfig.setUpdateUserId(getUserId());
        easyConfigService.save(easyConfig);
        return JSONResult.success(true);
    }

    /**
     * 修改系统参数配置表
     * @date 2022-05-27
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改系统参数配置表", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easyConfig:update")
    public JSONResult update(@RequestBody EasyConfig easyConfig){
        easyConfig.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyConfig.setUpdateUserId(getUserId());
        easyConfigService.updateById(easyConfig);
        return JSONResult.success(true);
    }

    /**
     * 批量删除【系统参数配置表】信息
     * @date 2022-05-27
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "批量删除[系统参数配置表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyConfig:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyConfigQuery query){
        //删除数据库记录
        easyConfigService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【系统参数配置表】信息
    * @date 2022-05-27
    */
    @GetMapping(value = "/{configId}")
    @MyLog(title = "根据ID查询[系统参数配置表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("configId")Long configId)
    {
        return JSONResult.success(easyConfigService.getById(configId));
    }

    /**
    * 根据config_key查询【系统参数配置表】信息
    * @date 2022-05-27
    */
    @GetMapping(value = "/configKey/{configKey}")
    @MyLog(title = "根据config_key查询[系统参数配置表]详情", businessType = BusinessType.QUERY)
    public JSONResult configKey(@PathVariable("configKey")String configKey)
    {
        QueryWrapper<EasyConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_key", configKey);
        EasyConfig config = easyConfigService.getOne(queryWrapper);
        return JSONResult.success(config);
    }


    /**
    * 查看【系统参数配置表】所有数据（不分页）
    * @date 2022-05-27
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询[系统参数配置表]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        List<EasyConfig> list = easyConfigService.list();
        return JSONResult.success(list);
    }

    /**
     * 分页查询【系统参数配置表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-27
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【系统参数配置表】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyConfig:pagelist")
    public JSONResult pagelist(@RequestBody EasyConfigQuery query)
    {
        Page<EasyConfig> page = easyConfigService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}