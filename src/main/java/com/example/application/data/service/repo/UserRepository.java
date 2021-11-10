package com.example.application.data.service.repo;

import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getByUsername(String username);
    List<User> getByRole(Role role);

}

