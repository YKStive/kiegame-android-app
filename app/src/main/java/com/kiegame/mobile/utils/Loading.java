package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
    private static ValueAnimator animator;
    private static boolean isShowing;

    /**
     * 显示加载动画
     */
    public static void show() {
        if (Loading.view == null) {
            Loading.view = InjectView.ins().make(R.layout.view_loading);
        }
        if (Loading.animator == null) {
            Loading.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
            Loading.animator.setDuration(200);
            Loading.animator.addUpdateListener(animation -> {
                if (Loading.view != null) {
                    Loading.view.setAlpha((Float) animation.getAnimatedValue());
                }
            });
            Loading.animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    if (!isReverse) {
                        InjectView.ins().inject(Loading.view);
                        Loading.isShowing = true;
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    if (isReverse) {
                        InjectView.ins().clean(Loading.view);
                        Loading.isShowing = false;
                    }
                }
            });
        }
        if (!Loading.isShowing) {
            Loading.animator.start();
        }
    }

    /**
     * 隐藏加载动画
     */
    public static void hide() {
        if (Loading.animator != null && Loading.isShowing) {
            Loading.animator.reverse();
        }
    }
}
