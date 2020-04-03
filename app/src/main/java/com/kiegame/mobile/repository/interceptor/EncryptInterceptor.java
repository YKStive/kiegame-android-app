package com.kiegame.mobile.repository.interceptor;

import com.kiegame.mobile.utils.AES;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by: var.
 * Created date: 2020/4/3.
 * Description: 加密拦截器
 */
public class EncryptInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody body = request.body();
        if (body != null && body.contentLength() != 0) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            String encrypt = AES.encrypt(buffer.readString(StandardCharsets.UTF_8));
            if (encrypt != null) {
                return chain.proceed(request.newBuilder().post(RequestBody.create(encrypt, body.contentType())).build());
            }
        }
        return chain.proceed(request);
    }
}
