package com.kiegame.mobile;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.multidex.MultiDexApplication;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.kiegame.mobile.exceptions.crash.GlobalCrashCapture;
import com.kiegame.mobile.logger.Log;
import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.ui.activity.LoginActivity;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.worker.Worker;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by: var_rain.
 * Created date: 2020/1/3.
 * Description: 入口
 */
public class Game extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, CameraXConfig.Provider {

    // 静态实例
    @SuppressLint("StaticFieldLeak")
    private static Game INS;
    // 当前所在的Activity
    private Activity activity;
    // Activity管理集合
    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        Worker.init(Worker.newCachedThreadPool());
        GlobalCrashCapture.ins().init(this);
        this.activities = new ArrayList<>();
        this.registerActivityLifecycleCallbacks(this);
        Game.INS = this;
        Network.init();


        Worker.newSingleThreadExecutor().execute(this::initThirdLib);

    }

    private void initThirdLib() {
        Worker.newSingleThreadExecutor().execute(() -> {
            SpeechUtility.createUtility(Game.this, SpeechConstant.APPID + "=5fabb780"+"," + SpeechConstant.NET_CHECK + "=false");
            JPushInterface.setDebugMode(true);
            JPushInterface.init(Game.this);
        });


    }



    /**
     * 获取静态实例
     *
     * @return 返回一个 {@link Game} 对象
     */
    public static Game ins() {
        return INS;
    }

    /**
     * 获取当前显示的Activity
     *
     * @return 返回一个 {@link Activity} 对象
     */
    public Activity activity() {
        return this.activity;
    }

    /**
     * 获取屏幕大小信息
     *
     * @param real 是否真实数据
     * @return {@link DisplayMetrics}
     */
    public DisplayMetrics metrics(boolean real) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (this.activity != null) {
            WindowManager windowManager = this.activity.getWindowManager();
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                if (display != null) {
                    if (real) {
                        display.getRealMetrics(metrics);
                    } else {
                        display.getMetrics(metrics);
                    }
                }
            }
        }
        return metrics;
    }

    /**
     * 退出APP
     */
    public void exit() {
        for (Activity act : activities) {
            if (act != null && !act.isFinishing()) {
                act.finish();
            }
        }
        activities.clear();
        Worker.destroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 登录验证超时(Token过期)
     */
    public void expired(String msg) {
        DialogBox.ins()
                .text(Text.empty(msg) ? "登录验证已过期,请重新登录" : msg)
                .confirm(() -> {
                    if (activity != null) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                })
                .show();
    }

    /**
     * 已在其他地方登录
     */
    public void logged(String msg) {
        DialogBox.ins()
                .text(Text.empty(msg) ? "当前账号已在其他地方登录" : msg)
                .confirm(() -> {
                    if (activity != null) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                })
                .show();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Window window = activity.getWindow();
        if (window != null) {
            ViewGroup view = (ViewGroup) window.getDecorView();
            LayoutTransition layoutTransition = view.getLayoutTransition();
            if (layoutTransition != null) {
                layoutTransition.setDuration(200);
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
            } else {
                LayoutTransition transition = new LayoutTransition();
                transition.setDuration(200);
                transition.enableTransitionType(LayoutTransition.CHANGING);
                view.setLayoutTransition(transition);
            }
        }
        this.activities.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        this.activities.remove(activity);
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
