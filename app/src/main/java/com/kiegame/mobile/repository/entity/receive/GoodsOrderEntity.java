package com.kiegame.mobile.repository.entity.receive;

import com.kiegame.mobile.utils.CustomUtils;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/11/11.
 * Description: 商品订单
 */
public class GoodsOrderEntity {


    /**
     * orderType :
     * payType : 0
     * orderId :
     * idCard :
     * buyChannel : 0
     * payTypeState : 0
     * customerName :
     * seatNumber :
     * orderState : 0
     * products : [{"process":{"makeTime":"","distributeTime":"","shareTime":"","acceptOrderTime":"","oppId":"","startTime":"","state":0,"timeLeft":0},"disCountAmount":0,"productFlavorName":"","productId":"","sellAmount":0,"productSpecName":"","sellCount":0,"sellPrice":0,"productName":"","createDate":""}]
     */
    private String orderType;
    private int payType;
    private String orderId;
    private String idCard;
    private int buyChannel;
    private int payTypeState;
    private int paymentPayTime;

    public int getPaymentPayTime() {
        return paymentPayTime;
    }

    public void setPaymentPayTime(int paymentPayTime) {
        this.paymentPayTime = paymentPayTime;
    }

    private String customerName;
    private String seatNumber;

    private List<ProductsEntity> products;

    public boolean isExpand() {
        return isExpand;
    }

    private boolean isExpand = true;

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setBuyChannel(int buyChannel) {
        this.buyChannel = buyChannel;
    }

    public void setPayTypeState(int payTypeState) {
        this.payTypeState = payTypeState;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }


    public void setProducts(List<ProductsEntity> products) {
        this.products = products;
    }

    public String getOrderType() {
        return orderType;
    }

    public int getPayType() {
        return payType;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getIdCard() {
        return idCard;
    }

    public int getBuyChannel() {
        return buyChannel;
    }

    public int getPayTypeState() {
        return payTypeState;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }


    public List<ProductsEntity> getProducts() {
        return products;
    }

    public class ProductsEntity {
        /**
         * process : {"makeTime":"","distributeTime":"","shareTime":"","acceptOrderTime":"","oppId":"","startTime":"","state":0,"timeLeft":0}
         * disCountAmount : 0
         * productFlavorName :
         * productId :
         * sellAmount : 0
         * productSpecName :
         * sellCount : 0
         * sellPrice : 0
         * productName :
         * createDate :
         */


        public String getOrderId() {
            return orderId;
        }


        public String getExpandInfo() {
            if (!products.isEmpty() && products.size() >= 2) {
                return products.get(0).getProductName() +
                        "、" +
                        products.get(1).getProductName() +
                        "等" + products.size() + "件商品";
            }
            return "";
        }


        public String getUserInfo() {

            return getSeatNumber() + "|" + CustomUtils.splitIdCard(getIdCard()) + "|" + getCustomerName();

        }


        private ProcessEntity process;
        private int disCountAmount;
        private String productFlavorName;
        private String productId;
        private int sellAmount;
        private String productSpecName;
        private int sellCount;
        private int sellPrice;
        private String productName;
        private String createDate;
        private int state;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setProcess(ProcessEntity process) {
            this.process = process;
        }

        public void setDisCountAmount(int disCountAmount) {
            this.disCountAmount = disCountAmount;
        }

        public void setProductFlavorName(String productFlavorName) {
            this.productFlavorName = productFlavorName;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setSellAmount(int sellAmount) {
            this.sellAmount = sellAmount;
        }

        public void setProductSpecName(String productSpecName) {
            this.productSpecName = productSpecName;
        }

        public void setSellCount(int sellCount) {
            this.sellCount = sellCount;
        }

        public void setSellPrice(int sellPrice) {
            this.sellPrice = sellPrice;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public ProcessEntity getProcess() {
            return process;
        }

        public int getDisCountAmount() {
            return disCountAmount;
        }

        public String getProductFlavorName() {
            return productFlavorName;
        }

        public String getProductId() {
            return productId;
        }

        public int getSellAmount() {
            return sellAmount;
        }

        public String getProductSpecName() {
            return productSpecName;
        }

        public int getSellCount() {
            return sellCount;
        }

        public int getSellPrice() {
            return sellPrice;
        }

        public String getProductName() {
            return productName;
        }

        public String getCreateDate() {
            return createDate;
        }

        public class ProcessEntity {
            /**
             * makeTime :
             * distributeTime :
             * shareTime :
             * acceptOrderTime :
             * oppId :
             * startTime :
             * state : 0
             * timeLeft : 0
             */
            //状态 1:待接单 2：待出品 3：配送中 4：已超时 5：已完成 6:抢单
            private String makeTime;
            private String distributeTime;
            private String shareTime;
            private String acceptOrderTime;
            private String oppId;
            private String startTime;
            private int state;
            private int timeLeft;

            public void setMakeTime(String makeTime) {
                this.makeTime = makeTime;
            }

            public void setDistributeTime(String distributeTime) {
                this.distributeTime = distributeTime;
            }

            public void setShareTime(String shareTime) {
                this.shareTime = shareTime;
            }

            public void setAcceptOrderTime(String acceptOrderTime) {
                this.acceptOrderTime = acceptOrderTime;
            }

            public void setOppId(String oppId) {
                this.oppId = oppId;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public void setState(int state) {
                this.state = state;
            }

            public void setTimeLeft(int timeLeft) {
                this.timeLeft = timeLeft;
            }

            public String getMakeTime() {
                return makeTime;
            }

            public String getDistributeTime() {
                return distributeTime;
            }

            public String getShareTime() {
                return shareTime;
            }

            public String getAcceptOrderTime() {
                return acceptOrderTime;
            }

            public String getOppId() {
                return oppId;
            }

            public String getStartTime() {
                return startTime;
            }

            public int getState() {
                return state;
            }

            public int getTimeLeft() {
                return timeLeft;
            }
        }
    }
}
