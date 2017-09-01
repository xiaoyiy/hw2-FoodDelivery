package com.service.impl;

import com.domain.PaymentInfo;
import com.domain.PaymentInfoRepository;
import com.service.PaymentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
public class PaymentProcessServiceImpl implements PaymentProcessService {
    PaymentInfoRepository paymentInfoRepository;

    @Autowired
    public PaymentProcessServiceImpl(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @Override
    public PaymentInfo savePaymentInfo(PaymentInfo paymentInfo) {
        if (!validatePaymentInfo(paymentInfo))
            paymentInfo.setPaymentStatus(PaymentInfo.PaymentStatus.FAIL);
        return paymentInfoRepository.save(paymentInfo);
    }

    @Override
    public PaymentInfo getPaymentInfo(Long paymentId) {
        return paymentInfoRepository.getFirstByPaymentId(paymentId);
    }

    @Override
    @Transactional
    public void deleteByPaymentId(Long paymentId) {
        paymentInfoRepository.deleteByPaymentId(paymentId);
    }

    @Override
    public void deleteAll() {
        paymentInfoRepository.deleteAll();
    }

    // simplified version of credit card billing, simply checking if data is valid
    private boolean validatePaymentInfo(PaymentInfo paymentInfo) {
        return paymentInfo != null &&
                !StringUtils.isEmpty(paymentInfo.getCardHolder()) &&
                paymentInfo.getOrderId() > 0 &&
                validateCardNumber(paymentInfo) &&
                validateCVV(paymentInfo);
    }

    private static boolean validateCardNumber(PaymentInfo paymentInfo) {
        if (paymentInfo == null || paymentInfo.getCardNumber() == null) {
            return false;
        }
        String cardNumber = paymentInfo.getCardNumber();
        return cardNumber.matches("^[0-9]+$") && cardNumber.length() >= 15 && cardNumber.length() <= 16;
    }

    private static boolean validateCVV(PaymentInfo paymentInfo) {
        return paymentInfo != null && paymentInfo.getCvv().matches("^[0-9]+$")
                && paymentInfo.getCvv().length() >= 3 && paymentInfo.getCvv().length() <= 4;
    }
}
