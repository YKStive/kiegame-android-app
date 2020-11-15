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
import com.kiegame.mobile.repository.entity.receive.ServiceCallEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.ui.dialog.GoodsOrderDialog;
import com.kiegame.mobile.ui.dialog.SuccessDialog;
import com.kiegame.mobile.utils.CustomUtils;
import com.kiegame.mobile.utils.Toast;

import java.util.List;
import java.util.Objects;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 商品接单
 */
public class ServiceGoodsOrderFragment extends BaseFragment<FragmentServiceGoodsOrderBinding> {


    private BaseQuickAdapter<GoodsOrderEntity, BaseViewHolder> adapter;
    private ServiceModel serviceModel;

    //商品订单完成
    public static final int DO_ORDER_TYPE_COMPLETE = 0;
    //商品订单接单
    public static final int DO_ORDER_TYPE_TAKE = 1;
    //商品订单出品
    public static final int DO_ORDER_TYPE_PRODUCE = 2;
    //商品订单抢单
    public static final int DO_ORDER_TYPE_GRAB = 3;
    private List<GoodsOrderEntity> productOrderData;
    private int mCurrentRvState;

    @Override
    protected int onLayout() {
        return R.layout.fragment_service_goods_order;
    }

    @Override
    protected void onObject() {
        serviceModel = new ViewModelProvider(Objects.requireNonNull(getParentFragment())).get(ServiceModel.class);
        serviceModel.getGoodsOrderData().observe(this, goodsOrderEntities -> {
            this.productOrderData = goodsOrderEntities;
            adapter.setNewData(goodsOrderEntities);
            binding.smartRefreshLayout.finishLoadMore();
        });

        //订单商品操作回调事件
        serviceModel.goodsOrderOperateSuccess.observe(this, productOrderOperateState -> {
            String msg = CustomUtils.convertOperateType(productOrderOperateState.type);
            if (productOrderOperateState.isSuccess) {
                SuccessDialog.getInstance("商品" + msg + "成功").show(getChildFragmentManager());
            } else {
                Toast.show("商品" + msg + "失败，请稍后重试");
            }
        });


        //全局计时器，每次回调item的倒计时-1
        serviceModel.counter.observe(this, left -> {
            dealItemCounter();
        });
    }

    private void dealItemCounter() {
        if (productOrderData != null && !productOrderData.isEmpty()) {
            for (GoodsOrderEntity goodsOrderEntity : productOrderData) {
                for (GoodsOrderEntity.ProductsEntity product : goodsOrderEntity.getProducts()) {
                    GoodsOrderEntity.ProductsEntity.ProcessEntity process = product.getProcess();
                    process.setTimeLeft(process.getTimeLeft() - 1);
                }
            }

            if(mCurrentRvState==SCROLL_STATE_IDLE){
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void onView() {
        adapter = new BaseQuickAdapter<GoodsOrderEntity, BaseViewHolder>(R.layout.item_service_goods_order) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, GoodsOrderEntity item) {
                //用户信息
                String userInfo = item.getSeatNumber() + "|" + CustomUtils.splitIdCard(item.getIdCard()) + "|" + item.getCustomerName();
                helper.setText(R.id.tv_user_info, userInfo);


                //支付方式
                helper.setText(R.id.tv_pay_type, CustomUtils.convertPayType(item.getPayType()));


                //todo 用户信息？
                helper.setText(R.id.tv_user_name, item.getCustomerName());

                //订单时间和展开title
                List<GoodsOrderEntity.ProductsEntity> products = item.getProducts();
                if (!products.isEmpty()) {
                    String createDate = products.get(0).getCreateDate();
                    helper.setText(R.id.tv_pay_time, createDate);

                    helper.setText(R.id.tv_expand_message, generateProductExpandTitle(products));
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
                    childAdapter.setNewData(products);
                }


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

            /**
             * 展开title
             * @param products 商品列表
             * @return title
             */
            private String generateProductExpandTitle(List<GoodsOrderEntity.ProductsEntity> products) {
                if (!products.isEmpty() && products.size() >= 2) {
                    return products.get(0).getProductName() +
                            "、" +
                            products.get(1).getProductName() +
                            "等" + products.size() + "件商品";
                }

                return null;
            }
        };
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvData.setAdapter(adapter);
        binding.rvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurrentRvState =  newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //加载更多
        binding.smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> serviceModel.loadMoreGoodsOrderData());


    }

    @Override
    protected void onData() {

    }


    class SingleGoodsAdapter extends BaseQuickAdapter<GoodsOrderEntity.ProductsEntity, BaseViewHolder> {


        private boolean isExpand;

        public SingleGoodsAdapter(boolean isExpand) {
            super(R.layout.item_service_goods_order_single);
            this.isExpand = isExpand;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, GoodsOrderEntity.ProductsEntity item) {
            helper.setText(R.id.tv_goods_name, item.getProductName());
            helper.setText(R.id.tv_goods_price, "￥" + item.getSellPrice());
            helper.setText(R.id.tv_goods_info, item.getProductFlavorName());
            helper.setText(R.id.tv_time_end, CustomUtils.convertTimeCounter(item.getProcess().getTimeLeft()));
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
                dialog.setOnOrderDoSomethingClickListener((type, orderId, productId) -> {
                    switch (type) {
                        case DO_ORDER_TYPE_COMPLETE:
                            completeProductOrder(orderId, productId);
                            break;
                        case DO_ORDER_TYPE_TAKE:
                            takeProductOrder(orderId, productId);
                            break;
                        case DO_ORDER_TYPE_PRODUCE:
                            produceProductOrder(orderId, productId);
                            break;
                        case DO_ORDER_TYPE_GRAB:
                            grabProductOrder(orderId, productId);
                            break;
                        default:
                            break;
                    }

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

    /**
     * 抢单
     */
    private void grabProductOrder(String orderId, String productId) {
        serviceModel.grabProductOrder(orderId, productId);
    }

    /**
     * 出品
     */
    private void produceProductOrder(String orderId, String productId) {
        serviceModel.produceProductOrder(orderId, productId);
    }

    /**
     * 接单
     */
    private void takeProductOrder(String orderId, String productId) {
        serviceModel.takeProductOrder(orderId, productId);
    }

    /**
     * 完成
     */
    private void completeProductOrder(String orderId, String productId) {
        serviceModel.completeProductOrder(orderId, productId);
    }
}
