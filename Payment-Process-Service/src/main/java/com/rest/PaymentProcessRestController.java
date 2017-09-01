package com.rest;

import com.domain.PaymentInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.PaymentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.*;

@EnableBinding(Source.class)
@RestController
public class PaymentProcessRestController {
    @Autowired
    private PaymentProcessService paymentProcessService;
    @Autowired
    private MessageChannel output;
    @Autowired
    private ObjectMapper objectMapper;

    public PaymentProcessRestController() {
    }

//    @Autowired
//    public PaymentProcessRestController(PaymentProcessService paymentProcessService, MessageChannel messageChannel,
//                                        ObjectMapper objectMapper) {
//        this.paymentProcessService = paymentProcessService;
//        this.output = messageChannel;
//        this.objectMapper = objectMapper;
//    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public PaymentInfo upload(@RequestBody PaymentInfo paymentInfo) throws Exception {
        PaymentInfo savedPaymentInfo = paymentProcessService.savePaymentInfo(paymentInfo);
        output.send(MessageBuilder.withPayload(objectMapper.writeValueAsString(savedPaymentInfo)).build());
        return savedPaymentInfo;
    }

    @RequestMapping(value = "/payment/{paymentId}", method = RequestMethod.GET)
    public PaymentInfo getPaymentInfo(@PathVariable Long paymentId) {
        return paymentProcessService.getPaymentInfo(paymentId);
    }

    @RequestMapping(value = "/payment/{paymentId}", method = RequestMethod.DELETE)
    public void deleteByPaymentId(@PathVariable Long paymentId) {
        paymentProcessService.deleteByPaymentId(paymentId);
    }

    @RequestMapping(value = "/payment", method = RequestMethod.DELETE)
    public void purge() {
        paymentProcessService.deleteAll();
    }
}
