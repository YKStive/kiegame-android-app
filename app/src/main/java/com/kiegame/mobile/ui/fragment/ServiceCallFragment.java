package com.kiegame.mobile.ui.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentServiceCallBinding;
import com.kiegame.mobile.model.ServiceModel;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.TransferDialog;

import java.util.Objects;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 呼叫服务
 */
public class ServiceCallFragment extends BaseFragment<FragmentServiceCallBinding> {

    //列表数据adapter9
    private BaseQuickAdapter<ServiceCallEntity, BaseViewHolder> adapter;
    private ServiceModel serviceModel;

    @Override
    protected int onLayout() {
        return R.layout.fragment_service_call;
    }

    @Override
    protected void onObject() {
        serviceModel = new ViewModelProvider(Objects.requireNonNull(getParentFragment())).get(ServiceModel.class);
        serviceModel.getServiceCallListData().observe(this, serviceCallEntities -> {
            adapter.setNewData(serviceCallEntities);
            binding.smartRefreshLayout.finishLoadMore();
        });
    }

    @Override
    protected void onView() {
        adapter = new BaseQuickAdapter<ServiceCallEntity, BaseViewHolder>(R.layout.item_service_call, serviceModel.getServiceCallListData().getValue()) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ServiceCallEntity item) {
                helper.setText(R.id.tv_name, "B012|1130|李某某");
                helper.setText(R.id.tv_time_call, "2020-11-10 20:43");
                helper.setText(R.id.tv_time_end, "倒计时02:55s");
                if (item.getState() == 1) {
                    helper.setTextColor(R.id.tv_time_end, 0XFFE02E2C);
                    helper.setBackgroundRes(R.id.tv_state, R.drawable.radius_yellow_10dp);
                    helper.setText(R.id.tv_state, "转接");
                } else {
                    helper.setTextColor(R.id.tv_time_end, 0XFFCBCBCB);
                    helper.getView(R.id.tv_state).setBackground(null);
                    helper.setText(R.id.tv_state, "已完成");
                }
                //点击事件
                helper.getView(R.id.tv_state).setOnClickListener(v -> {
                    TransferDialog.getInstance(item).show(getChildFragmentManager());
                });
            }
        };
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvData.setAdapter(adapter);
        //加载更多
        binding.smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            serviceModel.loadMoreServiceData();
        });
    }

    @Override
    protected void onData() {

    }
}
