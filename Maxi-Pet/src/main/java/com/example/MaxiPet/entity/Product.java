package com.example.MaxiPet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "base_price")
    private Float basePrice;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Formula("base_price - base_price * discount_percent / 100")
    private Float discountedPrice;

    @Column
    private Integer stock;

    @Column
    private String category;

    @Column
    private String image;


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<ShoppingCartProduct> shoppingCartProductList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<OrderProduct> orderProductList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Review> reviewList;
}
