package cn.wujiangbo.vo.system;

import lombok.Data;

/**
 * @description: 文件上传成功后返回前端参数
 *
 */
@Data
public class UploadFileVo {

    private String fileName;//文件存储路径
    private String filePrefix;//文件存储路径的前缀
}
