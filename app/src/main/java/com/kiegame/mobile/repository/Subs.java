package com.kiegame.mobile.repository;

import com.google.gson.JsonParseException;
import com.kiegame.mobile.R;
import com.kiegame.mobile.repository.entity.result.Result;
import com.kiegame.mobile.utils.Loading;
import com.kiegame.mobile.utils.Toast;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by: var_rain.
 * Created date: 2018/10/21.
 * Description: 观察着对象封装
 */
public abstract class Subs<T> implements Observer<Result<T>> {

    // 是否显示对话框,提示信息
    private boolean isShowMsg;
    // 是否支付接口调用
    private boolean isPay;

    /**
     * 构造方法,默认显示对话框,提示信息
     */
    protected Subs() {
        this.isShowMsg = true;
        this.isPay = false;
    }

    /**
     * 构造方法
     *
     * @param isShowMsg 是否显示提示消息,加载框等
     */
    protected Subs(boolean isShowMsg) {
        this.isShowMsg = isShowMsg;
        this.isPay = false;
    }

    /**
     * 构造方法
     *
     * @param isShowMsg 是否显加载框
     * @param isPay     是否支付接口
     */
    public Subs(boolean isShowMsg, boolean isPay) {
        this.isShowMsg = isShowMsg;
        this.isPay = isPay;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!d.isDisposed() && this.isShowMsg) {
            Loading.show();
        }
    }

    @Override
    public void onNext(Result<T> data) {
        if (this.isShowMsg) {
            Loading.hide();
        }
        if (data.getCode() == 5001 && !data.isSuccess() && isPay) {
            onPayFailure(data.getMessage());
            return;
        }
        if (data.getCode() == 200 && data.isSuccess()) {
            onSuccess(data.getData(), data.getTotal(), data.getLength());
        } else {
            if (this.isShowMsg) {
                Toast.show(data.getMessage());
            }
            onFail();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (this.isShowMsg) {
            Loading.hide();
            if (e instanceof HttpException) {
                Toast.show(R.string.toast_error_http);
            } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                Toast.show(R.string.toast_error_connect);
            } else if (e instanceof InterruptedIOException) {
                Toast.show(R.string.toast_error_time_out);
            } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
                Toast.show(R.string.toast_error_data);
            } else {
                Toast.show(R.string.toast_error_unknown);
            }
        }
        onFail();
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求成功
     *
     * @param data 返回数据
     */
    public abstract void onSuccess(T data, int total, int length);

    /**
     * 支付失败调用
     *
     * @param msg 失败信息
     */
    public void onPayFailure(String msg) {

    }

    /**
     * 请求失败,需要时重写
     */
    public void onFail() {

    }
}
