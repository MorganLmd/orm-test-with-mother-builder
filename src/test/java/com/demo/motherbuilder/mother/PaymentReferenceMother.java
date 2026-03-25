package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.PaymentProcessor;
import com.demo.motherbuilder.entity.PaymentReference;
import com.demo.motherbuilder.entity.PaymentStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentReferenceMother {

    // ------------------------------------------------------------------ Constants

    public static final String PENDING_TRANSACTION_ID_PREFIX = "TXN-PENDING-";
    public static final String SUCCESS_TRANSACTION_ID_PREFIX = "TXN-SUCCESS-";

    // ------------------------------------------------------------------ Builder

    private final PaymentReference.PaymentReferenceBuilder paymentReferenceBuilder;

    // ------------------------------------------------------------------ Object Mother

    public static PaymentReferenceMother pendingPayment() {
        return new PaymentReferenceMother(
                PaymentReference.builder()
                        .transactionId(PENDING_TRANSACTION_ID_PREFIX + System.nanoTime())
                        .status(PaymentStatus.PENDING)
                        .paymentProcessor(PaymentProcessor.STRIPE)
        );
    }

    public static PaymentReferenceMother successfulPayment() {
        return new PaymentReferenceMother(
                PaymentReference.builder()
                        .transactionId(SUCCESS_TRANSACTION_ID_PREFIX + System.nanoTime())
                        .status(PaymentStatus.SUCCESS)
                        .paymentProcessor(PaymentProcessor.PAYPAL)
        );
    }

    // ------------------------------------------------------------------ Test Data Builder

    public PaymentReferenceMother withStatus(PaymentStatus status) {
        this.paymentReferenceBuilder.status(status);
        return this;
    }

    // ------------------------------------------------------------------ final build

    public PaymentReference build() {
        return paymentReferenceBuilder.build();
    }
}
