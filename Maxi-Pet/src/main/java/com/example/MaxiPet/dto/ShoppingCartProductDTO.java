package com.example.MaxiPet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShoppingCartProductDTO {
    private Integer id;
    private Integer shoppingCartId;
    private Integer productId;
    private ProductDTO productDTO;
    private Integer quantity;
}
