package com.service;

import com.domain.OrderInfo;
import com.domain.OrderStatus;

public interface OrderManagementService {
    OrderInfo saveOrder(OrderInfo orderInfo);
    OrderInfo findByOrderId(long orderId);
    OrderInfo updateOrderStatus(long orderId, OrderStatus orderStatus);
    void updateEstimatedTime(long orderId, int minutes);
    void deleteByOrderId(long orderId);
    void deleteAll();
}
