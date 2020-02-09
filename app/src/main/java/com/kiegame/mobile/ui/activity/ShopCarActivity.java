package com.kiegame.mobile.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.consts.Payment;
import com.kiegame.mobile.databinding.ActivityShopCarBinding;
import com.kiegame.mobile.model.ShopCarModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.CouponSelect;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 确认订单
 */
public class ShopCarActivity extends BaseActivity<ActivityShopCarBinding> {

    private BaseQuickAdapter<BuyShop, BaseViewHolder> adapter;
    private UserInfoEntity userInfo;
    private ShopCarModel model;
    private PopupWindow pw;
    private LinearLayout list;
    private LayoutInflater inflater;
    private int orderType = -1;
    private final int RESULT_CODE_SCAN = 10086;

    @Override
    protected int onLayout() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(ShopCarModel.class);
        binding.setModel(model);
        binding.setActivity(this);
        binding.setCache(Cache.ins());
        userInfo = Cache.ins().getUserInfo();
        if (userInfo != null) {
            String item;
            if (Text.empty(userInfo.getSeatNumber())) {
                item = String.format("       %s | %s", Text.formatIdCardNum(userInfo.getIdCard()), Text.formatCustomName(userInfo.getCustomerName()));
            } else {
                item = String.format("%s | %s | %s", userInfo.getSeatNumber(), Text.formatIdCardNum(userInfo.getIdCard()), Text.formatCustomName(userInfo.getCustomerName()));
            }
            model.userName.setValue(item);
        }
        model.searchName.observe(this, this::searchUserInfoList);
    }

    @Override
    protected void onView() {
        setShopListVisible();
        adapter = new BaseQuickAdapter<BuyShop, BaseViewHolder>(R.layout.item_shop_car_shop, Cache.ins().getShops()) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, BuyShop item) {
                CheckBox cb = helper.getView(R.id.cb_shop_select);
                cb.setChecked(item.isBuy());
                cb.setOnClickListener(v -> {
                    cb.setChecked(!item.isBuy());
                    item.setBuy(!item.isBuy());
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
        binding.rvShopLayout.setLayoutManager(new LinearLayoutManager(this));
        binding.rvShopLayout.setAdapter(adapter);
    }

    @Override
    protected void onData() {

    }

    /**
     * 搜索用户信息列表
     *
     * @param keywords 关键字
     */
    private void searchUserInfoList(String keywords) {
        if (Text.empty(keywords) && pw != null && pw.isShowing()) {
            pw.dismiss();
        } else {
            LiveData<List<UserInfoEntity>> data = model.searchUserInfos(keywords);
            if (!data.hasObservers()) {
                data.observe(this, this::searchUserInfosResult);
            }
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
            pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.translucent)));
            pw.setOutsideTouchable(false);
            pw.setSplitTouchEnabled(true);
        }
        if (data != null && !data.isEmpty()) {
            list.removeAllViews();
            for (UserInfoEntity entity : data) {
                View view = inflater.inflate(R.layout.item_search_user_info, null, false);
                TextView tv = view.findViewById(R.id.tv_user_item);
                String item;
                if (Text.empty(entity.getSeatNumber())) {
                    item = String.format("       %s | %s", Text.formatIdCardNum(entity.getIdCard()), Text.formatCustomName(entity.getCustomerName()));
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
                        model.userName.setValue(name);
                    } else {
                        if (userInfo.getCustomerId().equals(Cache.ins().getUserInfo().getCustomerId())) {
                            StringBuilder sb = new StringBuilder();
                            if (Cache.ins().getNetFeeNum() == 0) {
                                sb.append(String.format("你想将会员账号切换到 %s 吗? 这将使你购买的商品也记录到 %s 的会员账号下",
                                        Text.formatCustomName(entity.getCustomerName()),
                                        Text.formatCustomName(entity.getCustomerName())));
                            } else {
                                sb.append(String.format("你想将会员账号切换到 %s 吗? 这将使你充值的网费和购买的商品也记录到 %s 的会员账号下",
                                        Text.formatCustomName(entity.getCustomerName()),
                                        Text.formatCustomName(entity.getCustomerName())));
                            }
                            DialogBox.ins().text(sb.toString()).confirm(() -> {
                                this.userInfo = entity;
                                model.userName.setValue(name);
                            }).cancel(null).show();
                        } else if (!userInfo.getCustomerId().equals(entity.getCustomerId())) {
                            DialogBox.ins().text(String.format("你想将会员账号切换为 %s 吗?", Text.formatCustomName(entity.getCustomerName()))).confirm(() -> {
                                this.userInfo = entity;
                                model.userName.setValue(name);
                            }).cancel(null).show();
                        }
                    }
                });
                list.addView(view);
            }
            if (!pw.isShowing()) {
                pw.showAsDropDown(binding.llSearch);
            }
        } else {
            if (pw.isShowing()) {
                pw.dismiss();
            }
        }
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
        if (data.getMax() <= 0) {
            Toast.show("这种商品已经售空了");
            return;
        }
        String size = tv.getText().toString();
        int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
        if (Cache.ins().shopTotal(data.getProductId()) > data.getMax()) {
            Toast.show("不能再多了");
        } else {
            less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setText(String.valueOf(num));
            Cache.ins().attachShop(data, data.getProductFlavorName(), data.getProductSpecName(), 1);
            List<ShopEntity> entities = Cache.ins().getEntities();
            for (ShopEntity buy : entities) {
                if (buy.getProductId().equals(data.getProductId())) {
                    buy.setBuySize(buy.getBuySize() + 1);
                    break;
                }
            }
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
            less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setText(num == 0 ? "" : String.valueOf(num));
            Cache.ins().detachShop(data.getProductId(), data.getProductFlavorName(), data.getProductSpecName());
            List<ShopEntity> entities = Cache.ins().getEntities();
            for (ShopEntity buy : entities) {
                if (buy.getProductId().equals(data.getProductId())) {
                    buy.setBuySize(buy.getBuySize() - 1);
                    break;
                }
            }
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
            CouponSelect.ins().set().show();
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 删除会员
     */
    public void deleteVipInfo() {
        DialogBox.ins().text(String.format("你想删除会员账号 %s 吗?", Text.formatCustomName(Cache.ins().getUserInfo().getCustomerName())))
                .confirm(() -> {
                    this.userInfo = null;
                    model.userName.setValue("没有选择会员");
                }).cancel(null).show();
    }

    /**
     * 去充值
     */
    public void toRecharge() {
        this.finish();
    }

    /**
     * 下订单
     */
    public void createOrder() {
        if (this.userInfo != null) {
            createOrderOrPayment(null, 1);
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 结算
     */
    public void totalOrder() {
        if (this.userInfo != null) {
            switch (Cache.ins().getPayment()) {
                case Payment.PAY_TYPE_ONLINE:
                    // 在线支付
                    startActivityForResult(new Intent(this, ScanActivity.class)
                                    .putExtra(Setting.APP_SCAN_TITLE,
                                            String.format("扫码支付%s元",
                                                    cal(Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum()))),
                            RESULT_CODE_SCAN);
                    break;
                case Payment.PAY_TYPE_BUCKLE:
                    // 卡扣支付
                    startActivityForResult(new Intent(this, ScanActivity.class)
                                    .putExtra(Setting.APP_SCAN_TITLE,
                                            String.format("卡扣支付%s元",
                                                    cal(Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum()))),
                            RESULT_CODE_SCAN);
                    break;
                case Payment.PAY_TYPE_SERVICE:
                    // 客维支付
                    // TODO: 2020/2/9 提示用户需要输入客维密码
                    break;
            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 创建订单或结算
     *
     * @param buyPayPassword 支付密码
     * @param isAddOrder     下单或结算 1: 下单 2: 结算
     */
    private void createOrderOrPayment(String buyPayPassword, int isAddOrder) {
        int totalMoney = Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum();
        if (totalMoney > 0) {
            this.orderType = isAddOrder;
            LiveData<List<AddOrderEntity>> order = model.addOrder(
                    Cache.ins().getNetFeeNum(),
                    Cache.ins().getShopMoneyTotalNum(),
                    userInfo.getSeatNumber(),
                    userInfo.getCustomerId(),
                    userInfo.getBonusBalance(),
                    null,
                    null,
                    null,
                    Cache.ins().getPayment(),
                    String.valueOf(totalMoney),
                    buyPayPassword,
                    isAddOrder);
            if (!order.hasObservers()) {
                order.observe(this, this::onCreateOrderResult);
            }
        } else {
            switch (isAddOrder) {
                case 1:
                    Toast.show("没有商品或网费可以下单");
                    break;
                case 2:
                    Toast.show("没有商品或网费可以结算");
                    break;
            }
        }
    }

    /**
     * 下订单返回处理
     */
    private void onCreateOrderResult(List<AddOrderEntity> data) {
        if (data != null) {
            this.adapter.notifyDataSetChanged();
            Cache.ins().getNetFeeObserver().setValue(this.orderType);
            if (orderType == 1) {
                // 下单
                Toast.show("下单成功");
            } else {
                // 结算
                // TODO: 2020/2/9 结算成功或失败的返回信息展示
            }
            this.finish();
        }
    }
}
