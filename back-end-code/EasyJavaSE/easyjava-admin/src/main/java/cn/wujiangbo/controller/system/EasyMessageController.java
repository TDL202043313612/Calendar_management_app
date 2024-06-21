package cn.wujiangbo.controller.system;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.domain.system.EasySystemMsg;
import cn.wujiangbo.mapper.system.EasySystemMsgMapper;
import cn.wujiangbo.service.system.EasyMessageService;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.domain.system.EasyMessage;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.query.system.EasyMessageQuery;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc 系统消息表 API接口
 *
 */
@RestController
@RequestMapping("/easyMessage")
public class EasyMessageController extends BaseController{

    @Autowired
    public EasyMessageService easyMessageService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    @Autowired
    public EasySystemMsgMapper easySystemMsgMapper;

    /**
     * 新增系统消息表
     * @date 2022-06-02
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyMessage:add")
    @MyLog(title = "新增系统消息表", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody EasyMessage easyMessage){
        Long[] receiveUserIds = easyMessage.getReceiveUserIds();
        if(receiveUserIds == null){
            throw new MyException(ErrorCode.ERROR_CODE_1041);
        }
        String userName = getUserName();
        Long userId = getUserId();
        for(Long receiveUserId: receiveUserIds){
            easyMessage.setCreateTime(DateUtils.getCurrentLocalDateTime());
            easyMessage.setCreateUserId(userId);
            easyMessage.setCreateUserName(userName);
            easyMessage.setReceiveUserId(receiveUserId);
            easyMessageService.save(easyMessage);
        }
        return JSONResult.success(true);
    }

    /**
     * 批量删除【系统消息表】信息
     * @date 2022-06-02
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "批量删除[系统消息表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyMessage:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyMessageQuery query){
        //删除数据库记录
        easyMessageService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【系统消息表】信息
    * @date 2022-06-02
    */
    @GetMapping(value = "/{id}")
    @MyLog(title = "根据ID查询[系统消息表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyMessageService.getById(id));
    }


    /**
    * 查看【系统消息表】所有数据（不分页）
    * @date 2022-06-02
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询[系统消息表]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        List<EasyMessage> list = easyMessageService.list(null);
        return JSONResult.success(list);
    }

    /**
     * 分页查询【系统消息表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-06-02
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "分页查询【系统消息表】数据", businessType = BusinessType.QUERY)
    @CheckPermission(per = "easyMessage:pagelist")
    public JSONResult pagelist(@RequestBody EasyMessageQuery query)
    {
        Page<EasyMessage> page = easyMessageService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/

    /**
     * @desc 获取系统消息数量（包含消息+公告的总数量）
     * @author wujiangbo
     * @date 2022/8/19 10:44
     * @return cn.wujiangbo.result.JSONResult
     */
    @GetMapping("/getMsgCount")
    public JSONResult getMsgCount(){
        //1、查询登录人未读的消息总数量
        QueryWrapper<EasyMessage> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("receive_user_id", getUserId());
        queryWrapper1.eq("msg_status", Constants.YMSD.UN_READ);
        List<EasyMessage> xiaoxiList = easyMessageService.list(queryWrapper1);
        int xiaoxiCount = CollectionUtils.isEmpty(xiaoxiList) ? 0 : xiaoxiList.size();

        //2、查询登录人未读的公告总数量
        QueryWrapper<EasySystemMsg> queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("receive_user_id", getUserId());
        queryWrapper2.eq("msg_status", Constants.YMSD.UN_READ);
        List<EasySystemMsg> gonggaoList = easySystemMsgMapper.selectList(queryWrapper2);
        int gonggaoCount = CollectionUtils.isEmpty(gonggaoList) ? 0 : gonggaoList.size();

        //返回总和
        return JSONResult.success(xiaoxiCount + gonggaoCount);
    }

    /**
     * 获取所有消息集合信息
     * @return
     */
    @GetMapping("/getAllMessage")
    public JSONResult getAllMessage(){
        //1、查询登录人的系统消息集合
        QueryWrapper<EasyMessage> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("receive_user_id", getUserId()).orderByDesc("create_time");
        List<EasyMessage> xiaoxiList = easyMessageService.list(queryWrapper1);

        //2、查询所有公告信息集合
        List<EasySystemMsg> gonggaoList = easySystemMsgMapper.selectList(null);

        Map<String, Object> map = new HashMap<>();
        map.put("xiaoxi", xiaoxiList);
        map.put("gonggao", gonggaoList);

        //返回总和
        return JSONResult.success(map);
    }

    /**
     * 修改系统消息状态
     */
    @GetMapping(value = "/updateMessageStatus/{id}")
    public JSONResult updateMessageStatus(@PathVariable("id") Long id)
    {
        //状态修改为已读
        easyMessageService.updateMessageStatus(id);
        return JSONResult.success();
    }

}