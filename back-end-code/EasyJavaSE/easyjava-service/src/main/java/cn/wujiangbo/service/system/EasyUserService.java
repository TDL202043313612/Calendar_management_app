package cn.wujiangbo.service.system;

import cn.hutool.core.lang.tree.Tree;
import cn.wujiangbo.domain.system.EasyUser;
import cn.wujiangbo.query.system.EasyUserQuery;
import cn.wujiangbo.result.JSONResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 */
public interface EasyUserService extends IService<EasyUser> {

    //分页查询
    Page<EasyUser> selectMyPage(EasyUserQuery query);

    EasyUser selectUserById(Long userId);

    JSONResult saveUser(EasyUser easyUser);

    JSONResult deleteUserByIds(Long[] userIds);

    /**
     * 校验用户是否允许操作
     * @param user 用户信息
     */
    void checkUserAllowed(EasyUser user);

    JSONResult updateUserStatus(EasyUser user);

    JSONResult updateUser(EasyUser easyUser);

    JSONResult resetPwd(EasyUser user);

    JSONResult getLoginPageInfo();

    JSONResult login(HttpServletRequest request, String username, String password, String code, String uuid);

    /**
     * 根据用户ID查询用户所属角色组
     */
    String selectUserRoleGroup(Long userId);

    /**
     * 根据用户ID查询用户所属岗位组
     */
    String selectUserPostGroup(Long userId);

    int updateUserAvatar(Long userId, String imagePath);

    List<Tree<String>> getUserTree();

    JSONResult deptUserTree();

    JSONResult refreshPermission(HttpServletRequest request, Long userId);
}