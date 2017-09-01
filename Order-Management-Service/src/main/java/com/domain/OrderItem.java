package com.domain;

import lombok.Data;

@Data
public class OrderItem {
    private String itemName;
    private Double unitPrice;
    private Integer quantity;
}