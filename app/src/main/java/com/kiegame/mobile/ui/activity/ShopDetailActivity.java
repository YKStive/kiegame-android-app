package com.kiegame.mobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityShopDetailBinding;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.ui.views.FlowLayout;
import com.kiegame.mobile.utils.Pixel;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;
import com.youth.banner.loader.ImageLoader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 商品详情
 */
public class ShopDetailActivity extends BaseActivity<ActivityShopDetailBinding> {

    private List<TextView> selectFlavor;
    private TextView selectNorm;
    private ShopEntity shop;
    private int buySourceSize;

    // 标签类型 口味
    private static final int TAG_TYPE_FLAVOR = 1;
    // 标签类型 规格
    private static final int TAG_TYPE_NORM = 2;

    @Override
    protected int onLayout() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void onObject() {
        binding.setActivity(this);
        shop = (ShopEntity) getIntent().getSerializableExtra(Setting.APP_SHOP_ENTITY);
        if (shop != null) {
            if (shop.getProductVariety() == 1) {
                buySourceSize = shop.getBarCount() - Cache.ins().getShopSumById(shop.getProductId()) >= 1 ? 1 : 0;
            } else {
                buySourceSize = 1;
            }
        }
    }

    @Override
    protected void onView() {
        // #833 后台设置多张图片，选择商品/商品详情界面图片展示失败
//        Glide.with(this).load(shop.getProductImg()).into(binding.ivShopBanner);
//        Glide.with(this).load(shop.getShopImage()).into(binding.ivShopImage);
        initImageBanner();
        binding.tvShopName.setText(shop.getProductName());
        binding.tvShopStand.setText(shop.getProductSpecName());
        binding.tvShopDes.setText(shop.getProductDesc());
        binding.tvShopPrice.setText(cal(shop.getSellPrice() * buySourceSize));
        binding.tvShopBuyInfo.setText(shop.getProductName());
        binding.tvShopNum.setText(String.valueOf(buySourceSize));
//        binding.tvShopNum.setVisibility(buySourceSize != 0 ? View.VISIBLE : View.INVISIBLE);
//        binding.tvBtnLess.setVisibility(buySourceSize != 1 ? View.VISIBLE : View.INVISIBLE);
        makeFlavorTags(shop.getProductFlavorName());
        makeSpecTags(shop.getProductSpecName());
    }

    /**
     * 初始化商品详情图片
     */
    private void initImageBanner() {
        String img = shop.getProductImg();
        if (!Text.empty(img)) {
            String[] images = img.split(",");
            binding.ivShopBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(imageView).load((String) path).into(imageView);
                }
            });
            binding.ivShopBanner.setImages(Arrays.asList(images));
            binding.ivShopBanner.start();
        }
    }

    /**
     * 创建规格选择标签
     */
    private void makeSpecTags(String spec) {
        if (!Text.empty(spec)) {
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

    @Override
    protected void onData() {

    }

    /**
     * 创建口味/规格选择按钮
     */
    private TextView makeTextView(String text, int type) {
        FlowLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Pixel.dp2px(30));
        TextView textView = new TextView(this);
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
        textView.setTextSize(12);
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
     * 设置选中状态
     */
    private void setTextViewSelect(TextView view) {
        view.setTextColor(getResources().getColor(R.color.black_dark));
        view.setBackgroundResource(R.drawable.shape_shop_norm_btn_press_background);
        String flavor = selectFlavor != null && selectFlavor.size() != 0 ? getFlavor() : "";
        String norm = selectNorm != null ? selectNorm.getText().toString() : "";
        binding.tvShopBuyInfo.setText(!Text.empty(flavor) && !Text.empty(norm) ? String.format("%s/%s/%s", shop.getProductName(), norm, flavor) : String.format("%s/%s", shop.getProductName(), Text.empty(norm) ? flavor : norm));
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
     * 清除点击样式
     *
     * @param view {@link TextView}
     */
    private void cleanBtnStyle(TextView view) {
        view.setBackgroundResource(R.drawable.shape_shop_norm_btn_none_background);
        view.setTextColor(getResources().getColor(R.color.gray_white));
    }

    /**
     * 增加
     */
    public void plus() {
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
//                binding.tvBtnLess.setVisibility(View.VISIBLE);
//                binding.tvShopNum.setVisibility(View.VISIBLE);
//            }
            this.buySourceSize = num;
            tv.setText(String.valueOf(num));
            binding.tvShopPrice.setText(cal(shop.getSellPrice() * buySourceSize));
        }
    }

    /**
     * 减少
     */
    public void less() {
        TextView tv = binding.tvShopNum;
        String size = tv.getText().toString();
        int num = Text.empty(size) ? -1 : Integer.parseInt(size) - 1;
        if (num < 1) {
            Toast.show("不能再少了");
        } else {
//            if (num == 1) {
//                binding.tvBtnLess.setVisibility(View.INVISIBLE);
//                tv.setVisibility(View.INVISIBLE);
//                tv.setText("");
//            } else {
            tv.setText(String.valueOf(num));
//            }
            this.buySourceSize = num;
            binding.tvShopPrice.setText(cal(shop.getSellPrice() * buySourceSize));
        }
    }

    /**
     * 添加到购物车
     */
    public void addShopCar() {
        if (this.buySourceSize > 0) {
            addShopToOrderList();
            // #787 商品详情加入购物车后，未自动返回到选择商品界面
            Toast.show("添加成功");
            finish();
        } else {
            Toast.show("购买数量太少了, 再多买点吧");
        }
    }

    /**
     * 添加商品到购物车
     */
    private void addShopToOrderList() {
        String flavor = selectFlavor != null && selectFlavor.size() != 0 ? getFlavor() : "";
        String spec = selectNorm != null ? selectNorm.getText().toString() : "";
        Cache.ins().attachShop(shop, flavor, spec, this.buySourceSize);
        this.buySourceSize = 0;
//        binding.tvShopNum.setText("0");
//        binding.tvShopNum.setVisibility(View.INVISIBLE);
//        binding.tvBtnLess.setVisibility(View.INVISIBLE);
    }

    /**
     * 立即购买
     */
    public void buyNow() {
        if (this.buySourceSize > 0) {
            addShopToOrderList();
            startActivity(new Intent(this, ShopCarActivity.class));
            finish();
        } else {
            Toast.show("购买数量太少了, 再多买点吧");
        }
    }
}
