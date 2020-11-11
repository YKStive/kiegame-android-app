package com.kiegame.mobile.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogGoodsOrderBinding;
import com.kiegame.mobile.repository.entity.receive.GoodsOrderEntity;
import com.kiegame.mobile.utils.Toast;

/**
 * 商品接单dialog
 */
public class GoodsOrderDialog extends BaseDialogFragment {

    private DialogGoodsOrderBinding binding;
    private GoodsOrderEntity.SingleOrderEntity orderEntity;
    //出品点击回调（如果其他需要在界面中处理逻辑，可用同样的方法处理）
    private OnPositiveClickListener onOrderProducedClickListener;


    public static GoodsOrderDialog getInstance(GoodsOrderEntity.SingleOrderEntity orderEntity) {
        GoodsOrderDialog dialog = new GoodsOrderDialog();
        dialog.orderEntity = orderEntity;
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_goods_order;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle savedInstanceState) {
        binding = (DialogGoodsOrderBinding) rootBinding;
        binding.tvTitle.setText("即将超时：剩余00:26s");
        binding.tvUserInfo.setText("B135|7780|吧台-李四");
        binding.tvTime.setText("09-03 11:11");
        binding.tvGoodsName.setText("香草拿铁");
        binding.tvGoodsInfo.setText("大/加冰/不加糖");
        binding.tvGoodsPrice.setText("￥9.99");
        binding.tvOther.setText("金桔柠檬、乐事薯片等3件商品");
        ////状态 1:待接单 2：待出品 3：配送中 4：已超时 5：已完成 6:抢单
        for (int i = 0; i < binding.llAction.getChildCount(); i++) {
            binding.llAction.getChildAt(i).setVisibility(View.GONE);
        }
        switch (orderEntity.getState()) {
            case 1:
                binding.tvCancel.setVisibility(View.VISIBLE);
                binding.tvOrderReceiveSmall.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.tvCancel.setVisibility(View.VISIBLE);
                binding.tvOrderProduced.setVisibility(View.VISIBLE);
                break;
            case 4:
                binding.tvOrderReceive.setVisibility(View.VISIBLE);
                break;
            case 6:
                binding.tvCancel.setVisibility(View.VISIBLE);
                binding.tvOrderRob.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        binding.tvOrderReceive.setOnClickListener(v -> {
            Toast.show("接单");
            dismiss();
        });
        binding.tvOrderReceiveSmall.setOnClickListener(v -> {
            Toast.show("接单");
            dismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            dismiss();
        });
        binding.tvConfirm.setOnClickListener(v -> {
            Toast.show("确定");
            dismiss();
        });
        binding.tvOrderRob.setOnClickListener(v -> {
            Toast.show("抢单");
            dismiss();
        });
        binding.tvOrderProduced.setOnClickListener(v -> {
            if (onOrderProducedClickListener!=null){
                onOrderProducedClickListener.onPositiveClick();
            }
            dismiss();
        });
    }


    public interface OnPositiveClickListener{
        void onPositiveClick();
    }

    public void setOnOrderProducedClickListener(OnPositiveClickListener onOrderProducedClickListener) {
        this.onOrderProducedClickListener = onOrderProducedClickListener;
    }
}
