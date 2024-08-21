package org.example.crudboot.service;

import org.example.crudboot.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Role findByName(String name);
    void save(Role role);
    void deleteById(Long id);
}
