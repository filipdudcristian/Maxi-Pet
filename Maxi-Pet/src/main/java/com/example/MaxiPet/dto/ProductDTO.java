package com.example.MaxiPet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDTO {

    private Integer id;
    private String name;
    private Float basePrice;
    private Integer discountPercent;
    private Float discountedPrice;
    private Integer stock;
    private String category;
    private String image;
    //private List<ShoppingCartProductDTO> shoppingCartProductDTOList;
    //private List<OrderProductDTO> orderProductDTOList;
    private List<ReviewDTO> reviewDTOList;
}
