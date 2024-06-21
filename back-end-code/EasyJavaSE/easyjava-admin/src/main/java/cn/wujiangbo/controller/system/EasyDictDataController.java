package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyDictDataService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyDictData;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.query.system.EasyDictDataQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 字典数据表 API接口
 *
 */
@RestController
@RequestMapping("/easyDictData")
public class EasyDictDataController extends BaseController{

    @Autowired
    public EasyDictDataService easyDictDataService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public JSONResult dictType(@PathVariable String dictType)
    {
        List<EasyDictData> data = easyDictDataService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<>();
        }
        return JSONResult.success(data);
    }

    /**
     * 新增字典数据表
     * @date 2022-05-27
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyDictData:add")
    @MyLog(title = "新增字典数据表", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyDictData easyDictData){
        easyDictData.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyDictData.setCreateUserId(getUserId());
        easyDictData.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyDictData.setUpdateUserId(getUserId());
        easyDictDataService.save(easyDictData);
        return JSONResult.success(true);
    }

    /**
     * 修改字典数据表
     * @date 2022-05-27
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改字典数据表", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easyDictData:update")
    public JSONResult update(@RequestBody EasyDictData easyDictData){
        easyDictData.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyDictData.setUpdateUserId(getUserId());
        easyDictDataService.updateById(easyDictData);
        return JSONResult.success(true);
    }

    /**
     * 批量删除【字典数据表】信息
     * @date 2022-05-27
     */
    @DeleteMapping(value="/batchDelete")
    @MyLog(title = "批量删除[字典数据表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyDictData:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyDictDataQuery query){
        //删除数据库记录
        easyDictDataService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【字典数据表】信息
    * @date 2022-05-27
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[字典数据表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyDictDataService.getById(id));
    }

    /**
     * 分页查询【字典数据表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-27
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【字典数据表】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyDictData:pagelist")
    public JSONResult pagelist(@RequestBody EasyDictDataQuery query)
    {
        Page<EasyDictData> page = easyDictDataService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}