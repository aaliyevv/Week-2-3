package com.ltc.paysnap.entity;

import com.ltc.paysnap.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripeSessionId;
    private String paymentUrl;
    private String qrCodePath;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order order;
}
