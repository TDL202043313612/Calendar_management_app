package cn.wujiangbo.controller.system;

import cn.wujiangbo.dto.oss.OssDto;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.service.system.EasyMenuService;
import cn.wujiangbo.service.system.EasyPostService;
import cn.wujiangbo.service.system.EasyRoleService;
import cn.wujiangbo.service.system.EasyUserService;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.domain.system.EasyUser;
import cn.wujiangbo.dto.token.UserToken;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.query.system.EasyUserQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import cn.wujiangbo.utils.poi.ExcelUtil;
import cn.wujiangbo.utils.sign.Md5Utils;
import cn.wujiangbo.vo.system.ExtraInfo;
import cn.wujiangbo.vo.system.LoginSuccessUserInfoVo;
import cn.wujiangbo.vo.system.UploadFileVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @desc 用户信息表 API接口
 *
 */
@RestController
@RequestMapping("/easyUser")
public class EasyUserController extends BaseController{

    @Autowired
    public EasyUserService easyUserService;

    @Autowired
    public EasyRoleService easyRoleService;

    @Autowired
    public EasyPostService easyPostService;

    @Autowired
    public EasyMenuService easyMenuService;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    @Autowired
    public EasyUserMapper easyUserMapper;

    @Autowired
    public CommonTools commonTools;

    @Autowired
    private OssDto ossPropertites;


    @MyLog(title = "导出用户信息", businessType = BusinessType.EXPORT)
    @CheckPermission(per = "easyUser:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, EasyUserQuery query)
    {
        Page<EasyUser> page = easyUserService.selectMyPage(query);
        List<EasyUser> list = page.getRecords();
        ExcelUtil<EasyUser> util = new ExcelUtil<>(EasyUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @PostMapping("/resetPwd")
    @CheckPermission(per = "easyUser:resetPwd")
    @MyLog(title = "重置用户的密码", businessType = BusinessType.UPDATE)
    public JSONResult resetPwd(@RequestBody EasyUser user) {
        user.setPassword(Md5Utils.getPassword(user.getPassword()));
        user.setUpdateUserId(getUserId());
        user.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        return easyUserService.resetPwd(user);
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public JSONResult getUserInfo()
    {
        //当前登录用户信息
        UserToken userToken = getUser();
        EasyUser user = easyUserMapper.selectUserById(userToken.getUserId());
        // 角色集合
        Set<String> roles = easyRoleService.getRolePermission(user.getId());
        // 权限集合
        Set<String> permissions = easyMenuService.getMenuPermission(user.getId(), userToken.isSuperAdmin());
        Map<String, Object> map = new HashMap<>();
        map.put("user", getUserInfo(user));
        map.put("roles", roles);
        map.put("permissions", permissions);
        return JSONResult.success(map);
    }

    /**
     * 获取额外信息
     */
    @GetMapping("/getExtraInfo")
    public JSONResult getExtraInfo()
    {
        ExtraInfo extra = new ExtraInfo();
        extra.setOssPrefix("https://" + ossPropertites.getBucketName() + "." + ossPropertites.getEndpoint() + "/");
        return JSONResult.success(extra);
    }

    //封装用户信息返回前端
    public LoginSuccessUserInfoVo getUserInfo(EasyUser user){
        LoginSuccessUserInfoVo vo = new LoginSuccessUserInfoVo();
        vo.setId(user.getId());
        vo.setAvatar(user.getAvatar());
        vo.setSex(user.getSex());
        vo.setNickName(user.getNickName());
        vo.setUserName(user.getUserName());
        vo.setEmpNo(user.getEmpNo());
        vo.setLoginIp(user.getLoginIp());
        vo.setLoginDate(user.getLoginDate());
        return vo;
    }


    @GetMapping(value = { "/", "/{userId}" })
    @MyLog(title = "根据用户编号获取详细信息", businessType = BusinessType.QUERY)
    public JSONResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        Map<String, Object> map = new HashMap<>();
        List<EasyRole> roles = easyRoleService.selectRoleAll();
        map.put("roles", roles);
        map.put("posts", easyPostService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            EasyUser user = easyUserService.selectUserById(userId);
            map.put("userInfo", user);
            map.put("postIds", easyPostService.selectPostListByUserId(userId));
            map.put("roleIds", user.getRoles().stream().map(EasyRole::getId).collect(Collectors.toList()));
        }
        return JSONResult.success(map);
    }

    /**
     * 新增用户信息表
     * @date 2022-05-26
     */
    @PostMapping(value="/save")
    @CheckPermission(per = "easyUser:add")
    @MyLog(title = "新增用户信息表", businessType = BusinessType.INSERT)
    public JSONResult save(@Validated @RequestBody EasyUser easyUser){
        easyUser.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyUser.setCreateUserId(getUserId());
        easyUser.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyUser.setUpdateUserId(getUserId());
        easyUser.setEmpNo(commonTools.getEmpNo());//员工编号
        return easyUserService.saveUser(easyUser);
    }

    /**
     * 修改用户信息表
     * @date 2022-05-26
     */
    @PostMapping(value="/update")
    @MyLog(title = "修改用户信息表", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "easyUser:update")
    public JSONResult update(@Validated @RequestBody EasyUser easyUser){
        easyUser.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyUser.setUpdateUserId(getUserId());
        return easyUserService.updateUser(easyUser);
    }

    /**
     * 根据ID删除【用户信息表】信息
     */
    @MyLog(title = "单条删除[用户信息表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyUser:delete")
    @DeleteMapping("/{userIds}")
    public JSONResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId())) {
            //不能删除自己
            throw new MyException(ErrorCode.ERROR_CODE_1028);
        }
        return easyUserService.deleteUserByIds(userIds);
    }

    /**
     * 批量删除【用户信息表】信息
     * @date 2022-05-26
     */
    @PostMapping(value="/batchDelete")
    @MyLog(title = "批量删除[用户信息表]信息", businessType = BusinessType.DELETE)
    @CheckPermission(per = "easyUser:deleteBatch")
    public JSONResult batchDelete(@RequestBody EasyUserQuery query){
        Long[] ids = query.getIds();
        if (ArrayUtils.contains(ids, getUserId())) {
            //不能删除自己
            throw new MyException(ErrorCode.ERROR_CODE_1028);
        }
        easyUserService.deleteUserByIds(query.getIds());
        return JSONResult.success(true);
    }

    /**
    * 根据ID查询【用户信息表】信息
    * @date 2022-05-26
    */
    @GetMapping(value = "/getUserInfoById/{id}")
    @MyLog(title = "根据ID查询[用户信息表]详情", businessType = BusinessType.QUERY)
    public JSONResult get(@PathVariable("id")Long id)
    {
        return JSONResult.success(easyUserService.selectUserById(id));
    }


    /**
    * 查看【用户信息表】所有数据（不分页）
    * @date 2022-05-26
    */
    @GetMapping(value = "/list")
    @MyLog(title = "查询[用户信息表]所有数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list(){
        QueryWrapper<EasyUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_show", "1");//是否显示（0不显示，1显示）
        List<EasyUser> list = easyUserService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * 分页查询【用户信息表】数据
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2022-05-26
     */
    @PostMapping(value = "/pagelist")
    @CheckPermission(per = "easyUser:pagelist")
    @MyLog(title = "分页查询【用户信息表】数据", businessType = BusinessType.QUERY)
    public JSONResult pagelist(@RequestBody EasyUserQuery query)
    {
        Page<EasyUser> page = easyUserService.selectMyPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    @PostMapping(value = "/changeStatus")
    @CheckPermission(per = "easyUser:changeStatus")
    @MyLog(title = "用户状态修改", businessType = BusinessType.UPDATE)
    public JSONResult changeStatus(@RequestBody EasyUser user)
    {
        easyUserService.checkUserAllowed(user);
        user.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        user.setUpdateUserId(getUserId());
        return easyUserService.updateUserStatus(user);
    }

    /***********************************************************************************
    以上代码是自动生成的
    自己写的代码放在下面
    ***********************************************************************************/


    @GetMapping("/getUserProfile")
    public JSONResult getUserProfile(){
        UserToken userToken = getUser();
        EasyUser user = easyUserMapper.selectUserById(userToken.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("roleGroup", easyUserService.selectUserRoleGroup(userToken.getUserId()));
        map.put("postGroup", easyUserService.selectUserPostGroup(userToken.getUserId()));
        map.put("user", user);
        return JSONResult.success(map);
    }

    @PostMapping("/updateUserPwd")
    @MyLog(title = "用户修改个人密码", businessType = BusinessType.UPDATE)
    public JSONResult updateUserPwd(@RequestBody EasyUser user) {
        //校验旧密码是否正确
        if(StringUtils.isBlank(user.getOldPassword())){
            throw new MyException(ErrorCode.ERROR_CODE_1042);
        }
        if(StringUtils.isBlank(user.getNewPassword())){
            throw new MyException(ErrorCode.ERROR_CODE_1043);
        }
        //获取当前登录人数据库的密码
        EasyUser userDb = easyUserMapper.selectUserById(getUserId());
        if(!userDb.getPassword().equals(Md5Utils.getPassword(user.getOldPassword()))){
            throw new MyException(ErrorCode.ERROR_CODE_1044);
        }
        user.setId(userDb.getId());
        user.setPassword(Md5Utils.getPassword(user.getNewPassword()));
        user.setUpdateUserId(getUserId());
        user.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        return easyUserService.resetPwd(user);
    }

    @PostMapping("/updateUserProfile")
    @MyLog(title = "用户修改个人信息", businessType = BusinessType.UPDATE)
    public JSONResult updateUserProfile(@RequestBody EasyUser user) {
        user.setUpdateUserId(getUserId());
        user.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        return easyUserService.resetPwd(user);
    }

    @PostMapping("/updateUserAvatar")
    @MyLog(title = "用户修改个人头像", businessType = BusinessType.UPDATE)
    public JSONResult avatar(@RequestParam("avatarfile") MultipartFile file) {
        if (!file.isEmpty()) {
            UserToken userToken = getUser();
            EasyUser user = easyUserService.getById(userToken.getUserId());
            UploadFileVo vo = fileUploadUtils.fileUploadOss(Constants.OSS.USER_IMAGE, file);
            int result = easyUserService.updateUserAvatar(user.getId(), vo.getFileName());
            if(result == 1){
                //删除OSS中用户原来的头像
                fileUploadUtils.removeOssFile(MyTools.getOssFileName(user.getAvatar(), Constants.OSS.USER_IMAGE));
            }
            return JSONResult.success(vo.getFileName());
        }
        return JSONResult.error(ErrorCode.ERROR_CODE_1040);
    }

    //获取部门用户树数据
    @GetMapping("/getDeptUserTree")
    public JSONResult getUserTree(){
        return JSONResult.success(easyUserService.getUserTree());
    }

    /**
     * 获取部门员工下拉树列表
     */
    @GetMapping("/deptUserTree")
    public JSONResult deptUserTree()
    {
        return easyUserService.deptUserTree();
    }

}