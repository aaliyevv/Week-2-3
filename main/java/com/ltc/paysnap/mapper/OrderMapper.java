package com.ltc.paysnap.mapper;

import com.ltc.paysnap.dto.OrderHistoryResponse;
import com.ltc.paysnap.entity.Order;

public class OrderMapper {

    public static OrderHistoryResponse toDto(Order order) {
        return OrderHistoryResponse.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .currency(order.getCurrency())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .completedAt(order.getCompletedAt())
                .build();
    }
}
