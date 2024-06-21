package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.dto.oss.OssDto;
import cn.wujiangbo.exception.MyException;
import cn.wujiangbo.result.JSONResult;
import cn.wujiangbo.utils.DateUtils;
import cn.wujiangbo.utils.file.FileUploadUtils;
import cn.wujiangbo.utils.uuid.UUID;
import cn.wujiangbo.vo.system.UploadFileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 */
@Service
@Slf4j
public class FileService {

    @Autowired
    private FileUploadUtils fileUploadUtils;

    @Resource
    private OssDto ossDto;

    public JSONResult uploadFile(MultipartFile multipartFile, String folderName) {
        UploadFileVo vo = fileUploadUtils.fileUploadOss(folderName, multipartFile);
        return JSONResult.success(vo);
    }

    //上传文件到本地磁盘
    public String uploadFileToDisk(MultipartFile file) {
        //获取本地磁盘保存目录
        String diskSavePath = ossDto.getDiskSavePath();
        String date = DateUtils.getYear() + File.separator + DateUtils.getMonth();
        //路径加上年份和月份
        diskSavePath = diskSavePath + File.separator + date;
        //如果目录不存在，自动创建文件夹
        File dir = new File(diskSavePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //获取文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件名
        String fileName = UUID.randomUUID() + suffix;
        //需要保存的文件对象
        File saveFile = new File(diskSavePath + File.separator + fileName);
        try {
            //将上传的文件写入到服务器磁盘
            file.transferTo(saveFile);
        } catch (IOException e) {
            log.error("文件拷贝异常：{}", e.getMessage());
            e.printStackTrace();
            throw new MyException("文件拷贝异常:" + e.getMessage());
        }
        return date + "/" + fileName;
    }
}
