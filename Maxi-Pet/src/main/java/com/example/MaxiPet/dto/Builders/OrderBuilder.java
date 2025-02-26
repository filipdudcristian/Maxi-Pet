package com.example.MaxiPet.dto.Builders;

import com.example.MaxiPet.dto.OrderDTO;
import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.User;

import java.util.Collections;
import java.util.List;

public class OrderBuilder {

    public static OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .orderProductDTOList(OrderProductBuilder.toOrderProductDTOList(order.getOrderProductList()))
                .userId(order.getUser().getId())
                .build();
    }

    public static Order toEntity(OrderDTO orderDTO) {
        return Order.builder()
                .id(orderDTO.getId())
                .status(orderDTO.getStatus())
                .totalPrice(orderDTO.getTotalPrice())
                .orderDate(orderDTO.getOrderDate())
                .user(User.builder()
                        .id(orderDTO.getId())
                        .build())
                .build();
    }
    public static List<OrderDTO> toOrderDTOList(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return Collections.emptyList();
        }
        return orderList.stream().map(OrderBuilder::toOrderDTO).toList();
    }
}
