package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.OrderAdapter;
import com.kiegame.mobile.databinding.FragmentOrderBinding;
import com.kiegame.mobile.model.OrderModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.BuyOrderEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.Text;

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
    private OrderModel model;
    private DatePickerDialog dialog;
    private String date;
    private int year;
    private int month;
    private int dayOfMonth;
    private SimpleDateFormat format;

    @Override
    protected int onLayout() {
        return R.layout.fragment_order;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onObject() {
        binding.setLogin(Cache.ins().getLoginInfo());
        binding.setFragment(this);
        model = new ViewModelProvider(this).get(OrderModel.class);
        format = new SimpleDateFormat("yyyy-MM-dd");
        this.views = new ArrayList<>();
        this.titles = new String[]{
                getString(R.string.order_wait_payment),
                getString(R.string.order_all),
        };

        toDay();

        dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), R.style.AppTheme_DatePickerDialog, (view, year, month, dayOfMonth) -> {
            this.year = year;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
            date = String.format("%s-%s-%s", year, month, dayOfMonth);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
        }, year, month, dayOfMonth);

        Cache.ins().getOrderObserver().observe(this, integer -> requestData());
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
            dialog.updateDate(year, month, dayOfMonth);
            dialog.show();
        });
    }

    @Override
    protected void onData() {
        requestData();
    }

    /**
     * 请求数据
     */
    void requestData() {
        String start = String.format("%s 00:00:00", date);
        String end = String.format("%s 23:59:59", date);
        LiveData<List<BuyOrderEntity>> liveData = model.queryOrders(null, start, end, null, null, null);
        if (!liveData.hasObservers()) {
            liveData.observe(this, this::onQueryOrderResult);
        }
    }

    /**
     * 下一天
     */
    public void nextDay() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(format.parse(date)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            date = format.format(calendar.getTime());
            this.splitDate(date);
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
            date = format.format(calendar.getTime());
            this.splitDate(date);
            binding.tvOrderCreateTime.setText(date);
            binding.srlLayout.autoRefresh();
            calendar.clear();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单查询返回
     *
     * @param data 订单数据
     */
    private void onQueryOrderResult(List<BuyOrderEntity> data) {
        binding.srlLayout.finishRefresh();
        if (data != null) {
            wait.refreshData(data);
            all.refreshData(data);
        }
    }

    /**
     * 获取当天时间
     */
    private void toDay() {
        Date date = new Date(System.currentTimeMillis());
        this.date = format.format(date);
        this.splitDate(this.date);
    }

    /**
     * 拆分日期时间
     */
    private void splitDate(String date) {
        if (!Text.empty(date)) {
            String[] split = date.split("-");
            if (split.length >= 3) {
                year = Integer.valueOf(split[0]);
                month = Integer.valueOf(split[1]);
                dayOfMonth = Integer.valueOf(split[2]);
            }
        }
    }
}
