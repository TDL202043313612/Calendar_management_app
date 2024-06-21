package cn.wujiangbo.service.system.impl;

import cn.wujiangbo.constants.Constants;
import cn.wujiangbo.domain.system.EasyImgResource;
import cn.wujiangbo.mapper.system.EasyImgResourceMapper;
import cn.wujiangbo.query.system.EasyImgResourceQuery;
import cn.wujiangbo.service.system.EasyImgResourceService;
import cn.wujiangbo.tools.CommonTools;
import cn.wujiangbo.utils.MyTools;
import cn.wujiangbo.utils.PageTools;
import cn.wujiangbo.utils.file.FileUploadUtils;
import cn.wujiangbo.vo.system.UploadFileVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 图片资源信息表 服务实现类
 * </p>
 *
 */
@Service
@Slf4j
public class EasyImgResourceServiceImpl extends ServiceImpl<EasyImgResourceMapper, EasyImgResource> implements EasyImgResourceService {

    @Autowired
    private EasyImgResourceMapper easyImgResourceMapper;

    @Autowired
    public FileUploadUtils fileUploadUtils;

    @Autowired
    public CommonTools commonTools;

    //查询列表
    @Override
    public Page<EasyImgResource> selectMyPage(EasyImgResourceQuery query) {
        Page page = PageTools.getInstance().getPage(query);
        List<EasyImgResource> list = easyImgResourceMapper.selectMyPage(page, query);
        return page.setRecords(list);
    }

    //新增多个资源到数据库
    @Override
    public String insertMultiResource(List<MultipartFile> files, Long userId, String funcFlag) {
        String busiNo = MyTools.getYmHappyBusiNo();
        EasyImgResource easyImgResource = null;
        for(int i=0; i<files.size(); i++){
            MultipartFile multipartFile = files.get(i);
            UploadFileVo vo = fileUploadUtils.fileUploadOss(Constants.OSS.DEFAULT_NAME, multipartFile);
            easyImgResource.setResPath(vo.getFileName());
            commonTools.saveRes(multipartFile, vo.getFileName(), userId, busiNo, funcFlag);
        }
        return busiNo;
    }

    @Override
    public void deleteById(Long id, String tableName, String folderName) {
        //1、先删除OSS中的资源
        List<EasyImgResource> easyImgResourceList = easyImgResourceMapper.selectById(id, tableName);
        for(int i=0; i<easyImgResourceList.size(); i++){
            EasyImgResource easyImgResource = easyImgResourceList.get(i);
            fileUploadUtils.removeOssFile(MyTools.getOssFileName(easyImgResource.getResPath(), folderName));
            //2、删除数据库中的资源
            easyImgResourceMapper.deleteById(easyImgResource.getId());
        }
    }
}