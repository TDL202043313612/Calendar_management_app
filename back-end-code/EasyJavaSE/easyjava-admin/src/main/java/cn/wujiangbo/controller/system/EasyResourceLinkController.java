package cn.wujiangbo.controller.system;

import cn.wujiangbo.domain.system.EasyResourceLink;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.service.system.EasyResourceLinkService;
import cn.wujiangbo.query.system.EasyResourceLinkQuery;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 资源链接表 API接口
 *
 */
@RestController
@RequestMapping("/easyResourceLink")
public class EasyResourceLinkController extends BaseController{

    @Autowired
    public EasyResourceLinkService easyResourceLinkService;

    /**
     * 新增数据到【资源链接表】
     * @date 2022-11-08
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyResourceLink:save")
    @MyLog(title = "新增数据到【资源链接表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyResourceLink easyResourceLink){
        easyResourceLink.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyResourceLink.setCreateUserName(getUserName());
        easyResourceLink.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyResourceLink.setUpdateUserName(getUserName());
        easyResourceLinkService.save(easyResourceLink);
        return JSONResult.success(true);
    }

    /**
     * 修改【资源链接表】表数据
     * @date 2022-11-08
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改【资源链接表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easyResourceLink:update")
    public JSONResult update(@RequestBody EasyResourceLink easyResourceLink){
        easyResourceLink.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyResourceLink.setUpdateUserName(getUserName());
        easyResourceLinkService.updateById(easyResourceLink);
        return JSONResult.success(true);
    }

    /**
     * 删除【资源链接表】数据
     * @date 2022-11-08
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【资源链接表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyResourceLink:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyResourceLinkQuery query){
        //删除数据库数据
        easyResourceLinkService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【资源链接表】数据
    * @date 2022-11-08
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询【资源链接表】数据", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(easyResourceLinkService.getById(id));
    }

    /**
    * 查询【资源链接表】数据（不分页）
    * @date 2022-11-08
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【资源链接表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<EasyResourceLink> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<EasyResourceLink> list = easyResourceLinkService.list(null);
        return JSONResult.success(list);
    }

    /**
     * 查询【资源链接表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-11-08
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【资源链接表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyResourceLink:pagelist")
    public JSONResult pagelist(@RequestBody EasyResourceLinkQuery query){
        Page<EasyResourceLink> page = easyResourceLinkService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 根据 QueryWrapper 对象查询【资源链接表】数据
    * @date 2022-11-08
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<EasyResourceLink> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        EasyResourceLink one = easyResourceLinkService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}