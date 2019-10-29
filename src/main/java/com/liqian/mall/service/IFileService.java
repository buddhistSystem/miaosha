package com.liqian.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Administrator
 * @Date: 2019/8/22 0022 15:27
 * @Description:
 */
public interface IFileService {

    String upload(String path, MultipartFile multipartFile);
}
