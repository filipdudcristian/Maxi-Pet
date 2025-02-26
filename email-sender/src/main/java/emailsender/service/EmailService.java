package emailsender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import emailsender.dtos.EmailRequestDto;
import emailsender.entities.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import repository.EmailRepository;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ObjectMapper objectMapper;

    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public class EmailTemplateLoader {
        public String loadTemplate(String filePath) {
            try {
                return new String(Files.readAllBytes(Paths.get(filePath)));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void sendEmail(EmailRequestDto emailRequestDto) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(emailRequestDto);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("cristianfilipdud@gmail.com", "Maxi-Pet");
            helper.setTo(emailRequestDto.getRecipientEmail());
            helper.setSubject(emailRequestDto.getSubject());
            EmailTemplateLoader loader = new EmailTemplateLoader();
            String htmlTemplate = null;
            try {
                htmlTemplate = StreamUtils.copyToString(
                        new ClassPathResource("templates/email.html").getInputStream(),
                        StandardCharsets.UTF_8);
            } catch (IOException e) {

                e.printStackTrace();
            }
            String modifiedHtmlContent = htmlTemplate.replace("$BODY_CONTENT$", emailRequestDto.getBody());
            helper.setText(modifiedHtmlContent, true);

            // Attach chosen file to email
            String filePath = "E:\\Anu3 Sem2\\PS\\Maxi-Pet\\";
            String fileType;

            if(emailRequestDto.getFileType().equals("text")) {

                fileType = "Text Receipt.txt";

                filePath = filePath + fileType;

                FileSystemResource file = new FileSystemResource(new File(filePath));
                helper.addAttachment(fileType, file);
            }
            else if (emailRequestDto.getFileType().equals("pdf")) {
                fileType = "Order Receipt.pdf";
                filePath = filePath + fileType;

                FileSystemResource file = new FileSystemResource(new File(filePath));
                helper.addAttachment(fileType, file);
            }
            else if(emailRequestDto.getFileType().equals("csv")) {
                fileType = "CsvFile.csv";
                filePath = filePath + fileType;

                FileSystemResource file = new FileSystemResource(new File(filePath));
                helper.addAttachment(fileType, file);
            }



            /*filePath = filePath + fileType;

            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(fileType, file);*/

            // Send email
            emailSender.send(message);

            Email emailEntity = new Email();
            emailEntity.setRecipientEmail(emailRequestDto.getRecipientEmail());
            emailEntity.setSubject(emailRequestDto.getSubject());
            emailEntity.setLastName(emailRequestDto.getLastName());
            emailEntity.setFirstName(emailRequestDto.getFirstName());
            emailEntity.setBody(emailRequestDto.getBody());
            emailRepository.save(emailEntity);

        } catch (JsonProcessingException | MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
