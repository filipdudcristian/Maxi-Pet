package com.example.MaxiPet.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderProductId implements Serializable {
    private Integer orderId;
    private Integer productId;
}
