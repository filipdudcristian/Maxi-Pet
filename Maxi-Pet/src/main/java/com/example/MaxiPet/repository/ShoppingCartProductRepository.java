package com.example.MaxiPet.repository;

import com.example.MaxiPet.entity.ShoppingCart;
import com.example.MaxiPet.entity.ShoppingCartProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartProductRepository extends CrudRepository<ShoppingCartProduct, Integer> {
    List<ShoppingCartProduct> getAllByShoppingCart(ShoppingCart shoppingCart);
}
