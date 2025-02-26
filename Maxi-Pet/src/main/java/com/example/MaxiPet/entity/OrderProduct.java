package com.example.MaxiPet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(value = OrderProductId.class)
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @Column
    private Integer orderId;

    @Id
    @Column
    private Integer productId;


    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;


    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    @Column
    private Integer quantity;
}
