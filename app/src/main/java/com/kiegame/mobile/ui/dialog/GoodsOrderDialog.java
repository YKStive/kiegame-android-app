package com.kiegame.mobile.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogGoodsOrderBinding;
import com.kiegame.mobile.repository.entity.receive.GoodsOrderEntity;
import com.kiegame.mobile.utils.DateUtil;
import com.kiegame.mobile.utils.Toast;

import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_PRODUCE;
import static com.kiegame.mobile.ui.fragment.ServiceGoodsOrderFragment.DO_ORDER_TYPE_TAKE;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 商品接单
 */
public class GoodsOrderDialog extends BaseDialogFragment {

    private DialogGoodsOrderBinding binding;
    private GoodsOrderEntity.ProductsEntity product;

    //操作回调
    private OnOperateClickListener onOrderProducedClickListener;


    public static GoodsOrderDialog getInstance(GoodsOrderEntity.ProductsEntity product) {
        GoodsOrderDialog dialog = new GoodsOrderDialog();
        dialog.product = product;
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
        GoodsOrderEntity.ProductsEntity.ProcessEntity process = product.getProcess();
        int timeLeft = process.getTimeLeft();
        initTimerCounter(timeLeft);



        binding.tvUserInfo.setText(product.getUserInfo());
        binding.tvTime.setText(product.getCreateDate());
        binding.tvGoodsName.setText(product.getProductName());
        binding.tvGoodsInfo.setText(product.getProductFlavorName());
        binding.tvGoodsPrice.setText("￥"+product.getSellPrice());
        binding.tvOther.setText(product.getExpandInfo());
        ////状态 1:待接单 2：待出品 3：配送中 4：已超时 5：已完成 6:抢单
        for (int i = 0; i < binding.llAction.getChildCount(); i++) {
            binding.llAction.getChildAt(i).setVisibility(View.GONE);
        }
        switch (product.getState()) {
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

    private void initTimerCounter(int timeLeft) {
        String[] result = DateUtil.second2MS(timeLeft);
        binding.tvTitle.setText("即将超时：剩余"+result[0]+":"+result[1]+"s");
        new CountDownTimer(timeLeft, 2000) {
            @Override
            public void onTick(long left) {
                String[] result = DateUtil.second2MS((int) left);
                binding.tvTitle.setText("即将超时：剩余"+result[0]+":"+result[1]+"s");
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        binding.tvOrderReceive.setOnClickListener(v -> {
            if (onOrderProducedClickListener != null) {
                onOrderProducedClickListener.doSomething(DO_ORDER_TYPE_TAKE,product.getOrderId(),product.getProductId());
            }
            dismiss();
        });
        binding.tvOrderReceiveSmall.setOnClickListener(v -> {
            if (onOrderProducedClickListener != null) {
                onOrderProducedClickListener.doSomething(DO_ORDER_TYPE_TAKE,product.getOrderId(),product.getProductId());
            }
            dismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            dismiss();
        });
        binding.tvConfirm.setOnClickListener(v -> {
            dismiss();
        });
        binding.tvOrderRob.setOnClickListener(v -> {
            if (onOrderProducedClickListener != null) {
                onOrderProducedClickListener.doSomething(DO_ORDER_TYPE_TAKE,product.getOrderId(),product.getProductId());
            }
            dismiss();
        });
        binding.tvOrderProduced.setOnClickListener(v -> {
            if (onOrderProducedClickListener != null) {
                onOrderProducedClickListener.doSomething(DO_ORDER_TYPE_PRODUCE,product.getOrderId(),product.getProductId());
            }
            dismiss();
        });
    }


    public interface OnOperateClickListener {

        void doSomething(int type,String orderId,String productId);
    }

    public void setOnOrderDoSomethingClickListener(OnOperateClickListener onOrderProducedClickListener) {
        this.onOrderProducedClickListener = onOrderProducedClickListener;
    }
}
