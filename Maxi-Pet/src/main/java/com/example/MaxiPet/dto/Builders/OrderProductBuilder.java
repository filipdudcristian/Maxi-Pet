package com.example.MaxiPet.dto.Builders;

import com.example.MaxiPet.dto.OrderProductDTO;
import com.example.MaxiPet.entity.OrderProduct;

import java.util.Collections;
import java.util.List;

public class OrderProductBuilder {
    public static OrderProductDTO toOrderProductDTO(OrderProduct orderProduct)
    {
        return OrderProductDTO.builder()
                .orderId(orderProduct.getOrder().getId())
                .productDTO(ProductBuilder.toProductDTO(orderProduct.getProduct()))
                .quantity(orderProduct.getQuantity())
                .build();

    }
    public static List<OrderProductDTO> toOrderProductDTOList(List<OrderProduct> orderProductList) {
        if (orderProductList == null || orderProductList.isEmpty()) {
            return Collections.emptyList();
        }

        return orderProductList.stream().map(s -> OrderProductBuilder.toOrderProductDTO(s)).toList();
    }
}
