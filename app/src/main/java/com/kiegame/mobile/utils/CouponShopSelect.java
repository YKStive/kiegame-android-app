package com.kiegame.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import com.kiegame.mobile.databinding.ViewCouponShopUseBinding;
import com.kiegame.mobile.model.CouponModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ActivityEntity;
import com.kiegame.mobile.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/2/2.
 * Description: 优惠券选择框
 */
public class CouponShopSelect {

    private static CouponShopSelect INS;
    private ViewCouponShopUseBinding binding;
    private ValueAnimator alphaAnimator;
    private ValueAnimator moveAnimator;
    private boolean isShowing;
    private float moveSize;
    private OnCouponShopUseCallback callback;
    private CouponModel model;
    private LifecycleOwner owner;
    private List<ActivityEntity> serviceActivities;
    private List<ActivityEntity> customerCoupons;
    private BaseQuickAdapter<ActivityEntity, BaseViewHolder> adapter;
    private String customerId;
    private String productId;
    private int index;
    private boolean isSuccess;
    private int type;

    /**
     * 构造方法
     */
    private CouponShopSelect() {
        this.serviceActivities = new ArrayList<>();
        this.customerCoupons = new ArrayList<>();
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_coupon_shop_use, null, false);
        this.moveSize = Game.ins().metrics(true).heightPixels * 0.45f;
        this.initAnim();
        this.initViews();
    }

    /**
     * 获取 {@link CouponShopSelect} 对象
     *
     * @return {@link CouponShopSelect}
     */
    public static CouponShopSelect ins() {
        if (CouponShopSelect.INS == null) {
            CouponShopSelect.INS = new CouponShopSelect();
        }
        return CouponShopSelect.INS;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        this.alphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.alphaAnimator.setDuration(150);
        this.alphaAnimator.addUpdateListener(animation -> {
            if (this.binding != null) {
                this.binding.vvBackground.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        this.alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    InjectView.ins().clean(binding.getRoot());
                    isShowing = false;
                }
            }
        });
        this.moveAnimator = ValueAnimator.ofFloat(moveSize, 0.0f);
        this.moveAnimator.setDuration(150);
        this.moveAnimator.addUpdateListener(animation -> {
            if (this.binding != null) {
                this.binding.clContent.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        this.moveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                if (!isReverse) {
                    InjectView.ins().inject(binding.getRoot());
                    isShowing = true;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (isReverse) {
                    if (alphaAnimator != null) {
                        alphaAnimator.reverse();
                    }
                } else {
                    if (alphaAnimator != null) {
                        alphaAnimator.start();
                    }
                    query();
                }
            }
        });
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
                    callback.onCouponUse(null, null);
                }
                this.hide();
            });
            binding.tvOkBtn.setOnClickListener(v -> {
//                List<ActivityEntity> service = new ArrayList<>();
//                List<ActivityEntity> customer = new ArrayList<>();
//                for (ActivityEntity entity : serviceActivities) {
//                    if (entity.isSelect()) {
//                        service.add(entity);
//                    }
//                }
//                for (ActivityEntity coupon : customerCoupons) {
//                    if (coupon.isSelect()) {
//                        customer.add(coupon);
//                    }
//                }
//                if (callback != null) {
//                    callback.onCouponUse(service, customer);
//                }
                hide();
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
        adapter = new BaseQuickAdapter<ActivityEntity, BaseViewHolder>(R.layout.item_coupon_shop) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ActivityEntity item) {
                helper.setVisible(R.id.iv_coupon_image_select, item.isSelect());
                helper.setVisible(R.id.iv_coupon_image_select_fg, item.isSelect());
//                if (item.getActivityType() == 1) {
//                    helper.getView(R.id.tv_rmb).setVisibility(View.VISIBLE);
//                    TextView price = helper.getView(R.id.tv_coupon_price);
//                    price.setText(item.getActivityMoney());
//                    price.setTextSize(30);
//                } else {
//                    helper.getView(R.id.tv_rmb).setVisibility(View.GONE);
//                    TextView price = helper.getView(R.id.tv_coupon_price);
//                    price.setText(item.getActivityRatioString());
//                    price.setTextSize(18);
//                }
                helper.getView(R.id.iv_coupon_image).setOnClickListener(v -> {
                    if (item.getIsBoolUse() != null && item.getIsBoolUse() == 2) {
                        Toast.show("该优惠券暂不可使用");
                        return;
                    }
                    if (!Cache.ins().hasShop(item.getProductId())) {
                        Toast.show("购物车中没有商品可使用此优惠券");
                        return;
                    }
                    item.setSelect(!item.isSelect());
                    // 记录选择顺序
                    if (item.isSelect()) {
                        item.setIndex(index);
                        index++;
                    }
                    updateCoupons();
                    notifyCoupons();
                    adapter.notifyDataSetChanged();
                });
                helper.setText(R.id.tv_coupon_date, "有效期: ");
                helper.setText(R.id.tv_coupon_date_start, item.getStartTime());
                helper.setText(R.id.tv_coupon_date_end, item.getEndTime());
//                helper.setText(R.id.tv_coupon_date, String.format("有效期%s-%s", cleanDate(item.getStartTime()), cleanDate(item.getEndTime())));
                helper.setText(R.id.tv_coupon_name, item.getActivityName());
            }
        };
        adapter.setEmptyView(createEmptyView());
        this.binding.rvContent.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        this.binding.rvContent.setAdapter(adapter);
    }

//    /**
//     * 清除时间的时分秒部分
//     *
//     * @param datetime 时间日期
//     * @return 返回日期 年/月/日
//     */
//    private String cleanDate(String datetime) {
//        if (datetime != null) {
//            String[] split = datetime.split(" ");
//            if (split.length >= 2) {
//                return split[0];
//            }
//        }
//        return datetime;
//    }

    @NotNull
    private TextView createEmptyView() {
        TextView view = new TextView(binding.getRoot().getContext());
        view.setText(R.string.app_no_coupons);
        view.setBackgroundResource(R.color.white);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    /**
     * 更新优惠券已选择状态
     */
    private void updateCoupons() {
        List<ActivityEntity> service = new ArrayList<>();
        List<ActivityEntity> customer = new ArrayList<>();
        for (ActivityEntity entity : serviceActivities) {
            if (entity.isSelect()) {
                service.add(entity);
            }
        }
        for (ActivityEntity coupon : customerCoupons) {
            if (coupon.isSelect()) {
                customer.add(coupon);
            }
        }
        // 根据优惠券选择顺序排序
        Collections.sort(service, (o1, o2) -> o1.getIndex() > o2.getIndex() ? 1 : -1);
        Collections.sort(customer, (o1, o2) -> o1.getIndex() > o2.getIndex() ? 1 : -1);
        Cache.ins().setProductCoupon(service, customer);
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
    public CouponShopSelect bind(BaseActivity activity) {
        this.owner = activity;
        return this;
    }

    /**
     * 绑定生命周期
     */
    public CouponShopSelect bind(Fragment fragment) {
        this.owner = fragment;
        return this;
    }

    /**
     * 设置视图模型
     *
     * @param model {@link CouponModel}
     */
    public CouponShopSelect model(CouponModel model) {
        this.model = model;
        return this;
    }

    /**
     * 设置数据类型
     *
     * @param type 1: 网费 2: 商品
     * @return {@link CouponShopSelect}
     */
    public CouponShopSelect type(int type) {
        this.type = type;
        return this;
    }

    /**
     * 设置数据
     */
    public CouponShopSelect set(String customerId, String productId) {
        this.customerId = customerId;
        this.productId = productId;
        return this;
    }

    /**
     * 设置优惠券使用回调
     *
     * @param callback {@link OnCouponShopUseCallback}
     */
    public CouponShopSelect callback(OnCouponShopUseCallback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 显示优惠券
     */
    public void show() {
        if (!this.isShowing) {
            this.isSuccess = false;
            this.serviceActivities.clear();
            this.customerCoupons.clear();
            adapter.notifyDataSetChanged();
            selectTab();
            this.moveAnimator.start();
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
        if (this.moveAnimator != null && this.isShowing) {
            this.moveAnimator.reverse();
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
                    serviceCouponSelect(datum);
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
                    customerCouponSelect(datum);
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
     * 更新用户优惠券选择状态
     */
    private void notifyCoupons() {
        for (ActivityEntity act : this.customerCoupons) {
            customerCouponSelect(act);
        }
        for (ActivityEntity act : this.serviceActivities) {
            serviceCouponSelect(act);
        }
    }

    /**
     * 优惠选择状态处理
     *
     * @param datum 优惠券对象
     */
    private void serviceCouponSelect(ActivityEntity datum) {
        String service = Cache.ins().getProtectService();
        if (service != null) {
            String activityId = datum.getActivityId();
            datum.setSelect(activityId != null && service.contains(activityId));
        }
    }

    /**
     * 优惠选择状态处理
     *
     * @param datum 优惠券对象
     */
    private void customerCouponSelect(ActivityEntity datum) {
        String customer = Cache.ins().getProtectCustomer();
        if (customer != null) {
            String activityCardResultId = datum.getActivityCardResultId();
            datum.setSelect(activityCardResultId != null && customer.contains(activityCardResultId));
        }
    }

    /**
     * 优惠券使用回调
     */
    public interface OnCouponShopUseCallback {

        /**
         * 优惠卷使用回调方法
         */
        void onCouponUse(List<ActivityEntity> service, List<ActivityEntity> customer);
    }
}
