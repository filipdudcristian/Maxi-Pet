package emailsender.controller;

import emailsender.dtos.EmailRequestDto;
import emailsender.dtos.ResponseDto;
import emailsender.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailController {
    public static final String FIRST_TOKEN = "41b3fbb8-fea8-11ee-8ff1-325096b39f4-";
    public static final String SECOND_TOKEN = "7ef1dea2-feab-11ee-a7c6-325096b39f47";

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<ResponseDto> sendEmail(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody EmailRequestDto emailRequestDto,
            BindingResult bindingResult) {

        ResponseDto responseDto = new ResponseDto();

        if(!isTokenValid(token)){
            responseDto.setMessage("Invalid authorization token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        if(bindingResult.hasErrors()){
            responseDto.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        emailService.sendEmail(emailRequestDto);
        responseDto.setMessage("Email successfully sent!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    private boolean isTokenValid(String token){
        String validToken = FIRST_TOKEN + SECOND_TOKEN;
        String t = token.substring(7);
        return validToken.equals(t);
    }
}
