package com.git.onedayrex.nettyaction.nettyguide91.dto;

import java.io.Serializable;

public class OrderReq implements Serializable {
    private static final long serialVersionUID = 417569821835114275L;

    private String orderNo;

    private String goodName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    @Override
    public String toString() {
        return "OrderReq{" +
                "orderNo='" + orderNo + '\'' +
                ", goodName='" + goodName + '\'' +
                '}';
    }
}
