package com.kiegame.mobile.utils;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ViewShopDetailBinding;
import com.kiegame.mobile.model.CommodityModel;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.ui.fragment.CommodityFragment;
import com.kiegame.mobile.ui.views.FlowLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/28.
 * Description: 商品详情
 */
public class ShopDetail {

    @SuppressLint("StaticFieldLeak")
    private static ShopDetail INS;
    private ViewShopDetailBinding binding;
    private boolean isShowing;
    private List<TextView> selectFlavor;
    private TextView selectNorm;
    private ShopEntity shop;
    private int buySourceSize;
    private CommodityModel model;
    private CommodityFragment fragment;

    // 标签类型 口味
    private static final int TAG_TYPE_FLAVOR = 1;
    // 标签类型 规格
    private static final int TAG_TYPE_NORM = 2;

    private ShopDetail() {
        this.binding = DataBindingUtil.inflate(LayoutInflater.from(Game.ins().activity()), R.layout.view_shop_detail, null, false);
    }

    /**
     * 获取 {@link ShopDetail} 对象
     *
     * @return 返回 {@link ShopDetail}
     */
    public static ShopDetail ins() {
        if (ShopDetail.INS == null) {
            ShopDetail.INS = new ShopDetail();
        }
        return ShopDetail.INS;
    }

    /**
     * 显示商品详情
     */
    public void show() {
        if (!this.isShowing) {
            isShowing = true;
            InjectView.ins().inject(binding.getRoot());
        }
    }

    /**
     * 设置请求接口
     */
    public ShopDetail model(CommodityModel model) {
        this.model = model;
        return this;
    }

    /**
     * 上下文参数
     */
    public ShopDetail context(CommodityFragment fragment) {
        this.fragment = fragment;
        return this;
    }

    /**
     * 设置数据
     *
     * @param data 商品数据对象
     */
    public ShopDetail set(ShopEntity data) {
        if (data != null) {
            this.shop = data;
            this.buySourceSize = 1;
            Glide.with(Game.ins().activity()).load(data.getShopImage()).into(binding.ivShopImage);
            binding.tvShopName.setText(data.getProductName());
            binding.tvShopNorm.setText(data.getProductSpecName());
            binding.tvShopPrice.setText(String.format("￥%s", cal(data.getSellPrice())));
            binding.flTasteLayout.setVisibility(View.VISIBLE);
            binding.tvTasteText.setVisibility(View.VISIBLE);
            binding.flNormLayout.setVisibility(View.VISIBLE);
            binding.tvNormText.setVisibility(View.VISIBLE);
            this.makeFlavorTags(data.getProductFlavorName());
            this.makeSpecTags(data.getProductSpecName());
            binding.tvShopNum.setText(String.valueOf(this.buySourceSize));
//            binding.ivBtnLess.setVisibility(this.buySourceSize == 0 ? View.INVISIBLE : View.VISIBLE);
//            binding.tvShopNum.setVisibility(this.buySourceSize == 0 ? View.INVISIBLE : View.VISIBLE);
            binding.ivBtnLess.setOnClickListener(v -> this.onBtnLess());
            binding.ivBtnPlus.setOnClickListener(v -> this.onBtnPlus());
            binding.tvBtnCancel.setOnClickListener(v -> this.hide());
            binding.tvBtnOk.setOnClickListener(v -> {
                if (this.buySourceSize > 0) {
                    addShopToOrderList();
                    this.hide();
                } else {
                    Toast.show("购买数量太少了, 再多买点吧");
                }
            });
        }
        return this;
    }

    /**
     * 添加商品到购物车
     */
    private void addShopToOrderList() {
        String flavor = selectFlavor != null && selectFlavor.size() != 0 ? getFlavor() : "";
        String spec = selectNorm != null ? selectNorm.getText().toString() : "";
        Cache.ins().attachShop(shop, flavor, spec, this.buySourceSize);
        Cache.ins().getShopObserver().setValue(-1);
    }

    /**
     * 获取全部口味
     *
     * @return 返回多个口味
     */
    private String getFlavor() {
        StringBuilder sb = new StringBuilder();
        for (TextView view : this.selectFlavor) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(view.getText().toString());
        }
        return sb.toString();
    }

    /**
     * 减少
     */
    private void onBtnLess() {
        TextView tv = binding.tvShopNum;
        String size = tv.getText().toString();
        int num = Text.empty(size) ? -1 : Integer.parseInt(size) - 1;
        if (num < 1) {
            Toast.show("不能再少了");
        } else {
//            if (num == 0) {
//                binding.ivBtnLess.setVisibility(View.INVISIBLE);
//                tv.setVisibility(View.INVISIBLE);
//                tv.setText("");
//            } else {
            tv.setText(String.valueOf(num));
//            }
            this.buySourceSize = num;
        }
    }

    /**
     * 增加
     */
    private void onBtnPlus() {
        if (shop.getProductVariety() == 1 && shop.getBarCount() <= 0) {
            Toast.show("这种商品已经售空了");
            return;
        }
        TextView tv = binding.tvShopNum;
        String size = tv.getText().toString();
        int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
        if (shop.getProductVariety() == 1 && shop.getIsIgnoreStock() == 2 && Cache.ins().getShopSumById(shop.getProductId()) + num > shop.getBarCount()) {
            Toast.show("不能再多了");
        } else {
//            if (num > 0) {
//                binding.ivBtnLess.setVisibility(View.VISIBLE);
//                binding.tvShopNum.setVisibility(View.VISIBLE);
//            }
//            /* --------- 新增自制商品库存判断 -------- */
//            if (shop.getProductVariety() == 2) {
//                LiveData<Object> stock = model.queryProductStock(shop.getProductId(), num);
//                if (!stock.hasObservers()) {
//                    stock.observe(fragment, o -> {
//                        this.buySourceSize = num;
//                        tv.setText(String.valueOf(num));
//                    });
//                }
//                /* --------- 新增自制商品库存判断 -------- */
//            } else {
            this.buySourceSize = num;
            tv.setText(String.valueOf(num));
//            }
        }
    }

    /**
     * 计算金额,分转换位元,保留两位
     *
     * @param money 金额, 分
     * @return 转换后的金额, 元
     */
    private String cal(int money) {
        BigDecimal source = new BigDecimal(money);
        BigDecimal ratio = new BigDecimal(100);
        BigDecimal divide = source.divide(ratio, 2, RoundingMode.HALF_UP);
        return divide.toString();
    }

    /**
     * 创建规格选择标签
     */
    private void makeSpecTags(String spec) {
        if (!Text.empty(spec)) {
            binding.flNormLayout.removeAllViews();
            String[] norm = spec.split(",");
            for (String name : norm) {
                binding.flNormLayout.addView(makeTextView(name, TAG_TYPE_NORM));
            }
        } else {
            binding.flNormLayout.setVisibility(View.GONE);
            binding.tvNormText.setVisibility(View.GONE);
        }
    }

    /**
     * 创建口味选择标签
     */
    private void makeFlavorTags(String flavor) {
        if (!Text.empty(flavor)) {
            binding.flTasteLayout.removeAllViews();
            String[] taste = flavor.split(",");
            for (String name : taste) {
                binding.flTasteLayout.addView(makeTextView(name, TAG_TYPE_FLAVOR));
            }
            // 一个以上的口味
            if (taste.length > 1) {
                if (this.selectFlavor != null && !this.selectFlavor.isEmpty()) {
                    for (TextView view : selectFlavor) {
                        cleanBtnStyle(view);
                    }
                    selectFlavor.clear();
                }
            }
        } else {
            binding.flTasteLayout.setVisibility(View.GONE);
            binding.tvTasteText.setVisibility(View.GONE);
        }
    }

    /**
     * 创建口味/规格选择按钮
     */
    private TextView makeTextView(String text, int type) {
        FlowLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Pixel.dp2px(30));
        TextView textView = new TextView(binding.getRoot().getContext());
        textView.setText(text);
        textView.setTag(type);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(Pixel.dp2px(16), 0, Pixel.dp2px(16), 0);
        cleanBtnStyle(textView);
        if (type == TAG_TYPE_FLAVOR) {
            if (selectFlavor == null) {
                selectFlavor = new ArrayList<>();
                selectFlavor.add(textView);
                setTextViewSelect(textView);
            }
        }
        if (type == TAG_TYPE_NORM) {
            if (selectNorm == null) {
                selectNorm = textView;
                setTextViewSelect(selectNorm);
            }
        }
        textView.setTextSize(10);
        textView.setOnClickListener(v -> setBtnStyle((TextView) v));
        return textView;
    }

    /**
     * 设置点击样式
     *
     * @param view {@link TextView}
     */
    private void setBtnStyle(TextView view) {
        int type = (int) view.getTag();
        switch (type) {
            case TAG_TYPE_FLAVOR:
//                if (selectFlavor != null) {
//                    cleanBtnStyle(selectFlavor);
//                }
                if (selectFlavor == null) {
                    selectFlavor = new ArrayList<>();
                }
                if (selectFlavor.contains(view)) {
                    selectFlavor.remove(view);
                    cleanBtnStyle(view);
                } else {
                    selectFlavor.add(view);
                    setTextViewSelect(view);
                }
                break;
            case TAG_TYPE_NORM:
                if (selectNorm != null) {
                    cleanBtnStyle(selectNorm);
                }
                selectNorm = view;
                setTextViewSelect(selectNorm);
                break;
        }
    }

    /**
     * 设置选中状态
     */
    private void setTextViewSelect(TextView view) {
        view.setTextColor(view.getResources().getColor(R.color.black_dark));
        view.setBackgroundResource(R.drawable.shape_shop_norm_btn_press_background);
    }

    /**
     * 清除点击样式
     *
     * @param view {@link TextView}
     */
    private void cleanBtnStyle(TextView view) {
        view.setBackgroundResource(R.drawable.shape_shop_norm_btn_none_background);
        view.setTextColor(view.getResources().getColor(R.color.gray_white));
    }

    /**
     * 隐藏商品详情
     */
    private void hide() {
        if (this.isShowing) {
            isShowing = false;
            InjectView.ins().clean(binding.getRoot());
        }
        if (this.selectFlavor != null) {
            this.selectFlavor.clear();
            this.selectFlavor = null;
        }
        this.selectNorm = null;
    }
}
