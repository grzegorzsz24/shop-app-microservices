package org.example.orderservice.domain.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.example.orderservice.application.dto.CartItemDto;
import org.example.orderservice.domain.model.order.Order;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class PdfGeneratorService {

    public byte[] generateOrderSummary(Order order) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        generateContent(order, document);

        document.close();

        return baos.toByteArray();
    }

    private static void generateContent(Order order, Document document) {
        Font titleFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        document.add(new Paragraph("Order Summary", titleFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Order number: " + order.getId()));
        document.add(new Paragraph("Orderer email: " + order.getOrdererEmail()));
        document.add(new Paragraph("Order date: " + order.getOrderDate().toString()));
        document.add(new Paragraph(" "));

        Font productFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        document.add(new Paragraph("Ordered products:", productFont));

        for (CartItemDto cartItem : order.getOrderedProducts()) {
            document.add(new Paragraph(
                    "- " + cartItem.name()
                            + " (Amount: " + cartItem.quantity() + ", Price: " + cartItem.price() + ")",
                    productFont
            ));
        }

        document.add(new Paragraph(" "));
        BigDecimal totalPrice = order.getPrice();
        document.add(new Paragraph("Total amount: " + totalPrice + " USD", productFont));
    }
}
