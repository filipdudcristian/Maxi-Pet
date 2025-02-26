package com.example.MaxiPet.service.filegenerator;

import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.OrderProduct;
import com.itextpdf.text.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CsvFileGenerator implements FileGenerator {
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }
    public String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    @Override
    public Document generateFile(Order order) {
        File csvOutputFile = new File("CsvFile.csv");
        List<String[]> dataLines = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProductList()) {
            dataLines.add(new String[] {
                    orderProduct.getProduct().getName(),
                    orderProduct.getQuantity().toString(),
                    orderProduct.getProduct().getDiscountedPrice().toString(),
                    String.valueOf((orderProduct.getQuantity() * orderProduct.getProduct().getDiscountedPrice()))
            });

        }
        dataLines.add(new String[] {"Total Price", order.getTotalPrice().toString()});

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
