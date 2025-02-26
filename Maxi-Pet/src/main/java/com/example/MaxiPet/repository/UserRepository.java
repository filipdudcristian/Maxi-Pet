package com.example.MaxiPet.repository;

import com.example.MaxiPet.entity.Order;
import com.example.MaxiPet.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);

}
