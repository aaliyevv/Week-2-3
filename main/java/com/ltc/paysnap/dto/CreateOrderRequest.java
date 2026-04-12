package com.ltc.paysnap.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Double amount;
    private String currency;
    private String description;
}
