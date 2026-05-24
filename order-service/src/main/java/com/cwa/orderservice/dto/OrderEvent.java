package com.cwa.orderservice.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderEvent(
        String orderId,
        String customerId,
        String productName,
        int quantity,
        BigDecimal totalAmount,
        String status,
        Instant createdAt
) {

    public static OrderEvent of(String orderId, String customerId, String productName, int quantity, BigDecimal totalAmount) {
        return new OrderEvent(orderId, customerId, productName, quantity, totalAmount, "CREATED", Instant.now());
    }
}
