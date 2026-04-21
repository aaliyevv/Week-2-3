package com.ltc.paysnap.integration;

import com.ltc.paysnap.entity.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session; // Use checkout.Session, not billingportal.Session
import com.stripe.param.checkout.SessionCreateParams; // Use checkout.SessionCreateParams
import org.springframework.beans.factory.annotation.Value; // Correct Spring @Value
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Session createSession(Order order) throws StripeException {

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/success")
                        .setCancelUrl("http://localhost:8080/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency(order.getCurrency())
                                                        .setUnitAmount((long) (order.getAmount() * 100))
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(order.getDescription())
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        return Session.create(params);
    }
}
