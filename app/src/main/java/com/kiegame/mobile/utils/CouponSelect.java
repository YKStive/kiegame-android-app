package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewCouponUseBinding;
import com.kiegame.mobile.ui.base.listener.OnAnimationListener;

/**
 * Created by: var_rain.
 * Created date: 2020/2/2.
 * Description: 优惠券选择框
 */
public class CouponSelect {

    private static CouponSelect INS;
    private ViewCouponUseBinding binding;
    private ValueAnimator alphaAnimator;
    private ValueAnimator moveAnimator;
    private boolean isShowing;
    private float moveSize;
    private OnCouponUseCallback callback;

    /**
     * 构造方法
     */
    private CouponSelect() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_coupon_use, null, false);
        this.moveSize = Game.ins().metrics(true).heightPixels * 0.45f;
        this.initAnim();
    }

    /**
     * 获取 {@link CouponSelect} 对象
     *
     * @return {@link CouponSelect}
     */
    public static CouponSelect ins() {
        if (CouponSelect.INS == null) {
            CouponSelect.INS = new CouponSelect();
        }
        return CouponSelect.INS;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        this.alphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.alphaAnimator.setDuration(150);
        this.alphaAnimator.addUpdateListener(animation -> {
            if (this.binding != null) {
                this.binding.vvBackground.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        this.alphaAnimator.addListener(new OnAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    InjectView.ins().clean(binding.getRoot());
                    isShowing = false;
                }
            }
        });
        this.moveAnimator = ValueAnimator.ofFloat(moveSize, 0.0f);
        this.moveAnimator.setDuration(150);
        this.moveAnimator.addUpdateListener(animation -> {
            if (this.binding != null) {
                this.binding.clContent.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        this.moveAnimator.addListener(new OnAnimationListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                InjectView.ins().inject(binding.getRoot());
                isShowing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    if (alphaAnimator != null) {
                        alphaAnimator.reverse();
                    }
                } else {
                    if (alphaAnimator != null) {
                        alphaAnimator.start();
                    }
                }
            }
        });
    }

    /**
     * 设置数据
     */
    public CouponSelect set() {
        binding.clRoot.setOnClickListener(v -> this.hide());
        binding.tvCloseBtn.setOnClickListener(v -> this.hide());
        binding.tlCouponTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return this;
    }

    /**
     * 设置优惠券使用回调
     *
     * @param callback {@link OnCouponUseCallback}
     */
    public CouponSelect callbak(OnCouponUseCallback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 显示优惠券
     */
    public void show() {
        if (!this.isShowing) {
            this.moveAnimator.start();
        }
    }

    /**
     * 隐藏优惠券
     */
    private void hide() {
        if (this.moveAnimator != null && this.isShowing) {
            this.moveAnimator.reverse();
        }
    }

    /**
     * 优惠券使用回调
     */
    public interface OnCouponUseCallback {

        /**
         * 优惠卷使用回调方法
         */
        void onCouponUse();
    }
}
