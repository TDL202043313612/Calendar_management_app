package cn.wujiangbo.service.system.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.domain.system.EasyPost;
import cn.wujiangbo.domain.system.EasyRole;
import cn.wujiangbo.domain.system.EasyUser;
import cn.wujiangbo.dto.system.*;
import cn.wujiangbo.dto.token.UserToken;
import cn.wujiangbo.enums.UserStatus;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.mapper.system.EasyPostMapper;
import cn.wujiangbo.mapper.system.EasyRoleMapper;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.query.system.EasyUserQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.EasyMenuService;
import cn.wujiangbo.service.system.EasyUserService;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.tools.SystemConfigTool;
import cn.wujiangbo.tools.SystemLogTools;
import cn.wujiangbo.utils.*;
import cn.wujiangbo.utils.ip.AddressUtils;
import cn.wujiangbo.utils.ip.IpUtils;
import cn.wujiangbo.utils.sign.Md5Utils;
import cn.wujiangbo.utils.uuid.UUID;
import cn.wujiangbo.vo.system.LoginSuccessVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyUserServiceImpl extends ServiceImpl<EasyUserMapper, EasyUser> implements EasyUserService {

    @Resource
    private EasyUserMapper easyUserMapper;

    @Resource
    public SystemConfigTool systemConfigTool;

    @Resource
    private EasyMenuService easyMenuService;

    @Resource
    private EasyRoleMapper easyRoleMapper;

    @Resource
    private EasyPostMapper easyPostMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private SystemLogTools systemLog;

    @Resource
    private CommonTools commonTools;

    @Resource
    private SystemConfigDto systemConfigDto;

    //查询列表
    @Override
    public Page<EasyUser> selectMyPage(EasyUserQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyUser> list = easyUserMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    @Override
    public EasyUser selectUserById(Long userId) {
        return easyUserMapper.selectUserById(userId);
    }

    //保存用户信息到数据库
    @Override
    public JSONResult saveUser(EasyUser user) {
        //校验登录账号是否已存在
        int userNameResult = easyUserMapper.checkUserNameUnique(user.getUserName());
        if(userNameResult > 0){
            //说明注册的账号已经存在了
            throw new MyException(ErrorCode.USERNAME_EXIST);
        }
        //校验手机号码是否已存在（手机号可不填写）
        if(MyTools.hasLength(user.getPhonenumber())){
            EasyUser phoneResult = easyUserMapper.checkPhoneUnique(user.getPhonenumber());
            if(phoneResult != null){
                //说明注册手机号已经存在了
                throw new MyException(ErrorCode.ERROR_CODE_1027);
            }
        }
        //校验邮箱是否已存在（邮箱可不填写）
        if(MyTools.hasLength(user.getEmail())){
            EasyUser emailResult = easyUserMapper.checkEmailUnique(user.getEmail());
            if(emailResult != null){
                //说明注册邮箱已经存在了
                throw new MyException(ErrorCode.ERROR_CODE_1022);
            }
        }

        //密码加密
        String password = Md5Utils.getPassword(user.getPassword());
        user.setPassword(password);
        user.setAvatar(systemConfigDto.getUserDefaultAvatarPath());//设置默认头像
        //保存用户信息到数据库
        easyUserMapper.insert(user);

        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);

        //返回结果集
        return JSONResult.success(true);
    }

    //修改用户信息
    @Override
    @Transactional
    public JSONResult updateUser(EasyUser user) {
        Long userId = user.getId();
        if(MyTools.hasLength(user.getPhonenumber())){
            //校验手机号码是否已存在
            EasyUser phoneResult = easyUserMapper.checkPhoneUnique(user.getPhonenumber());
            if(StringUtils.isNotNull(phoneResult) && phoneResult.getId().longValue() != userId.longValue()){
                //说明注册手机号已经存在了
                throw new MyException(ErrorCode.ERROR_CODE_1027);
            }
        }
        if(MyTools.hasLength(user.getEmail())){
            //校验邮箱是否已存在
            EasyUser emailResult = easyUserMapper.checkEmailUnique(user.getEmail());
            if(StringUtils.isNotNull(emailResult) && emailResult.getId().longValue() != userId.longValue()){
                //说明注册邮箱已经存在了
                throw new MyException(ErrorCode.ERROR_CODE_1022);
            }
        }

        // 删除用户与角色关联
        easyUserMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);

        // 删除用户与岗位关联
        easyUserMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);

        //更新用户信息
        easyUserMapper.updateUser(user);

        //返回结果集
        return JSONResult.success(true);
    }

    //重置用户密码
    @Override
    public JSONResult resetPwd(EasyUser user) {
        easyUserMapper.updateUser(user);
        return JSONResult.success(true);
    }

    @Override
    public JSONResult getLoginPageInfo() {
        return JSONResult.success();
    }

    @Override
    public JSONResult login(HttpServletRequest request, String username, String password, String code, String uuid) {
        //根据账号查询用户信息
        EasyUser user = easyUserMapper.selectUserByUserName(username);

        /**
         * 开始校验账号信息
         */
        if(user == null){
            //账号不存在
            setLoginInfo(username, ErrorCode.USERNAME_NOT_EXIST.getMessage());
            throw new MyException(ErrorCode.USERNAME_NOT_EXIST);
        }

        //获取用户ID
        Long userId = user.getId();

        //查询用户所拥有的角色ID
        Long[] userRoleIds = easyUserMapper.selectUserRoleIdsByUserId(userId);

        if(!user.getPassword().equals(Md5Utils.getPassword(password))){
            //登录密码错误
            setLoginInfo(username, ErrorCode.LOGIN_PASSWORD_ERROR.getMessage());
            throw new MyException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        if(user.getStatus().equals(UserStatus.DISABLE.getCode())){
            //账号停用了
            setLoginInfo(username, ErrorCode.USERNAME_NO_USEING.getMessage());
            throw new MyException(ErrorCode.USERNAME_NO_USEING);
        }

        //用户信息存Redis的key值
        String redisToken = UUID.fastUUID().toString(true);

        //查询用户是否是超级管理员
        Integer result = easyUserMapper.selectRoleIdByUserId(userId);

        /**
         * 1、此时说明账号登录成功，需要返回token
         * 2、token中需要包含用户相关信息，以及权限字符串集合
         */
        Set<String> permissionsSet = easyMenuService.selectMenuPermsByUserId(userId, result == null ? false : result.intValue() == 1);

        //获取登录人IP地址
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

        //封装token对象
        UserToken uToken = new UserToken();
        uToken.setUserId(userId);
        uToken.setEmpNo(user.getEmpNo());
        uToken.setRoleId(userRoleIds);
        uToken.setDeptId(null);
        uToken.setPermissions(permissionsSet);
        uToken.setIpaddr(ip);
        uToken.setUserName(user.getUserName());
        uToken.setNickName(user.getNickName());
        uToken.setAvatar(user.getAvatar());
        uToken.setCreateTime(user.getCreateTime());
        uToken.setPhonenumber(user.getPhonenumber());
        uToken.setDeptId(user.getDeptId());
        uToken.setDeptName(user.getDeptName());
        uToken.setEmail(user.getEmail());
        uToken.setSex(user.getSex());
        String timeStamp = String.valueOf(System.currentTimeMillis());
        uToken.setTimeStamp(timeStamp);
        uToken.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        uToken.setBrowser(userAgent.getBrowser().getName());
        uToken.setOs(userAgent.getOperatingSystem().getName());
        uToken.setLoginTime(new Date().getTime());

        uToken.setSuperAdmin(result == null ? false : result.intValue() == 1);

        //用户信息存Redis
        redisCache.setCacheObject(redisToken, JSONObject.toJSONString(uToken), Constants.TOKEN_REDIS_ALIVE_TIME, TimeUnit.HOURS);


        //封装返回值
        LoginSuccessVo vo = new LoginSuccessVo();
        vo.setToken(redisToken);
        vo.setUserToken(uToken);
        vo.setUserId(userId);

        //封装对象
        EasyUser u = new EasyUser();
        u.setId(userId);
        u.setLoginIp(ip);
        u.setLoginDate(DateUtils.getCurrentLocalDateTime());
        easyUserMapper.updateById(u);

        //记录登录日志
        setLoginInfo(user.getNickName(), "登录成功");

        return JSONResult.success(vo);
    }

    @Override
    public String selectUserRoleGroup(Long userId) {
        List<EasyRole> roleList = easyRoleMapper.selectRolesByUserId(userId);
        if (CollectionUtils.isEmpty(roleList))
        {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        return roleList.stream().map(EasyRole::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    public String selectUserPostGroup(Long userId) {
        List<EasyPost> postList = easyPostMapper.selectPostsByUserId(userId);
        if (CollectionUtils.isEmpty(postList))
        {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        return postList.stream().map(EasyPost::getPostName).collect(Collectors.joining(","));
    }

    @Override
    public int updateUserAvatar(Long userId, String imagePath) {
        return easyUserMapper.updateUserAvatar(userId, imagePath);
    }

    /**
     * 获取部门用户树数据
     */
    @Override
    public List<Tree<String>> getUserTree() {
        //查询部门用户数据
        List<DeptUserTreeDto> deptUserTreeList = easyUserMapper.getDeptUserTree();
        /**
         * 这里利用Hutool工具类处理
         */
        //树相关属性配置类
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 默认支持排序
        treeNodeConfig.setWeightKey("orderNum");
        //可配置树深度
        treeNodeConfig.setDeep(10);
        treeNodeConfig.setIdKey("id");
        List<Tree<String>> build = TreeUtil.build(deptUserTreeList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getOrderNum());
                    tree.setName(treeNode.getName());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("label", treeNode.getName());
                });
        return build;
    }

    @Override
    public JSONResult deptUserTree() {
        List<DeptUserDto> deptTrees = commonTools.getDeptUserTreesData();
        List<UserTreeSelect> list = deptTrees.stream().map(UserTreeSelect::new).collect(Collectors.toList());
        return JSONResult.success(list);
    }

    @Override
    public JSONResult refreshPermission(HttpServletRequest request, Long userId) {
        EasyUser user = easyUserMapper.selectUserById(userId);
        //查询用户是否是超级管理员
        Integer result = easyUserMapper.selectRoleIdByUserId(userId);
        //查询当前用户所拥有的权限集合
        Set<String> permissionsSet = easyMenuService.selectMenuPermsByUserId(userId, result == null ? false : result.intValue() == 1);
        //查询当前用户所拥有的角色ID
        Long[] userRoleIds = easyUserMapper.selectUserRoleIdsByUserId(userId);

        //获取登录人IP地址
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

        UserToken uToken = getUserToken(userId, user, userRoleIds, permissionsSet, ip, result);

        //用户信息存Redis
        String redisToken = request.getHeader(Constants.TOKEN);
        redisCache.setCacheObject(redisToken, JSONObject.toJSONString(uToken), Constants.TOKEN_REDIS_ALIVE_TIME, TimeUnit.HOURS);
        return JSONResult.success();
    }

    public UserToken getUserToken(Long userId, EasyUser user,
                                  Long[] userRoleIds, Set<String> permissionsSet, String ip, Integer result){
        //获取token
        UserToken uToken = new UserToken();
        uToken.setUserId(userId);
        uToken.setEmpNo(user.getEmpNo());
        uToken.setRoleId(userRoleIds);
        uToken.setDeptId(null);
        uToken.setPermissions(permissionsSet);
        uToken.setIpaddr(ip);
        uToken.setUserName(user.getUserName());
        uToken.setNickName(user.getNickName());
        uToken.setAvatar(user.getAvatar());
        uToken.setCreateTime(user.getCreateTime());
        uToken.setPhonenumber(user.getPhonenumber());
        uToken.setDeptId(user.getDeptId());
        uToken.setDeptName(user.getDeptName());
        uToken.setEmail(user.getEmail());
        uToken.setSex(user.getSex());
        String timeStamp = String.valueOf(System.currentTimeMillis());
        uToken.setTimeStamp(timeStamp);

        uToken.setSuperAdmin(result == null ? false : result.intValue() == 1);
        return uToken;
    }

    //记录登录日志
    public void setLoginInfo(String username, String message){
        systemLog.saveLoginLog(username, message);
    }

    @Override
    @Transactional
    public JSONResult deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new EasyUser(userId));
        }
        // 删除用户与角色关联
        easyUserMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        easyUserMapper.deleteUserPost(userIds);
        easyUserMapper.deleteUserByIds(userIds);
        return JSONResult.success(true);
    }

    /**
     * 校验用户是否允许操作
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(EasyUser user)
    {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin())
        {
            throw new MyException(ErrorCode.ERROR_CODE_1029);
        }
    }

    @Override
    public JSONResult updateUserStatus(EasyUser user) {
        easyUserMapper.updateUser(user);
        return JSONResult.success(true);
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(EasyUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<UserPostDto> list = new ArrayList<>();
            for (Long postId : posts)
            {
                UserPostDto up = new UserPostDto();
                up.setUserId(user.getId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                easyUserMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(EasyUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<UserRoleDto> list = new ArrayList<>();
            for (Long roleId : roles)
            {
                UserRoleDto ur = new UserRoleDto();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                easyUserMapper.batchUserRole(list);
            }
        }
    }
}