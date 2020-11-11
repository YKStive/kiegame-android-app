package com.kiegame.mobile.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogTransferBinding;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.utils.Toast;

/**
 * 转接Dialog
 */
public class TransferDialog extends BaseDialogFragment {

    private DialogTransferBinding binding;

    private ServiceCallEntity callEntity;

    public static TransferDialog getInstance(ServiceCallEntity callEntity) {
        TransferDialog dialog = new TransferDialog();
        dialog.callEntity = callEntity;
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_transfer;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle savedInstanceState) {
        binding = (DialogTransferBinding) rootBinding;
        // TODO: 2020/11/10 修改为正常的字段数据
        binding.tvTimeEnd.setText("即将超时：剩余00:26s");
        binding.tvMessage.setText("B135机呼叫已超时，请立即\n前往服务");

        if (callEntity.getState() == 1) {
            binding.llTransfer.setVisibility(View.VISIBLE);
            binding.tvConfirm.setVisibility(View.GONE);
        } else {
            binding.llTransfer.setVisibility(View.GONE);
            binding.tvConfirm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        //确定点击事件
        binding.tvConfirm.setOnClickListener(v -> dismiss());
        //取消点击事件
        binding.tvCancel.setOnClickListener(v -> dismiss());
        //转接
        binding.tvTransfer.setOnClickListener(v -> {
            // TODO: 2020/11/10 转接，如果需要在界面上实现，可通过接口回调方法实现
            Toast.show("转接");
            dismiss();
        });
    }
}
