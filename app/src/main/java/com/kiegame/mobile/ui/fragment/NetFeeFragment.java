package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentNetFeeBinding;
import com.kiegame.mobile.model.CouponModel;
import com.kiegame.mobile.model.NetFeeModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.ui.activity.LoginActivity;
import com.kiegame.mobile.ui.activity.MainActivity;
import com.kiegame.mobile.ui.activity.ShopCarActivity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.CouponSelect;
import com.kiegame.mobile.utils.DialogBox;
import com.kiegame.mobile.utils.Menu;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;
import com.youth.banner.loader.ImageLoader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 网费充值
 */
public class NetFeeFragment extends BaseFragment<FragmentNetFeeBinding> {

    private NetFeeModel model;
    private TextView moneyBtn;
    private PopupWindow pw;
    private LinearLayout list;
    private LayoutInflater inflater;
    private Menu menu;
    private int pwHeight;
    private CouponModel couponModel;

    @Override
    protected int onLayout() {
        return R.layout.fragment_net_fee;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(NetFeeModel.class);
        couponModel = new ViewModelProvider(this).get(CouponModel.class);
        binding.setModel(model);
        binding.setFragment(this);
        binding.setCache(Cache.ins());
        pwHeight = (int) (Game.ins().metrics(true).heightPixels * 0.3f);
        model.userSearch.observe(this, this::searchUserInfoList);
        model.getFailMessage().observe(this, this::onCreateOrderFailure);
        menu = new Menu(getContext()).callback(v -> {
            Cache.ins().initialize();
            MainActivity activity = (MainActivity) getActivity();
            startActivity(new Intent(activity, LoginActivity.class));
            if (activity != null) {
                activity.finish();
            }
        });
        Cache.ins().getNetFeeObserver().observe(this, integer -> this.recharge(this.moneyBtn, 0));
    }

    @Override
    protected void onView() {
        binding.bnBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(imageView).load(((BannerEntity) path).getBannerUrl()).into(imageView);
            }
        });
    }

    @Override
    protected void onData() {
        model.queryBanner().observe(this, this::queryBannerResult);
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
     * banner数据处理
     *
     * @param data 数据对象
     */
    private void queryBannerResult(List<BannerEntity> data) {
        binding.bnBanner.setImages(data);
        binding.bnBanner.start();
    }

    /**
     * 查询用户数据返回处理
     *
     * @param data 数据对象
     */
    @SuppressLint("InflateParams")
    private void searchUserInfosResult(List<UserInfoEntity> data) {
        if (pw == null) {
            inflater = LayoutInflater.from(this.getContext());
            View view = inflater.inflate(R.layout.view_search_auto_fill, null, false);
            list = view.findViewById(R.id.ll_search_list);
            pw = new PopupWindow(this.getContext());
            pw.setContentView(view);
            pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pw.setHeight(pwHeight);
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
                    item = String.format("%s | %s", Text.formatIdCardNum(entity.getIdCard()), Text.formatCustomName(entity.getCustomerName()));
                } else {
                    item = String.format("%s | %s | %s", entity.getSeatNumber(), Text.formatIdCardNum(entity.getIdCard()), Text.formatCustomName(entity.getCustomerName()));
                }
                tv.setText(item);
                tv.setOnClickListener(v -> {
                    String name = tv.getText().toString();
                    model.userSearch.setValue("");
                    hideInputMethod();
                    binding.etSearchInput.clearFocus();
                    if (Cache.ins().getUserInfo() == null) {
                        changeUserInfo(entity, name);
                    } else {
                        if (!Cache.ins().getUserInfo().getCustomerId().equals(entity.getCustomerId())) {
                            DialogBox.ins().text(String.format("你想将会员账号切换为 %s 吗?", Text.formatCustomName(entity.getCustomerName())))
                                    .confirm(() -> changeUserInfo(entity, name))
                                    .cancel(null)
                                    .show();
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
     * 切换会员信息
     *
     * @param entity 会员信息数据对象
     * @param name   会员 机号/身份证号/名称
     */
    private void changeUserInfo(UserInfoEntity entity, String name) {
        Cache.ins().setUserInfo(entity);
        Cache.ins().setUserName(name);
        model.amount.setValue(cal(entity.getAccountBalance()));
        model.award.setValue(cal(entity.getBonusBalance()));
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
     * 隐藏输入法
     */
    private void hideInputMethod() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                View focus = activity.getCurrentFocus();
                if (focus != null) {
                    inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 删除会员
     */
    public void deleteVipInfo() {
        DialogBox.ins().text(String.format("你想删除会员账号 %s 吗?", Text.formatCustomName(Cache.ins().getUserInfo().getCustomerName())))
                .confirm(() -> {
                    if (this.moneyBtn != null) {
                        this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_none_border);
                    }
                    this.model.recharge.setValue("0.00");
                    model.resetData();

                    Cache.ins().setUserInfo(null);
                    Cache.ins().setNetFee(0);
                    Cache.ins().setUserName("没有选择会员");
                }).cancel(null).show();
    }

    /**
     * 充值金额
     *
     * @param view  选择金额按钮
     * @param money 金额数量
     */
    public void recharge(View view, int money) {
//        String userNameValue = Cache.ins().getUserName();
//        if (userNameValue == null || userNameValue.equals("没有选择会员")) {
//            Toast.show("请先选择会员");
//            return;
//        }
        if (this.moneyBtn != null) {
            this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_none_border);
            this.moneyBtn.setTextColor(getResources().getColor(R.color.gray_white));
        }
        if (money == 0 || (view != null && view.equals(this.moneyBtn))) {
            money = 0;
            this.moneyBtn = null;
        } else {
            if (view != null) {
                this.moneyBtn = (TextView) view;
                this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_press_border);
                this.moneyBtn.setTextColor(getResources().getColor(R.color.black_text));
            }
        }
        this.model.recharge.setValue(String.format("%s.00", money));
        this.model.gabon.setValue(String.format("%s.00", money));
        this.model.bonus.setValue("0.00");

        Cache.ins().setNetFeeCoupon(null);
        // 此处扩大100倍为了总计时计算
        Cache.ins().setNetFee(money * 100);
    }

    /**
     * 显示菜单
     */
    public void showMenu() {
        menu.show(binding.tvServiceId);
    }

    /**
     * 去购物
     */
    public void goShopping() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setCurrentPage(1);
        }
    }

    /**
     * 结算
     */
    public void totalShop() {
        if (Cache.ins().getUserInfo() != null) {
            if (canCreateOrderOrPayment(2)) {
                startActivity(new Intent(getActivity(), ShopCarActivity.class));
            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 下订单
     */
    public void createOrder() {
        if (Cache.ins().getUserInfo() != null) {
            if (canCreateOrderOrPayment(1)) {
                ActivityEntity coupon = Cache.ins().getNetFeeCoupon();
                int totalMoney = Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum();
                LiveData<List<AddOrderEntity>> order = model.addOrder(
                        Cache.ins().getNetFeeNum(),
                        Cache.ins().getShopMoneyTotalNum(),
                        Cache.ins().getUserInfo().getSeatNumber(),
                        Cache.ins().getUserInfo().getCustomerId(),
                        Cache.ins().getUserInfo().getBonusBalance(),
                        coupon == null ? null : coupon.getActivityType(),
                        coupon == null ? null : (coupon.getActivityCardResultId() == null ? coupon.getActivityId() : coupon.getActivityCardResultId()),
                        coupon == null ? null : new BigDecimal(coupon.getActivityMoney()).multiply(new BigDecimal("100")).intValue(),
                        Cache.ins().getPayment(),
                        String.valueOf(totalMoney),
                        null,
                        null,
                        1);
                if (!order.hasObservers()) {
                    order.observe(this, this::onCreateOrderResult);
                }
            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 是否可以下单
     *
     * @return true: 可以 false: 不可以
     */
    private boolean canCreateOrderOrPayment(int orderType) {
        int money = Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum();
        if (money <= 0) {
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
        return money > 0;
    }

    /**
     * 优惠券
     */
    public void couponUse() {
        if (Cache.ins().getUserInfo() != null) {
            if (canCreateOrderOrPayment(3)) {
                CouponSelect.ins()
                        .bind(this)
                        .model(couponModel)
                        .set(Cache.ins().getUserInfo().getCustomerId(), null)
                        .type(1)
                        .callback(data -> {
                            if (data != null) {
                                if (data.getActivityType() == 1) {
                                    if (Cache.ins().getNetFeeNum() < data.getActivityMoneyMax()) {
                                        Toast.show("当前交易金额无法使用此优惠券");
                                        return;
                                    }
                                    this.model.bonus.setValue(String.format("%s.00", data.getActivityMoney()));
                                } else {
                                    Toast.show("当前交易无法使用此优惠券");
                                    return;
                                }
                            } else {
                                this.model.bonus.setValue("0.00");
                            }
                            Cache.ins().setNetFeeCoupon(data);
                        })
                        .show();
            }
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 下订单返回处理
     */
    private void onCreateOrderResult(List<AddOrderEntity> data) {
        if (data != null) {
            this.recharge(this.moneyBtn, 0);
            resetData();
            Cache.ins().getOrderObserver().setValue(data.size());
            Toast.show("下单成功");
        } else {
            Toast.show("下单失败");
        }
    }

    /**
     * 下单失败
     *
     * @param msg 失败消息
     */
    private void onCreateOrderFailure(String msg) {
        Toast.show(msg);
    }

    /**
     * 重置商品数据
     */
    private void resetData() {
        Cache.ins().setNetFeeCoupon(null);
        Cache.ins().setProductCoupon(null);
        // 重置金额选择
        if (this.moneyBtn != null) {
            this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_none_border);
        }
        this.model.recharge.setValue("0.00");
        this.model.bonus.setValue("0.00");
        model.resetData();
        // 重置会员信息
        Cache.ins().setUserInfo(null);
        Cache.ins().setNetFee(0);
        Cache.ins().setUserName("没有选择会员");
        // 重置商品数据
        List<BuyShop> shops = Cache.ins().getShops();
        if (shops != null && !shops.isEmpty()) {
            List<BuyShop> buys = new ArrayList<>();
            for (BuyShop shop : shops) {
                if (shop != null && shop.isBuy()) {
                    buys.add(shop);
                }
            }
            List<ShopEntity> entities = Cache.ins().getEntities();
            for (BuyShop buy : buys) {
                if (entities != null && !entities.isEmpty()) {
                    for (ShopEntity shop : entities) {
                        if (shop != null && shop.getProductId().equals(buy.getProductId())) {
                            shop.setBuySize(shop.getBuySize() - buy.getProductBuySum());
                            break;
                        }
                    }
                }
                shops.remove(buy);
            }
            Cache.ins().setShops(shops);
            Cache.ins().getShopObserver().setValue(buys.size());
        }
    }
}
