package com.example.MaxiPet.service.filegenerator;

import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.OrderProduct;
import com.itextpdf.text.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class TextFileGenerator implements FileGenerator {
    @Override
    public Document generateFile(Order order) throws IOException {
        String str = "Hello";
        BufferedWriter writer = new BufferedWriter(new FileWriter("Text Receipt.txt"));

        for (OrderProduct orderProduct : order.getOrderProductList()) {
            String row = orderProduct.getProduct().getName() + ": quantity:" + orderProduct.getQuantity() + ", unit price:" +
                    orderProduct.getProduct().getDiscountedPrice() + ", total price:" +
                    orderProduct.getQuantity() * orderProduct.getProduct().getDiscountedPrice() + "\n";
            writer.write(row);
        }

        writer.write( "Total Price: " + order.getTotalPrice());

        writer.close();
        return null;
    }
}
