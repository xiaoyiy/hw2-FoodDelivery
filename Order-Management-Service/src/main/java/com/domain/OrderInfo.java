package com.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "orders")
public class OrderInfo {
    @Id
    private long orderId;
    private long restaurantId;
    private String user;
    private String address;
    private String note;
    private List<OrderItem> orderItems;
    private Double totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime = new Date();
    private OrderStatus orderStatus = OrderStatus.NEW;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date estimatedTime;
}
