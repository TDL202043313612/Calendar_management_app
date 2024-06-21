package cn.wujiangbo.controller.system;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.controller.base.BaseController;
import cn.wujiangbo.dto.oss.OssDto;
import cn.wujiangbo.dto.system.DownloadDto;
import cn.wujiangbo.dto.system.SystemConfigDto;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.service.system.impl.FileService;
import cn.wujiangbo.utils.StringUtils;
import cn.wujiangbo.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

;

/**
 * 处理文件相关请求
 *
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;

    @Autowired
    private OssDto ossDto;

    /**
     * 处理文件下载
     * 张总专用
     */
    @PostMapping("/download")
    public void downloadFile(DownloadDto dto, HttpServletResponse response) throws Exception {
        String filePath = ossDto.getDiskSavePath() + "/" + dto.getFilaName();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, dto.getFilaName());
        FileUtils.writeBytes(filePath, response.getOutputStream());
    }

    /**
     * 富文本编辑框上传图片接口
     */
    @PostMapping("/uploadQuillImage")
    public JSONResult uploadQuillImage(@RequestParam("fileName") MultipartFile multipartFile) {
        return fileService.uploadFile(multipartFile, Constants.OSS.QUILL_IMAGE);
    }

    /**
     * @desc 上传图片到OSS，返回路径给前端
     */
    @PostMapping("/uploadImageCommon")
    public JSONResult uploadTanhuapingju(@RequestParam("fileName") MultipartFile multipartFile) {
        return fileService.uploadFile(multipartFile, Constants.OSS.RESOURCE_LINK_IMG);
    }

    /**
     * 下载需要导出的Excel方法
     */
    @GetMapping("/downloadExcel")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new MyException(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = SystemConfigDto.getExportExcelPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败：", e);
        }
    }
}