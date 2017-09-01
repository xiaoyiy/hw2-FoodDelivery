package com.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<OrderInfo, Long> {
    OrderInfo findByOrderId(long orderId);
    void deleteByOrderId(long orderId);
}
