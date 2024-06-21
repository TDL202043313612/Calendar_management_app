package cn.wujiangbo.utils;

import cn.hutool.crypto.SecureUtil;
import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.UserConstants;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import java.util.Base64;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc 工具类
 */
@Slf4j
public class MyTools {

    public static void main(String[] args) {
        System.out.println(getLoginToken());
    }

    /**
     * word文档转成PDF文档(注意：要使用这个word转pdf的话，运行这个代码的服务器上需要安装WPS或者office软件)
     * @param wordPath word文档路径
     * @param pdfPath pdf文档路径
     */
    public static void word2pdf(String wordPath, String pdfPath){
        File inputWord = new File(wordPath);
        File outputFile = new File(pdfPath);
        InputStream docxInputStream = null;
        OutputStream outputStream = null;
        IConverter converter = null;
        try  {
            docxInputStream = new FileInputStream(inputWord);
            outputStream = new FileOutputStream(outputFile);
            converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("word文档转成PDF文档时，发生异常：{}", e.getLocalizedMessage());
        }
    }

    /**
     * 删除Base64数据的头信息
     * @param str
     * @return
     */
    public static String deleteBase64Prefix(String str){
        if(hasLength(str)){
            if(str.contains(Constants.BASE64_HEAD_PNG)){
                str = str.replace(Constants.BASE64_HEAD_PNG, "");
            }
            else if(str.contains(Constants.BASE64_HEAD)){
                str = str.replace(Constants.BASE64_HEAD, "");
            }
        }
        return str;
    }

    /**
     * 数字转大写
     * @param i
     * @return
     */
    public static String conversion(int i){
        String s = String.valueOf(i);
        StringBuilder sb =new StringBuilder();
        int num = s.length();
        int max=0;
        StringBuilder sb1= new StringBuilder();
        sb1.append(s);
        int x=0;
        //如果不是四的倍数，补足不够四位的位置，用0补
        if (num%4!=0){
            x = 4-num%4;
            for (int m =0;m<x;m++){

                sb1.insert(0,"0");
            }
        }
        int sb1len=sb1.length();
        int b=sb1len/4;
        for (int j = 0; j < sb1len; j++) {
            max++;
            char c = sb1.charAt(j);
            String match = match(c);
            //字符串拼接
//                 sb = new StringBuilder();

            if (x!=0){
                x--;
                continue;
            }
            if (sb.length()>0&&"零".equals(sb.substring(sb.length()-1))&&match.equals("零")){
                continue;
            }
            sb.append(match);
            if (max==4){
                while (sb.length()>2&&"零".equals(sb.substring(sb.length()-1))){
                    sb.deleteCharAt(sb.length()-1);
                }
                if (b==3){
                    sb.append("亿");
                    b--;
                }else if (b==2){
                    sb.append("万");
                    b--;
                }
                max=0;
            }
            if (sb.length()>0&&"零".equals(sb.substring(sb.length()-1))&&match.equals("零")){
                continue;
            }
            if (max==1){
                sb.append("千");
            }else if (max==2){
                sb.append("佰");
            }else if (max==3){
                sb.append("拾");
            }

        }
        while (sb.length()>2&&"零".equals(sb.substring(sb.length()-1))){
            sb.deleteCharAt(sb.length()-1);
        }
        String sbto = sb.toString();

        return sbto;
    }
    //字符转换
    public static String match(char c){
        switch (c){
            case '9':
                return "九";
            case '8':
                return "八";
            case '7':
                return "七";
            case '6':
                return "六";
            case '5':
                return "五";
            case '4':
                return "四";
            case '3':
                return "三";
            case '2':
                return "二";
            case '1':
                return "一";
            case '0':
                return "零";
            default:
                break;
        }
        return null;
    }

    /**
     * 获得指定图片文件的base64编码数据
     * @param filePath 文件路径
     * @return base64编码数据
     */
    public static String getBase64ByPath(String filePath) {
        if(!hasLength(filePath)){
            return "";
        }
        File file = new File(filePath);
        if(!file.exists()) {
            return "";
        }
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            assert in != null;
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * @desc 获取客户端请求相关参数信息
     */
    public static Map<String, String> getClientInfo() {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        Map map = new HashMap();
        map.put("os", os);
        map.put("browser", browser);
        return map;
    }

    /**
     * @desc 获取token值
     * @author wujiangbo
     * @date 2022/6/5 16:27
     */
    public static String getLoginToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @desc 判断字符串是否有长度
     * @author wujiangbo
     */
    public static boolean hasLength(String str) {
        if("undefined".equals(str)){
            return false;
        }
        return org.springframework.util.StringUtils.hasLength(str);
    }

    /**
     * @desc 判断集合是否为空
     * @author wujiangbo
     */
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * @desc 传入参数，获取OSS中文件路径
     * @author wujiangbo
     * @date 2022/6/8 13:19
     * @param ossFullPath 如：https://easyjava.oss-cn-chengdu.aliyuncs.com/userImage/commonUserPhoto.png
     * @param folderName 如：userImage
     * @return java.lang.String
     */
    public static String getOssFileName(String ossFullPath, String folderName){
        return ossFullPath.substring(ossFullPath.lastIndexOf(folderName));
    }

    /**
     * 判断是否是超级管理员角色
     */
    public static boolean isAdminRole(Long id){
        return id.longValue() == UserConstants.SUPER_ADMIN_ROLE_ID.longValue();
    }

    /**
     * 判断是否是超级管理员用户
     */
    public static boolean isAdminUser(Long id){
        return id.longValue() == UserConstants.SUPER_ADMIN_USER_ID.longValue();
    }

    /**
     * 中文转拼音
     */
    public static String getPingYin(String src)
    {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = " ";
        int t0 = t1.length;
        try
        {
            for (int i = 0; i < t0; i++)
            {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else
                {
                    t4 += Character.toString(t1[i]);
                }
            }
            return t4.trim();
        } catch (BadHanyuPinyinOutputFormatCombination e1)
        {
            log.error("汉字转拼音，出现异常：{}", e1);
            e1.printStackTrace();
            return getRandomNum(8);
        }
    }

    /**
     * 获取登录密码的密文
     */
    public static String getPwd(String password)
    {
        return SecureUtil.md5(password);
    }

    /**
     * @Description: 将本地图片转成Base64数据
     */
    public static String ImageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            log.error("图片转Base64数据时发生异常:{}", e.getLocalizedMessage());
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        // 返回Base64编码过的字节数组字符串
        return Constants.BASE64_HEAD + encoder.encode(Objects.requireNonNull(data));
    }

    /*
     * @Description: 验证密码，必须是6-18位长度，必须由数字和字母组成，并且要同时含有数字和字母，且长度要在6-18位之间
     */
    public static boolean checkPassword(String str){
        boolean checkResult = false;
        if(!StringUtils.isBlank(str)){
            String regEx = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            if(m.matches()){
                checkResult = true;
            }
        }
        return checkResult;
    }

    /*
     * @Description: 验证字母和数字，必须是6-18位长度
     */
    public static boolean checkNumChar(String str){
        boolean checkResult = false;
        if(!StringUtils.isBlank(str)){
            String regEx = "^[0-9A-Za-z]{6,18}$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            if(m.matches()){
                checkResult = true;
            }
        }
        return checkResult;
    }

    //获取随机的标签类型值
    public static String getRandomTagType(){
        List<String> tagList = new ArrayList<>();
        tagList.add("");
        tagList.add("success");
        tagList.add("info");
        tagList.add("danger");
        tagList.add("warning");
        return tagList.get(Integer.valueOf(getRandomDotString(0, 4)));
    }

    /**
     * 校验手机号格式-严格版
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone){
        boolean checkResult = false;
        if(!StringUtils.isBlank(phone)){
            if(phone.length() == 11){
                String regEx = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0, 2, 5-9]))\\d{8}$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(phone);
                if(m.matches()){
                    checkResult = true;
                }
            }
        }
        return checkResult;
    }

    /*
     * @Description: 验证邮箱格式
     */
    public static boolean checkEmail(String email){
        boolean checkResult = false;
        if(!StringUtils.isBlank(email)){
            String regEx = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(email);
            if(m.matches()){
                checkResult = true;
            }
        }
        return checkResult;
    }

    /**
     * @Description: 获取指定位数的随机整数
     */
    public synchronized static String getRandomNum(int num) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= num; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

    /**
     * @Description: 获取客户端编号(当前时间串+6位随机数字)
     */
    public synchronized static String getClientNo() {
        return getCurrentTimeStr() + getRandomNum(6);
    }

    /**
     * @Description: 获取老师编号(当前时间串+6位随机数字)
     */
    public synchronized static String getTeacherNo() {
        return getCurrentTimeStr() + getRandomNum(6);
    }

    /**
     * @Description: 获取流程编号(当前时间串+6位随机数字)
     */
    public synchronized static String getFlowNo() {
        return getCurrentTimeStr() + getRandomNum(6);
    }

    /**
     * @Description: 获取班级编号(当前时间串+6位随机数字)
     */
    public synchronized static String getClassNo(LocalDateTime beginDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = beginDate.format(fmt);
        return getRandomNum(6) + dateStr;
    }

    /**
     * @Description: 获取问卷编号(当前时间串+6位随机数字)
     */
    public synchronized static String getQuestionnaireNo() {
        return getCurrentTimeStr() + getRandomNum(6);
    }

    /**
     * @Description: 获取【快乐瞬间表】的资源编号
     * 返回长度：20
     */
    public synchronized static String getYmHappyBusiNo() {
        return getCurrentTimeStr() + getRandomNum(6);
    }

    /**
     * @Description: 获取随机用户昵称
     * 返回长度：20
     */
    public synchronized static String getNickName() {
        return getRandomChar(8);
    }

    /**
     * @Description: 获取订单编号
     * 返回长度：20
     */
    public synchronized static String getOrderNo() {
        return getCurrentTimeStr() + getRandomNum(6);
    }

    /**
     * @description: 获取指定串中随机指定位数的字符串
     */
    public synchronized static String getRandomChar(int num) {
        //先定义取值范围
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < num; i++) {
            value.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        return value.toString();
    }

    /*
     * @Description: 获取当前时间字符串(yyyyMMdd)，14位长度
     */
    public synchronized static String getCurrentDateStr() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    /*
     * @Description: 获取当前时间字符串(yyyyMMddHHmmss)，14位长度
     */
    public synchronized static String getCurrentTimeStr() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    /*
     * @Description: 根据MultipartFile文件获取图片Base64数据
     */
    public static String MutipartFileToBase64(MultipartFile file) {
        String base64EncoderImg = "";
        try {
            Base64.Encoder base64Encoder = Base64.getEncoder();
            base64EncoderImg = Constants.BASE64_HEAD + base64Encoder.encode(file.getBytes());
        } catch (Exception e) {
            log.error("根据MultipartFile文件获取Base64数据，异常：{}", e);
        }
        return base64EncoderImg;
    }

    /*
     * @Description: 将图片Base64数据(包含头信息)字符串转成MultipartFile文件类型
     */
    public static MultipartFile base64ToMutipartFile(String imgStr) {
        try {
            // Base64数据头：data:image/png;base64,
            String[] baseStr = imgStr.split(",");
            Base64.Decoder base64Decoder = Base64.getDecoder();
            byte[] b = new byte[0];
            b = base64Decoder.decode(baseStr[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new BASE64DecodedMultipartFile(b, baseStr[0]);
        } catch (Exception e) {
            log.error("图片Base64串转MultipartFile发生异常:{}", e);
            return null;
        }
    }

    //随机获取指定区间的整数
    public synchronized static String getRandomDotString(int min, int max) {
        Random rand = new Random();
        StringBuffer result = new StringBuffer();
        result.append(rand.nextInt(max - min + 1) + min);
        return result.toString();
    }

    /**
     * 获取班级名称
     * @param xuekeId 学科类型
     * @param kaibanDate 开班日期
     * @return String
     */
    public static String getBanjiName(String xuekeId, LocalDateTime kaibanDate) {
        String banjiName = "";
        String xuekeName = "";
        String date = kaibanDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        switch (xuekeId){
            case "1":
                xuekeName = "PRODUCT";//产品经理学科
                break;
            case "2":
                xuekeName = "TEST";//测试学科
                break;
            case "3":
                xuekeName = "H5";//前端学科
                break;
            case "4":
                xuekeName = "JAVA";//Java学科
                break;
        }
        banjiName = xuekeName + date;
        return banjiName;
    }

}
