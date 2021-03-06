package com.kiegame.mobile.ui.dialog;

import android.os.Bundle;
import android.os.Handler;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogSuccessBinding;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 成功
 */
public class SuccessDialog extends BaseDialogFragment {

    private String message;

    public static SuccessDialog getInstance(String message) {
        SuccessDialog dialog = new SuccessDialog();
        dialog.message = message;
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_success;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        DialogSuccessBinding binding = (DialogSuccessBinding) rootBinding;
        binding.tvMessage.setText(message);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        new Handler().postDelayed(this::dismiss, 1500);
    }
}
