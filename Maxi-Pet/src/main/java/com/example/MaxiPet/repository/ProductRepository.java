package com.example.MaxiPet.repository;

import com.example.MaxiPet.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Optional<Product> getProductsByCategory(String category);

    List<Product> findByCategory(String category);

    List<Product> findProductByNameContaining(String name);

}
