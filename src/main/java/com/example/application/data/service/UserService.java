package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.entity.enums.Role;
import com.example.application.data.service.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    UserRepository UserRepository;

    public User getByUsername(String username){return UserRepository.getByUsername(username);}
    public UserService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    public List<User> findAll() {
        return UserRepository.findAll();
    }

    public List<User> findByRole(Role role){
        return UserRepository.getByRole(role);
    }

    public long count() {
        return UserRepository.count();
    }

    public void delete(User User) {
        UserRepository.delete(User);
    }

    public void save(User User) {
        if (User == null) {
            LOGGER.log(Level.SEVERE,
                    "User is null. Are you sure you have connected your form to the application?");
            return;
        }
        UserRepository.save(User);
    }
}
