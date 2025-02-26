package emailsender.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import emailsender.dtos.EmailRequestDto;
import emailsender.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "email-queue")
    public void processEmailRequest(String in) {
        try {
            EmailRequestDto emailRequestDto = objectMapper.readValue(in, EmailRequestDto.class);
            System.out.println(emailRequestDto);
            emailService.sendEmail(emailRequestDto);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
}

