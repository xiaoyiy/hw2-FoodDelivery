package com;


import com.domain.OrderStatus;
import com.domain.PaymentInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.OrderManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;

import java.io.IOException;
import java.util.Random;

@EnableBinding(Sink.class)
@Slf4j
public class OrderManagementServiceSink {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderManagementService orderManagementService;

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void updateOrder(String input) throws IOException {
        log.info("PaymentInfo input in updater: " + input);
        PaymentInfo payload = this.objectMapper.readValue(input, PaymentInfo.class);
        OrderStatus orderStatus;
        if (payload.getPaymentStatus() == PaymentInfo.PaymentStatus.SUCCESS) {
            orderStatus = OrderStatus.COMPLETED;
        } else {
            orderStatus = OrderStatus.CANCELLED;
        }
        orderManagementService.updateOrderStatus(payload.getOrderId(), orderStatus);
        if (orderStatus == OrderStatus.COMPLETED) {
            Random rand = new Random();
            int minutes = rand.nextInt(56) + 5;
            orderManagementService.updateEstimatedTime(payload.getOrderId(), minutes);
        }
    }
}
