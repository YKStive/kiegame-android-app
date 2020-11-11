package com.kiegame.mobile.ui.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentServiceGoodsOrderBinding;
import com.kiegame.mobile.model.ServiceModel;
import com.kiegame.mobile.repository.entity.receive.GoodsOrderEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.GoodsOrderDialog;
import com.kiegame.mobile.ui.dialog.SuccessDialog;

import java.util.Objects;

/**
 * 服务fragment->商品接单
 */
public class ServiceGoodsOrderFragment extends BaseFragment<FragmentServiceGoodsOrderBinding> {


    private BaseQuickAdapter<GoodsOrderEntity, BaseViewHolder> adapter;
    private ServiceModel serviceModel;

    @Override
    protected int onLayout() {
        return R.layout.fragment_service_goods_order;
    }

    @Override
    protected void onObject() {
        serviceModel = new ViewModelProvider(Objects.requireNonNull(getParentFragment())).get(ServiceModel.class);
        serviceModel.getGoodsOrderData().observe(this, goodsOrderEntities -> {
            adapter.setNewData(goodsOrderEntities);
            binding.smartRefreshLayout.finishLoadMore();
        });
    }

    @Override
    protected void onView() {
        adapter = new BaseQuickAdapter<GoodsOrderEntity, BaseViewHolder>(R.layout.item_service_goods_order) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, GoodsOrderEntity item) {
                helper.setText(R.id.tv_user_info, "B135|7780|吧台-李四");
                helper.setText(R.id.tv_pay_type, "在线支付");
                helper.setText(R.id.tv_pay_time, "2020-11-11 12:48");
                helper.setText(R.id.tv_user_name,"张三的分享");
                helper.setText(R.id.tv_expand_message, "展开查看金桔柠檬、乐事薯片等x件商品");
                helper.setGone(R.id.tv_expand_message, !item.isExpand());
                helper.getView(R.id.iv_expand).setRotation(item.isExpand() ? 270 : 90);
                RecyclerView rvChild = helper.getView(R.id.rv_data);
                SingleGoodsAdapter childAdapter;
                if (rvChild.getLayoutManager() == null) {
                    rvChild.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                if (rvChild.getAdapter() == null) {
                    childAdapter = new SingleGoodsAdapter(item.isExpand());
                    rvChild.setAdapter(childAdapter);

                } else {
                    childAdapter = (SingleGoodsAdapter) rvChild.getAdapter();
                }
                childAdapter.setExpand(item.isExpand());
                childAdapter.setNewData(item.getSingleOrderEntityList());

                //事件
                helper.getView(R.id.tv_expand_message).setOnClickListener(v -> {
                    item.setExpand(!item.isExpand());
                    refreshNotifyItemChanged(helper.getLayoutPosition());
                });
                helper.getView(R.id.iv_expand).setOnClickListener(v -> {
                    item.setExpand(!item.isExpand());
                    refreshNotifyItemChanged(helper.getLayoutPosition());
                });
                helper.getView(R.id.tv_user_info).setOnClickListener(v -> {
                    item.setExpand(!item.isExpand());
                    refreshNotifyItemChanged(helper.getLayoutPosition());
                });
            }
        };
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvData.setAdapter(adapter);

        //加载更多
        binding.smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> serviceModel.loadMoreGoodsOrderData());
    }

    @Override
    protected void onData() {

    }


    class SingleGoodsAdapter extends BaseQuickAdapter<GoodsOrderEntity.SingleOrderEntity, BaseViewHolder> {


        private boolean isExpand;

        public SingleGoodsAdapter(boolean isExpand) {
            super(R.layout.item_service_goods_order_single);
            this.isExpand = isExpand;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, GoodsOrderEntity.SingleOrderEntity item) {
            helper.setText(R.id.tv_goods_name, "香草拿铁");
            helper.setText(R.id.tv_goods_price, "￥9.99");
            helper.setText(R.id.tv_goods_info, "大/加冰/不加糖");
            helper.setText(R.id.tv_time_end, "05:38s");
            //状态 1:待接单 2：待出品 3：配送中 4：已超时 5：已完成
            TextView tvState = helper.getView(R.id.tv_state);
            TextView tvAction = helper.getView(R.id.tv_action);
            helper.setTextColor(R.id.tv_time_end, 0xFF1A1A1A);
            helper.setGone(R.id.view_bottom, helper.getLayoutPosition() != getItemCount() - 1);
            switch (item.getState()) {
                case 1:
                    tvState.setText("待接单");
                    tvAction.setText("接单");
                    tvAction.setTextColor(0xFFFFFFFF);
                    tvAction.setBackgroundResource(R.drawable.radius_pink_10dp);
                    tvState.setVisibility(View.GONE);
                    tvAction.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvState.setText("待出品");
                    tvAction.setText("出品");
                    tvAction.setTextColor(0xFFFFFFFF);
                    tvAction.setBackgroundResource(R.drawable.radius_blue_10dp);
                    tvState.setVisibility(View.GONE);
                    tvAction.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    tvState.setText("配送中");
                    tvAction.setText("配送");
                    tvState.setVisibility(View.VISIBLE);
                    tvAction.setVisibility(View.GONE);
                    break;
                case 4:
                    tvState.setText("已超时");
                    tvAction.setText("接单");
                    tvAction.setTextColor(0xFFFFFFFF);
                    tvAction.setBackgroundResource(R.drawable.radius_pink_10dp);
                    tvState.setVisibility(View.GONE);
                    tvAction.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    tvState.setText("抢单");
                    tvAction.setText("抢单");
                    helper.setTextColor(R.id.tv_time_end, 0xFFE53945);
                    tvAction.setBackgroundResource(R.drawable.radius_yellow_10dp);
                    tvState.setVisibility(View.GONE);
                    tvAction.setVisibility(View.VISIBLE);
                    break;
                case 5:
                default:
                    tvState.setText("已完成");
                    tvState.setVisibility(View.VISIBLE);
                    tvAction.setVisibility(View.GONE);
                    break;
            }

            //事件
            helper.getView(R.id.tv_action).setOnClickListener(v -> {
                GoodsOrderDialog dialog = GoodsOrderDialog.getInstance(item);
                dialog.setOnOrderProducedClickListener(() -> {
                    SuccessDialog.getInstance("出品成功,待配送").show(getChildFragmentManager());
                });
                dialog.show(getChildFragmentManager());
            });
        }

        @Override
        public int getItemCount() {
            if (!isExpand) {
                return Math.min(1, super.getItemCount());
            } else {
                return super.getItemCount();
            }
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }
    }
}
