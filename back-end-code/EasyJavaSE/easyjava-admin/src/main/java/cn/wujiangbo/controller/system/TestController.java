package cn.wujiangbo.controller.system;

import cn.wujiangbo.annotation.AccessLimiter;
import cn.wujiangbo.annotation.MyCache;
import cn.wujiangbo.annotation.MyCacheEvict;
import cn.wujiangbo.domain.system.EasyUser;
import cn.wujiangbo.dto.system.EmailMQDto;
import cn.wujiangbo.dto.system.SystemConfigDto;
import cn.wujiangbo.dto.system.UserRoleDto;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.EasyUserService;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.FreemarkerExportWordUtil;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.sign.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private EasyUserService easyUserService;

    @Resource
    private CommonTools commonTools;

    @Resource
    private SystemConfigDto systemConfigDto;

    @Resource
    private EasyUserMapper easyUserMapper;

    //批量添加系统用户测试
    @GetMapping("/test003")
    public JSONResult test003(){
        Long roleId = 12L; // 普通员工角色
        List<UserRoleDto> userRoleList = new ArrayList<>();

        //构造测试数据
        userRoleList.add(new UserRoleDto(getUser("江汉油田-收款", "jhyt-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江汉油田-总账", "jhyt-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江汉油田-付款", "jhyt-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江汉油田-帐表负责人", "jhyt-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江汉油田-审核二", "jhyt-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江汉油田-审核三", "jhyt-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("湖北石油-收款", "hbsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖北石油-总账", "hbsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖北石油-付款", "hbsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖北石油-帐表负责人", "hbsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖北石油-审核二", "hbsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖北石油-审核三", "hbsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("北京石油-收款", "bjsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京石油-总账", "bjsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京石油-付款", "bjsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京石油-帐表负责人", "bjsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京石油-审核二", "bjsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京石油-审核三", "bjsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("四川石油-收款", "scsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("四川石油-总账", "scsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("四川石油-付款", "scsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("四川石油-帐表负责人", "scsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("四川石油-审核二", "scsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("四川石油-审核三", "scsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("重庆石油-收款", "cqsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("重庆石油-总账", "cqsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("重庆石油-付款", "cqsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("重庆石油-帐表负责人", "cqsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("重庆石油-审核二", "cqsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("重庆石油-审核三", "cqsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("河北石油-收款", "hebsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河北石油-总账", "hebsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河北石油-付款", "hebsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河北石油-帐表负责人", "hebsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河北石油-审核二", "hebsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河北石油-审核三", "hebsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("江西石油-收款", "jxsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江西石油-总账", "jxsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江西石油-付款", "jxsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江西石油-帐表负责人", "jxsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江西石油-审核二", "jxsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("江西石油-审核三", "jxsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("山西石油-收款", "sxsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("山西石油-总账", "sxsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("山西石油-付款", "sxsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("山西石油-帐表负责人", "sxsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("山西石油-审核二", "sxsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("山西石油-审核三", "sxsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("河南石油-收款", "hensy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河南石油-总账", "hensy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河南石油-付款", "hensy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河南石油-帐表负责人", "hensy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河南石油-审核二", "hensy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("河南石油-审核三", "hensy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("安徽石油-收款", "ahsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("安徽石油-总账", "ahsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("安徽石油-付款", "ahsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("安徽石油-帐表负责人", "ahsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("安徽石油-审核二", "ahsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("安徽石油-审核三", "ahsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("湖南石油-收款", "hnsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖南石油-总账", "hnsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖南石油-付款", "hnsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖南石油-帐表负责人", "hnsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖南石油-审核二", "hnsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("湖南石油-审核三", "hnsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("贵州石油-收款", "gzsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("贵州石油-总账", "gzsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("贵州石油-付款", "gzsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("贵州石油-帐表负责人", "gzsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("贵州石油-审核二", "gzsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("贵州石油-审核三", "gzsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("易捷公司-收款", "yj-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("易捷公司-总账", "yj-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("易捷公司-付款", "yj-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("易捷公司-帐表负责人", "yj-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("易捷公司-审核二", "yj-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("易捷公司-审核三", "yj-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("天津悦泰-收款", "tjyt-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("天津悦泰-总账", "tjyt-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("天津悦泰-付款", "tjyt-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("天津悦泰-帐表负责人", "tjyt-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("天津悦泰-审核二","tjyt-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("天津悦泰-审核三","tjyt-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("吉林石油-收款", "jlsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("吉林石油-总账", "jlsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("吉林石油-付款", "jlsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("吉林石油-帐表负责人", "jlsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("吉林石油-审核二", "jlsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("吉林石油-审核三", "jlsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("黑龙江石油-收款", "hljsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("黑龙江石油-总账", "hljsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("黑龙江石油-付款", "hljsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("黑龙江石油-帐表负责人", "hljsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("黑龙江石油-审核二", "hljsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("黑龙江石油-审核三", "hljsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("辽宁石油-收款", "lnsy-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("辽宁石油-总账", "lnsy-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("辽宁石油-付款", "lnsy-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("辽宁石油-帐表负责人", "lnsy-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("辽宁石油-审核二", "lnsy-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("辽宁石油-审核三", "lnsy-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("燃料油公司-收款", "rly-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("燃料油公司-总账", "rly-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("燃料油公司-付款", "rly-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("燃料油公司-帐表负责人", "rly-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("燃料油公司-审核二", "rly-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("燃料油公司-审核三", "rly-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("北京龙禹-收款", "bjly-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京龙禹-总账", "bjly-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京龙禹-付款", "bjly-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京龙禹-帐表负责人", "bjly-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京龙禹-审核二", "bjly-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("北京龙禹-审核三", "bjly-sh3").getId(), roleId));

        userRoleList.add(new UserRoleDto(getUser("雄安新能源-收款", "xaxny-sk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("雄安新能源-总账", "xaxny-zz").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("雄安新能源-付款", "xaxny-fk").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("雄安新能源-帐表负责人", "xaxny-zb").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("雄安新能源-审核二", "xaxny-sh2").getId(), roleId));
        userRoleList.add(new UserRoleDto(getUser("雄安新能源-审核三", "xaxny-sh3").getId(), roleId));

        //用户角色信息批量入库
        easyUserMapper.batchUserRole(userRoleList);
        return JSONResult.success();
    }

    public EasyUser getUser(String nickName, String loginName){
        EasyUser user = new EasyUser();
        Long userId = 12L;//超级管理员账号
        Long deptId = 118L; // 118属于业务部
        user.setCreateTime(DateUtils.getCurrentLocalDateTime());
        user.setCreateUserId(userId);
        user.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        user.setUpdateUserId(userId);
        user.setEmpNo(commonTools.getEmpNo());//员工编号
        user.setNickName(nickName);
        user.setUserName(loginName);
        //密码加密
        String password = Md5Utils.getPassword("123456");
        user.setPassword(password);
        user.setAvatar(systemConfigDto.getUserDefaultAvatarPath());//设置默认头像
        user.setDeptId(deptId);
        user.setRemark("批量录入");
        easyUserService.save(user);
        return user;
    }

    @GetMapping("/test002/{name}")
    public JSONResult test002(@PathVariable("name") String name){
        log.info("这是 info 级别日志");
        log.warn("这是 warn 级别日志");
        log.error("这是 error 级别日志");
        log.debug("这是 debug 级别日志");
        return JSONResult.success(name);
    }

    @GetMapping ("/getWord")
    public void exportSellPlan(HttpServletRequest request, HttpServletResponse response) {
        String imagePath1 ="D:\\1.png";
        String imagePath2 ="D:\\2.png";
        //String imagePath1 ="D:\\ITSource\\git\\MyBusiness\\MyInfo\\wjb.png";
        //String imagePath2 ="D:\\ITSource\\git\\MyBusiness\\MyInfo\\customer.png";
        //获得数据
        Map<String, Object> map = new HashMap<>();
        map.put("wjb", MyTools.getBase64ByPath(imagePath1));//获取图片的字节码
        map.put("customer", MyTools.getBase64ByPath(imagePath2));//获取图片的字节码
        try {
            FreemarkerExportWordUtil.exportMillCertificateWord(request, response, map, "租房合同", "test2.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发送消息到MQ中测试
    @GetMapping("/sendMq")
    public JSONResult send1() {
        EmailMQDto dto = new EmailMQDto();
        List<String> address = new ArrayList<>();
        address.add("11111111@qq.com");
        address.add("22222222@qq.com");
        address.add("33333333@qq.com");
        dto.setEmailAddress(address);
        dto.setEmailContent("快来改变命运吧！");

        //发短信消息
        //rabbitTemplate.convertAndSend(
        //        Constants.MQ.EXCHANGE_EASYJAVA_TOPIC,
        //        "test.email",
        //        dto);
        return JSONResult.success("消息发送成功");
    }

    //测试自定义注解实现Redis缓存删除
    @GetMapping("/deleteCache")
    @MyCacheEvict(cacheNames = "user-server", key = "userInfo")
    public JSONResult deleteCache(){
        System.out.println("删除缓存测试");
        return JSONResult.success("删除缓存测试");
    }

    //测试自定义注解实现Redis缓存新增
    @GetMapping("/addCache")
    @MyCache(cacheNames = "user-server", key = "userInfo")
    public JSONResult addCache(){
        System.out.println("新增缓存测试");
        return JSONResult.success("新增缓存测试");
    }

    //2分钟内只能访问3次，否则报错
    @AccessLimiter(frequency = 3, cycle = 2 * 60 * 1000, message = "刷我接口，每门儿，滚蛋吧你.....")
    @GetMapping(value = "/test")
    public JSONResult test001(){
        return JSONResult.success();
    }
}
