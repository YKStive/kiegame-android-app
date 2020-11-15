package com.kiegame.mobile.utils;

import android.annotation.SuppressLint;
import android.view.View;

import com.kiegame.mobile.R;

/**
 * Created by: var_rain.
 * Created date: 2020/1/15.
 * Description: 加载框
 */
public class Loading {

    @SuppressLint("StaticFieldLeak")
    private static View view;
    private static boolean isShowing;
    private static long time;

    /**
     * 显示加载动画
     */
    public static void show() {
        long millis = System.currentTimeMillis();
        if (millis - time < 1000) {
            return;
        }
        time = millis;
        if (Loading.view == null) {
            Loading.view = InjectView.ins().make(R.layout.view_loading);
        }
        if (!Loading.isShowing) {
            Loading.isShowing = true;
            InjectView.ins().inject(Loading.view);
        }
    }

    /**
     * 隐藏加载动画
     */
    public static void hide() {
        if (Loading.isShowing) {
            Loading.isShowing = false;
            InjectView.ins().clean(Loading.view);
        }
    }
}
