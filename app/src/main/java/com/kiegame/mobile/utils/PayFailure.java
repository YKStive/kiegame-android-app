package com.kiegame.mobile.utils;

import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewPaymentFailureBinding;

/**
 * Created by: var_rain.
 * Created date: 2020/2/8.
 * Description: 支付成功对话框
 */
public class PayFailure {

    private static PayFailure INS;
    private ViewPaymentFailureBinding binding;
    private boolean isShowing;

    /**
     * 构造方法
     */
    private PayFailure() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_payment_failure, null, false);
    }

    /**
     * 获取当前对象的静态实例
     *
     * @return {@link PayFailure}
     */
    public static PayFailure ins() {
        if (PayFailure.INS == null) {
            PayFailure.INS = new PayFailure();
        }
        return PayFailure.INS;
    }

    /**
     * 设置失败信息
     *
     * @param msg 失败信息
     * @return {@link PayFailure}
     */
    public PayFailure message(String msg) {
        binding.tvPayMsg.setText(msg);
        return this;
    }

    /**
     * 确认按钮
     *
     * @param listener 回调监听
     * @return {@link PayFailure}
     */
    public PayFailure confirm(OnClickListener listener) {
        binding.tvBtnOk.setOnClickListener(v -> {
            this.hide();
            if (listener != null) {
                listener.onClick();
            }
        });
        return this;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (!this.isShowing) {
            isShowing = true;
            InjectView.ins().inject(binding.getRoot());
        }
    }

    /**
     * 隐藏对话框
     */
    private void hide() {
        if (this.isShowing) {
            isShowing = false;
            InjectView.ins().clean(binding.getRoot());
        }
    }

    /**
     * 点击事件
     */
    public interface OnClickListener {

        /**
         * 点击回调方法
         */
        void onClick();
    }
}
