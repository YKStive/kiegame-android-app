package com.kiegame.mobile.ui.base.listener;

import android.animation.Animator;

/**
 * Created by: var_rain.
 * Created date: 2018/10/20.
 * Description: 动画监听
 */
public interface OnAnimationListener extends Animator.AnimatorListener {

    @Override
    default void onAnimationStart(Animator animation) {

    }

    @Override
    default void onAnimationEnd(Animator animation) {

    }

    @Override
    default void onAnimationCancel(Animator animation) {

    }

    @Override
    default void onAnimationRepeat(Animator animation) {

    }
}
