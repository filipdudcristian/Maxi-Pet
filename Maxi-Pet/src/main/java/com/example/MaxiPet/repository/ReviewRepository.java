package com.example.MaxiPet.repository;

import com.example.MaxiPet.entity.Product;
import com.example.MaxiPet.entity.Review;
import com.example.MaxiPet.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> getAllByUser(User user);
    List<Review> getAllByProduct(Product product);
}
