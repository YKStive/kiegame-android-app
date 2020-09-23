package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.OrderAdapter;
import com.kiegame.mobile.databinding.FragmentOrderBinding;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.ui.activity.LoginActivity;
import com.kiegame.mobile.ui.activity.MainActivity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.Menu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 订单
 */
public class OrderFragment extends BaseFragment<FragmentOrderBinding> {

    private List<Fragment> views;
    private String[] titles;
    private WaitPaymentFragment wait;
    private AllOrderFragment all;
    private DatePickerDialog dialog;
    private String date;
    private Calendar cal;
    private SimpleDateFormat format;
    private int currentPage = 0;
    private Menu menu;

    @Override
    protected int onLayout() {
        return R.layout.fragment_order;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onObject() {
        cal = Calendar.getInstance();
        binding.setLogin(Cache.ins().getLoginInfo());
        binding.setFragment(this);
        format = new SimpleDateFormat("yyyy-MM-dd");
        this.views = new ArrayList<>();
        this.titles = new String[]{
                getString(R.string.order_wait_payment),
                getString(R.string.order_all),
        };

        toDay();

        dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), R.style.AppTheme_DatePickerDialog, (view, year, month, dayOfMonth) -> {
            date = String.format("%s-%s-%s", year, month + 1, dayOfMonth);
            updateDate(date);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        Cache.ins().getOrderObserver().observe(this, integer -> refreshAllData());
        Cache.ins().getOrderObserver().observe(this, page -> {
            if (page != null) {
                binding.vpOrderViews.setCurrentItem(page);
                String start = String.format("%s 00:00:00", date);
                String end = String.format("%s 23:59:59", date);
                if (page == 0) {
                    wait.refreshData(start, end);
                } else {
                    all.refreshData(start, end);
                }
            }
        });
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onView() {
        // 待支付
        wait = new WaitPaymentFragment(this);
        views.add(wait);
        // 全部订单
        all = new AllOrderFragment(this);
        views.add(all);
        OrderAdapter adapter = new OrderAdapter(getParentFragmentManager(), views, titles);
        binding.vpOrderViews.setAdapter(adapter);
        binding.vpOrderViews.setOffscreenPageLimit(2);
        binding.tlOrderTab.setupWithViewPager(binding.vpOrderViews);
        binding.srlLayout.setOnRefreshListener(refreshLayout -> requestData());
        binding.tvOrderCreateTime.setText(date);
        binding.tvOrderCreateTime.setOnClickListener(v -> {
            dialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
        binding.vpOrderViews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        menu = new Menu(getContext()).callback(v -> {
            Cache.ins().initialize();
            // #782 退出登录，需把界面已选择内容清空
            Cache.ins().getNetFeeObserver().setValue(-1);
            MainActivity activity = (MainActivity) getActivity();
            startActivity(new Intent(activity, LoginActivity.class));
            if (activity != null) {
                activity.finish();
            }
        });
    }

    @Override
    protected void onData() {
        refreshAllData();
    }

    /**
     * 更新日期
     *
     * @param date 日期字符串
     */
    private void updateDate(String date) {
        try {
            cal.setTime(Objects.requireNonNull(format.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新所有数据
     */
    void refreshAllData() {
        String start = String.format("%s 00:00:00", date);
        String end = String.format("%s 23:59:59", date);
        wait.refreshData(start, end);
        all.refreshData(start, end);
    }

    /**
     * 请求数据
     */
    private void requestData() {
        String start = String.format("%s 00:00:00", date);
        String end = String.format("%s 23:59:59", date);
        if (currentPage == 0) {
            // 待支付
            wait.refreshData(start, end);
        } else {
            // 全部订单
            all.refreshData(start, end);
        }
    }

    /**
     * 结束刷新
     */
    void finishRefresh() {
        binding.srlLayout.finishRefresh();
    }

    /**
     * 下一天
     */
    public void nextDay() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(format.parse(date)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date time = calendar.getTime();
            cal.setTime(time);
            date = format.format(time);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
            calendar.clear();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上一天
     */
    public void pastDay() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(format.parse(date)));
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            Date time = calendar.getTime();
            cal.setTime(time);
            date = format.format(time);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
            calendar.clear();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示菜单
     */
    public void showMenu() {
        menu.show(binding.ivExitBtn);
    }

    /**
     * 获取当天时间
     */
    private void toDay() {
        Date date = new Date(System.currentTimeMillis());
        this.date = format.format(date);
        this.updateDate(this.date);
    }
}
