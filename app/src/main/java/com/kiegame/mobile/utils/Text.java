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

    /**
     * 格式化身份证号码显示
     *
     * @param idCardNum 身份证号码
     * @return 返回格式化后的字符串
     */
    public static String formatIdCardNum(String idCardNum) {
        if (Text.empty(idCardNum) || idCardNum.length() <= 7) {
            return idCardNum;
        }
        String start = idCardNum.substring(0, 3);
        String end = idCardNum.substring(idCardNum.length() - 4);
        return start + "****" + end;
    }

    /**
     * 获取身份证号码的后四位
     *
     * @param idNum 身份证号码
     * @return 返回后四位
     */
    public static String lastIdNum(String idNum) {
        if (!Text.empty(idNum) && idNum.length() >= 4) {
            return idNum.substring(idNum.length() - 4);
        } else {
            return idNum;
        }
    }

    /**
     * 字符串长度大于30则自动缩减
     *
     * @param info 会员信息
     * @return 缩减后的会员信息或原信息
     */
    public static String zoomCustomInfo(String info) {
        if (!Text.empty(info)) {
            if (info.length() > 30) {
                String start = info.substring(0, info.indexOf('*'));
                String end = info.substring(info.lastIndexOf('*'));
                return String.format("%s****%s", start, end);
            }
        }
        return info;
    }

    /**
     * 格式化会员名称
     *
     * @param customName 会员名
     * @return 返回姓氏缩写
     */
    public static String formatCustomName(String customName) {
//        if (Text.empty(customName) || customName.length() <= 1) {
//            return customName;
//        }
//        StringBuilder sb = new StringBuilder(customName.substring(0, 1));
//        for (int i = 0; i < customName.length() - 1; i++) {
//            sb.append("某");
//        }
//        return sb.toString();
        return customName;
    }
}