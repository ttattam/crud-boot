package org.example.crudboot.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.Collections;

import org.example.crudboot.model.Role;
import org.example.crudboot.model.User;
import org.example.crudboot.service.UserService;
import org.example.crudboot.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/admin/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "create_user";
    }

    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "roles", required = false) List<String> roleNames,
                             Model model) {
        System.out.println("Creating user: " + user.getUsername());
        System.out.println("Received role names: " + roleNames);

//        if (bindingResult.hasErrors()) {
//            System.out.println("Validation errors: " + bindingResult.getAllErrors());
//            model.addAttribute("allRoles", roleService.findAll());
//            return "create_user";
//        }

        Set<Role> roles = new HashSet<>();
        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                Role role = roleService.findByName(roleName);
                if (role != null) {
                    roles.add(role);
                    System.out.println("Added role: " + role.getName());
                } else {
                    System.out.println("Role not found: " + roleName);
                }
            }
        }

        user.setRoles(roles);
        System.out.println("User roles before save: " + user.getRoles());

        try {
            userService.save(user);
            System.out.println("User saved successfully");
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error saving user: " + e.getMessage());
            model.addAttribute("allRoles", roleService.findAll());
            return "create_user";
        }

        return "redirect:/admin";
    }




    @GetMapping("/admin/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "edit_user";
    }

    @PostMapping("/admin/edit")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "newPassword", required = false) String newPassword,
                             @RequestParam(value = "roles", required = false) List<Long> roles,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll());
            return "edit_user";
        }

        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            return "redirect:/admin";
        }

        // Обновляем поля
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());
        existingUser.setAge(user.getAge());

        // Обрабатываем пароль
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(newPassword);
        }

        // Обрабатываем роли
        Set<Role> userRoles = new HashSet<>();
        if (roles != null && !roles.isEmpty()) {
            for (Long roleId : roles) {
                Optional<Role> roleOptional = roleService.findById(roleId);
                roleOptional.ifPresent(userRoles::add);
            }
        }
        existingUser.setRoles(userRoles);


        try {
            userService.update(existingUser);
        } catch (Exception e) {
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            model.addAttribute("allRoles", roleService.findAll());
            return "edit_user";
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String userInfo(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-info";
    }
}