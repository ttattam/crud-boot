package org.example.crudboot.config;

import org.example.crudboot.model.Role;
import org.example.crudboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;

    @Autowired
    public DataInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        initRoles();
    }

    private void initRoles() {
        if (roleService.findByName("USER") == null) {
            Role userRole = new Role("USER");
            roleService.save(userRole);
            System.out.println("USER создана");
        }

        if (roleService.findByName("ADMIN") == null) {
            Role adminRole = new Role("ADMIN");
            roleService.save(adminRole);
            System.out.println("ADMIN создана");
        }
    }
}