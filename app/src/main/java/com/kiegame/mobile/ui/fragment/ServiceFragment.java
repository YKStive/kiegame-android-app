package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ServiceAdapter;
import com.kiegame.mobile.databinding.FragmentServiceBinding;
import com.kiegame.mobile.model.ServiceModel;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.ChangeUserDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 服务Fragment
 */
public class ServiceFragment extends BaseFragment<FragmentServiceBinding> {

    private ServiceModel model;
    private List<Fragment> views;
    private String[] titles;
    private String date;
    private Calendar cal;
    private SimpleDateFormat format;
    private ServiceCallFragment serviceCallFragment;
    private ServiceGoodsOrderFragment goodsOrderFragment;


    @Override
    protected int onLayout() {
        return R.layout.fragment_service;
    }

    @Override
    protected void onObject() {
        this.model = new ViewModelProvider(this).get(ServiceModel.class);
        binding.setModel(model);
        binding.setFragment(this);
        cal = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM-dd");
        toDay();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onView() {
        this.views = new ArrayList<>();
        //呼叫服务
        serviceCallFragment = new ServiceCallFragment();
        this.views.add(serviceCallFragment);
        //商品订单
        goodsOrderFragment = new ServiceGoodsOrderFragment();
        this.views.add(goodsOrderFragment);
        this.titles = new String[]{
                getString(R.string.service_tab_call_service),
                getString(R.string.service_tab_goods_order),
        };
        ServiceAdapter adapter = new ServiceAdapter(getChildFragmentManager(), views, titles);
        binding.vpViews.setAdapter(adapter);
        binding.vpViews.setOffscreenPageLimit(2);
        binding.tlOrderTab.setupWithViewPager(binding.vpViews);
        binding.tvOrderCreateTime.setText(date);
        //刷新事件
        binding.srlLayout.setOnRefreshListener(refreshLayout -> model.refresh());
        //刷新完成
        model.getIsRefreshFinish().observe(this,isRefreshFinish -> {
            if (isRefreshFinish){
                binding.srlLayout.finishRefresh();
            }
        });
    }

    @Override
    protected void onData() {
        model.refresh();
    }


    private void refresh(){
        model.refresh();
    }

    /**
     * 显示菜单
     */
    public void showMenu() {
         ChangeUserDialog.getInstance(user -> {
             binding.tvServiceId.setText(String.format("%s / %s", model.getLogin().getServiceName(), user));
         }).show(getChildFragmentManager());
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
     * 获取当天时间
     */
    private void toDay() {
        Date date = new Date(System.currentTimeMillis());
        this.date = format.format(date);
        this.updateDate(this.date);
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
}
