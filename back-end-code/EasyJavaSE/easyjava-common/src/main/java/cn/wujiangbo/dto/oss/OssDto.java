package cn.wujiangbo.dto.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 */
@Data
@Component
@ConfigurationProperties(prefix="file.alicloud")
public class OssDto {

    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private Boolean enable;
    private String diskSavePath; //保存本地磁盘路径
}