package cn.wujiangbo.controller.system;

import cn.wujiangbo.mapper.system.EasyDictDataMapper;
import cn.wujiangbo.service.system.EasyDictTypeService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyDictType;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.query.system.EasyDictTypeQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 字典类型表 API接口
 *
 */
@RestController
@RequestMapping("/easyDictType")
public class EasyDictTypeController extends BaseController{

    @Autowired
    public EasyDictTypeService easyDictTypeService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    @Autowired
    public EasyDictDataMapper easyDictDataMapper;

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public JSONResult optionselect()
    {
        List<EasyDictType> dictTypeList = easyDictTypeService.selectDictTypeAll();
        return JSONResult.success(dictTypeList);
    }

    /**
     * 刷新字典缓存
     */
    @DeleteMapping("/refreshCache")
    @CheckPermission(per = "easyDictType:refreshCache")
    @MyLog(title = "刷新数据字典缓存", businessType = BusinessType.UPDATE)
    public JSONResult refreshCache() {
        //TODO
        return JSONResult.success();
    }

    /**
     * 新增字典类型表
     * @date 2022-05-27
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyDictType:add")
    @MyLog(title = "新增字典类型表", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyDictType easyDictType){
        easyDictType.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyDictType.setCreateUserId(getUserId());
        easyDictType.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyDictType.setUpdateUserId(getUserId());
        easyDictTypeService.save(easyDictType);
        return JSONResult.success(true);
    }

    /**
     * 修改字典类型表
     * @date 2022-05-27
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改字典类型表", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easyDictType:update")
    public JSONResult update(@RequestBody EasyDictType easyDictType){
        easyDictType.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyDictType.setUpdateUserId(getUserId());
        easyDictTypeService.updateById(easyDictType);
        return JSONResult.success(true);
    }

    /**
     * 批量删除【字典类型表】信息
     * @date 2022-05-27
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "批量删除[字典类型表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyDictType:deleteBatch")
    @Transactional
    public JSONResult batchDelete(@RequestBody EasyDictTypeQuery query){
        //删除类型下面的所有数据字典详情
        List<Long> ids = Arrays.asList(query.getIds());
        easyDictDataMapper.deleteDictDataByTypeIds(ids);

        //删除类型
        easyDictTypeService.removeByIds(ids);
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【字典类型表】信息
    * @date 2022-05-27
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[字典类型表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyDictTypeService.getById(id));
    }

    /**
     * 分页查询【字典类型表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-27
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【字典类型表】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyDictType:pagelist")
    public JSONResult pagelist(@RequestBody EasyDictTypeQuery query)
    {
        Page<EasyDictType> page = easyDictTypeService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}