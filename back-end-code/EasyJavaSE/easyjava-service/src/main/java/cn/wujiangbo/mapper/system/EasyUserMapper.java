package cn.wujiangbo.mapper.system;

import cn.wujiangbo.domain.system.EasyUser;
import cn.wujiangbo.dto.system.DeptUserTreeDto;
import cn.wujiangbo.dto.system.UserPostDto;
import cn.wujiangbo.dto.system.UserRoleDto;
import cn.wujiangbo.query.system.EasyUserQuery;
import cn.wujiangbo.vo.system.OnlineUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* <p>
* 用户信息表 Mapper接口
* </p>
*
*/
public interface EasyUserMapper extends BaseMapper<EasyUser> {

    List<EasyUser> selectMyPage(Page<EasyUser> page, @Param("param") EasyUserQuery query);

    EasyUser selectUserById(Long userId);

    EasyUser selectUserByEmpNo(String empNo);

    //根据员工编号集合查询员工姓名集合
    List<String> selectUserNameListByEmpNo(List<String> empNoList);

    //校验用户名称是否唯一
    int checkUserNameUnique(String userName);

    //校验手机号唯一
    EasyUser checkPhoneUnique(String phonenumber);

    //校验邮箱唯一
    EasyUser checkEmailUnique(String email);

    void batchUserPost(List<UserPostDto> list);

    void batchUserRole(List<UserRoleDto> list);

    void deleteUserRole(Long[] userIds);

    void deleteUserPost(Long[] userIds);

    void deleteUserByIds(Long[] userIds);

    void updateUser(EasyUser user);

    void deleteUserRoleByUserId(Long userId);

    void deleteUserPostByUserId(Long userId);

    EasyUser selectUserByUserName(String username);

    //查询最大的员工编号
    EasyUser selectMaxEmpNo();

    //获取所有在线用户信息
    List<OnlineUserVO> getOnlineUserIdList(Page<OnlineUserVO> page, @Param("list") List<Long> list);

    int updateUserAvatar(@Param("userId") Long userId, @Param("avatar") String avatar);

    List<DeptUserTreeDto> getDeptUserTree();

    Long[] selectUserRoleIdsByUserId(Long userId);

    Integer selectRoleIdByUserId(Long userId);
}