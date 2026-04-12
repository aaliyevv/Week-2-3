package com.ltc.paysnap.dto;

import com.ltc.paysnap.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderHistoryResponse {

    private Long id;
    private Double amount;
    private String currency;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}