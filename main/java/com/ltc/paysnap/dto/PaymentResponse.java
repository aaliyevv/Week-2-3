package com.ltc.paysnap.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String paymentUrl;
    private String qrCodeBase64;
}