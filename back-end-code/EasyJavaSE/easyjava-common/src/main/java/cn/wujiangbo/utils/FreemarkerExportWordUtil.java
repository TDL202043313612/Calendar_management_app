package cn.wujiangbo.utils;

import cn.wujiangbo.utils.file.FileUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * 模板word导出：简化导出代码
 *
 */
public class FreemarkerExportWordUtil {

    private static Configuration configuration = null;

    static {
        configuration = new Configuration(new Version("2.3.0"));
        configuration.setDefaultEncoding("utf-8");
        //获取模板路径    setClassForTemplateLoading 这个方法默认路径是webRoot 路径下
        configuration.setClassForTemplateLoading(FreemarkerExportWordUtil.class, "/templates");
    }

    private FreemarkerExportWordUtil() {
        throw new AssertionError();
    }

    /**
     * 根据 /resources/templates 目录下的ftl模板文件生成文件并写到客户端进行下载
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param map 数据集合
     * @param fileName 用户下载到的文件名称
     * @param ftlFileName ftl模板文件名称
     * @throws IOException
     */
    public static void exportMillCertificateWord(HttpServletRequest request, HttpServletResponse response, Map map, String fileName, String ftlFileName) throws IOException {
        Template freemarkerTemplate = configuration.getTemplate(ftlFileName);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, fileName);
        // 调用工具类的createDoc方法生成Word文档
        File file = createDoc(map, freemarkerTemplate);
        //将word文档转成pdf
        File pdfFile = new File("pdf_temp.pdf");
        MyTools.word2pdf(file.getAbsolutePath(), pdfFile.getAbsolutePath());
        //将word文档写到前端
        FileUtils.download(pdfFile.getAbsolutePath(), response);
    }

    private static File createDoc(Map<?, ?> dataMap, Template template) {
        //名字临时变量而已
        String name = "template.doc";
        File f = new File(name);
        Template t = template;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }
}