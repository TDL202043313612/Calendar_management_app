package cn.wujiangbo.tools;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.domain.system.EasyDept;
import cn.wujiangbo.domain.system.EasyImgResource;
import cn.wujiangbo.domain.system.EasyUser;
import cn.wujiangbo.dto.oss.OssDto;
import cn.wujiangbo.dto.system.DeptUserDto;
import cn.wujiangbo.dto.token.UserToken;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.mapper.system.EasyDeptMapper;
import cn.wujiangbo.mapper.system.EasyImgResourceMapper;
import cn.wujiangbo.mapper.system.EasyUserMapper;
import cn.wujiangbo.utils.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用的工具方法
 *
 */
@Component
public class CommonTools {

    @Autowired
    private SystemConfigTool systemConfigTool;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private EasyImgResourceMapper easyImgResourceMapper;

    @Autowired
    private EasyDeptMapper easyDeptMapper;

    @Autowired
    private EasyUserMapper easyUserMapper;

    @Autowired
    private OssDto ossDto;

    //获取文件在OSS存储的路径前缀
    public String getOssPrePath(){
        return "https://" + ossDto.getBucketName() + "." + ossDto.getEndpoint() + "/";
    }

    /**
     * @desc 获取员工编号
     * 生成规则：指定前缀字符串+当前年份+序号(递增的4位数）
     * @return 最新的员工编号
     */
    public synchronized String getEmpNo() {
        String maxEmpNo = easyUserMapper.selectMaxEmpNo().getEmpNo();
        String prefix = "YG" + DateUtils.getCurrentDateString("yyyyMMdd");
        String empNo = prefix + "0001";
        if(!StringUtils.isBlank(maxEmpNo)){
            String maxOldNum = maxEmpNo.substring(10);
            int i = Integer.parseInt(maxOldNum) + 1;
            //%nd 输出的整型宽度至少为n位，右对齐，%8d即宽度至少为8位，位数大于8则输出实际位数，0表示用0补齐
            empNo = prefix + String.format("%04d", i);
        }
        return empNo;
    }


    /**
     * @desc 根据员工编号查询员工详情信息(包含所属部门、角色、职位等信息)
     * @author wujiangbo
     * @date 2022/8/3 13:18
     * @param empNo 员工编号
     * @return cn.wujiangbo.domain.system.EasyUser
     */
    public EasyUser getUserDetailsByEmpNo(String empNo){
        EasyUser easyUser = easyUserMapper.selectUserByEmpNo(empNo);
        return easyUser == null ? new EasyUser() : easyUser;
    }

    /**
     * 保存资源到数据库
     * @param multipartFile 文件对象
     * @param filePath 服务器存储路径
     * @param userId 操作人ID
     * @param busiNo 业务编号
     * @param funcFlag 功能标识（与业务场景有关，自由指定）
     */
    public void saveRes(MultipartFile multipartFile, String filePath, Long userId, String busiNo, String funcFlag){
        if(multipartFile == null){
            throw new MyException(ErrorCode.ERROR_CODE_1040);
        }
        if(StringUtils.isBlank(busiNo)){
            throw new MyException(ErrorCode.ERROR_CODE_1052);
        }
        //资源信息保存到数据库
        EasyImgResource easyImgResource = new EasyImgResource();
        easyImgResource.setCreateTime(DateUtils.getCurrentLocalDateTime());
        easyImgResource.setCreateUserId(userId);
        easyImgResource.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        easyImgResource.setUpdateUserId(userId);
        easyImgResource.setBusiNo(busiNo);
        easyImgResource.setFuncFlag(funcFlag);
        easyImgResource.setResType(multipartFile.getContentType());
        easyImgResource.setResSize(multipartFile.getSize());
        String fileName = multipartFile.getOriginalFilename();
        easyImgResource.setResName(fileName);
        easyImgResource.setResSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
        easyImgResource.setResPath(filePath);
        easyImgResourceMapper.insert(easyImgResource);
    }

    /**
     * @desc 校验图片验证码是否正确
     * @author wujiangbo
     * @date 2022/7/2 13:44
     * @param uuid 前端传过来的UUID
     * @param code 前端传过来用户输入的图片验证码内容
     */
    public void checkImgCode(String uuid, String code){
        //是否校验登录图片验证码：0：校验；1：不校验
        String captchaOnOff = systemConfigTool.getConfigValueByKey(Constants.SYSTEM_CONFIG_KEY.DEFAULT_CAPTCHAONOFF);
        if("0".equals(captchaOnOff)){
            //验证图片验证码正确性
            String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
            String codeValue = redisCache.getCacheObject(verifyKey);
            if(StringUtils.isBlank(codeValue)){
                throw new MyException(ErrorCode.LOGIN_CODE_EXPIRE);
            }
            if(!code.equalsIgnoreCase(codeValue)){
                throw new MyException(ErrorCode.LOGIN_CODE_ERROR);
            }
        }
    }

    /**
     * 查询部门树形结构数据集合
     * @return
     */
    public List<EasyDept> getDeptTreesData(){
        //查询所有部门信息
        QueryWrapper<EasyDept> deptWrapper = new QueryWrapper<>();
        deptWrapper.eq("status", "0");//部门状态（0正常 1停用）
        deptWrapper.eq("del_flag", "0");//删除标志（0代表存在 2代表删除）
        List<EasyDept> deptList = easyDeptMapper.selectList(deptWrapper);

        Map<Long, EasyDept> map = new HashMap<>();
        for(int i=0; i<deptList.size(); i++){
            EasyDept easyDept = deptList.get(i);
            map.put(easyDept.getId(), easyDept);
        }

        List<EasyDept> firstList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(deptList)){
            for(int i=0; i<deptList.size(); i++){
                EasyDept easyDept = deptList.get(i);
                if(easyDept.getParentId() == null || easyDept.getParentId().longValue() == 0){
                    //说明是顶级部门
                    firstList.add(easyDept);
                }else{
                    //说明都是非顶级部门了
                    EasyDept parentDept = map.get(easyDept.getParentId());
                    parentDept.getChildren().add(easyDept);
                }
            }
        }
        return firstList;
    }

    /**
     * 查询部门用户树形结构数据集合
     * @return
     */
    public List<DeptUserDto> getDeptUserTreesData(){
        //查询部门用户信息
        List<DeptUserDto> deptList = easyDeptMapper.selectDeptUserList();

        Map<String, DeptUserDto> map = new HashMap<>();
        for(int i=0; i<deptList.size(); i++){
            DeptUserDto easyDept = deptList.get(i);
            map.put(easyDept.getId(), easyDept);
        }

        List<DeptUserDto> firstList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(deptList)){
            for(int i=0; i<deptList.size(); i++){
                DeptUserDto easyDept = deptList.get(i);
                if("0".equals(easyDept.getParentId())){
                    //说明是顶级部门
                    firstList.add(easyDept);
                }else{
                    //说明都是非顶级部门了
                    DeptUserDto parentDept = map.get(easyDept.getParentId());
                    parentDept.getChildren().add(easyDept);
                }
            }
        }
        return firstList;
    }

    public UserToken getUser(){
        HttpServletRequest request = ServletUtils.getRequest();
        if(request != null){
            String headerToken = request.getHeader("token");
            if(MyTools.hasLength(headerToken)){
                String userTokenString = redisCache.getCacheObject(headerToken);
                if(MyTools.hasLength(userTokenString)){
                    UserToken userToken = (UserToken)JSONObject.parseObject(userTokenString, UserToken.class);
                    if(userToken != null){
                        return userToken;
                    }
                    else{
                        throw new MyException(ErrorCode.NO_PERMISSION);
                    }
                }
                throw new MyException(ErrorCode.TOKEN_EXPIRE);
            }
            else{
                throw new MyException(ErrorCode.NO_PERMISSION);
            }
        }
        return new UserToken();
    }

    /**
     * 获取登录人的主键ID
     * @return
     */
    public Long getUserId(){
        return getUser().getUserId();
    }

    /**
     * 获取登录人的真实姓名
     * @return
     */
    public String getUserName(){
        return getUser().getNickName();
    }

    /**
     * 获取登录人的员工编号
     * @return
     */
    public String getCurrentEmpNo(){
        return getUser().getEmpNo();
    }
}