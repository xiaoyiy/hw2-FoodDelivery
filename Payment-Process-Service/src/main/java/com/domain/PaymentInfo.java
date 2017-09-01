package com.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@Table(name = "PAYMENT_INFO")
public class PaymentInfo {
    public enum PaymentStatus {
        SUCCESS, FAIL
    }

    @Id
    @GeneratedValue
    private long paymentId;

    private long orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime = new Date();
    private String cardHolder;
    private String cardNumber;
    private String expiration;
    private String cvv;
    private double amount;
    private PaymentStatus paymentStatus = PaymentStatus.SUCCESS;

    public PaymentInfo() {
    }
}
