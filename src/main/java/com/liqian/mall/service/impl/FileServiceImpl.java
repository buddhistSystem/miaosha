package com.liqian.mall.service.impl;

import com.liqian.mall.config.FtpConfig;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IFileService;
import com.liqian.mall.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 上传文件到ftp服务器
 *
 * @author liqian
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    @Resource
    private FtpConfig ftpConfig;

    @Override
    public String upload(String path, MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new BusinessException(EmError.UPLOAD_FILE_NOT_NULL);
        }
        String fileName = multipartFile.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("[开始上传文件,上传文件名:{},上传路径:{},新文件名:{}]", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            //上传成功
            multipartFile.transferTo(targetFile);
            List<File> fileList = new ArrayList<>();
            fileList.add(targetFile);
            //将targetFile上传到ftp，上传成成功后删除Tomcat中的文件
            FtpUtil.uploadFile(fileList, ftpConfig);
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常！", e);
        }
        return targetFile.getName();
    }
}
