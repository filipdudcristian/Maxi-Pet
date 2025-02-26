package com.example.MaxiPet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class EmailRequestDto implements Serializable {
    @NotNull
    private Integer id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Email(message = "Invalid email format!")
    private String recipientEmail;

    @NotNull
    private String subject;

    @NotNull
    private String body;

    @NotNull
    private String fileType;
}
