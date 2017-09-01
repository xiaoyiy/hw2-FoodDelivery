package com.service.impl;

import com.domain.OrderInfo;
import com.domain.OrderItem;
import com.domain.OrderRepository;
import com.domain.OrderStatus;
import com.service.NextSequenceService;
import com.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class OrderManagementServiceImpl implements OrderManagementService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    NextSequenceService nextSequenceService;
    @Autowired
    private MongoOperations mongo;

    @Override
    public OrderInfo saveOrder(OrderInfo orderInfo) {
        if (orderInfo.getOrderItems() != null) {
            double totalPrice = 0;
            for (OrderItem orderItem : orderInfo.getOrderItems()) {
                totalPrice += orderItem.getUnitPrice() * orderItem.getQuantity();
            }
            orderInfo.setTotalPrice(totalPrice);
        }
        if (!validateOrderInfo(orderInfo)) {
            orderInfo.setOrderStatus(OrderStatus.ERROR);
        }

        orderInfo.setOrderId(nextSequenceService.getNextSequence("orders"));
        return orderRepository.save(orderInfo);
    }

    @Override
    public OrderInfo findByOrderId(long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public OrderInfo updateOrderStatus(long orderId, OrderStatus orderStatus) {
        OrderInfo orderInfo = mongo.findAndModify(
                query(where("orderId").is(orderId)),
                new Update().set("orderStatus", orderStatus),
                options().returnNew(true).upsert(true),
                OrderInfo.class);
        return orderInfo;
    }

    @Override
    public void updateEstimatedTime(long orderId, int minutes) {
        OrderInfo orderInfo = findByOrderId(orderId);
        if (orderInfo != null) {
            Date createdTime = orderInfo.getCreatedTime();
            Date estimatedTime = new Date(createdTime.getTime() + minutes * 60000);
            mongo.findAndModify(
                    new Query().addCriteria(new Criteria().andOperator(
                            Criteria.where("orderId").is(orderId)
                    )),
                    new Update().set("estimatedTime", estimatedTime),
                    options().upsert(true), OrderInfo.class);
        }
    }

    @Override
    public void deleteByOrderId(long orderId) {
        orderRepository.deleteByOrderId(orderId);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }

    private boolean validateOrderInfo(OrderInfo orderInfo) {
        return orderInfo != null
                && orderInfo.getRestaurantId() > 0
                && !StringUtils.isEmpty(orderInfo.getAddress())
                && !StringUtils.isEmpty(orderInfo.getUser())
                && !CollectionUtils.isEmpty(orderInfo.getOrderItems());
    }
}
