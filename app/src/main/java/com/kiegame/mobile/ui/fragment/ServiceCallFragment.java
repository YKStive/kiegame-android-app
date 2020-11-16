package com.kiegame.mobile.ui.fragment;

import android.database.DatabaseUtils;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

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
import com.kiegame.mobile.repository.Network;
import com.kiegame.mobile.repository.Scheduler;
import com.kiegame.mobile.repository.Subs;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.Employee;
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.repository.entity.submit.RequestOtherEmployee;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.ChoiceOtherEmployeeDialog;
import com.kiegame.mobile.ui.dialog.TransferDialog;
import com.kiegame.mobile.utils.DateUtil;
import com.kiegame.mobile.utils.Toast;

import java.util.List;
import java.util.Objects;

import static android.view.View.VISIBLE;
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
    private static final String tg="ServiceCallFragment";

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
                if(callEntity.getState()==1){
                    int result  = callEntity.getTimeLeft() - 1>=0?callEntity.getTimeLeft():0;
                    callEntity.setTimeout(result==0);
                    callEntity.setTimeLeft(result);
                }
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


                helper.setText(R.id.tv_time_call, DateUtil.date2Str(DateUtil.str2Date(item.getStartTime(),DateUtil.FORMAT_YMDHMS),DateUtil.FORMAT_YMDHM));

                Log.d(tg,item.getTimeLeft()+"");

                if (item.getState() == 1) {
                    helper.setVisible(R.id.tv_time_end, true);
                    if(item.isTimeout()){
                        helper.setText(R.id.tv_time_end, "已经超时");
                        helper.setTextColor(R.id.tv_time_end, 0XFFE02E2C);
                    }else {
                        helper.setText(R.id.tv_time_end, timeLeft2Counter(Math.abs(item.getTimeLeft())));
                        helper.setTextColor(R.id.tv_time_end, Color.BLACK);
                    }
                    helper.setTextColor(R.id.tv_time_end, 0XFFE02E2C);
                    helper.setBackgroundRes(R.id.tv_state, R.drawable.radius_yellow_10dp);
                    helper.setText(R.id.tv_state, "转接");
                } else {
                    helper.setText(R.id.tv_time_end, "已经完成");
                    helper.setTextColor(R.id.tv_time_end, 0XFFCBCBCB);
                    helper.getView(R.id.tv_state).setBackground(null);
                    helper.setText(R.id.tv_state, "已完成");
                }
                //点击事件
                helper.getView(R.id.tv_state).setOnClickListener(v -> {
                    if (item.getState() == 1) {
                        TransferDialog.getInstance(item, (ownId, cpId) -> {
                            queryOtherEmployees(ownId,cpId);
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


    public void queryOtherEmployees(String excludeEmpId,String  cpId) {
        RequestOtherEmployee requestOtherEmployee = new RequestOtherEmployee();
        requestOtherEmployee.setExcludeEmpId(excludeEmpId);
        requestOtherEmployee.setServiceId(Cache.ins().getLoginInfo().getServiceId());
        Log.d(tg,"queryOtherEmployees param=="+requestOtherEmployee.toString());
        Network.api().otherEmployee(requestOtherEmployee)
                .compose(Scheduler.apply())
                .subscribe(new Subs<List<Employee>>() {
                               @Override
                               public void onSuccess(List<Employee> data, int total, int length) {
                                   ChoiceOtherEmployeeDialog.getInstance(data, employeeId -> {
                                        serviceModel.callServiceTransfer(employeeId,cpId);
                                   }).show(getChildFragmentManager());
                               }
                           }
                );


    }
}
