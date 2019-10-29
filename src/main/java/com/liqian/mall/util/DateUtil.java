package com.liqian.mall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author liqian
 */
public class DateUtil {


    private static ThreadLocal<Map<String, SimpleDateFormat>> threadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap<String, SimpleDateFormat>(8);
        }
    };

    /**
     * 生成对应格式的SimpleDateFormat对象
     *
     * @param pattern 解析格式
     * @return sdf SimpleDateFormat对象
     */
    private static SimpleDateFormat getSimpleDateFormat(final String pattern) {
        Map<String, SimpleDateFormat> tl = threadLocal.get();
        SimpleDateFormat sdf = tl.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            tl.put(pattern, sdf);
        }
        return sdf;
    }

    /**
     * 生成对应格式的SimpleDateFormat对象
     *
     * @param pattern 解析格式
     * @return sdf SimpleDateFormat对象
     */
    public static SimpleDateFormat getDateFormat(String pattern) {
        return getSimpleDateFormat(pattern);
    }

    /**
     * 简单日期正则表达式
     */
    private static final String REG = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";


    /**
     * 判断字符串日期格式，符合返回Date，不符合返回null
     *
     * @param stringDate 字符串日期
     * @param pattern 解析格式
     * @return Date
     * @throws ParseException
     */
    public static Date parseStringDate(String stringDate, String pattern) throws ParseException {
        Pattern regPattern = Pattern.compile(REG);
        Matcher matcher = regPattern.matcher(stringDate);
        boolean flag = matcher.matches();
        if (flag) {
            SimpleDateFormat sdf = getDateFormat(pattern);
            return sdf.parse(stringDate);
        }
        return null;
    }

}
