package com.example.MaxiPet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShoppingCartDTO {

    private Integer id;
    private Integer userId;
    private List<ShoppingCartProductDTO> shoppingCartProductDTOList;
    private Float totalPrice;
}
