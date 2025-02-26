package com.example.MaxiPet.config;

import com.example.MaxiPet.dto.EmailRequestDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.MaxiPet.config.AMQPConfig.EXCHANGE_NAME;
import static com.example.MaxiPet.config.AMQPConfig.ROUTING_KEY;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send(EmailRequestDto payload) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, payload);
    }
}

