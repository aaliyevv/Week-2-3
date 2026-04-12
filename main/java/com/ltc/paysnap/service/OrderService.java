package com.ltc.paysnap.service;

import com.ltc.paysnap.dto.CreateOrderRequest;
import com.ltc.paysnap.dto.OrderHistoryResponse;
import com.ltc.paysnap.dto.PaymentResponse;
import com.ltc.paysnap.entity.Order;
import com.ltc.paysnap.entity.PaymentSession;
import com.ltc.paysnap.entity.User;
import com.ltc.paysnap.entity.enums.PaymentStatus;
import com.ltc.paysnap.integration.StripeService;
import com.ltc.paysnap.repository.OrderRepository;
import com.ltc.paysnap.repository.PaymentSessionRepository;
import com.ltc.paysnap.security.CustomUserDetails;
import com.ltc.paysnap.util.AuthUtil;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ltc.paysnap.integration.QRCodeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentSessionRepository sessionRepository;
    private final AuthUtil authUtil;
    private final StripeService stripeService;
    private final QRCodeService qrCodeService;

    public PaymentResponse createOrder(CreateOrderRequest request, User user) throws Exception {

        // 1. Get logged-in user

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        user = userDetails.getUser();

        // 2. Create Order
        Order order = Order.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        orderRepository.save(order);

        // 3. Create Stripe Session
        Session session = stripeService.createSession(order);

        // 4. Generate QR Code
        String qrBase64 = qrCodeService.generateBase64QR(session.getUrl());

        // 5. Save Payment Session
        PaymentSession paymentSession = PaymentSession.builder()
                .stripeSessionId(session.getId())
                .paymentUrl(session.getUrl())
                .status(PaymentStatus.PENDING)
                .order(order)
                .build();

        sessionRepository.save(paymentSession);

        // 6. Return response
        return PaymentResponse.builder()
                .paymentUrl(session.getUrl())
                .qrCodeBase64(qrBase64)
                .build();
    }

    public List<OrderHistoryResponse> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> OrderHistoryResponse.builder()
                        .id(order.getId())
                        .amount(order.getAmount())
                        .currency(order.getCurrency())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .completedAt(order.getCompletedAt())
                        .build())
                .toList();
    }

    public List<OrderHistoryResponse> getMyOrders() {

        User user = authUtil.getCurrentUser();

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(order -> OrderHistoryResponse.builder()
                        .id(order.getId())
                        .amount(order.getAmount())
                        .currency(order.getCurrency())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .completedAt(order.getCompletedAt())
                        .build())
                .toList();
    }
}
