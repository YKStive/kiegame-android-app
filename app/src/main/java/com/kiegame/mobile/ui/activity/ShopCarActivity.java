package com.kiegame.mobile.ui.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityShopCarBinding;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.repository.entity.submit.BuyShop;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.CouponSelect;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/20.
 * Description: 确认订单
 */
public class ShopCarActivity extends BaseActivity<ActivityShopCarBinding> {

    private BaseQuickAdapter<BuyShop, BaseViewHolder> adapter;

    @Override
    protected int onLayout() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void onObject() {
        binding.setActivity(this);
        binding.setCache(Cache.ins());
    }

    @Override
    protected void onView() {
        setShopListVisible();
        adapter = new BaseQuickAdapter<BuyShop, BaseViewHolder>(R.layout.item_shop_car_shop, Cache.ins().getShops()) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, BuyShop item) {
                CheckBox cb = helper.getView(R.id.cb_shop_select);
                cb.setChecked(item.isBuy());
                cb.setOnClickListener(v -> {
                    cb.setChecked(!item.isBuy());
                    item.setBuy(!item.isBuy());
                    Cache.ins().updateTotalMoney();
                    Cache.ins().notifyChange();
                });
                Glide.with(helper.itemView).load(item.getShopImage()).into((ImageView) helper.getView(R.id.iv_shop_image));
                helper.setText(R.id.tv_shop_name, item.getShopName());
                if (!Text.empty(item.getProductFlavorName()) && !Text.empty(item.getProductSpecName())) {
                    helper.setText(R.id.tv_shop_des, String.format("%s/%s", item.getProductSpecName(), item.getProductFlavorName()));
                } else {
                    helper.setText(R.id.tv_shop_des, Text.empty(item.getProductSpecName()) ? item.getProductFlavorName() : item.getProductSpecName());
                }
                helper.setText(R.id.tv_shop_price, getString(R.string.net_fee_use_coupon_format, cal(item.getFee())));
                helper.setText(R.id.tv_shop_num, String.valueOf(item.getProductBuySum()));
                ImageView plus = helper.getView(R.id.iv_btn_plus);
                ImageView less = helper.getView(R.id.iv_btn_less);
                TextView tv = helper.getView(R.id.tv_shop_num);
                plus.setOnClickListener(v -> plus(less, tv, item));
                less.setOnClickListener(v -> less(less, tv, item));
            }
        };
        binding.rvShopLayout.setLayoutManager(new LinearLayoutManager(this));
        binding.rvShopLayout.setAdapter(adapter);
    }

    @Override
    protected void onData() {

    }

    /**
     * 设置商品列表显示或隐藏
     */
    private void setShopListVisible() {
        if (Cache.ins().getShopSum() <= 0) {
            binding.rvShopLayout.setVisibility(View.GONE);
            binding.vvDividerLineStart.setVisibility(View.GONE);
            binding.llShopTotal.setVisibility(View.GONE);
        } else {
            binding.rvShopLayout.setVisibility(View.VISIBLE);
            binding.vvDividerLineStart.setVisibility(View.VISIBLE);
            binding.llShopTotal.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 增加
     */
    public void plus(ImageView less, TextView tv, BuyShop data) {
        if (data.getMax() <= 0) {
            Toast.show("这种商品已经售空了");
            return;
        }
        String size = tv.getText().toString();
        int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
        if (Cache.ins().shopTotal(data.getProductId()) > data.getMax()) {
            Toast.show("不能再多了");
        } else {
            less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setText(String.valueOf(num));
            Cache.ins().attachShop(data, data.getProductFlavorName(), data.getProductSpecName(), 1);
            List<ShopEntity> entities = Cache.ins().getEntities();
            for (ShopEntity buy : entities) {
                if (buy.getProductId().equals(data.getProductId())) {
                    buy.setBuySize(buy.getBuySize() + 1);
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 减少
     */
    public void less(ImageView less, TextView tv, BuyShop data) {
        String size = tv.getText().toString();
        int num = Text.empty(size) ? -1 : Integer.parseInt(size) - 1;
        if (num < 0) {
            Toast.show("不能再少了");
        } else {
            less.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            tv.setText(num == 0 ? "" : String.valueOf(num));
            Cache.ins().detachShop(data.getProductId(), data.getProductFlavorName(), data.getProductSpecName());
            List<ShopEntity> entities = Cache.ins().getEntities();
            for (ShopEntity buy : entities) {
                if (buy.getProductId().equals(data.getProductId())) {
                    buy.setBuySize(buy.getBuySize() - 1);
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
        if (Cache.ins().getShopSum() <= 0) {
            setShopListVisible();
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
     * 使用优惠券
     */
    public void couponUse() {
        CouponSelect.ins().set().show();
    }

    /**
     * 删除会员
     */
    public void deleteVipInfo() {
//        Cache.ins().setUserName("没有选择会员");
    }
}
