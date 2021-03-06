package com.kiegame.mobile.utils;

import android.view.Gravity;

import androidx.annotation.StringRes;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.ui.views.ToastView;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: Toast工具
 */
public class Toast {

    // 长时间显示
    public final static int TOAST_DURATION_LONG = 1;
    // 短时间显示
    public final static int TOAST_DURATION_SHORT = 0;

    /**
     * 显示Toast
     *
     * @param text 字符串
     */
    public static void show(String text) {
//        android.widget.Toast.makeText(Game.ins(), text, TOAST_DURATION_SHORT).show();
        ToastView.INS.showToast(text, Gravity.CENTER, Toast.TOAST_DURATION_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param id 字符串资源ID
     */
    public static void show(@StringRes int id) {
        ToastView.INS.showToast(Game.ins().getString(id), Gravity.CENTER, TOAST_DURATION_SHORT);
//        android.widget.Toast.makeText(Game.ins(), Game.ins().getString(id), TOAST_DURATION_SHORT).show();
    }

    /**
     * 显示Toast
     *
     * @param id   字符串资源ID
     * @param args 格式化参数
     */
    public static void show(@StringRes int id, Object... args) {
//        android.widget.Toast.makeText(Game.ins(), Game.ins().getString(id, args), TOAST_DURATION_SHORT).show();
        ToastView.INS.showToast(Game.ins().getString(id, args), Gravity.CENTER, TOAST_DURATION_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param text     字符串
     * @param gravity  显示位置,参考 {@link Gravity}
     * @param duration 显示时长,接受参数 {@link Toast#TOAST_DURATION_SHORT,Toast#TOAST_DURATION_LONG}
     */
    public static void show(String text, int gravity, int duration) {
        ToastView.INS.showToast(text, gravity, duration);
    }
}
