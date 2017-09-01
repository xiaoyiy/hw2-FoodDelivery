package com.domain;

import com.OrderManagementServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


//INTEGRATION TEST
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OrderManagementServiceApplication.class)
@WebAppConfiguration
public class OrderRepositoryTest {
    @Autowired
    OrderRepository repository;

    @Test
    public void saveOrder() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAddress("123street");
        orderInfo.setOrderId(10000);
        repository.save(orderInfo);
        assertThat(repository.findByOrderId((long) 10000).getAddress()).isEqualTo("123street");
    }
}
