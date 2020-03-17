package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.adapter.ShopAdapter;
import com.kiegame.mobile.databinding.FragmentCommodityBinding;
import com.kiegame.mobile.model.CommodityModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.receive.ShopSortEntity;
import com.kiegame.mobile.ui.activity.ShopCarActivity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 商品
 */
public class CommodityFragment extends BaseFragment<FragmentCommodityBinding> {

    private CommodityModel model;
    private Badge badge;
    private LinearLayout selectMenu;
    private List<ShopSortEntity> types;
    private List<ShopSortEntity> tags;
    private List<ShopSortEntity> menus;
    private List<ShopEntity> shops;
    private BaseQuickAdapter<ShopSortEntity, BaseViewHolder> menuAdapter;
    private String productTagId;
    private String productTypeId;
    private String productName;
    private ShopAdapter shopAdapter;
    private boolean isTypeDone;
    private boolean isTagDone;
    private boolean isSearch;

    @Override
    public void onResume() {
        super.onResume();
        if (shopAdapter != null) {
            shopAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int onLayout() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void onObject() {
        this.model = new ViewModelProvider(this).get(CommodityModel.class);
        binding.setFragment(this);
        binding.setModel(this.model);
        this.types = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.shops = new ArrayList<>();
        Cache.ins().getShopObserver().observe(this, integer -> {
            if (integer != null) {
                if (integer == -1) {
                    shopAdapter.notifyDataSetChanged();
                } else {
                    queryShops(productTypeId, null, productTagId);
                }
            }
        });
        model.searchShop.observe(this, this::shopSearch);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onView() {
        badge = new QBadgeView(getContext())
                .bindTarget(binding.tvBadgeNum);
        badge.setBadgeNumber(Cache.ins().getShopSum());

        menuAdapter = new BaseQuickAdapter<ShopSortEntity, BaseViewHolder>(R.layout.item_more_list_menu, menus) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ShopSortEntity item) {
//                int index = menus.indexOf(item);
//                if (index == 0) {
//                    setMenuItemStyle(helper.getView(R.id.ll_item_root));
//                }
                String menuName = item.getProductTypeName() == null ? item.getProductTagName() : item.getProductTypeName();
                TextView menu = helper.getView(R.id.tv_item_menu);
                menu.setLetterSpacing(menuName.length() == 2 ? 0.4f : 0.05f);
                menu.setText(menuName);
            }
        };
        View header = LayoutInflater.from(getContext()).inflate(R.layout.item_more_list_menu, null, false);
        TextView menu = header.findViewById(R.id.tv_item_menu);
        menu.setLetterSpacing(0.4f);
        menu.setText("全部");
        header.setOnClickListener(v -> {
            setMenuItemStyle((LinearLayout) v);
            queryShops(null, null, null);
        });
        setMenuItemStyle((LinearLayout) header);
        menuAdapter.addHeaderView(header);
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            setMenuItemStyle((LinearLayout) view);
            if (position < this.menus.size()) {
                ShopSortEntity sort = this.menus.get(position);
                if (sort != null) {
                    productTagId = sort.getProductTagId();
                    productTypeId = sort.getProductTypeId();
                    productName = sort.getProductTypeName() == null ? sort.getProductTagName() : sort.getProductTypeName();
                    queryShops(productTypeId, null, productTagId);
                }
            }
        });
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvMenu.setAdapter(menuAdapter);
        shopAdapter = new ShopAdapter(this.shops);
        binding.rvContent.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvContent.setAdapter(shopAdapter);

        binding.srlLayout.setOnRefreshListener(refreshLayout -> queryAllShopData());
        binding.etSearchShop.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                model.searchShop.setValue("");
            }
        });
    }

    @Override
    protected void onData() {
        queryAllShopData();
        Cache.ins().setOnShopSumChangeListener(this, this::setShopSum);
    }

    /**
     * 查询商品数据
     *
     * @param productTypeId 商品分类ID
     * @param productName   商品名称
     * @param productTagId  商品标签ID
     */
    private synchronized void queryShops(String productTypeId, String productName, String productTagId) {
        queryShops(productTypeId, productName, productTagId, true);
    }

    /**
     * 查询商品数据
     *
     * @param productTypeId 商品分类ID
     * @param productName   商品名称
     * @param productTagId  商品标签ID
     */
    private synchronized void queryShops(String productTypeId, String productName, String productTagId, boolean isShowLoading) {
        shops.clear();
        LiveData<List<ShopEntity>> listShops = model.listShops(productTypeId, productName, productTagId, isShowLoading);
        if (!listShops.hasObservers()) {
            listShops.observe(this, this::lisShopResult);
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
     * 查询商品标签数据
     */
    private void queryAllShopData() {
        this.isTypeDone = false;
        this.isTagDone = false;
        this.menus.clear();
        LiveData<List<ShopSortEntity>> listType = model.listShopType();
        if (!listType.hasObservers()) {
            listType.observe(this, this::listTypeResult);
        }
        LiveData<List<ShopSortEntity>> listTag = model.listShopTag();
        if (!listTag.hasObservers()) {
            listTag.observe(this, this::listTagResult);
        }
    }

    /**
     * 商品分类数据返回处理
     *
     * @param data 数据对象
     */
    private void listTypeResult(List<ShopSortEntity> data) {
        this.isTypeDone = true;
        this.types.clear();
        this.types.addAll(data);
//        Collections.sort(this.types, (o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        sortMenuData();
    }

    /**
     * 商品标签数据返回处理
     *
     * @param data 数据对象
     */
    private void listTagResult(List<ShopSortEntity> data) {
        this.isTagDone = true;
        this.tags.clear();
        this.tags.addAll(data);
//        Collections.sort(this.tags, (o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        sortMenuData();
    }

    /**
     * 添加数据
     */
    private synchronized void sortMenuData() {
        if (isTagDone && isTypeDone) {
            this.menus.clear();
            this.menus.addAll(this.types);
            this.menus.addAll(this.tags);
            this.menuAdapter.notifyDataSetChanged();
//            ShopSortEntity sort = this.menus.get(0);
//            if (sort != null) {
//                productName = sort.getProductTypeName() == null ? sort.getProductTagName() : sort.getProductTypeName();
//                productTagId = sort.getProductTagId();
//                productTypeId = sort.getProductTypeId();
//                queryShops(productTypeId, null, productTagId, false);
//            }
            queryShops(null, null, null, false);
        }
        binding.tvShopEmpty.setVisibility(this.menus.size() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 商品搜索
     *
     * @param name 商品关键字
     */
    private void shopSearch(String name) {
        String productName = Text.empty(name) ? null : name;
        if (productName == null) {
            queryShops(productTypeId, null, productTagId);
        } else {
            isSearch = true;
            queryShops(null, productName, null);
        }
    }

    /**
     * 添加到购物车
     *
     * @param shopSum 商品
     */
    private void setShopSum(int shopSum) {
        badge.setBadgeNumber(shopSum);
    }

    /**
     * 商品处理
     *
     * @param data 数据对象
     */
    private void lisShopResult(List<ShopEntity> data) {
        binding.srlLayout.finishRefresh();
        if (data != null && !data.isEmpty()) {
            arrangeShops(data);
            shopAdapter.notifyDataSetChanged();
        }
        binding.tvShopListEmpty.setVisibility(this.shops.size() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 分类商品,并创建商品分类列表
     *
     * @param shops 商品数据
     */
    private synchronized void arrangeShops(List<ShopEntity> shops) {
        this.shops.clear();
        if (shops != null) {
            for (int i = 0; i < shops.size(); i++) {
                ShopEntity shop = shops.get(i);
                ShopEntity div = new ShopEntity(1);
                if (isSearch) {
                    div.setProductTypeName(shop.getProductTypeName());
                } else {
                    if (i == 0) {
                        div.setProductTypeName(productName);
                    }
                }
                this.shops.add(div);
                this.shops.add(shop);
            }
            isSearch = false;
        }
    }

    /**
     * 跳转到购物车
     */
    public void startShopCarActivity() {
        if (badge.getBadgeNumber() > 0) {
            startActivity(new Intent(getActivity(), ShopCarActivity.class));
        } else {
            Toast.show("请选择商品");
        }
    }
}
