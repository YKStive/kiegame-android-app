package com.kiegame.mobile.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.consts.Payment;
import com.kiegame.mobile.databinding.FragmentAllOrderBinding;
import com.kiegame.mobile.model.OrderModel;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BuyOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BuyShopEntity;
import com.kiegame.mobile.repository.entity.receive.PayResultEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.activity.ScanActivity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.views.MarginItemDecoration;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.PayFailure;
import com.kiegame.mobile.utils.PaySuccess;
import com.kiegame.mobile.utils.Pixel;
import com.kiegame.mobile.utils.ServicePay;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;
import com.kiegame.mobile.worker.Worker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 全部订单
 */
public class AllOrderFragment extends BaseFragment<FragmentAllOrderBinding> {

    private final int RESULT_CODE_SCAN = 10086;
    private BaseQuickAdapter<BuyOrderEntity, BaseViewHolder> adapter;
    private List<BuyOrderEntity> orders;
    private OrderModel model;
    private int money;
    private MutableLiveData<Integer> moneyObserver;
    private OrderFragment fragment;
    private String[] permissions;
    private List<BuyOrderEntity> buys;
    // 支付参数
    private int totalMoney;
    private StringBuilder rechargeOrderList;
    private StringBuilder productOrderList;
    private int totalPayType;
    // 轮询次数记录
    private int requestCount;
    // 轮询类型
    private int resultType;
    // 订单号
    private String baseOrderId;

    AllOrderFragment(OrderFragment fragment) {
        this.fragment = fragment;
        this.buys = new ArrayList<>();
        this.rechargeOrderList = new StringBuilder();
        this.productOrderList = new StringBuilder();
    }

    @Override
    protected int onLayout() {
        return R.layout.fragment_all_order;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(OrderModel.class);
        orders = new ArrayList<>();
        this.moneyObserver = new MutableLiveData<>();
        this.moneyObserver.observe(this, integer -> {
            if (integer != null) {
                binding.tvTotalMoney.setText(cal(integer));
            }
        });
        model.failureMessage.observe(this, s -> PayFailure.ins().message(s).show());
        permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE,
        };
    }

    @Override
    protected void onView() {
        binding.tvTotalMoney.setText(cal(money));

        binding.rvOrder.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        binding.rvOrder.addItemDecoration(new MarginItemDecoration(10));
        adapter = new BaseQuickAdapter<BuyOrderEntity, BaseViewHolder>(R.layout.item_order_wait_pay, this.orders) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, BuyOrderEntity item) {
                setPayState(helper, item);
                List<BuyShopEntity> shops = item.getItemList();
                if (item.getItemList().size() > 1) {
                    setMultiShopState(helper, shops);
                } else {
                    setSingleShopState(helper, shops.get(0));
                }
                String payName = getPaymentType(item.getPayType());
                helper.setText(R.id.tv_pay_type, payName);
                helper.setVisible(R.id.iv_pay_type_image, !Text.empty(payName));
                helper.setVisible(R.id.tv_pay_type, !Text.empty(payName));
                helper.setText(R.id.tv_user_name, getUserName(item));
                // 支付成功后订单金额显示不正确的问题
                helper.setText(R.id.tv_shop_total_money, String.format("¥%s", cal(item.getOrderAmount())));
//                helper.setText(R.id.tv_shop_total_money, String.format("¥%s", cal(item.getTotalAmount())));
                helper.getView(R.id.iv_delete_btn).setOnClickListener(v -> DialogBox.ins().text("你想要删除这个订单吗?").confirm(() -> deleteOrder(item.getOrderBaseId())).cancel(null).show());
                helper.getView(R.id.tv_cancel_btn).setOnClickListener(v -> DialogBox.ins().text("你想要取消这个订单吗?").confirm(() -> {
                    String detailId = getOrderDetailId(item.getItemList());
                    cancelOrder(item.getOrderId(), detailId);
                    baseOrderId = item.getOrderState() == 2 || item.getOrderState() == 5 ? item.getOrderBaseId() : null;
                }).cancel(null).show());
                helper.getView(R.id.tv_pay_btn).setOnClickListener(v -> DialogBox.ins().text("你想要支付这个订单吗?").confirm(() -> {
                    buys.clear();
                    buys.add(item);
                    totalShops();
                }).cancel(null).show());
            }
        };
        binding.rvOrder.setAdapter(adapter);
    }

    @Override
    protected void onData() {
        updateMoney();
    }

    /**
     * 结算商品
     */
    private void totalShops() {
        productOrderList.setLength(0);
        rechargeOrderList.setLength(0);
        totalMoney = 0;
        totalPayType = -1;
        for (BuyOrderEntity order : buys) {
            if (order.getOrderType() == 1) {
                // 网费充值
                if (rechargeOrderList.length() != 0) {
                    rechargeOrderList.append(",");
                }
                rechargeOrderList.append(order.getOrderId());
            } else {
                // 商品订单
                if (productOrderList.length() != 0) {
                    productOrderList.append(",");
                }
                productOrderList.append(order.getOrderId());
            }
            if (totalPayType == -1) {
                totalPayType = order.getPayType();
            } else {
                if (order.getPayType() != totalPayType) {
                    Toast.show("所选订单支付存在不相同的支付方式");
                    return;
                }
            }
            totalMoney += order.getTotalAmount();
        }
        selectPayType();
    }

    /**
     * 选择支付方式
     */
    private void selectPayType() {
        if (totalPayType == Payment.PAY_TYPE_SERVICE) {
            ServicePay.ins().money(totalMoney).confirm(pwd -> payOrder(productOrderList.toString(), rechargeOrderList.toString(), totalPayType, pwd, String.valueOf(totalMoney))).show();
        } else {
            requestSelfPermission(permissions, (authorize, permissions1) -> {
                if (authorize) {
                    String value;
                    if (totalPayType == Payment.PAY_TYPE_BUCKLE) {
                        value = String.format("卡扣支付%s元", cal(totalMoney));
                    } else if (totalPayType == Payment.PAY_TYPE_ONLINE) {
                        value = String.format("扫码支付%s元", cal(totalMoney));
                    } else {
                        value = "扫码支付";
                    }
                    startActivityForResult(new Intent(getActivity(), ScanActivity.class)
                                    .putExtra(Setting.APP_SCAN_TITLE, value),
                            RESULT_CODE_SCAN);
                } else {
                    Toast.show("相机权限授权失败");
                }
            });
        }
    }

    /**
     * 支付订单
     *
     * @param productOrderList  商品订单号
     * @param rechargeOrderList 充值订单号
     * @param buyPayType        支付类型
     * @param buyPayPassword    支付密码
     * @param paidInAmount      支付金额
     */
    private void payOrder(String productOrderList, String rechargeOrderList, int buyPayType, String buyPayPassword, String paidInAmount) {
        LiveData<List<AddOrderEntity>> liveData = model.updateOrder(productOrderList, rechargeOrderList, buyPayType, buyPayPassword, paidInAmount, null);
        if (!liveData.hasObservers()) {
            liveData.observe(this, this::payResult);
        }
    }

    /**
     * 支付返回
     *
     * @param data 数据对象
     */
    private void payResult(List<AddOrderEntity> data) {
        if (data != null) {
            AddOrderEntity order = data.get(0);
            if (order != null) {
                if (totalPayType == Payment.PAY_TYPE_ONLINE) {
                    queryPayResult(order.getPaymentPayId(), 1);
                } else {
                    PaySuccess.ins().order(order.getPaymentPayId()).confirm(() -> fragment.requestData()).show();
                }
            }
        }
    }

    /**
     * 删除订单
     *
     * @param orderId 订单号
     */
    private void deleteOrder(String orderId) {
        LiveData<Object> liveData = model.deleteOrder(orderId);
        if (!liveData.hasObservers()) {
            liveData.observe(this, this::onDeleteOrCancelOrderResult);
        }
    }

    /**
     * 取消订单
     *
     * @param orderId      订单号
     * @param orderDetilId 订单明细ID
     */
    private void cancelOrder(String orderId, String orderDetilId) {
        LiveData<Object> liveData = model.cancelOrder(orderId, orderDetilId, null);
        if (!liveData.hasObservers()) {
            liveData.observe(this, this::onDeleteOrCancelOrderResult);
        }
    }

    /**
     * 删除/取消订单返回
     *
     * @param object 数据对象
     */
    private void onDeleteOrCancelOrderResult(Object object) {
        if (this.baseOrderId != null) {
            queryPayResult(this.baseOrderId, 2);
        } else {
            this.fragment.requestData();
            Toast.show("取消成功");
        }
    }

    /**
     * 获取订单详情ID
     *
     * @param orders 订单号
     * @return 返回订单详情ID字符串
     */
    private String getOrderDetailId(List<BuyShopEntity> orders) {
        StringBuilder sb = new StringBuilder();
        if (orders != null && !orders.isEmpty()) {
            for (BuyShopEntity order : orders) {
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(order.getOrderDetailId());
            }
        }
        return sb.toString();
    }

    /**
     * 获取用户名称
     *
     * @param item 数据对象
     * @return 返回组合的用户名称
     */
    private String getUserName(BuyOrderEntity item) {
        if (Text.empty(item.getSeatNumber())) {
            if (Text.empty(item.getIdCard())) {
                return String.format("%s", Text.formatCustomName(item.getCustomerName()));
            } else {
                return String.format("%s | %s", Text.lastIdNum(item.getIdCard()), Text.formatCustomName(item.getCustomerName()));
            }
        } else {
            return String.format("%s | %s | %s", item.getSeatNumber(), Text.lastIdNum(item.getIdCard()), Text.formatCustomName(item.getCustomerName()));
        }
    }

    /**
     * 获取支付类型
     *
     * @param type 支付类型
     * @return 返回支付类型字符串
     */
    private String getPaymentType(int type) {
        String name = "在线支付";
        switch (type) {
            case Payment.PAY_TYPE_ONLINE:
                name = "在线支付";
                break;
            case Payment.PAY_TYPE_BUCKLE:
                name = "卡扣支付";
                break;
            case Payment.PAY_TYPE_SERVICE:
                name = "客维支付";
                break;
        }
        return name;
    }

    /**
     * 多商品状态
     */
    private void setMultiShopState(@NonNull BaseViewHolder helper, List<BuyShopEntity> shops) {
        helper.getView(R.id.tv_shop_total_num).setVisibility(View.VISIBLE);
        helper.getView(R.id.nsl_shop_image_scroll).setVisibility(View.VISIBLE);
        helper.getView(R.id.iv_shop_image).setVisibility(View.INVISIBLE);
        helper.getView(R.id.tv_shop_name).setVisibility(View.GONE);
        helper.getView(R.id.tv_shop_des).setVisibility(View.GONE);
        LinearLayout images = helper.getView(R.id.ll_shop_image_content);
        images.removeAllViews();
        int count = 0;
        for (BuyShopEntity shop : shops) {
            // 最多显示3个商品图标
            if (images.getChildCount() < 3) {
                ImageView view = new ImageView(helper.itemView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Pixel.dp2px(70), Pixel.dp2px(70));
                params.rightMargin = Pixel.dp2px(10);
                view.setLayoutParams(params);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                images.addView(view);
                Glide.with(view).load(shop.getProductImg()).into(view);
            }
            count += Integer.valueOf(shop.getSellCount());
        }
        helper.setText(R.id.tv_shop_total_num, String.format("共%s件", count));
    }

    /**
     * 单商品状态
     */
    private void setSingleShopState(@NonNull BaseViewHolder helper, BuyShopEntity shop) {
        helper.getView(R.id.tv_shop_total_num).setVisibility(View.VISIBLE);
        helper.getView(R.id.nsl_shop_image_scroll).setVisibility(View.GONE);
        helper.getView(R.id.iv_shop_image).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_shop_name).setVisibility(View.VISIBLE);
        helper.getView(R.id.tv_shop_des).setVisibility(View.VISIBLE);
        Glide.with(helper.itemView).load(shop.getProductImg()).into((ImageView) helper.getView(R.id.iv_shop_image));
        helper.setText(R.id.tv_shop_name, shop.getProductName());
        helper.setText(R.id.tv_shop_des, String.format("商品描述: %s", shop.getProductDesc()));
        helper.setText(R.id.tv_shop_total_num, String.format("共%s件", shop.getSellCount()));
    }

    /**
     * 待支付状态
     */
    private void setPayState(@NonNull BaseViewHolder helper, BuyOrderEntity data) {
        helper.getView(R.id.tv_cancel_btn).setVisibility(View.GONE);
        helper.getView(R.id.tv_pay_btn).setVisibility(View.GONE);
        helper.getView(R.id.tv_pay_time).setVisibility(View.GONE);
        helper.getView(R.id.cb_shop_select).setVisibility(View.GONE);
        TextView tv = helper.getView(R.id.tv_payment_state);
        // 待支付1、支付中2、退款中3、取消订单4（最终状态）、订单完成5（最终状态）、已退款6（最终状态）
        switch (data.getOrderState()) {
            case 1:
                tv.setText("待支付");
                tv.setTextColor(getResources().getColor(R.color.wait_pay_state));
                helper.getView(R.id.tv_cancel_btn).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_pay_btn).setVisibility(View.VISIBLE);
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
                tv.setText("已取消");
                tv.setTextColor(getResources().getColor(R.color.gray));
                helper.getView(R.id.tv_pay_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_pay_time, data.getOperateTime());
                break;
            case 5:
                tv.setText("已支付");
                tv.setTextColor(getResources().getColor(R.color.black));
                helper.getView(R.id.tv_pay_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_pay_time, data.getOperateTime());
                break;
            case 6:
                tv.setText("已退款");
                tv.setTextColor(getResources().getColor(R.color.wait_pay_state));
                helper.getView(R.id.tv_pay_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_pay_time, data.getOperateTime());
                break;
        }
    }

    /**
     * 查询支付结果
     *
     * @param data ID
     * @param type 类型 1 支付 2 退款
     */
    private void queryPayResult(String data, int type) {
        this.resultType = type;
        LiveData<List<PayResultEntity>> liveData;
        if (type == 1) {
            liveData = model.queryPayState(data, null);
        } else {
            liveData = model.queryPayState(null, data);
        }
        if (!liveData.hasObservers()) {
            liveData.observe(this, payResultEntities -> {
                if (payResultEntities != null) {
                    PayResultEntity res = payResultEntities.get(0);
                    if (res.getPayState() == 3 || res.getReturnState() == 3) {
                        if (resultType == 2) {
                            if (requestCount == 10) {
                                Toast.show("退款结果请稍后查看");
                                return;
                            }
                            requestCount++;
                        }
                        queryPayResult(resultType == 1 ? res.getPaymentPayId() : res.getOrderBaseId(), resultType);
                    } else {
                        if (resultType == 1) {
                            if (res.getPayState() == 2) {
                                PaySuccess.ins().confirm(() -> fragment.requestData()).order(res.getPaymentPayId()).show();
                            } else if (res.getPayState() == 4) {
                                fragment.requestData();
                                PayFailure.ins().message("支付失败").show();
                            }
                        } else {
                            fragment.requestData();
                            if (res.getReturnState() == 4) {
                                Toast.show("退款成功");
                            } else if (res.getReturnState() == 5) {
                                Toast.show("退款失败");
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 待支付订单数据处理
     *
     * @param data 数据对象
     */
    void refreshData(List<BuyOrderEntity> data) {
        this.orders.clear();
        this.orders.addAll(data);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            updateMoney();
        }
    }

    /**
     * 更新金额
     */
    private void updateMoney() {
        Worker.execute(() -> {
            this.money = 0;
            if (this.orders != null) {
                for (BuyOrderEntity order : this.orders) {
                    this.money += order.getTotalAmount();
                }
            }
            moneyObserver.postValue(this.money);
        });
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
