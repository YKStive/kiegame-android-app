package com.kiegame.mobile.repository.interceptor;

import com.kiegame.mobile.repository.entity.submit.Submit;
import com.kiegame.mobile.utils.AES;
import com.kiegame.mobile.utils.JSON;

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
            buffer.close();
            if (encrypt != null) {
                Submit submit = new Submit();
                submit.setParamAes(encrypt);
                return chain.proceed(request.newBuilder().post(RequestBody.create(JSON.toJson(submit), body.contentType())).build());
            }
        }
        return chain.proceed(request);
    }
}
