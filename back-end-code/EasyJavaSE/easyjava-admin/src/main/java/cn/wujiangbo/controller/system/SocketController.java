package cn.wujiangbo.controller.system;

import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.query.system.EasyUserQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.service.websocket.PushMessage;
import cn.wujiangbo.service.websocket.WebSocketUtils;
import cn.wujiangbo.service.websocket.service.SocketIoService;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.vo.system.OnlineUserVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 */
@RestController
@RequestMapping("/socket")
public class SocketController {

    @Autowired
    private WebSocketUtils webSocketUtils;

    @Autowired
    private SocketIoService socketIoService;

    @Autowired
    private EasyUserMapper easyUserMapper;

    /**
     * 获取所有在线用户信息
     */
    @PostMapping("/getOnlineUserIdList")
    @CheckPermission(per = "socket:getOnlineUserIdList")
    public JSONResult getOnlineUserIdList(@RequestBody EasyUserQuery query){
        //获取在线用户信息
        List<Long> onlineUserIdList = socketIoService.getOnlineUserIdList();
        if(!CollectionUtils.isEmpty(onlineUserIdList)){
            Page page = PageTools.getInstance().getPage(query);
            List<OnlineUserVO> list = easyUserMapper.getOnlineUserIdList(page, onlineUserIdList);
            page.setRecords(list);
            return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
        }else{
            return JSONResult.success(new PageList<>());
        }
    }

    /**
     * 群发系统实时消息
     */
    @PostMapping("/pushMessageToAllUser")
    @CheckPermission(per = "socket:pushMessageToAllUser")
    public JSONResult pushMessageToAllUser(@RequestBody PushMessage msg){
        webSocketUtils.pushMessageToAllUser(msg.getContent());
        return JSONResult.success(true);
    }

    /**
     * 发送实时消息给某个用户(测试方法)
     */
    @PostMapping("/pushMessageToUser")
    public JSONResult pushMessageToUser(@RequestBody PushMessage msg){
        webSocketUtils.pushMessageToSingleUser(msg.getUserId(), msg.getContent());
        return JSONResult.success(true);
    }

    /**
     * 强制用户下线
     */
    @GetMapping("/forceLogout/{userId}")
    @CheckPermission(per = "socket:forceLogout")
    public JSONResult logout(@PathVariable("userId") Long userId){
        socketIoService.logout(SocketIoService.PUSH_LOGINOUT_EVENT, String.valueOf(userId));
        return JSONResult.success(true);
    }
}