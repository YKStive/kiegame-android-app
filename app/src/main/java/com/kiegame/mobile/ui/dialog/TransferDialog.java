package com.kiegame.mobile.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogTransferBinding;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.utils.DateUtil;
import com.kiegame.mobile.utils.Toast;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 转接
 */
public class TransferDialog extends BaseDialogFragment {

    private DialogTransferBinding binding;

    private ServiceCallEntity callEntity;
    private OnSureListener listener;

    public static TransferDialog getInstance(ServiceCallEntity callEntity,OnSureListener listener) {
        TransferDialog dialog = new TransferDialog();
        dialog.callEntity = callEntity;
        dialog.listener = listener;
        return dialog;
    }





    @Override
    public int getLayoutId() {
        return R.layout.dialog_transfer;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle savedInstanceState) {
        initCounter();
        binding = (DialogTransferBinding) rootBinding;
        // TODO: 2020/11/10 修改为正常的字段数据


        binding.tvMessage.setText("B135机呼叫即将超时，请立即\n前往服务");

        if (callEntity.getState() == 1) {
            binding.llTransfer.setVisibility(View.VISIBLE);
            binding.tvConfirm.setVisibility(View.GONE);
        } else {
            binding.llTransfer.setVisibility(View.GONE);
            binding.tvConfirm.setVisibility(View.VISIBLE);
        }
    }

    private void initCounter() {
        CountDownTimer timer = new CountDownTimer(callEntity.getTimeLeft()*1000, 1000) {
            @Override
            public void onTick(long left) {
                String[] result = DateUtil.second2MS((int) left/1000);
                binding.tvTimeEnd.setText("即将超时：剩余"+result[0]+":"+result[1]+"s");
            }

            @Override
            public void onFinish() {
                binding.tvTimeEnd.setText("已经超时");
                binding.tvMessage.setText("B135机呼叫已超时，请立即\n前往服务");

            }
        }.start();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        //确定点击事件
        binding.tvConfirm.setOnClickListener(v -> dismiss());
        //取消点击事件
        binding.tvCancel.setOnClickListener(v -> dismiss());
        //转接
        binding.tvTransfer.setOnClickListener(v -> {
            if(listener!=null){
                listener.onSure(callEntity.getCancelOperationId(),callEntity.getCpId());
            }
            dismiss();
        });
    }

   public interface OnSureListener{
        void onSure(String cancelOperatorId,String cpId);
   }

   public void setOnSureListener(OnSureListener listener){
        this.listener = listener;
   }
}
