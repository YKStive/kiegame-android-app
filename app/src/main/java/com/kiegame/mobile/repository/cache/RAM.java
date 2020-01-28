package com.kiegame.mobile.repository.cache;

import com.kiegame.mobile.repository.entity.receive.LoginEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.PreferPlus;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 内存缓存
 */
public class RAM {

    // 用户登录信息
    private static LoginEntity login;
    // token
    private static String token;

    public static String getToken() {
        if (RAM.token == null) {
            RAM.token = Prefer.get(Setting.APP_NETWORK_TOKEN, "");
        }
        return token;
    }

    public static void setToken(String token) {
        RAM.token = token;
        Prefer.put(Setting.APP_NETWORK_TOKEN, token);
    }

    public static LoginEntity getLoginInfo() {
        if (RAM.login == null) {
            RAM.login = PreferPlus.get(Setting.USER_LOGIN_OBJECT, LoginEntity.class);
        }
        return login;
    }

    public static void setLoginInfo(LoginEntity login) {
        RAM.login = login;
        PreferPlus.put(Setting.USER_LOGIN_OBJECT, login);
    }
}
