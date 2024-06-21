package cn.wujiangbo.utils.file;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.constants.ErrorCode;
import cn.wujiangbo.dto.oss.OssDto;
import cn.wujiangbo.dto.system.SystemConfigDto;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.uuid.IdUtils;
import cn.wujiangbo.vo.system.UploadFileVo;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 上传文件的工具类
 *
 */
@Component
@Slf4j
public class FileUploadUtils {

    @Autowired
    private OssDto ossPropertites;

    /**
     * 上传文件到阿里OSS服务器
     * @param multipartFile
     * @return
     */
    public UploadFileVo fileUploadOss(String folderName, MultipartFile multipartFile){
        if(!ossPropertites.getEnable()){
            throw new MyException(ErrorCode.OSS_ENABLE_FASLE);
        }
        //获取文件名
        String fileName = extractFilename(multipartFile);
        fileName = folderName + "/" + fileName;
        OSS build = new OSSClientBuilder().build(ossPropertites.getEndpoint(), ossPropertites.getAccessKey(), ossPropertites.getSecretKey());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossPropertites.getBucketName(), fileName, multipartFile.getInputStream());
            //将文件推到OSS服务器
            build.putObject(putObjectRequest);
            //String imagePath = "https://" + ossPropertites.getBucketName() + "." + ossPropertites.getEndpoint() + "/" + fileName;
            UploadFileVo vo = new UploadFileVo();
            vo.setFileName(fileName);
            vo.setFilePrefix("https://" + ossPropertites.getBucketName() + "." + ossPropertites.getEndpoint() + "/");
            return vo;
        }catch (Exception e){
            log.error("上传文件到阿里OSS服务器发生异常：{}", e);
            throw new MyException(ErrorCode.ERROR_CODE_1039);
        }finally {
            build.shutdown();//关闭OSSClient
        }
    }

    public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = SystemConfigDto.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * 从阿里OSS服务器删除文件
     */
    public boolean removeOssFile(String fileName) {
        if(StringUtils.isNotBlank(fileName)){
            try{
                OSS build = new OSSClientBuilder().build(ossPropertites.getEndpoint(), ossPropertites.getAccessKey(), ossPropertites.getSecretKey());
                build.deleteObject(ossPropertites.getBucketName(), fileName);
                build.shutdown();
            }catch (Exception e){
                log.error("从阿里OSS服务器删除文件发生异常：{}", e);
                return false;
            }
        }
        return true;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.dateTime() + "_" + IdUtils.fastUUID().replace("-","") + "." + extension;
        return fileName;
    }

    /**
     * 获取文件名的后缀
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}