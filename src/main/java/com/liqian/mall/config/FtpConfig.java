package com.liqian.mall.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: Administrator
 * @Date: 2019/8/28 0028 16:44
 * @Description:
 */
@Component
@Getter
@Setter
public class FtpConfig {

    @Value("${ftp.ip}")
    private String ip;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.imageHost}")
    private String imageHost;
}
