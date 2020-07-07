package com.kiegame.mobile.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.consts.Payment;
import com.kiegame.mobile.databinding.ActivityShopCarBinding;
import com.kiegame.mobile.model.CouponModel;
import com.kiegame.mobile.model.ShopCarModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.PayResultEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.CouponShopSelect;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.PayFailure;
import com.kiegame.mobile.utils.PaySuccess;
import com.kiegame.mobile.utils.ServicePay;
import com.kiegame.mobile.utils.SoftInputAdapter;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 确认订单
 */
public class ShopCarActivity extends BaseActivity<ActivityShopCarBinding> {

    private BaseItemDraggableAdapter<BuyShop, BaseViewHolder> adapter;
    private UserInfoEntity userInfo;
    private ShopCarModel model;
    private PopupWindow pw;
    private LinearLayout list;
    private LayoutInflater inflater;
    private int orderType = -1;
    private final int RESULT_CODE_SCAN = 10086;
    private String[] permissions;
    private int pwHeight;
    private CouponModel couponModel;
    private BuyShop delete;

    @Override
    protected int onLayout() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(ShopCarModel.class);
        couponModel = new ViewModelProvider(this).get(CouponModel.class);
        binding.setModel(model);
        binding.setActivity(this);
        binding.setCache(Cache.ins());
        userInfo = Cache.ins().getTempInfo() == null ? Cache.ins().getUserInfo() : Cache.ins().getTempInfo();
        pwHeight = (int) (Game.ins().metrics(true).heightPixels * 0.3f);
        if (userInfo != null) {
            String item;
            if (Text.empty(userInfo.getSeatNumber())) {
                item = String.format("%s | %s", Text.formatIdCardNum(userInfo.getIdCard()), Text.formatCustomName(userInfo.getCustomerName()));
            } else {
                item = String.format("%s | %s | %s", userInfo.getSeatNumber(), Text.formatIdCardNum(userInfo.getIdCard()), Text.formatCustomName(userInfo.getCustomerName()));
            }
            model.userName.setValue(Text.zoomCustomInfo(item));
        } else {
            Cache.ins().setProductCoupon(null, null);
        }
        model.searchName.observe(this, keywords -> {
            if (Text.empty(keywords)) {
                if (pw != null && pw.isShowing()) {
                    pw.dismiss();
                }
            }
        });
        model.getFailMessage().observe(this, this::onCreateOrderFailure);
        permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE,
        };
    }

    @Override
    protected void onView() {
        // #793 填写备注，键盘挡住了订单信息
        SoftInputAdapter.assistActivity(this, true);
        setShopListVisible();
        adapter = new BaseItemDraggableAdapter<BuyShop, BaseViewHolder>(R.layout.item_shop_car_shop, Cache.ins().getShops()) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, BuyShop item) {
                CheckBox cb = helper.getView(R.id.cb_shop_select);
                cb.setChecked(item.isBuy());
                cb.setOnClickListener(v -> {
                    cb.setChecked(!item.isBuy());
                    item.setBuy(!item.isBuy());
                    Cache.ins().setProductCoupon(null, null);
                    Cache.ins().updateTotalMoney();
                    Cache.ins().notifyChange();
                });
                helper.setVisible(R.id.iv_btn_less, item.getProductBuySum() != 0);
                helper.setVisible(R.id.tv_shop_num, item.getProductBuySum() != 0);
                Glide.with(helper.itemView).load(item.getShopImage()).into((ImageView) helper.getView(R.id.iv_shop_image));
                helper.setText(R.id.tv_shop_name, item.getShopName());
                if (!Text.empty(item.getProductFlavorName()) && !Text.empty(item.getProductSpecName())) {
                    helper.setText(R.id.tv_shop_des, String.format("%s/%s", item.getProductSpecName(), item.getProductFlavorName()));
                } else {
                    helper.setText(R.id.tv_shop_des, Text.empty(item.getProductSpecName()) ? item.getProductFlavorName() : item.getProductSpecName());
                }
                helper.setText(R.id.tv_shop_price, getString(R.string.net_fee_use_coupon_format, cal(item.getFee())));
                helper.setText(R.id.tv_shop_num, String.valueOf(item.getProductBuySum()));
                ImageView plus = helper.getView(R.id.iv_btn_plus);
                ImageView less = helper.getView(R.id.iv_btn_less);
                TextView tv = helper.getView(R.id.tv_shop_num);
                plus.setOnClickListener(v -> plus(less, tv, item));
                less.setOnClickListener(v -> less(less, tv, item));
            }
        };
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {
                List<BuyShop> shops = Cache.ins().getShops();
                if (shops != null) {
                    delete = shops.get(i);
                }
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                if (delete != null) {
                    Cache.ins().setShopSumById(delete.getProductId(), Cache.ins().getShopSumById(delete.getProductId()) - delete.getProductBuySum());
                }
                Cache.ins().setProductCoupon(null, null);
                Cache.ins().updateTotalMoney();
                Cache.ins().notifyChange();
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(adapter));
        itemTouchHelper.attachToRecyclerView(binding.rvShopLayout);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(onItemSwipeListener);
        binding.rvShopLayout.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        binding.rvShopLayout.setAdapter(adapter);
        binding.etSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String value = model.searchName.getValue();
                if (!Text.empty(value)) {
                    searchUserInfoList(value);
                    return true;
                } else {
                    Toast.show(R.string.net_fee_search_input_hint);
                    return false;
                }
            }
            return false;
        });
    }

    @Override
    protected void onData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Cache.ins().getUserInfo() == null) {
            Cache.ins().setProductCoupon(null, null);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (CouponShopSelect.ins().isShowing()) {
                CouponShopSelect.ins().hide();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 搜索用户信息列表
     *
     * @param keywords 关键字
     */
    private void searchUserInfoList(String keywords) {
        if (Text.empty(keywords)) {
            if (pw != null && pw.isShowing()) {
                pw.dismiss();
            }
            return;
        }
        LiveData<List<UserInfoEntity>> data = model.searchUserInfos(keywords);
        if (!data.hasObservers()) {
            data.observe(this, this::searchUserInfosResult);
        }
    }

    /**
     * 查询用户数据返回处理
     *
     * @param data 数据对象
     */
    @SuppressLint("InflateParams")
    private void searchUserInfosResult(List<UserInfoEntity> data) {
        if (pw == null) {
            inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.view_search_auto_fill, null, false);
            list = view.findViewById(R.id.ll_search_list);
            pw = new PopupWindow(this);
            pw.setContentView(view);
            pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pw.setHeight(pwHeight);
            pw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.translucent)));
            pw.setOutsideTouchable(false);
            pw.setSplitTouchEnabled(true);
        }
        list.removeAllViews();
        if (data != null && !data.isEmpty()) {
            for (UserInfoEntity entity : data) {
                View view = inflater.inflate(R.layout.item_search_user_info, null, false);
                TextView tv = view.findViewById(R.id.tv_user_item);
                String item;
                if (Text.empty(entity.getSeatNumber())) {
                    item = String.format("%s | %s", Text.formatIdCardNum(entity.getIdCard()), Text.formatCustomName(entity.getCustomerName()));
                } else {
                    item = String.format("%s | %s | %s", entity.getSeatNumber(), Text.formatIdCardNum(entity.getIdCard()), Text.formatCustomName(entity.getCustomerName()));
                }
                tv.setText(item);
                tv.setOnClickListener(v -> {
                    String name = tv.getText().toString();
                    model.searchName.setValue("");
                    hideInputMethod();
                    binding.etSearchInput.clearFocus();
                    if (Cache.ins().getUserInfo() == null) {
                        this.userInfo = entity;
                        Cache.ins().setTempInfo(this.userInfo);
                        Cache.ins().setProductCoupon(null, null);
                        model.userName.setValue(name);
                    } else {
                        // #785 确认商品订单页面，删除已有会员，重新选择会员，系统闪退
                        if (userInfo == null) {
                            this.userInfo = entity;
                            Cache.ins().setTempInfo(this.userInfo);
                            model.userName.setValue(name);
                        } else {
                            if (!userInfo.getCustomerId().equals(entity.getCustomerId())) {
                                DialogBox.ins().text(String.format("你想将会员账号切换为 %s 吗?", Text.formatCustomName(entity.getCustomerName()))).confirm(() -> {
                                    this.userInfo = entity;
                                    Cache.ins().setTempInfo(this.userInfo);
                                    model.userName.setValue(name);
                                    Cache.ins().setProductCoupon(null, null);
                                }).cancel(null).show();
                            }
//                            if (!entity.getCustomerId().equals(Cache.ins().getUserInfo().getCustomerId())) {
//                                StringBuilder sb = new StringBuilder();
//                                if (Cache.ins().getNetFeeNum() == 0) {
//                                    sb.append(String.format("你想将会员账号切换到 %s 吗? 这将使你购买的商品也记录到 %s 的会员账号下",
//                                            Text.formatCustomName(entity.getCustomerName()),
//                                            Text.formatCustomName(entity.getCustomerName())));
//                                } else {
//                                    sb.append(String.format("你想将会员账号切换到 %s 吗? 这将使你充值的网费和购买的商品也记录到 %s 的会员账号下",
//                                            Text.formatCustomName(entity.getCustomerName()),
//                                            Text.formatCustomName(entity.getCustomerName())));
//                                }
//                                DialogBox.ins().text(sb.toString()).confirm(() -> {
//                                    this.userInfo = entity;
//                                    model.userName.setValue(name);
//                                }).cancel(null).show();
//                            }
                        }
                    }
                });
                list.addView(view);
            }
        } else {
            if (list.getChildCount() == 0) {
                View view = inflater.inflate(R.layout.item_search_user_info, null, false);
                TextView tv = view.findViewById(R.id.tv_user_item);
                tv.setText("暂无信息");
                list.addView(view);
            }
        }
        if (pw.isShowing()) {
            pw.dismiss();
        }
        pw.showAsDropDown(binding.llSearch);
    }

    /**
     * 隐藏输入法
     */
    private void hideInputMethod() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View focus = this.getCurrentFocus();
            if (focus != null) {
                inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 设置商品列表显示或隐藏
     */
    private void setShopListVisible() {
        if (Cache.ins().getShopSum() <= 0) {
            binding.rvShopLayout.setVisibility(View.GONE);
            binding.vvDividerLineStart.setVisibility(View.GONE);
            binding.llShopTotal.setVisibility(View.GONE);
        } else {
            binding.rvShopLayout.setVisibility(View.VISIBLE);
            binding.vvDividerLineStart.setVisibility(View.VISIBLE);
            binding.llShopTotal.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 增加
     */
    public void plus(ImageView less, TextView tv, BuyShop data) {
        if (data.getShopType() == 1 && data.getMax() <= 0) {
            Toast.show("这种商品已经售空了");
            return;
        }
        String size = tv.getText().toString();
        int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
        // #788 购物车中添加商品可添加一份额外的商品
        if (data.getShopType() == 1 && Cache.ins().getShopSumById(data.getProductId()) >= data.getMax()) {
            Toast.show("不能再多了");
        } else {
//            /* --------- 新增自制商品库存判断 -------- */
//            if (data.getShopType() == 2) {
//                LiveData<Object> stock = model.queryProductStock(data.getProductId(), num);
//                if (!stock.hasObservers()) {
//                    stock.observe(this, o -> {
//                        Cache.ins().setProductCoupon(null, null);
//                        less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
//                        tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
//                        tv.setText(String.valueOf(num));
//                        Cache.ins().attachShop(data, data.getProductFlavorName(), data.getProductSpecName(), 1);
//                    });
//                }
//                /* --------- 新增自制商品库存判断 -------- */
//            } else {
            Cache.ins().setProductCoupon(null, null);
            less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setText(String.valueOf(num));
            Cache.ins().attachShop(data, data.getProductFlavorName(), data.getProductSpecName(), 1);
//            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 减少
     */
    public void less(ImageView less, TextView tv, BuyShop data) {
        String size = tv.getText().toString();
        int num = Text.empty(size) ? -1 : Integer.parseInt(size) - 1;
        if (num < 0) {
            Toast.show("不能再少了");
        } else {
            Cache.ins().setProductCoupon(null, null);
            less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setText(num == 0 ? "" : String.valueOf(num));
            Cache.ins().detachShop(data.getProductId(), data.getProductFlavorName(), data.getProductSpecName());
        }
        adapter.notifyDataSetChanged();
        if (Cache.ins().getShopSum() <= 0) {
            setShopListVisible();
        }
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

    /**
     * 使用优惠券
     */
    public void couponUse() {
        if (this.userInfo != null) {
//            if (canCreateOrderOrPayment(3)) {
            CouponShopSelect.ins()
                    .bind(this)
                    .model(couponModel)
                    .set(userInfo.getCustomerId(), this.getProductIds())
                    .type(2)
                    .callback((service, customer) -> {
//                        if (data != null && !data.isEmpty()) {
//                            if (data.getActivityType() != 2) {
//                                Toast.show("当前交易不支持此优惠券");
//                                return;
//                            }
//                            if (!Cache.ins().hasShop(data.getProductId())) {
//                                Toast.show("购物车中没有商品可使用此优惠券");
//                                return;
//                            }
//                        }
//                        Cache.ins().setProductCoupon(service, customer);
                    })
                    .show();
//            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 获取商品列表的ID,以逗号间隔
     *
     * @return 返回商品列表的ID字符串或null
     */
    private String getProductIds() {
        List<BuyShop> shops = Cache.ins().getShops();
        if (shops != null && !shops.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (BuyShop shop : shops) {
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(shop.getProductId());
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 删除会员
     */
    public void deleteVipInfo() {
//        DialogBox.ins().text(String.format("你想删除会员账号 %s 吗?", Text.formatCustomName(userInfo.getCustomerName())))
//                .confirm(() -> {
        this.userInfo = null;
        Cache.ins().setTempInfo(this.userInfo);
        model.userName.setValue("没有选择会员");
//                }).cancel(null).show();
    }

    /**
     * 去充值
     */
    public void toRecharge() {
        String name;
        if (Text.empty(this.userInfo.getSeatNumber())) {
            name = String.format("%s | %s", Text.formatIdCardNum(this.userInfo.getIdCard()), Text.formatCustomName(this.userInfo.getCustomerName()));
        } else {
            name = String.format("%s | %s | %s", this.userInfo.getSeatNumber(), Text.formatIdCardNum(this.userInfo.getIdCard()), Text.formatCustomName(this.userInfo.getCustomerName()));
        }
        Cache.ins().setUserInfo(this.userInfo);
        Cache.ins().getUserInfoObserver().setValue(name);
        Cache.ins().getMainPageObserver().setValue(0);
        this.finish();
    }

    /**
     * 下订单
     */
    public void createOrder() {
        if (this.userInfo != null) {
            if (canCreateOrderOrPayment(1)) {
                createOrderOrPayment(null, 1);
            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 结算
     */
    public void totalOrder() {
        if (this.userInfo != null) {
            if (canCreateOrderOrPayment(2)) {
                int payment = Cache.ins().getPayment();
                if (payment == Payment.PAY_TYPE_SERVICE) {
                    // 客维支付
                    int totalMoney = Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum();
                    ServicePay.ins().money(totalMoney).confirm(password -> createOrderOrPayment(password, 2)).show();
                } else {
                    startScanActivity(payment);
                }
            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String password = data.getStringExtra(Setting.APP_SCAN_CONTENT);
            if (!Text.empty(password)) {
                createOrderOrPayment(password, 2);
            }
        }
    }

    /**
     * 跳转到扫码界面
     *
     * @param payType 支付类型
     */
    private void startScanActivity(int payType) {
        requestSelfPermission(permissions, (authorize, permissions1) -> {
            if (authorize) {
                String value;
                String money = cal(Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum());
                if (payType == Payment.PAY_TYPE_BUCKLE) {
                    value = String.format("卡扣支付%s元", money);
                } else if (payType == Payment.PAY_TYPE_ONLINE) {
                    value = String.format("扫码支付%s元", money);
                } else {
                    value = "扫码支付";
                }
                startActivityForResult(new Intent(this, ScanActivity.class)
                                .putExtra(Setting.APP_SCAN_TITLE, value),
                        RESULT_CODE_SCAN);
            } else {
                Toast.show("相机权限授权失败");
            }
        });
    }

    /**
     * 创建订单或结算
     *
     * @param buyPayPassword 支付密码
     * @param isAddOrder     下单或结算 1: 下单 2: 结算
     */
    private void createOrderOrPayment(String buyPayPassword, int isAddOrder) {
        if (canCreateOrderOrPayment(isAddOrder)) {
            ActivityEntity coupon = Cache.ins().getNetFeeCoupon();
            int totalMoney = Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum();
            this.orderType = isAddOrder;
            LiveData<List<AddOrderEntity>> order = model.addOrder(
                    Cache.ins().getNetFeeNum(),
                    Cache.ins().getShopMoneyTotalNum(),
                    userInfo.getSeatNumber(),
                    userInfo.getCustomerId(),
                    userInfo.getBonusBalance(),
                    coupon == null ? null : coupon.getDiscountType(),
                    coupon == null ? null : (coupon.getDiscountType() == 1 ? coupon.getActivityId() : coupon.getActivityCardResultId()),
                    coupon == null ? null : new BigDecimal(coupon.getActivityMoney()).multiply(new BigDecimal("100")).intValue(),
                    Cache.ins().getPayment(),
                    String.valueOf(totalMoney),
                    buyPayPassword,
                    isAddOrder,
                    Cache.ins().getProtectService(),
                    Cache.ins().getProtectCustomer());
            if (!order.hasObservers()) {
                order.observe(this, this::onCreateOrderResult);
            }
        }
    }

    /**
     * 是否可以下单
     *
     * @return true: 可以 false: 不可以
     */
    private boolean canCreateOrderOrPayment(int orderType) {
        int money = Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum();
        if (money <= 0 && Cache.ins().getProductCoupon() == null && Cache.ins().getNetFeeCoupon() == null) {
            switch (orderType) {
                case 1:
                    Toast.show("没有商品或网费可以下单");
                    break;
                case 2:
                    Toast.show("没有商品或网费可以结算");
                    break;
                case 3:
                    Toast.show("没有商品或网费可以使用优惠券");
                    break;
            }
        }
        return money > 0 || Cache.ins().getProductCoupon() != null || Cache.ins().getNetFeeCoupon() != null;
    }

    /**
     * 下订单返回处理
     */
    private void onCreateOrderResult(List<AddOrderEntity> data) {
        if (data != null) {
            if (orderType == 1) {
                // 下单
                Toast.show("下单成功");
                resetShopData();
                this.finish();
            } else {
                // 结算
                AddOrderEntity order = data.get(0);
                if (order != null) {
                    resetShopData();
                    if (Cache.ins().getPayment() == Payment.PAY_TYPE_ONLINE) {
                        queryPayResult(order.getPaymentPayId());
                    } else {
                        PaySuccess.ins().confirm(this::finish).order(order.getPaymentPayId()).show();
                    }
                }
            }
        } else {
            if (orderType == 1) {
                // 下单
                Toast.show("下单失败");
            } else {
                // 结算
                PayFailure.ins().message("未找到订单号").show();
            }
        }
    }

    /**
     * 查询支付结果
     *
     * @param payId 支付ID
     */
    private void queryPayResult(String payId) {
        LiveData<List<PayResultEntity>> liveData = model.queryPayState(payId);
        if (!liveData.hasObservers()) {
            liveData.observe(this, payResultEntities -> {
                if (payResultEntities != null) {
                    PayResultEntity res = payResultEntities.get(0);
                    if (res.getPayState() == 2) {
                        PaySuccess.ins().confirm(this::finish).order(res.getPaymentPayId()).show();
                    } else if (res.getPayState() == 4) {
                        PayFailure.ins().message("支付失败").show();
                    } else {
                        queryPayResult(res.getPaymentPayId());
                    }
                }
            });
        }
    }

    /**
     * 错误消息
     *
     * @param msg 消息内容
     */
    private void onCreateOrderFailure(String msg) {
        if (orderType != 1) {
            PayFailure.ins().message(msg).confirm(() -> {
                resetShopData();
                Cache.ins().getMainPageObserver().setValue(2);
                Cache.ins().getOrderObserver().setValue(1);
                finish();
            }).show();
        } else {
            Toast.show(msg);
        }
    }

    /**
     * 重置商品数据
     */
    private void resetShopData() {
        Cache.ins().setNetFeeCoupon(null);
        Cache.ins().setProductCoupon(null, null);
        Cache.ins().setTempInfo(null);
        Cache.ins().setProtectService(null);
        Cache.ins().setProtectCustomer(null);
        Cache.ins().setPayment(Payment.PAY_TYPE_ONLINE);
        List<BuyShop> shops = Cache.ins().getShops();
        if (shops != null && !shops.isEmpty()) {
            List<BuyShop> buys = new ArrayList<>();
            for (BuyShop shop : shops) {
                if (shop != null && shop.isBuy()) {
                    buys.add(shop);
                }
            }
            for (BuyShop buy : buys) {
                Cache.ins().setShopSumById(buy.getProductId(), Cache.ins().getShopSumById(buy.getProductId()) - buy.getProductBuySum());
                shops.remove(buy);
            }
            Cache.ins().setShops(shops);
            Cache.ins().getShopObserver().setValue(buys.size());
        }
        this.adapter.notifyDataSetChanged();
        Cache.ins().getNetFeeObserver().setValue(this.orderType);
        Cache.ins().getOrderObserver().setValue(this.orderType);
    }
}
