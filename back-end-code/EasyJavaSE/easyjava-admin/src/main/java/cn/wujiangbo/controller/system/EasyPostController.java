package cn.wujiangbo.controller.system;

import cn.wujiangbo.service.system.EasyPostService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyPost;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.query.system.EasyPostQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

/**
 * @desc 岗位信息表 API接口
 *
 */
@RestController
@RequestMapping("/easyPost")
public class EasyPostController extends BaseController{

    @Autowired
    public EasyPostService easyPostService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    /**
     * 新增岗位信息表
     * @date 2022-05-27
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyPost:add")
    @MyLog(title = "新增岗位信息表", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyPost easyPost){
        easyPost.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyPost.setCreateUserId(getUserId());
        easyPost.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyPost.setUpdateUserId(getUserId());
        easyPostService.save(easyPost);
        return JSONResult.success(true);
    }

    /**
     * 修改岗位信息表
     * @date 2022-05-27
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改岗位信息表", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easyPost:update")
    public JSONResult update(@RequestBody EasyPost easyPost){
        easyPost.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyPost.setUpdateUserId(getUserId());
        easyPostService.updateById(easyPost);
        return JSONResult.success(true);
    }

    /**
    * 根据ID删除【岗位信息表】信息
    * @date 2022-05-27
    */
    @DeleteMapping(value="/{id}")
    @MyLog(title = "单条删除[岗位信息表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyPost:delete")
    public JSONResult delete(@PathVariable("id") Long id){
      //删除数据库记录
      easyPostService.removeById(id);
      return JSONResult.success(true);
    }

    /**
     * 批量删除【岗位信息表】信息
     * @date 2022-05-27
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "批量删除[岗位信息表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyPost:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyPostQuery query){
        //删除数据库记录
        easyPostService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【岗位信息表】信息
    * @date 2022-05-27
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[岗位信息表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyPostService.getById(id));
    }

    /**
     * 分页查询【岗位信息表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-27
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【岗位信息表】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyPost:pagelist")
    public JSONResult pagelist(@RequestBody EasyPostQuery query)
    {
        Page<EasyPost> page = easyPostService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}