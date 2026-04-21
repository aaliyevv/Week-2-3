package com.ltc.paysnap.repository;

import com.ltc.paysnap.entity.PaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentSessionRepository extends JpaRepository<PaymentSession, Long> {
    Optional<PaymentSession> findByStripeSessionId(String sessionId);
}
