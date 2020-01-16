package com.kiegame.mobile.repository.interceptor;

import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.utils.Prefer;
import com.kiegame.mobile.utils.Text;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by: var_rain.
 * Created date: 2020/1/16.
 * Description: Token拦截器
 */
public class TokenInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String token = Prefer.get(Setting.APP_NETWORK_TOKEN, "");
        if (Text.empty(token)) {
            return chain.proceed(chain.request());
        } else {
            Request request = chain.request().newBuilder().addHeader("loginToken", token).build();
            return chain.proceed(request);
        }
    }
}
