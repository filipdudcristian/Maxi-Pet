package com.example.MaxiPet.dto.Builders;

import com.example.MaxiPet.dto.ShoppingCartDTO;
import com.example.MaxiPet.entity.ShoppingCart;

public class ShoppingCartBuilder {

    public static ShoppingCartDTO toShoppingCartDTO(ShoppingCart shoppingCart) {
        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .userId(shoppingCart.getUser().getId())
                .shoppingCartProductDTOList(ShoppingCartProductBuilder.toShoppingCartProductDTOList(shoppingCart.getShoppingCartProductList()))
                .totalPrice(shoppingCart.getTotalPrice())
                .build();
    }
}
