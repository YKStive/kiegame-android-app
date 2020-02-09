package com.kiegame.mobile.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentAllOrderBinding;
import com.kiegame.mobile.model.OrderModel;
import com.kiegame.mobile.repository.entity.receive.BuyOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BuyShopEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.views.MarginItemDecoration;
import com.kiegame.mobile.utils.Pixel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 全部订单
 */
public class AllOrderFragment extends BaseFragment<FragmentAllOrderBinding> {

    private OrderModel model;
    private BaseQuickAdapter<BuyOrderEntity, BaseViewHolder> adapter;

    @Override
    protected int onLayout() {
        return R.layout.fragment_all_order;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(OrderModel.class);
    }

    @Override
    protected void onView() {
        binding.tvTotalMoney.setText("0");

        binding.rvOrder.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        binding.rvOrder.addItemDecoration(new MarginItemDecoration(10));
        adapter = new BaseQuickAdapter<BuyOrderEntity, BaseViewHolder>(R.layout.item_order_wait_pay) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, BuyOrderEntity item) {
                setPayState(helper, item.getPayState());
                List<BuyShopEntity> shops = item.getItemList();
                if (item.getItemList().size() > 1) {
                    setMultiShopState(helper);
                    helper.setText(R.id.tv_shop_total_num, String.format("共%s件", shops.size()));
                    LinearLayout images = helper.getView(R.id.ll_shop_image_content);
                    images.removeAllViews();
                    for (BuyShopEntity shop : shops) {
                        // 最多显示3个商品图标
                        if (images.getChildCount() >= 3) {
                            break;
                        }
                        ImageView view = new ImageView(helper.itemView.getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Pixel.dp2px(70), Pixel.dp2px(70));
                        params.rightMargin = Pixel.dp2px(10);
                        view.setLayoutParams(params);
                        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        images.addView(view);
                        Glide.with(view).load(shop.getProductImg()).into(view);
                    }
                } else {
                    setSingleShopState(helper);
                    BuyShopEntity shop = shops.get(0);
                    Glide.with(helper.itemView).load(shop.getProductImg()).into((ImageView) helper.getView(R.id.iv_shop_image));
                    helper.setText(R.id.tv_user_name, item.getCustomerName());
                    helper.setText(R.id.tv_shop_name, shop.getProductName());
                }
                helper.setText(R.id.tv_shop_total_money, String.format("¥%s", cal(item.getOrderAmount())));
            }
        };
        binding.rvOrder.setAdapter(adapter);
    }

    @Override
    protected void onData() {
        model.queryOrders(null, null, null, null, null, null).observe(this, this::queryWaitPayOrderResult);
    }

    /**
     * 多商品状态
     */
    private void setMultiShopState(@NonNull BaseViewHolder helper) {
        helper.getView(R.id.tv_shop_total_num).setVisibility(View.VISIBLE);
        helper.getView(R.id.nsl_shop_image_scroll).setVisibility(View.VISIBLE);
        helper.getView(R.id.iv_shop_image).setVisibility(View.INVISIBLE);
        helper.getView(R.id.tv_shop_name).setVisibility(View.GONE);
        helper.getView(R.id.tv_shop_des).setVisibility(View.GONE);
    }

    /**
     * 单商品状态
     */
    private void setSingleShopState(@NonNull BaseViewHolder helper) {
        helper.getView(R.id.tv_shop_total_num).setVisibility(View.GONE);
        helper.getView(R.id.nsl_shop_image_scroll).setVisibility(View.GONE);
        helper.getView(R.id.iv_shop_image).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_shop_name).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_shop_des).setVisibility(View.VISIBLE);
    }

    /**
     * 待支付状态
     */
    private void setPayState(@NonNull BaseViewHolder helper, int state) {
        helper.getView(R.id.iv_pay_type_image).setVisibility(View.GONE);
        helper.getView(R.id.tv_pay_type).setVisibility(View.GONE);
        helper.getView(R.id.tv_pay_time).setVisibility(View.GONE);
        helper.getView(R.id.cb_shop_select).setVisibility(View.GONE);
        TextView tv = helper.getView(R.id.tv_payment_state);
        // 待支付1、支付中2、退款中3、取消订单4（最终状态）、订单完成5（最终状态）、已退款6（最终状态）
        switch (state) {
            case 1:
                tv.setText("待支付");
                tv.setTextColor(getResources().getColor(R.color.wait_pay_state));
                break;
            case 2:
                tv.setText("支付中");
                tv.setTextColor(getResources().getColor(R.color.wait_pay_state));
                break;
            case 3:
                tv.setText("退款中");
                tv.setTextColor(getResources().getColor(R.color.wait_pay_state));
                break;
            case 4:
                tv.setText("交易取消");
                tv.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 5:
                tv.setText("交易完成");
                tv.setTextColor(getResources().getColor(R.color.black));
                break;
            case 6:
                tv.setText("已退款");
                tv.setTextColor(getResources().getColor(R.color.wait_pay_state));
                break;
        }
    }

    /**
     * 待支付订单数据处理
     *
     * @param data 数据对象
     */
    private void queryWaitPayOrderResult(List<BuyOrderEntity> data) {
        adapter.setNewData(data);
    }

    /**
     * 计算金额,分转换位元,保留两位
     *
     * @param money 金额, 分
     * @return 转换后的金额, 元
     */
    private String cal(int money) {
        BigDecimal source = new BigDecimal(money);
        BigDecimal ratio = new BigDecimal(100);
        BigDecimal divide = source.divide(ratio, 2, RoundingMode.HALF_UP);
        return divide.toString();
    }
}
