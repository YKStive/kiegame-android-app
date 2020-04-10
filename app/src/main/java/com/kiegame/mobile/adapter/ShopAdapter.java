package com.kiegame.mobile.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
                int sum = Cache.ins().getShopSumById(item.getProductId());
                ImageView view = helper.getView(R.id.iv_shop_image);
//                Glide.with(helper.itemView).load(item.getProductImg())
//                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(6)))
//                        .into(view);
                // #833 后台设置多张图片，选择商品/商品详情界面图片展示失败
                Glide.with(helper.itemView).load(item.getShopImage())
                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(6)))
                        .into(view);
                helper.setText(R.id.tv_shop_name, item.getProductName());
                helper.setText(R.id.tv_shop_norm, item.getProductSpecName());
                helper.setText(R.id.tv_shop_money, cal(item.getSellPrice()));
                helper.setText(R.id.tv_shop_num, String.valueOf(sum));
                helper.setVisible(R.id.tv_btn_less, sum != 0);
                helper.setVisible(R.id.tv_shop_num, sum != 0);
                helper.setVisible(R.id.tv_bar_count, item.getProductVariety() == 1);
                if (item.getProductVariety() == 1) {
                    int count = item.getBarCount() - sum;
                    helper.setText(R.id.tv_bar_count, count == 0 ? "已售罄" : (count > 99999 ? String.format("剩余: %s件", 99999) : String.format("剩余: %s件", count)));
//                    helper.setText(R.id.tv_bar_count, count <= 10 ? (count == 0 ? "已售空" : String.format("剩余: %s件", count)) : "");
                }
                setPlusShopClickListener(helper, item);
                setLessShopClickListener(helper, item);
                view.setOnClickListener(v -> {
                    if (item.getProductVariety() == 1 && sum == item.getBarCount()) {
                        // 固装商品并且已售空
                        Toast.show("该商品已售空");
                        return;
                    }
                    v.getContext()
                            .startActivity(new Intent(v.getContext(), ShopDetailActivity.class)
                                    .putExtra(Setting.APP_SHOP_ENTITY, item));
                });
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
            if (needTips(item) && num != 0) {
                Toast.show(String.format("多%s的商品只能去购物车删除", needFlavorTips(item) ? "规格" : "口味"));
                return;
            }
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
                if (item.getProductVariety() == 1) {
                    int count = item.getBarCount() - num;
                    helper.setText(R.id.tv_bar_count, count == 0 ? "已售罄" : (count > 99999 ? String.format("剩余: %s件", 99999) : String.format("剩余: %s件", count)));
                }
                Cache.ins().detachShop(item.getProductId());
            }
        });
    }

    /**
     * 设置添加购物车点击事件处理
     */
    private void setPlusShopClickListener(@NonNull BaseViewHolder helper, ShopEntity item) {
        helper.getView(R.id.tv_btn_plus).setOnClickListener(v -> {
            if (item.getProductVariety() == 1 && item.getBarCount() <= 0) {
                Toast.show("这种商品已经售空了");
                return;
            }
            TextView tv = helper.getView(R.id.tv_shop_num);
            if (!Text.empty(item.getProductFlavorName()) || !Text.empty(item.getProductSpecName())) {
                ShopDetail.ins().set(item).show();
            } else {
                String size = tv.getText().toString();
                int num = Text.empty(size) ? 1 : Integer.parseInt(size) + 1;
                if (item.getProductVariety() == 1 && num > item.getBarCount()) {
                    Toast.show("不能再多了");
                } else {
                    helper.setVisible(R.id.tv_btn_less, num != 0);
                    tv.setVisibility(num != 0 ? View.VISIBLE : View.GONE);
                    if (num > 0) {
                        tv.setText(String.valueOf(num));
                    } else {
                        tv.setText("");
                    }
                    if (item.getProductVariety() == 1) {
                        int count = item.getBarCount() - num;
                        helper.setText(R.id.tv_bar_count, count == 0 ? "已售罄" : (count > 99999 ? String.format("剩余: %s件", 99999) : String.format("剩余: %s件", count)));
                    }
                    Cache.ins().attachShop(item, item.getProductFlavorName(), item.getProductSpecName());
                }
            }
        });
    }

    /**
     * 是否需要提示不能直接删除
     *
     * @param item 商品数据对象
     * @return true:需要提示 false:不需要
     */
    private boolean needTips(ShopEntity item) {
        return needFlavorTips(item) || needSpecTips(item);
    }

    /**
     * 规格是否大于1种
     *
     * @param item 商品数据对象
     * @return true:是 false:不是
     */
    private boolean needFlavorTips(ShopEntity item) {
        String flavor = item.getProductFlavorName();
        if (Text.empty(flavor)) {
            return false;
        }
        String[] fs = flavor.split(",");
        return fs.length > 1;
    }

    /**
     * 口味是否大于1种
     *
     * @param item 商品数据对象
     * @return true:是 false:不是
     */
    private boolean needSpecTips(ShopEntity item) {
        String spec = item.getProductSpecName();
        if (Text.empty(spec)) {
            return false;
        }
        String[] ss = spec.split(",");
        return ss.length > 1;
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
