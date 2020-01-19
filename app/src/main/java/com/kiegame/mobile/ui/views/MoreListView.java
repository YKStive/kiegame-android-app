package com.kiegame.mobile.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ShopAdapter;
import com.kiegame.mobile.databinding.ViewMoreListLayoutBinding;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/12.
 * Description: 多级列表
 */
public class MoreListView extends FrameLayout {

    private List<ShopSortEntity> menus;
    private List<ShopEntity> shops;
    private LinearLayout selectMenu;
    private ViewMoreListLayoutBinding binding;
    private BaseQuickAdapter<ShopSortEntity, BaseViewHolder> menuAdapter;
    private ShopAdapter shopAdapter;
    private SparseIntArray index;
    private boolean isScrollUp;

    public MoreListView(@NonNull Context context) {
        super(context);
        this.init();
    }

    public MoreListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public MoreListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.view_more_list_layout, this, true);
        this.index = new SparseIntArray();
        this.menus = new ArrayList<>();
        this.shops = new ArrayList<>();
        initSortMenu();
        initShopView();
    }

    /**
     * 初始化商品列表
     */
    private void initShopView() {
        binding.rvContent.setLayoutManager(new LinearLayoutManager(this.getContext()));
        shopAdapter = new ShopAdapter(shops);
        binding.rvContent.setAdapter(shopAdapter);
        binding.rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrollUp = dy > 0;
                scrollStartOrEnd(recyclerView);
            }
        });
        binding.rvContent.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                changeScrollMenuItem(view);
            }
        });
    }

    /**
     * 商品列表滑动时的分类菜单处理
     */
    private void changeScrollMenuItem(@NonNull View view) {
        if (view instanceof LinearLayout) {
            TextView tv = (TextView) ((LinearLayout) view).getChildAt(0);
            if (tv.getVisibility() == View.VISIBLE) {
                String me = tv.getText().toString();
                int index = calMenuItemPosition(me);
                if (index <= menus.size() && index >= 0) {
                    selectMenuItemByPosition(index);
                }
            }
        }
    }

    /**
     * 初始化分类列表
     */
    private void initSortMenu() {
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(this.getContext()));
        menuAdapter = new BaseQuickAdapter<ShopSortEntity, BaseViewHolder>(R.layout.item_more_list_menu, menus) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ShopSortEntity item) {
                int index = menus.indexOf(item);
                if (index == 0) {
                    setMenuItemStyle(helper.getView(R.id.ll_item_root));
                }
                helper.setText(R.id.tv_item_menu, item.getProductTypeName());
            }
        };
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            setMenuItemStyle((LinearLayout) view);
            binding.rvContent.smoothScrollToPosition(index.get(position));
        });
        binding.rvMenu.setAdapter(menuAdapter);
    }

    /**
     * 计算分类菜单索引位置
     */
    private int calMenuItemPosition(String menu) {
        int index = -1;
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getProductTypeName().equals(menu)) {
                if (isScrollUp) {
                    index = i + 1;
                } else {
                    index = i - 1;
                }
                break;
            }
        }
        return index;
    }

    /**
     * 滑动顶部或底部处理
     *
     * @param recyclerView {@link RecyclerView}
     */
    private void scrollStartOrEnd(@NonNull RecyclerView recyclerView) {
        if (!recyclerView.canScrollVertically(-1)) {
            // 到顶了
            selectMenuItemByPosition(0);
        } else if (!recyclerView.canScrollVertically(1)) {
            // 到底了
            selectMenuItemByPosition(menus.size() - 1);
        }
    }

    /**
     * 滑动到指定item并设置Item的选择状态
     *
     * @param index 索引位置
     */
    private void selectMenuItemByPosition(int index) {
        binding.rvMenu.smoothScrollToPosition(index);
        BaseViewHolder holder = (BaseViewHolder) binding.rvMenu.findViewHolderForAdapterPosition(index);
        if (holder != null) {
            setMenuItemStyle(holder.getView(R.id.ll_item_root));
        }
    }

    /**
     * 设置item选中状态
     */
    private void setMenuItemStyle(LinearLayout item) {
        if (selectMenu != null) {
            if (selectMenu.equals(item)) {
                return;
            }
            cleanMenuItemStyle(selectMenu);
        }
        selectMenu = item;
        item.setBackgroundColor(getResources().getColor(R.color.white));
        TextView textView = (TextView) item.getChildAt(0);
        textView.setBackgroundResource(R.drawable.vector_more_list_menu_background);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(getResources().getColor(R.color.black_text));
    }

    /**
     * 清除item选中状态
     */
    private void cleanMenuItemStyle(LinearLayout item) {
        item.setBackgroundColor(getResources().getColor(R.color.translucent));
        TextView textView = (TextView) item.getChildAt(0);
        textView.setBackground(null);
        textView.setTextColor(getResources().getColor(R.color.gray_black_text));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 设置商品数据
     *
     * @param shops 商品数据
     */
    public void setShops(List<ShopEntity> shops) {
        this.menus.clear();
        this.shops.clear();
        arrangeShops(shops);
        shopAdapter.notifyDataSetChanged();
        menuAdapter.notifyDataSetChanged();
    }

    /**
     * 分类商品,并创建商品分类列表
     *
     * @param shops 商品数据
     */
    private void arrangeShops(List<ShopEntity> shops) {
        for (int i = 0; i < shops.size(); i++) {
            ShopEntity shop = shops.get(i);
            ShopSortEntity menu = new ShopSortEntity();
            menu.setProductTypeName(shop.getProductTypeName());
            menu.setProductTypeId(shop.getProductTypeId());
            menu.setSort(shop.getSort());
            ShopEntity div = new ShopEntity(1);
            if (!menus.contains(menu)) {
                div.setProductTypeName(menu.getProductTypeName());
                menus.add(menu);
                index.append(index.size(), this.shops.size());
            }
            this.shops.add(div);
            this.shops.add(shop);
        }
    }

    /**
     * 设置商品添加回调
     *
     * @param callback {@link ShopAdapter.OnJoinShopCallback}
     */
    public void setCallback(ShopAdapter.OnJoinShopCallback callback) {
        shopAdapter.setCallback(callback);
    }
}
