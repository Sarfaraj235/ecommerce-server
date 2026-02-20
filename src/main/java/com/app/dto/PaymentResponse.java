package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    // Keep these for compatibility with old Razorpay-style frontend
    private String payment_link_id;
    private String payment_link_url;

    // Stripe-specific
    private String stripeSessionId;
    private String stripeCheckoutUrl;
    private String status;   // CREATED / SUCCESS / FAILED / CANCELLED
    private String message;
}
