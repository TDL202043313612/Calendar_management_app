package cn.wujiangbo.controller.app;

import cn.wujiangbo.domain.app.AppNews;
import cn.wujiangbo.result.PageList;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.service.app.AppNewsService;
import cn.wujiangbo.query.app.AppNewsQuery;
import cn.wujiangbo.annotation.CheckPermission;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.annotation.MyLog;
import cn.wujiangbo.enums.BusinessType;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.poi.ExcelUtil;
import cn.wujiangbo.constants.ErrorCode;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.HashMap;

/**
 * @author bobo(weixin : javabobo0513)
 * @desc 新闻信息表 API接口
 * @date 2024-04-30
 */
@RestController
@RequestMapping("/appNews")
public class AppNewsController extends BaseController {

    @Resource
    public AppNewsService appNewsService;

    /**
     * 新增数据到【新闻信息表】
     *
     * @date 2024-04-30
     */
    @PostMapping(value = "/save")
    @CheckPermission(per = "appNews:save")
    @MyLog(title = "新增数据到【新闻信息表】", businessType = BusinessType.INSERT)
    public JSONResult save(@RequestBody AppNews appNews) {
        appNews.setCreateTime(DateUtils.getCurrentLocalDateTime());
        appNews.setCreateUserId(getUserId());
        appNews.setCreateUserName(getUserName());
        appNews.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appNews.setUpdateUserId(getUserId());
        appNews.setUpdateUserName(getUserName());
        appNewsService.save(appNews);
        return JSONResult.success(true);
    }

    /**
     * 删除【新闻信息表】数据
     *
     * @date 2024-04-30
     */
    @PostMapping(value = "/batchDelete")
    @MyLog(title = "删除【新闻信息表】数据", businessType = BusinessType.DELETE)
    @CheckPermission(per = "appNews:deleteBatch")
    public JSONResult batchDelete(@RequestBody AppNewsQuery query) {
        //删除数据库数据
        appNewsService.removeByIds(Arrays.asList(query.getIds()));
        return JSONResult.success(true);
    }

    /**
     * 修改【新闻信息表】表数据
     *
     * @date 2024-04-30
     */
    @PostMapping(value = "/update")
    @MyLog(title = "修改【新闻信息表】表数据", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "appNews:update")
    public JSONResult update(@RequestBody AppNews appNews) {
        appNews.setUpdateTime(DateUtils.getCurrentLocalDateTime());
        appNews.setUpdateUserId(getUserId());
        appNews.setUpdateUserName(getUserName());
        appNewsService.updateById(appNews);
        return JSONResult.success(true);
    }

    /**
     * 查询【新闻信息表】数据（分页）
     *
     * @param query 查询对象
     * @return PageList 分页对象
     * @date 2024-04-30
     */
    @PostMapping(value = "/pagelist")
    @MyLog(title = "查询【新闻信息表】数据（分页）", businessType = BusinessType.QUERY)
    @CheckPermission(per = "appNews:pagelist")
    public JSONResult pagelist(@RequestBody AppNewsQuery query) {
        Page<AppNews> page = appNewsService.selectMySqlPage(query);
        return JSONResult.success(new PageList<>(page.getTotal(), page.getRecords()));
    }

    /**
     * 查询【新闻信息表】数据（不分页）
     *
     * @date 2024-04-30
     */
    @GetMapping(value = "/list")
    @MyLog(title = "查询【新闻信息表】数据（不分页）", businessType = BusinessType.QUERY)
    public JSONResult list() {
        QueryWrapper<AppNews> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppNews> list = appNewsService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * 根据ID查询【新闻信息表】数据
     *
     * @date 2024-04-30
     */
    @GetMapping(value = "/getDataById/{id}")
    @MyLog(title = "根据ID查询【新闻信息表】数据", businessType = BusinessType.QUERY)
    public JSONResult getDataById(@PathVariable("id") Long id) {
        return JSONResult.success(appNewsService.getById(id));
    }

    /**
     * 根据 QueryWrapper 对象查询【新闻信息表】数据
     *
     * @date 2024-04-30
     */
    @GetMapping(value = "/queryWrapper/{objName}")
    public JSONResult queryWrapper(@PathVariable("objName") String objName) {
        QueryWrapper<AppNews> queryWrapper = new QueryWrapper();
        queryWrapper.eq("", objName);
        AppNews one = appNewsService.getOne(queryWrapper);
        return JSONResult.success(one);
    }

    /**
     * 【新闻信息表】数据导出Excel
     */
    @PostMapping(value = "/exportExcelFile")
    @CheckPermission(per = "appNews:exportExcelFile")
    @MyLog(title = "【新闻信息表】数据导出Excel", businessType = BusinessType.EXPORT)
    public void exportExcelFile(HttpServletResponse response, AppNewsQuery query) {
        List<AppNews> list = appNewsService.selectExportExcelData(query);
        ExcelUtil<AppNews> util = new ExcelUtil<>(AppNews.class);
        util.exportExcel(response, list, "新闻信息表");
    }

    /**
     * xxx操作
     */
    @PostMapping(value = "/xxxHandle")
    @MyLog(title = "xxxxxxxxxx", businessType = BusinessType.UPDATE)
    @CheckPermission(per = "xxxxxx:xxxxxx")
    public JSONResult xxxHandle(@RequestBody AppNews appNews) {
        AppNews obj = appNewsService.getById(appNews.getId());
        if (obj == null) {
            throw new MyException(ErrorCode.COMMON_CODE_2001);
        }
        //做其他操作
        appNewsService.updateById(obj);
        return JSONResult.success(true);
    }

    /***********************************************************************************
     以上代码是自动生成的
     自己写的代码放在下面
     ***********************************************************************************/

    /**
     * APP-查询新闻列表数据
     */
    @PostMapping(value = "/getNewsList")
    public JSONResult getNewsList(@RequestBody AppNews appNews) {
        QueryWrapper<AppNews> queryWrapper = new QueryWrapper();
        if (MyTools.hasLength(appNews.getNewType())) {
            queryWrapper.eq("new_type", appNews.getNewType());
        }
        if (MyTools.hasLength(appNews.getNewTitle())) {
            queryWrapper.like("new_title", appNews.getNewTitle());
        }
        queryWrapper.orderByDesc("id");//根据ID降序排序，最新的数据展示在最上面
        List<AppNews> list = appNewsService.list(queryWrapper);
        return JSONResult.success(list);
    }

    /**
     * APP-根据ID查询新闻详情
     */
    @GetMapping(value = "/getNewsById/{id}")
    public JSONResult getNewsById(@PathVariable("id") Long id) {
        if (id == null) {
            throw new MyException("新闻ID不能为空！");
        }
        AppNews one = appNewsService.getById(id);
        if (one == null) {
            throw new MyException("新闻数据不存在！");
        }
        return JSONResult.success(one);
    }

    /**
     * APP-根据ID查询新闻详情-聚合数据
     */
    @GetMapping(value = "/getApiId/{id}")
    public JSONResult getApiId(@PathVariable("id") String id) {
        // 发送http请求的url
        String url = "http://v.juhe.cn/toutiao/content";

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key); // 在个人中心->我的数据,接口名称上方查看
        params.put("uniquekey", id); // 新闻ID


        String paramsStr = urlencode(params);
        System.out.println(paramsStr);
        String response = doGet(url, paramsStr);
        // // post请求
        // String response = doPost(url,paramsStr);

        // 输出请求结果
        System.out.println(response);

        try {
            // 解析请求结果，json：
            JSONObject jsonObject = JSONObject.parseObject(response);
            System.out.println(jsonObject);
            // 具体返回示例值，参考返回参数说明、json返回示例
            if ("0".equals(String.valueOf(jsonObject.get("error_code")))) {
                JSONObject json = (JSONObject)jsonObject.get("result");
                Map<String, Object> map = new HashMap<>();
                map.put("detail", json.get("detail"));
                map.put("content", json.get("content"));
                return JSONResult.success(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONResult.error("调用API接口异常！");
    }

    @Value("${easyjava.key}")
    private String key;

    /**
     * APP-调用聚合数据官网接口获取新闻
     */
    @PostMapping(value = "/getNewsApi")
    public JSONResult getNewsApi(@RequestBody AppNewsQuery query) {
        // 发送http请求的url
        String url = "http://v.juhe.cn/toutiao/index";

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key); // 在个人中心->我的数据,接口名称上方查看
        params.put("type", MyTools.hasLength(query.getNewsType()) ? query.getNewsType() : "top"); // 类型：top(推荐,默认)；更多看请求参数说明

        String paramsStr = urlencode(params);
        System.out.println(paramsStr);
        String response = doGet(url, paramsStr);
        // post请求
        // String response = doPost(url,paramsStr);

        // 输出请求结果
        System.out.println(response);

        try {
            // 解析请求结果，json：
            JSONObject jsonObject = JSONObject.parseObject(response);
            System.out.println("jsonObject" + jsonObject);
            if ("0".equals(String.valueOf(jsonObject.get("error_code")))) {
                JSONObject result = (JSONObject) jsonObject.get("result");
                return JSONResult.success(result.get("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSONResult.error("调用API接口异常！");
    }

    // 将map型转为请求参数型
    public static String urlencode(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * get方式的http请求
     *
     * @param httpUrl  请求地址
     * @param paramStr 请求参数
     * @return 返回结果
     */

    public static String doGet(String httpUrl, String paramStr) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;// 返回结果字符串
        try {
            httpUrl += "?" + paramStr;
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 封装输入流，并指定字符集
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
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
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return result;
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