package com.example.MaxiPet.service.filegenerator;

import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.OrderProduct;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfFileGenerator implements FileGenerator{
    @Override
    public Document generateFile(Order order) throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Order Receipt.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        for (OrderProduct orderProduct : order.getOrderProductList()) {
           String row = orderProduct.getProduct().getName() + ": quantity:" + orderProduct.getQuantity() + ", unit price:" +
                    orderProduct.getProduct().getDiscountedPrice() + ", total price:" +
                    orderProduct.getQuantity() * orderProduct.getProduct().getDiscountedPrice() + "\n";
            Paragraph helloWorldParagraph = new Paragraph(row, font);
            document.add(helloWorldParagraph);
        }

        Paragraph helloWorldParagraph = new Paragraph("Total Price: " + order.getTotalPrice(), font);
        document.add(helloWorldParagraph);
        document.close();
        return document;
    }
}
