package com.example.MaxiPet.service.filegenerator;

public class FileGeneratorFactory {
    public static FileGenerator getFileGenerator(String fileType) {
        switch (fileType.toLowerCase()) {
            case "text":
                return new TextFileGenerator();
            case "pdf":
                return new PdfFileGenerator();
            case "csv":
                return new CsvFileGenerator();
            default:
                throw new IllegalArgumentException("Unknown file type: " + fileType);
        }
    }
}