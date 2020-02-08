package com.kiegame.mobile.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.repository.cache.Cache;
import com.kiegame.mobile.repository.entity.receive.ShopEntity;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.activity.ShopDetailActivity;
import com.kiegame.mobile.utils.ShopDetail;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/12.
 * Description: 商品列表适配器
 */
public class ShopAdapter extends BaseMultiItemQuickAdapter<ShopEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopAdapter(List<ShopEntity> data) {
        super(data);
        this.addItemType(ShopEntity.CONTENT, R.layout.item_more_list_shop);
        this.addItemType(ShopEntity.TITLE, R.layout.item_more_list_title);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopEntity item) {
        switch (helper.getItemViewType()) {
            case ShopEntity.CONTENT:
                ImageView view = helper.getView(R.id.iv_shop_image);
                Glide.with(helper.itemView).load(item.getProductImg()).into(view);
                helper.setText(R.id.tv_shop_name, item.getProductName());
                helper.setText(R.id.tv_shop_norm, item.getProductSpecName());
                helper.setText(R.id.tv_shop_money, cal(item.getSellPrice()));
                helper.setVisible(R.id.tv_btn_less, item.getBuySize() != 0);
                helper.setVisible(R.id.tv_shop_num, item.getBuySize() != 0);
                helper.setText(R.id.tv_shop_num, String.valueOf(item.getBuySize()));
                setPlusShopClickListener(helper, item);
                setLessShopClickListener(helper, item);
                view.setOnClickListener(v -> v.getContext()
                        .startActivity(new Intent(v.getContext(), ShopDetailActivity.class)
                                .putExtra(Setting.APP_SHOP_ENTITY, item)));
                break;
            case ShopEntity.TITLE:
                TextView tv = helper.getView(R.id.tv_title);
                if (item.getProductTypeName() != null) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(item.getProductTypeName());
                } else {
                    tv.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 设置减少购物车点击事件处理
     */
    private void setLessShopClickListener(@NonNull BaseViewHolder helper, ShopEntity item) {
        helper.getView(R.id.tv_btn_less).setOnClickListener(v -> {
            TextView tv = helper.getView(R.id.tv_shop_num);
            String size = tv.getText().toString();
            int num = Text.empty(size) ? -1 : Integer.parseInt(size) - 1;
            if (num < 0) {
                Toast.show("不能再少了");
            } else {
                helper.setVisible(R.id.tv_btn_less, num != 0);
                tv.setVisibility(num != 0 ? View.VISIBLE : View.GONE);
                if (num == 0) {
                    tv.setText("");
                } else {
                    tv.setText(String.valueOf(num));
                }
                item.setBuySize(num);
                Cache.ins().detachShop(item.getProductId(), null, null);
            }
        });
    }

    /**
     * 设置添加购物车点击事件处理
     */
    private void setPlusShopClickListener(@NonNull BaseViewHolder helper, ShopEntity item) {
        helper.getView(R.id.tv_btn_plus).setOnClickListener(v -> {
            if (item.getBarCount() <= 0) {
                Toast.show("这种商品已经售空了");
                return;
            }
            TextView tv = helper.getView(R.id.tv_shop_num);
            if (!Text.empty(item.getProductFlavorName()) || !Text.empty(item.getProductSpecName())) {
                ShopDetail.ins().set(item).show();
            } else {
                String size = tv.getText().toString();
                int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
                if (num > item.getBarCount()) {
                    Toast.show("不能再多了");
                } else {
                    helper.setVisible(R.id.tv_btn_less, num != 0);
                    tv.setVisibility(num != 0 ? View.VISIBLE : View.GONE);
                    if (num > 0) {
                        tv.setText(String.valueOf(num));
                    } else {
                        tv.setText("");
                    }
                    item.setBuySize(num);
                    Cache.ins().attachShop(item, item.getProductFlavorName(), item.getProductSpecName());
                }
            }
        });
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
}
