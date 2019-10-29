package com.liqian.mall.util;


import com.liqian.mall.config.FtpConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 上传文件到ftp服务器工具类
 *
 * @author liqian
 */
@Slf4j
public class FtpUtil {

    private static final String FTP_REMOTE_PATH = "img";

    private FTPClient ftpClient;


    public static boolean uploadFile(List<File> fileList, FtpConfig ftpConfig) throws IOException {
        FtpUtil ftpUtil = new FtpUtil();
        log.info("开始连接ftp服务器");
        boolean result = ftpUtil.upload(fileList, ftpConfig);
        log.info("结束上传");
        return result;
    }


    private boolean upload(List<File> fileList, FtpConfig ftpConfig) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectFtpServer(ftpConfig)) {
            try {
                //设置远程地址
                ftpClient.changeWorkingDirectory(FTP_REMOTE_PATH);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //打开本地被动模式
                ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);
                }

            } catch (IOException e) {
                uploaded = false;
                log.error("上传文件异常", e);
            } finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    private boolean connectFtpServer(FtpConfig ftpConfig) {
        boolean flag = false;
        ftpClient = new FTPClient();
        try {
            log.info(ftpConfig.getIp() + ftpConfig.getUsername() + ftpConfig.getPassword());
            ftpClient.connect(ftpConfig.getIp());
            flag = ftpClient.login(ftpConfig.getUsername(), ftpConfig.getPassword());
        } catch (IOException e) {
            log.error("连接ftp异常", e);
            e.printStackTrace();
        }
        return flag;
    }


}
