package com.example.MaxiPet.service.filegenerator;

import com.example.MaxiPet.entity.Order;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface FileGenerator {
    Document generateFile(Order order) throws IOException, DocumentException;
}
