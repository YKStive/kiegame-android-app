package com.kiegame.mobile.ui.fragment;

import android.database.DatabaseUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.DataUrlLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentServiceCallBinding;
import com.kiegame.mobile.model.ServiceModel;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.TransferDialog;
import com.kiegame.mobile.utils.DateUtil;
import com.kiegame.mobile.utils.Toast;

import java.util.List;
import java.util.Objects;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 呼叫服务
 */
public class ServiceCallFragment extends BaseFragment<FragmentServiceCallBinding> {

    //列表数据adapter9
    private BaseQuickAdapter<ServiceCallEntity, BaseViewHolder> adapter;
    private ServiceModel serviceModel;
    private List<ServiceCallEntity> callServicesData;
    private int mCurrentRvState;

    @Override
    protected int onLayout() {
        return R.layout.fragment_service_call;
    }

    @Override
    protected void onObject() {
        serviceModel = new ViewModelProvider(Objects.requireNonNull(getParentFragment())).get(ServiceModel.class);
        serviceModel.getServiceCallListData().observe(this, serviceCallEntities -> {
            this.callServicesData = serviceCallEntities;
            adapter.setNewData(callServicesData);
            binding.smartRefreshLayout.finishLoadMore();
        });
        serviceModel.isTransferSuccess.observe(this, success -> {
            String toastText = success ? getString(R.string.transferSuccess) : getString(R.string.transferFalse);
            Toast.show(toastText);
        });

        //全局计时器，每次回调item的倒计时-1
        serviceModel.counter.observe(this, left -> {
            dealItemCounter();
        });
    }

    private void dealItemCounter() {
        if (callServicesData != null && !callServicesData.isEmpty()) {
            for (ServiceCallEntity callEntity : callServicesData) {
                callEntity.setTimeLeft(callEntity.getTimeLeft() - 1);
            }

            if (mCurrentRvState == SCROLL_STATE_IDLE) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onView() {
        adapter = new BaseQuickAdapter<ServiceCallEntity, BaseViewHolder>(R.layout.item_service_call, serviceModel.getServiceCallListData().getValue()) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ServiceCallEntity item) {
                String nameDesc = item.getCustomId() + "|" + item.getSeatNumber() + "|" + item.getCustomerName();
                helper.setText(R.id.tv_name, nameDesc);


                helper.setText(R.id.tv_time_call, item.getStartTime());

                helper.setText(R.id.tv_time_end, timeLeft2Counter(item.getTimeLeft()));
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
                    if (item.getState() == 1) {
                        TransferDialog.getInstance(item, (cancelOperatorId, cpId) -> {

                            serviceModel.callServiceTransfer(cancelOperatorId, cpId);
                        }).show(getChildFragmentManager());
                    }
                });
            }
        };
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvData.setAdapter(adapter);
        binding.rvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurrentRvState = newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //加载更多
        binding.smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            serviceModel.loadMoreServiceData();
        });
    }

    private String timeLeft2Counter(int timeLeft) {
        String[] result = DateUtil.second2MS(timeLeft);
        return "倒计时" + result[0] + ":" + result[1] + "s";
    }

    @Override
    protected void onData() {

    }
}
