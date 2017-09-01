package com.service;

import com.domain.PaymentInfo;

public interface PaymentProcessService {
    PaymentInfo savePaymentInfo(PaymentInfo paymentInfo);
    PaymentInfo getPaymentInfo(Long paymentId);
    void deleteByPaymentId(Long paymentId);
    void deleteAll();
}
