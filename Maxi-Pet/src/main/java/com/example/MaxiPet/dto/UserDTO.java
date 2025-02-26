package com.example.MaxiPet.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {

    private Integer id;
    private String role;
    private String name;
    private String firstName;
    @Email(message = "The email is not valid!")
    private String email;
    //@Pattern(regexp = "")
    private String phone;
    private String password;
    private ShoppingCartDTO shoppingCartDTO;
    private List<OrderDTO> orderDTOList;
    private List<ReviewDTO> reviewDTOList;
}
