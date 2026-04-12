package com.ltc.paysnap.integration;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.ltc.paysnap.entity.Order;
import com.ltc.paysnap.entity.Receipt;
import com.ltc.paysnap.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public void generateReceipt(Order order) throws Exception {

        // ✅ Create folder if not exists
        File folder = new File("receipts");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = "receipt_" + order.getId() + ".pdf";
        String filePath = "receipts/" + fileName;

        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Payment Receipt"));
        document.add(new Paragraph("Order ID: " + order.getId()));
        document.add(new Paragraph("Amount: " + order.getAmount()));
        document.add(new Paragraph("Currency: " + order.getCurrency()));
        document.add(new Paragraph("Date: " + order.getCompletedAt()));

        document.close();

        // ✅ Save to DB
        Receipt receipt = Receipt.builder()
                .filePath(filePath)
                .order(order)
                .build();

        receiptRepository.save(receipt);
    }
}