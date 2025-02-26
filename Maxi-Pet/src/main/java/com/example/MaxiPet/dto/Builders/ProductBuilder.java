package com.example.MaxiPet.dto.Builders;

import com.example.MaxiPet.dto.ProductDTO;
import com.example.MaxiPet.entity.Product;

public class ProductBuilder {

    public static ProductDTO toProductDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .basePrice(product.getBasePrice())
                .discountedPrice(product.getDiscountedPrice())
                .discountPercent(product.getDiscountPercent())
                .stock(product.getStock())
                .category(product.getCategory())
                .image(product.getImage())
                //.shoppingCartProductDTOList(ShoppingCartProductBuilder.toShoppingCartProductDTOList(product.getShoppingCartProductList()))
                //.orderProductDTOList(OrderProductBuilder.toOrderProductDTOList(product.getOrderProductList()))
                .reviewDTOList(ReviewBuilder.toReviewDTOList(product.getReviewList()))
                .build();
    }
    public static Product toEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .basePrice(productDTO.getBasePrice())
                .discountPercent(productDTO.getDiscountPercent())
                .discountedPrice(productDTO.getDiscountedPrice())
                .stock(productDTO.getStock())
                .category(productDTO.getCategory())
                .image(productDTO.getImage())
                .build();
    }
}
