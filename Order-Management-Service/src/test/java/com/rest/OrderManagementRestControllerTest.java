package com.rest;

import com.domain.OrderInfo;
import com.domain.OrderStatus;
import com.service.OrderManagementService;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//UNIT TEST
public class OrderManagementRestControllerTest {
    private OrderManagementService orderManagementService;
    OrderManagementRestController controller;

    @Before
    public void setupMock() {
        orderManagementService = mock(OrderManagementService.class);
        controller = new OrderManagementRestController(orderManagementService);
    }

    @Test
    public void testStatusUpdate() {
        OrderInfo order = new OrderInfo();
        order.setOrderStatus(OrderStatus.COMPLETED);
        when(orderManagementService.updateOrderStatus((long)1, OrderStatus.COMPLETED)).thenReturn(order);

        assertThat(controller.updatePaymentStatus((long)1, OrderStatus.COMPLETED).getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
    }
}
