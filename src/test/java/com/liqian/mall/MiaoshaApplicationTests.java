package com.liqian.mall;

import com.liqian.mall.config.FtpConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiaoshaApplicationTests {

    @Resource
    private FtpConfig ftpConfig;

    @Test
    public void test() {
        System.out.println(ftpConfig.getIp()+"**********************");
    }

}
