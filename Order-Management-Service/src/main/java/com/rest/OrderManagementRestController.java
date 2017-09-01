package com.rest;

import com.domain.OrderInfo;
import com.domain.OrderStatus;
import com.service.OrderManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OrderManagementRestController {
    private OrderManagementService orderManagementService;

    @Autowired
    public OrderManagementRestController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderInfo upload(@RequestBody OrderInfo orderInfo) {
        return orderManagementService.saveOrder(orderInfo);
    }

    @RequestMapping(value = "/order/status/{orderId}", method = RequestMethod.POST)
    public OrderInfo updatePaymentStatus(@PathVariable Long orderId,
                                         @RequestParam(name = "status") OrderStatus orderStatus) {
        return orderManagementService.updateOrderStatus(orderId, orderStatus);
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public OrderInfo getOrder(@PathVariable Long orderId) {
        return orderManagementService.findByOrderId(orderId);
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.DELETE)
    public void deleteByOrderId(@PathVariable Long orderId) {
        orderManagementService.deleteByOrderId(orderId);
    }

    @RequestMapping(value = "/order", method = RequestMethod.DELETE)
    public void purge() {
        orderManagementService.deleteAll();
    }
}
