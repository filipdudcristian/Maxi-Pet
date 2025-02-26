package com.example.MaxiPet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDTO {

    private Integer orderId;
    private ProductDTO productDTO;
    private Integer quantity;
}
