package com.ltc.paysnap.controller;

import com.ltc.paysnap.service.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) throws Exception {

        String payload = new String(request.getInputStream().readAllBytes());
        String sigHeader = request.getHeader("Stripe-Signature");

        webhookService.handleStripeEvent(payload, sigHeader);

        return ResponseEntity.ok("Success");
    }
}