package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentNetFeeBinding;
import com.kiegame.mobile.model.NetFeeModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.AddOrderEntity;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.ui.activity.MainActivity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.CouponSelect;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;
import com.youth.banner.loader.ImageLoader;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private UserInfoEntity userInfo;

    @Override
    protected int onLayout() {
        return R.layout.fragment_net_fee;
    }

    @Override
    protected void onObject() {
        model = new ViewModelProvider(this).get(NetFeeModel.class);
        binding.setModel(model);
        binding.setFragment(this);
        binding.setCache(Cache.ins());
        model.userSearch.observe(this, this::searchUserInfoList);
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
                    item = String.format("       %s | %s", entity.getIdCard(), entity.getCustomerName());
                } else {
                    item = String.format("%s | %s | %s", entity.getSeatNumber(), entity.getIdCard(), entity.getCustomerName());
                }
                tv.setText(item);
                tv.setOnClickListener(v -> {
                    userInfo = entity;
                    model.userSearch.setValue("");
                    hideInputMethod();
                    binding.etSearchInput.clearFocus();
                    Cache.ins().setUserName(tv.getText().toString());
                    model.amount.setValue(cal(entity.getAccountBalance()));
                    model.award.setValue(cal(entity.getBonusBalance()));
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
        if (this.moneyBtn != null) {
            this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_none_border);
        }
        this.model.recharge.setValue("0.00");
        model.resetData();
        this.userInfo = null;

        Cache.ins().setNetFee(0);
        Cache.ins().setUserName("没有选择会员");
    }

    /**
     * 充值金额
     *
     * @param view  选择金额按钮
     * @param money 金额数量
     */
    public void recharge(View view, int money) {
        String userNameValue = Cache.ins().getUserName();
        if (userNameValue == null || userNameValue.equals("没有选择会员")) {
            Toast.show("请先选择会员");
            return;
        }
        if (this.moneyBtn != null) {
            this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_none_border);
        }
        if (view.equals(this.moneyBtn)) {
            money = 0;
            this.moneyBtn = null;
        } else {
            this.moneyBtn = (TextView) view;
            this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_press_border);
        }
        this.model.recharge.setValue(String.format("%s.00", money));
        this.model.gabon.setValue(String.format("%s.00", money));

        // 此处扩大100倍为了总计时计算
        Cache.ins().setNetFee(money * 100);
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
        if (userInfo != null) {
            model.addOrder(
                    Cache.ins().getNetFeeNum(),
                    Cache.ins().getShopMoneyTotalNum(),
                    userInfo.getSeatNumber(),
                    userInfo.getCustomerId(),
                    userInfo.getBonusBalance(),
                    null,
                    null,
                    null,
                    4,
                    String.valueOf(Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum()),
                    null,
                    null,
                    2).observe(this, this::onTotalOrderResult);
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 下订单
     */
    public void createOrder() {
        if (userInfo != null) {
            model.addOrder(
                    Cache.ins().getNetFeeNum(),
                    Cache.ins().getShopMoneyTotalNum(),
                    userInfo.getSeatNumber(),
                    userInfo.getCustomerId(),
                    userInfo.getBonusBalance(),
                    null,
                    null,
                    null,
                    4,
                    String.valueOf(Cache.ins().getNetFeeNum() + Cache.ins().getShopMoneyTotalNum()),
                    null,
                    null,
                    1).observe(this, this::onCreateOrderResult);
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 优惠券
     */
    public void couponUse() {
        if (userInfo != null) {
            CouponSelect.ins().set().show();
        } else {
            Toast.show("请先选择会员");
        }
    }

    /**
     * 下订单返回处理
     */
    private void onCreateOrderResult(List<AddOrderEntity> data) {
        Toast.show("下单成功");
    }

    /**
     * 结算返回处理
     */
    private void onTotalOrderResult(List<AddOrderEntity> data) {

    }
}
