package com.kiegame.mobile.repository.entity.receive;

import java.util.List;

/**
 * 服务->商品接单
 * 商品订单实体类
 */
public class GoodsOrderEntity {


    // TODO: 2020/11/11 需要自己补充字段

    //商品列表
    private List<SingleOrderEntity> singleOrderEntityList;
    //列表是否展开 默认展开
    private boolean isExpand = true;

    public List<SingleOrderEntity> getSingleOrderEntityList() {
        return singleOrderEntityList;
    }

    public void setSingleOrderEntityList(List<SingleOrderEntity> singleOrderEntityList) {
        this.singleOrderEntityList = singleOrderEntityList;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public static class SingleOrderEntity {
        //状态 1:待接单 2：待出品 3：配送中 4：已超时 5：已完成 6:抢单
        private int state;

        public SingleOrderEntity(){}

        public SingleOrderEntity(int state){
            this.state = state;
        }


        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
