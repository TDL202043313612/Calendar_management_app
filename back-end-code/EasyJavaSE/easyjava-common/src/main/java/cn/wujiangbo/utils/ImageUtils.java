package cn.wujiangbo.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>图像工具类</p>
 *
 */
@Slf4j
public class ImageUtils {

    //定义图片水印字体类型
    public static final String FONT_NAME = "微软雅黑";

    //定义图片水印字体加粗、变细、倾斜等样式
    public static final int FONT_STYLE = Font.BOLD;

    //设置文字透明程度（0.1-1）
    public static float ALPHA = 1F;

    public static void main(String[] args) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("className", "0630测试班");//班级
        map.put("studentName", "王天霸");//姓名
        map.put("city", "武汉");//城市
        map.put("salary", "9000");//薪资

        File sourceFile = new File("D:\\ITSource\\git\\EasyJava\\EasyJava6.0\\easyjava-admin\\src\\main\\resources\\static\\goodnews_001.jpg");

        ImageUtils.markImageByMoreText(
                map,
                sourceFile,
                "D:\\ITSource\\git",
                map.get("studentName"),
                "jpg",
                Color.RED,
                null
        );
    }
    /**
     * 生成就业喜报图片
     * map 水印内容
     * source 需要添加水印的图片路径
     * outPut 添加水印后图片输出路径
     * imageName 图片名称
     * imageType 图片类型
     * color 水印文字的颜色
     * degree 水印文字旋转角度，为null表示不旋转
     */
    public static Boolean markImageByMoreText(Map<String, String> map, File sourceFile, String outputPath, String imageName, String imageType, Color color, Integer degree) {
        try {
            File f = new File(outputPath);
            if(!f.exists()){
                f.mkdirs();
            }
            if (!sourceFile.isFile()) {
                return false;
            }
            //获取源图像的宽度、高度
            Image image = ImageIO.read(sourceFile);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //创建绘图工具对象
            Graphics2D graphics2D = bufferedImage.createGraphics();
            //其中的0代表和原图位置一样
            graphics2D.drawImage(image, 0, 0, width, height, null);
            //设置水印颜色
            graphics2D.setColor(color);
            //设置水印透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
            //设置水印旋转
            if (null != degree) {
                graphics2D.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2, (double) bufferedImage.getHeight() / 2);
            }
            String stuName = map.get("studentName");
            String className = map.get("className");
            String city = map.get("city");
            String salary = map.get("salary");
            String content = className + stuName;
            //设置水印文字（设置水印字体样式、粗细、大小）
            graphics2D.setFont(new Font(FONT_NAME, FONT_STYLE, 160));
            if(stuName.length() == 3){
                graphics2D.drawString(content, 1100, 2800);
            }else{
                graphics2D.drawString(content, 1200, 2800);
            }

            //就业信息
            graphics2D.setFont(new Font(FONT_NAME, FONT_STYLE, 300));
            graphics2D.drawString(salary, 1600, 3270);

            //就业城市
            graphics2D.setFont(new Font(FONT_NAME, FONT_STYLE, 300));
            graphics2D.drawString(city, 1600, 3620);

            graphics2D.dispose();

            //输出图片
            File sf = new File(outputPath, imageName + "." + imageType);
            // 保存图片
            ImageIO.write(bufferedImage, imageType, sf);
            log.info("就业喜报生成完成，路径：{}，班级：{}，学生：{}，就业城市：{}，薪资：{}", outputPath, className, stuName, city, salary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}