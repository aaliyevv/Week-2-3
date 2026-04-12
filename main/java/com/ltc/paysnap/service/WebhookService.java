package com.ltc.paysnap.service;

import com.ltc.paysnap.entity.Order;
import com.ltc.paysnap.entity.PaymentSession;
import com.ltc.paysnap.entity.enums.PaymentStatus;
import com.ltc.paysnap.integration.ReceiptService;
import com.ltc.paysnap.repository.OrderRepository;
import com.ltc.paysnap.repository.PaymentSessionRepository;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final PaymentSessionRepository sessionRepository;
    private final OrderRepository orderRepository;
    private final ReceiptService receiptService;

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    public void handleStripeEvent(String payload, String sigHeader) throws Exception {

        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        if ("checkout.session.completed".equals(event.getType())) {

            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Invalid Stripe session"));

            PaymentSession paymentSession = sessionRepository
                    .findByStripeSessionId(session.getId())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            Order order = paymentSession.getOrder();

            order.setStatus(PaymentStatus.COMPLETED);
            order.setCompletedAt(LocalDateTime.now());

            paymentSession.setStatus(PaymentStatus.COMPLETED);

            orderRepository.save(order);
            sessionRepository.save(paymentSession);

            // generate receipt
            receiptService.generateReceipt(order);
        }
    }
}
