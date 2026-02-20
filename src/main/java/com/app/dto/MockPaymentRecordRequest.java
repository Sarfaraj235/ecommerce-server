package com.app.dto;

import com.app.pojos.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockPaymentRecordRequest {
    private Long orderId;
    private String paymentMethod; // card/upi/cod
    private PaymentStatus status; // SUCCESS/FAILED/CANCELLED...
    private String paymentId;     // mock txn id
    private Double amount;
    private String currency;      // INR
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewaySignature;
    private String message;
}
