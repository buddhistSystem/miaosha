package com.liqian.mall.util;


import java.util.UUID;

public class UUIdUtil {

    public synchronized static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        str = str.replace("-", "");
        return str;
    }

}
