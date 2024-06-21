package cn.wujiangbo.controller.system;

import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasySystemMsg;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.query.system.EasySystemMsgQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.service.system.EasySystemMsgService;
import cn.wujiangbo.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @desc 系统公告表 API接口
 *
 */
@RestController
@RequestMapping("/easySystemMsg")
public class EasySystemMsgController extends BaseController{

    @Autowired
    public EasySystemMsgService easySystemMsgService;

    @Autowired
    public EasyUserMapper easyUserMapper;

    /**
     * 新增数据到【公告信息表】
     * @date 2022-09-03
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easySystemMsg:save")
    @MyLog(title = "新增数据到【公告信息表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasySystemMsg easySystemMsg){
        /**
         * 新增一条公告信息，数据库就新增一条记录，所有人登录后都可以查看到这条信息
         */
        easySystemMsg.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easySystemMsg.setPublishEmpno(getEmpNo());
        easySystemMsg.setPublishName(getUserName());
        easySystemMsgService.save(easySystemMsg);
        return JSONResult.success(true);
    }

    /**
     * 修改【公告信息表】表数据
     * @date 2022-09-03
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改【公告信息表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easySystemMsg:update")
    public JSONResult update(@RequestBody EasySystemMsg easySystemMsg){
        easySystemMsgService.updateById(easySystemMsg);
        return JSONResult.success(true);
    }

    /**
     * 删除【公告信息表】数据
     * @date 2022-09-03
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "删除【公告信息表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easySystemMsg:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasySystemMsgQuery query){
        //删除数据库数据
        easySystemMsgService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【公告信息表】数据
    * @date 2022-09-03
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询【公告信息表】数据", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(easySystemMsgService.getById(id));
    }

    /**
    * 查询【公告信息表】数据（不分页）
    * @date 2022-09-03
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【公告信息表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        List<EasySystemMsg> list = easySystemMsgService.list(null);
        return JSONResult.success(list);
    }

    /**
     * 查询【公告信息表】数据（分页）
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-09-03
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【公告信息表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easySystemMsg:pagelist")
    public JSONResult pagelist(@RequestBody EasySystemMsgQuery query){
        Page<EasySystemMsg> page = easySystemMsgService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
    * 根据 QueryWrapper 对象查询【公告信息表】数据
    * @date 2022-09-03
    */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName){
        QueryWrapper<EasySystemMsg> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        EasySystemMsg one = easySystemMsgService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/
}