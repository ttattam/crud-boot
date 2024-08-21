package org.example.crudboot.service;

import org.example.crudboot.model.Role;
import org.example.crudboot.model.User;
import org.example.crudboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public void save(User user) {
        System.out.println("UserServiceImpl: Saving user: " + user.getUsername());
        System.out.println("UserServiceImpl: User roles before save: " + user.getRoles());

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleService.findByName("USER");
            System.out.println("UserServiceImpl: Adding default role: " + defaultRole);
            user.setRoles(new HashSet<>(Collections.singletonList(defaultRole)));
        }

        try {
            User savedUser = userRepository.save(user);
            System.out.println("UserServiceImpl: User saved. ID: " + savedUser.getId());
            System.out.println("UserServiceImpl: User roles after save: " + savedUser.getRoles());
        } catch (Exception e) {
            System.out.println("UserServiceImpl: Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public void createAdminUser(User user) {
        System.out.println("Creating admin user: " + user.getUsername());

        Role adminRole = roleService.findByName("ADMIN");
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(adminRole));
        } else {
            user.getRoles().add(adminRole);
        }

        User savedUser = userRepository.save(user);
        System.out.println("Admin user saved. ID: " + savedUser.getId());
        System.out.println("Admin user roles after save: " + savedUser.getRoles());
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
