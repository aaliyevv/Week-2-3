package com.ltc.paysnap.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CleanupScheduler {

    @Scheduled(fixedRate = 60000)
    public void cleanup() {
        // cleanup logic
    }
}