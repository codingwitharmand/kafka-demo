package com.cwa.notificationservice.dto;

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
}
