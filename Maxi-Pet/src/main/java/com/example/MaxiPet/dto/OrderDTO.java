package com.example.MaxiPet.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class OrderDTO {

    private Integer id;
    private String status;
    private LocalDate orderDate;
    private Float totalPrice;
    private List<OrderProductDTO> orderProductDTOList;
    private Integer userId;
}
