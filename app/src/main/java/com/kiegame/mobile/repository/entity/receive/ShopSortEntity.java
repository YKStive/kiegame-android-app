package com.kiegame.mobile.repository.entity.receive;

import java.util.Objects;

/**
 * Created by: var_rain.
 * Created date: 2020/1/19.
 * Description: 商品分类
 */
public class ShopSortEntity {

    /**
     * productTypeName : 饮品
     * sort : 3
     * productTypeId : TY3523798589635584
     */

    private String productTypeName;
    private String productTagName;
    private int sort;
    private String productTypeId;
    private String productTagId;

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTagName() {
        return productTagName;
    }

    public void setProductTagName(String productTagName) {
        this.productTagName = productTagName;
    }

    public String getProductTagId() {
        return productTagId;
    }

    public void setProductTagId(String productTagId) {
        this.productTagId = productTagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopSortEntity that = (ShopSortEntity) o;
        return Objects.equals(productTypeName, that.productTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productTypeName);
    }
}
