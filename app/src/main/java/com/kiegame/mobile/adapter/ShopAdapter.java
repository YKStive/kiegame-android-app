package com.kiegame.mobile.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.kiegame.mobile.R;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/12.
 * Description: 商品列表适配器
 */
public class ShopAdapter extends BaseMultiItemQuickAdapter<ShopAdapter.ShopEntity, BaseViewHolder> {

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
                break;
            case ShopEntity.TITLE:
                break;
        }
    }

    public static class ShopEntity implements MultiItemEntity {

        public final static int CONTENT = 0;
        public final static int TITLE = 1;

        private int itemType;

        public ShopEntity(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
