package com.git.onedayrex.nettyaction.nettyguide71.dto;

import java.io.Serializable;
import java.util.Date;

public class OrderResp implements Serializable {
    private static final long serialVersionUID = 5340006870565260643L;

    private String orderNo;

    private Date orderTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "OrderResp{" +
                "orderNo='" + orderNo + '\'' +
                ", orderTime=" + orderTime +
                '}';
    }
}
