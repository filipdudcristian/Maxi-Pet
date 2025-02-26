package com.example.MaxiPet.dto.Builders;

import com.example.MaxiPet.dto.ShoppingCartProductDTO;
import com.example.MaxiPet.entity.ShoppingCartProduct;

import java.util.Collections;
import java.util.List;

public class ShoppingCartProductBuilder {


    public static ShoppingCartProductDTO toShoppingCartProductDTO(ShoppingCartProduct shoppingCartProduct) {
        return ShoppingCartProductDTO.builder()
                .id(shoppingCartProduct.getId())
                .quantity(shoppingCartProduct.getQuantity())
                .productId(shoppingCartProduct.getProduct().getId())
                .productDTO(ProductBuilder.toProductDTO(shoppingCartProduct.getProduct()))
                .shoppingCartId(shoppingCartProduct.getShoppingCart().getId())
                .build();
    }

    public static List<ShoppingCartProductDTO> toShoppingCartProductDTOList(List<ShoppingCartProduct> shoppingCartProductList) {
        if (shoppingCartProductList == null || shoppingCartProductList.isEmpty()) {
            return Collections.emptyList();
        }

        return shoppingCartProductList.stream().map(s -> ShoppingCartProductBuilder.toShoppingCartProductDTO(s)).toList();
    }
}
