package com.kiegame.mobile.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewCouponUseBinding;
import com.kiegame.mobile.model.CouponModel;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/2/2.
 * Description: 优惠券选择框
 */
public class CouponSelect {

    private static CouponSelect INS;
    private ViewCouponUseBinding binding;
    private boolean isShowing;
    private OnCouponUseCallback callback;
    private CouponModel model;
    private LifecycleOwner owner;
    private List<ActivityEntity> serviceActivities;
    private List<ActivityEntity> customerCoupons;
    private BaseQuickAdapter<ActivityEntity, BaseViewHolder> adapter;
    private String customerId;
    private String productId;
    private boolean isSuccess;
    private int type;

    /**
     * 构造方法
     */
    private CouponSelect() {
        this.serviceActivities = new ArrayList<>();
        this.customerCoupons = new ArrayList<>();
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_coupon_use, null, false);
        this.initViews();
    }

    /**
     * 获取 {@link CouponSelect} 对象
     *
     * @return {@link CouponSelect}
     */
    public static CouponSelect ins() {
        if (CouponSelect.INS == null) {
            CouponSelect.INS = new CouponSelect();
        }
        return CouponSelect.INS;
    }

    /**
     * 初始化布局
     */
    private void initViews() {
        if (adapter == null) {
            initRecyclerView();
            binding.clRoot.setOnClickListener(v -> this.hide());
            binding.tvCloseBtn.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onCouponUse(null);
                }
                this.hide();
            });
            binding.tlCouponTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    adapter.setNewData(tab.getPosition() != 0 ? customerCoupons : serviceActivities);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    private void initRecyclerView() {
        adapter = new BaseQuickAdapter<ActivityEntity, BaseViewHolder>(R.layout.item_coupon) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ActivityEntity item) {
                if (item.getActivityType() == 1) {
                    helper.getView(R.id.tv_rmb).setVisibility(View.VISIBLE);
                    TextView price = helper.getView(R.id.tv_coupon_price);
                    price.setText(item.getActivityMoney());
                    price.setTextSize(30);
                } else {
                    helper.getView(R.id.tv_rmb).setVisibility(View.GONE);
                    TextView price = helper.getView(R.id.tv_coupon_price);
                    price.setText(item.getActivityRatioString());
                    price.setTextSize(18);
                }
                helper.getView(R.id.iv_coupon_image).setOnClickListener(v -> {
                    if (item.getIsBoolUse() != null && item.getIsBoolUse() == 2) {
                        Toast.show("该优惠券暂不可使用");
                        return;
                    }
                    if (callback != null) {
                        callback.onCouponUse(item);
                        hide();
                    }
                });
                helper.setText(R.id.tv_coupon_date, String.format("有效期%s-%s", cleanDate(item.getStartTime()), cleanDate(item.getEndTime())));
                helper.setText(R.id.tv_coupon_rule, item.getActivityName());
            }
        };
        adapter.setEmptyView(createEmptyView());
        this.binding.rvContent.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        this.binding.rvContent.setAdapter(adapter);
    }

    /**
     * 清除时间的时分秒部分
     *
     * @param datetime 时间日期
     * @return 返回日期 年/月/日
     */
    private String cleanDate(String datetime) {
        if (datetime != null) {
            String[] split = datetime.split(" ");
            if (split.length >= 2) {
                return split[0];
            }
        }
        return datetime;
    }

    @NotNull
    private TextView createEmptyView() {
        TextView view = new TextView(binding.getRoot().getContext());
        view.setText(R.string.app_no_coupons);
        view.setBackgroundResource(R.color.white);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    /**
     * 查询数据
     */
    private void query() {
        binding.clLoading.setVisibility(View.VISIBLE);
        LiveData<List<ActivityEntity>> service = model.queryServiceActivity(productId);
        if (!service.hasObservers()) {
            service.observe(owner, this::serviceResult);
        }
        LiveData<List<ActivityEntity>> customer = model.queryCustomerCoupons(customerId, productId);
        if (!customer.hasObservers()) {
            customer.observe(owner, this::customerResult);
        }
    }

    /**
     * 绑定生命周期
     */
    public CouponSelect bind(BaseActivity activity) {
        this.owner = activity;
        return this;
    }

    /**
     * 绑定生命周期
     */
    public CouponSelect bind(Fragment fragment) {
        this.owner = fragment;
        return this;
    }

    /**
     * 设置视图模型
     *
     * @param model {@link CouponModel}
     */
    public CouponSelect model(CouponModel model) {
        this.model = model;
        return this;
    }

    /**
     * 设置数据类型
     *
     * @param type 1: 网费 2: 商品
     * @return {@link CouponSelect}
     */
    public CouponSelect type(int type) {
        this.type = type;
        return this;
    }

    /**
     * 设置数据
     */
    public CouponSelect set(String customerId, String productId) {
        this.customerId = customerId;
        this.productId = productId;
        return this;
    }

    /**
     * 设置优惠券使用回调
     *
     * @param callback {@link OnCouponUseCallback}
     */
    public CouponSelect callback(OnCouponUseCallback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 显示优惠券
     */
    public void show() {
        if (!this.isShowing) {
            isShowing = true;
            InjectView.ins().inject(binding.getRoot());
            this.isSuccess = false;
            this.serviceActivities.clear();
            this.customerCoupons.clear();
            this.adapter.notifyDataSetChanged();
            binding.getRoot().postDelayed(() -> {
                selectTab();
                query();
            }, 200);
        }
    }

    private void selectTab() {
        TabLayout.Tab tab = binding.tlCouponTab.getTabAt(0);
        if (tab != null) {
            tab.select();
        }
    }

    /**
     * 隐藏优惠券
     */
    private void hide() {
        if (this.isShowing) {
            InjectView.ins().clean(binding.getRoot());
            isShowing = false;
        }
    }

    /**
     * 网费/商品优惠活动获取返回
     *
     * @param data 数据对象
     */
    private void serviceResult(List<ActivityEntity> data) {
        if (data != null && !data.isEmpty()) {
            for (ActivityEntity datum : data) {
                if (datum.getActivityType() == type) {
                    serviceActivities.add(datum);
                }
            }
        }
        adapter.setNewData(serviceActivities);
        if (isSuccess) {
            binding.clLoading.setVisibility(View.GONE);
        } else {
            isSuccess = true;
        }
    }

    /**
     * 用户门店优惠活动获取返回
     *
     * @param data 数据对象
     */
    private void customerResult(List<ActivityEntity> data) {
        if (data != null && !data.isEmpty()) {
            for (ActivityEntity datum : data) {
                if (datum.getActivityType() == type) {
                    customerCoupons.add(datum);
                }
            }
        }
        if (isSuccess) {
            binding.clLoading.setVisibility(View.GONE);
        } else {
            isSuccess = true;
        }
    }

    /**
     * 优惠券使用回调
     */
    public interface OnCouponUseCallback {

        /**
         * 优惠卷使用回调方法
         */
        void onCouponUse(ActivityEntity data);
    }
}
