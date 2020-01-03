package com.kiegame.mobile.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 文本工具类
 */
public class Text {

    /**
     * 判断字符串是否为空
     *
     * @param text 字符串
     * @return 为空则返回true, 不为空则返回false
     */
    public static boolean empty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * 去除字符串中的空格,回车,换行符,制表符等
     *
     * @param text 源字符串
     * @return 格式化后的字符串
     */
    public static String clean(String text) {
        String dest = "";
        if (text != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(text);
            dest = m.replaceAll("");
        }
        return dest;
    }
}