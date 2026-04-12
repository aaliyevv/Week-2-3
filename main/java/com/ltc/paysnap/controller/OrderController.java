package com.ltc.paysnap.controller;

import com.ltc.paysnap.dto.CreateOrderRequest;
import com.ltc.paysnap.dto.OrderHistoryResponse;
import com.ltc.paysnap.dto.PaymentResponse;
import com.ltc.paysnap.entity.User;
import com.ltc.paysnap.security.CustomUserDetails;
import com.ltc.paysnap.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(
            @RequestBody CreateOrderRequest request
    ) throws Exception {

        // TEMP: replace with authenticated user later
        User user = new User();
        user.setId(1L);

        return ResponseEntity.ok(orderService.createOrder(request, user));
    }

    @GetMapping("/my")
    public List<OrderHistoryResponse> getMyOrders() {
        return orderService.getMyOrders();
    }
}
