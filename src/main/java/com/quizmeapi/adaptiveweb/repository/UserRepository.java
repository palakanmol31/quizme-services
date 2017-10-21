package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    User findById(Integer id);
}
