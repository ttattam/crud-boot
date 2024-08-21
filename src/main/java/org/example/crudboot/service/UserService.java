package org.example.crudboot.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.example.crudboot.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void save(User user);
    List<User> findAll();
    User findById(Long id);
    void update(User user);
    void deleteById(Long id);
}