package com.qpro.groceryapi.repository;

import com.qpro.groceryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByusername(String userName);
}
