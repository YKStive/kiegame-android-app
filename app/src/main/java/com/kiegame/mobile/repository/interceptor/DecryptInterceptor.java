package com.kiegame.mobile.repository.interceptor;

import com.kiegame.mobile.utils.AES;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by: var.
 * Created date: 2020/4/3.
 * Description: 解密拦截器
 */
public class DecryptInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        if (body != null && body.contentLength() != 0) {
            String content = body.string();
            String decrypt;
            if (content.contains("{") && content.contains("}") && content.contains(":")) {
                decrypt = content;
            } else {
                decrypt = AES.decrypt(content);
            }
            if (decrypt != null) {
                return response.newBuilder().body(ResponseBody.create(decrypt, body.contentType())).build();
            }
        }
        return response;
    }
}
