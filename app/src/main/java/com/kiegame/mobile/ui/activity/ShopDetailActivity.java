package com.kiegame.mobile.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityShopDetailBinding;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.ui.views.FlowLayout;
import com.kiegame.mobile.utils.Pixel;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 商品详情
 */
public class ShopDetailActivity extends BaseActivity<ActivityShopDetailBinding> {

    private TextView selectFlavor;
    private TextView selectNorm;
    private ShopEntity shop;

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
    }

    @Override
    protected void onView() {
        Glide.with(this).load(shop.getProductImg()).into(binding.ivShopImage);
        binding.tvShopName.setText(shop.getProductName());
        binding.tvShopStand.setText(shop.getProductUnitName());
        binding.tvShopDes.setText(shop.getProductDesc());
        binding.tvShopPrice.setText(cal(shop.getSellPrice()));
        binding.tvShopBuyInfo.setText(shop.getProductName());
        makeFlavorTags(shop.getProductFlavorName());
        makeSpecTags(shop.getProductSpecName());
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
        if (selectFlavor == null) {
            if (type == TAG_TYPE_FLAVOR) {
                selectFlavor = textView;
                setTextViewSelect(selectFlavor);
            } else {
                cleanBtnStyle(textView);
            }
        }
        if (selectNorm == null) {
            if (type == TAG_TYPE_NORM) {
                selectNorm = textView;
                setTextViewSelect(selectNorm);
            } else {
                cleanBtnStyle(textView);
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
                if (selectFlavor != null) {
                    cleanBtnStyle(selectFlavor);
                }
                selectFlavor = view;
                setTextViewSelect(selectFlavor);
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
        String flavor = selectFlavor != null ? selectFlavor.getText().toString() : "";
        String norm = selectNorm != null ? selectNorm.getText().toString() : "";
        binding.tvShopBuyInfo.setText(!Text.empty(flavor) && !Text.empty(norm) ? String.format("%s/%s/%s", shop.getProductName(), norm, flavor) : String.format("%s/%s", shop.getProductName(), Text.empty(norm) ? flavor : norm));
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
        if (shop.getBarCount() <= 0) {
            Toast.show("这种商品已经售空了");
            return;
        }
        TextView tv = binding.tvShopNum;
        String size = tv.getText().toString();
        int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
        if (num > shop.getBarCount()) {
            Toast.show("不能再多了");
        } else {
            if (num > 0) {
                binding.tvBtnLess.setVisibility(View.VISIBLE);
            }
            shop.setBuySize(num);
            tv.setText(String.valueOf(num));
        }
    }

    /**
     * 减少
     */
    public void less() {
        TextView tv = binding.tvShopNum;
        String size = tv.getText().toString();
        int num = Text.empty(size) ? -1 : Integer.parseInt(size) - 1;
        if (num < 0) {
            Toast.show("不能再少了");
        } else {
            if (num == 0) {
                binding.tvBtnLess.setVisibility(View.INVISIBLE);
                tv.setText("");
            } else {
                tv.setText(String.valueOf(num));
            }
            shop.setBuySize(num);
        }
    }
}
