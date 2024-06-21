package cn.wujiangbo.controller.app;

import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.domain.community.AppUser;
import cn.wujiangbo.dto.app.ChatDto;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.query.community.AppUserQuery;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.service.community.AppUserService;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.SearchWeatherBaidu;
import cn.wujiangbo.utils.file.FileUploadUtils;
import cn.wujiangbo.utils.http.HttpUtils;
import cn.wujiangbo.utils.poi.ExcelUtil;
import cn.wujiangbo.vo.system.UploadFileVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author bobo(weixin : javabobo0513)
 * @desc APP用户信息表 API接口
 * @date 2024-03-29
 */
@RestController
@RequestMapping("/communityUser")
public class AppUserController extends BaseController {

    @Resource
    public AppUserService appUserService;

    @Resource
    private FileUploadUtils fileUploadUtils;

    /**
     * 新增数据到【社区APP用户信息表】
     *
     * @date 2024-03-29
     */
    @PostMapping(value = "/save")
    @CheckPermission(per = "communityUser:save")
    @MyLog(title = "新增数据到【社区APP用户信息表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody AppUser appUser) {
        appUser.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appUser.setCreateUserId(getUserId());
        appUser.setCreateUserName(getUserName());
        appUser.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appUser.setUpdateUserId(getUserId());
        appUser.setUpdateUserName(getUserName());
        appUserService.save(appUser);
        return JSONResult.success(true);
    }

    /**
     * 删除【社区APP用户信息表】数据
     *
     * @date 2024-03-29
     */
    @PostMapping(value = "/batchDelete")
    @MyLog(title = "删除【社区APP用户信息表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "communityUser:deleteBatch")
    public JSONResult batchDelete(@RequestBody AppUserQuery query) {
        //删除数据库数据
        appUserService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 修改【社区APP用户信息表】表数据
     *
     * @date 2024-03-29
     */
    @PostMapping(value = "/update")
    @MyLog(title = "修改【社区APP用户信息表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "communityUser:update")
    public JSONResult update(@RequestBody AppUser appUser) {
        appUser.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appUser.setUpdateUserId(getUserId());
        appUser.setUpdateUserName(getUserName());
        appUserService.updateById(appUser);
        return JSONResult.success(true);
    }

    /**
     * 查询【社区APP用户信息表】数据（分页）
     *
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2024-03-29
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【社区APP用户信息表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "communityUser:pagelist")
    public JSONResult pagelist(@RequestBody AppUserQuery query) {
        Page<AppUser> page = appUserService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
     * 查询【社区APP用户信息表】数据（不分页）
     *
     * @date 2024-03-29
     */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【社区APP用户信息表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list() {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppUser> list = appUserService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * 根据ID查询【社区APP用户信息表】数据
     *
     * @date 2024-03-29
     */
    @GetMapping(value = "/getDataById/{id}")
    @MyLog(title = "根据ID查询【社区APP用户信息表】数据", businessType = BusinessType.QUERY)
    public JSONResult getDataById(@PathVariable("id") Long id) {
        return JSONResult.success(appUserService.getById(id));
    }

    /**
     * 根据 QueryWrapper 对象查询【社区APP用户信息表】数据
     *
     * @date 2024-03-29
     */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        AppUser one = appUserService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /**
     * 【社区APP用户信息表】数据导出Excel
     */
    @PostMapping(value = "/exportExcelFile")
    @CheckPermission(per = "communityUser:exportExcelFile")
    @MyLog(title = "【社区APP用户信息表】数据导出Excel", businessType = BusinessType.EXPORT)
    public void exportExcelFile(HttpServletResponse response, AppUserQuery query) {
        List<AppUser> list = appUserService.selectExportExcelData(query);
        ExcelUtil<AppUser> util = new ExcelUtil<>(AppUser.class);
        util.exportExcel(response, list, "社区APP用户信息表");
    }

    /**
     * xxx操作
     */
    @PostMapping(value = "/xxxHandle")
    @MyLog(title = "xxxxxxxxxx", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "xxxxxx:xxxxxx")
    public JSONResult xxxHandle(@RequestBody AppUser appUser) {
        AppUser obj = appUserService.getById(appUser.getId());
        if (obj == null) {
            throw new MyException(ErrorCode.COMMON_CODE_2001);
        }
        //做其他操作
        appUserService.updateById(obj);
        return JSONResult.success(true);
    }

    /***********************************************************************************
     以上代码是自动生成的
     自己写的代码放在下面
     ***********************************************************************************/

    @Value("${easyjava.ak}")
    private String ak;

    @Value("${easyjava.sk}")
    private String sk;

    /**
     * 根据行政区划编码获取天气情况
     */
    @GetMapping(value = "/getWeather/{districtId}")
    public JSONResult getWeather(@PathVariable("districtId") String districtId) {
        SearchWeatherBaidu snCal = new SearchWeatherBaidu();
        Map params = new LinkedHashMap<String, String>();
        params.put("district_id", districtId);
        params.put("data_type", "all");
        params.put("ak", ak);
        String result = snCal.requestGetAK(params);
        JSONObject json = JSONObject.parseObject(result);
        int status = (int) json.get("status");
        if (status != 0) {
            throw new MyException("调用百度获取天气异常！");
        } else {
            Map<String, Object> map = new HashMap<>();
            JSONObject r = (JSONObject) json.get("result");
            map.put("location", r.get("location"));//位置信息
            map.put("now", r.get("now"));//当前天气信息
            map.put("forecasts", r.get("forecasts"));//接下来五天天气信息，数组
            return JSONResult.success(map);
        }
    }

    /**
     * APP查询个人资料
     *
     * @param userId 用户ID
     */
    @GetMapping(value = "/getUserInfoById/{userId}")
    public JSONResult getUserInfoById(@PathVariable("userId") Long userId) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userId);
        AppUser one = appUserService.getOne(queryWrapper);
        if (one == null) {
            throw new MyException("无此ID用户信息！");
        }
        return JSONResult.success(one);
    }

    /**
     * APP-充值接口
     */
    @PostMapping(value = "/chongzhi")
    public JSONResult chongzhi(@RequestBody AppUser user) {
        if (user.getId() == null) {
            throw new MyException("用户数据异常，请重新登录后再操作！");
        }
        if (user.getUserMoney() == null) {
            throw new MyException("充值金额有误！");
        }
        if (user.getUserMoney().compareTo(new BigDecimal(500)) > 0) {
            throw new MyException("充值金额不能大于500！");
        }
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", user.getId());
        AppUser one = appUserService.getOne(queryWrapper);
        if (one == null) {
            throw new MyException("无此用户信息，请重新登录！");
        }
        one.setUserMoney(one.getUserMoney().add(user.getUserMoney()));
        appUserService.updateById(one);
        return JSONResult.success(true, "充值成功！");
    }

    /**
     * APP-提现接口
     */
    @PostMapping(value = "/tixian")
    public JSONResult tixian(@RequestBody AppUser user) {
        if (user.getId() == null) {
            throw new MyException("用户数据异常，请重新登录后再操作！");
        }
        if (user.getUserMoney() == null) {
            throw new MyException("提现金额有误！");
        }
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", user.getId());
        AppUser one = appUserService.getOne(queryWrapper);
        if (one == null) {
            throw new MyException("无此用户信息，请重新登录！");
        }
        if (user.getUserMoney().compareTo(one.getUserMoney()) > 0) {
            throw new MyException("余额不足！");
        }

        one.setUserMoney(one.getUserMoney().subtract(user.getUserMoney()));
        appUserService.updateById(one);
        return JSONResult.success(true, "提现成功！");
    }

    /**
     * 上传文件接口
     *
     * @param file 文件对象
     */
    @PostMapping("/uploadUserImg")
    public JSONResult imageMission(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new MyException("上传的图片为空！");
        }
        UploadFileVo vo = fileUploadUtils.fileUploadOss(Constants.OSS.RESOURCE_LINK_IMG, file);
        String filePath = vo.getFilePrefix() + vo.getFileName();
        System.out.println("上传头像保存路径=" + filePath);
        //修改个人头像资料
        Long appUserId = getAppUserId();
        if (appUserId == null || appUserId == -1) {
            throw new MyException("用户信息错误，请重新登录！");
        }
        AppUser user = appUserService.getById(appUserId);
        user.setUserImg(filePath);
        appUserService.updateById(user);
        return JSONResult.success(true, "文件上传成功，保存路径为:" + filePath);
    }

    /**
     * APP页面-修改个人资料
     */
    @PostMapping(value = "/updateUserInfo")
    public JSONResult updateUserInfo(@RequestBody AppUser appUser) {
        if (appUser.getId() == null) {
            throw new MyException("用户数据异常，请重新登录后再操作！");
        }
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", appUser.getId());
        AppUser one = appUserService.getOne(queryWrapper);
        if (one == null) {
            throw new MyException("无此用户信息，请重新登录！");
        }
        one.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        one.setNickName(appUser.getNickName());
        one.setUserPhone(appUser.getUserPhone());
        appUserService.updateById(one);
        return JSONResult.success(true, "个人资料修改成功！");
    }

    /**
     * chat对话
     */
    @PostMapping(value = "/chat")
    public JSONResult chat(@RequestBody AppUserQuery query) {
        String keyword = query.getKeyword();
        if (!MyTools.hasLength(keyword)) {
            throw new MyException("请输入你需要问的问题哦！");
        }
        //1、发请求获取token
        String tokenResult = HttpUtils.sendPost("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + ak + "&client_secret=" + sk, "");
        if (MyTools.hasLength(tokenResult)) {
            JSONObject jsonObject = JSONObject.parseObject(tokenResult);
            if (jsonObject != null) {
                String accessToken = (String) jsonObject.get("access_token");
                //2、发请求获取答案
                JSONObject json = new JSONObject();
                json.put("role", "user");
                json.put("content", keyword);
                String str = "{\"messages\": [" + json.toString() + "]}";
                String answerResult = HttpUtils.sendPost("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-4.0-8k-0329?access_token=" + accessToken, str);
                JSONObject answerObject = JSONObject.parseObject(answerResult);
                return JSONResult.successData(answerObject.get("result").toString());
            }
        }
        return JSONResult.success(true, "暂时无法回答您！");
    }

    /**
     * post方式的http请求
     *
     * @param httpUrl  请求地址
     * @param paramStr 请求参数
     * @return 返回结果
     */
    public static String doPost(String httpUrl, String paramStr) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 通过连接对象获取一个输出流
            outputStream = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            outputStream.write(paramStr.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sbf = new StringBuilder();
                String temp;
                // 循环遍历一行一行读取数据
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append(System.getProperty("line.separator"));
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}