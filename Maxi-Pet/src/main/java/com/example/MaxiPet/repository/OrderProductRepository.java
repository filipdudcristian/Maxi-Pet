package com.example.MaxiPet.repository;

import com.example.MaxiPet.entity.OrderProduct;
import com.example.MaxiPet.entity.OrderProductId;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductId> {
}
