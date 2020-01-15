package com.kiegame.mobile.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ShopAdapter;
import com.kiegame.mobile.databinding.ViewMoreListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/12.
 * Description: 多级列表
 */
public class MoreListView extends FrameLayout {

    private List<String> menus;
    private List<ShopAdapter.ShopEntity> entities;
    private LinearLayout selectMenu;
    private ViewMoreListLayoutBinding binding;

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

        menus = new ArrayList<>();
        menus.add("热 卖");
        menus.add("饮 品");
        menus.add("熟 食");
        menus.add("小商品");
        menus.add("香 烟");
        menus.add("其 他");

        entities = new ArrayList<>();
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.TITLE));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.CONTENT));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.TITLE));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.CONTENT));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.TITLE));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.CONTENT));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.TITLE));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.CONTENT));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.TITLE));
        entities.add(new ShopAdapter.ShopEntity(ShopAdapter.ShopEntity.CONTENT));

        binding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.view_more_list_layout, this, true);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 初始化菜单列表适配器
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(this.getContext()));
        BaseQuickAdapter<String, BaseViewHolder> menuAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_more_list_menu) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, String item) {
                int index = menus.indexOf(item);
                if (index == 0) {
                    setMenuItemStyle(helper.getView(R.id.ll_item_root));
                }
                helper.setText(R.id.tv_item_menu, item);
            }
        };
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (selectMenu != null) {
                cleanMenuItemStyle(selectMenu);
            }
            setMenuItemStyle((LinearLayout) view);
        });
        binding.rvMenu.setAdapter(menuAdapter);
        menuAdapter.setNewData(menus);

        // 初始化商品列表适配器
        binding.rvContent.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvContent.setAdapter(new ShopAdapter(entities));
    }

    private void setMenuItemStyle(LinearLayout item) {
        selectMenu = item;
        item.setBackgroundColor(getResources().getColor(R.color.white));
        TextView textView = (TextView) item.getChildAt(0);
        textView.setBackgroundResource(R.drawable.vector_more_list_menu_background);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(getResources().getColor(R.color.black_text));
    }

    private void cleanMenuItemStyle(LinearLayout item) {
        item.setBackgroundColor(getResources().getColor(R.color.translucent));
        TextView textView = (TextView) item.getChildAt(0);
        textView.setBackground(null);
        textView.setTextColor(getResources().getColor(R.color.gray_black_text));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }
}
