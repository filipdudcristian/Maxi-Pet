package com.example.MaxiPet.repository;

import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

     List<Order> getAllByUser(User user);
     List<Order> findOrdersByUserId(int userId);
}
