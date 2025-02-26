package com.example.MaxiPet.dto;

import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Builder

public class ReviewDTO {


    private Integer id;
    private String prodName;
    private String text;
    private Integer rating;
    private Integer productId;
    private Integer userId;


}
