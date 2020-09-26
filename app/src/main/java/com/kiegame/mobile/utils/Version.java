package com.kiegame.mobile.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.kiegame.mobile.Game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by: var_rain.
 * Created date: 2019/2/24.
 * Description: 版本相关工具类
 */
public class Version {

    /**
     * 获取应用程序版本号
     *
     * @return 返回应用程序的当前版本号字符串
     */
    public static String appVersionName() {
        String version = null;
        try {
            PackageManager pm = Game.ins().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(Game.ins().getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 格式化版本号
     *
     * @param version 版本号
     * @return 返回版本号 x.y.z
     */
    private static String getVersion(String version) {
        Pattern compile = Pattern.compile("[^0-9.]");
        Matcher matcher = compile.matcher(version);
        return matcher.replaceAll("");
    }

    /**
     * 是否需要升级
     *
     * @param targetVersion 目标版本号
     * @return true:需要 false:不需要
     */
    public static boolean needUpdate(String targetVersion) {
        String locate = getVersion(appVersionName());
        String target = getVersion(targetVersion);
        String[] lv = locate.split("\\.");
        String[] tv = target.split("\\.");
        int min = Math.min(lv.length, tv.length);
        for (int i = 0; i < min; i++) {
            if (Integer.parseInt(lv[i]) > Integer.parseInt(tv[i])) {
                return false;
            }
            if (Integer.parseInt(tv[i]) > Integer.parseInt(lv[i])) {
                return true;
            }
        }
        if (lv.length != tv.length) {
            return lv.length < tv.length;
        }
        return false;
    }
}
