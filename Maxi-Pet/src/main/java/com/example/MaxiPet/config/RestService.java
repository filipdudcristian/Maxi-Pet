package com.example.MaxiPet.config;

import com.example.MaxiPet.entity.EmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    public static final String FIRST_TOKEN = "41b3fbb8-fea8-11ee-8ff1-325096b39f4-";
    public static final String SECOND_TOKEN = "7ef1dea2-feab-11ee-a7c6-325096b39f47";

    public static final  String authToken=FIRST_TOKEN+SECOND_TOKEN;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RestService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEmailToMailApp(EmailRequest emailRequestDto) {
        try {
            // Convert EmailRequestDto to JSON string
            String jsonPayload = objectMapper.writeValueAsString(emailRequestDto);

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + authToken); // Include the authorization token here

            // Set up request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

            // Make the HTTP POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/emails/sendEmail", // URL of your email app
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Handle response
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Email successfully sent!");
            } else {
                System.out.println("Failed to send email. Response: " + response.getBody());
            }
        } catch (JsonProcessingException e) {
            // Handle JSON processing exception
            e.printStackTrace();
        }
    }
}

