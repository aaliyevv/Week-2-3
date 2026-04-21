package com.ltc.paysnap.controller;

import com.ltc.paysnap.entity.Receipt;
import com.ltc.paysnap.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptRepository receiptRepository;

    @GetMapping("/{orderId}")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable Long orderId) throws Exception {

        Receipt receipt = receiptRepository.findAll()
                .stream()
                .filter(r -> r.getOrder().getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        Path path = Paths.get(receipt.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + path.getFileName())
                .body(resource);
    }
}