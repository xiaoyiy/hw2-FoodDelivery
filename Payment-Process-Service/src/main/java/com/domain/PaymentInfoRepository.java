package com.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    PaymentInfo getFirstByPaymentId(Long paymentId);
    void deleteByPaymentId(Long paymentId);
}
