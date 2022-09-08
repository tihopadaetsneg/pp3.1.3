package ru.kata.spring.boot_security.demo.controller;

import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@ComponentScan(basePackages = "demo")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/script")
    public String js() {
        return "js";
    }

//    @GetMapping(value = "/login")
//    public String newUser (ModelMap model) {
//
//        return "login";
//    }

//    @GetMapping(value = "/registration")
//    public String registration(ModelMap model) {
//        model.addAttribute("user1", new User());
//        return "admin1";
//    }

    @PostMapping(value = "/registration")
    public String addRegistration(@ModelAttribute("user1") @Valid User user) {

        userService.saveUser(user);
        User user1 = userService.findUserByEmail(user.getEmail());
        System.out.println(user1);

        return "redirect:/login";
    }

    @GetMapping(value = "/user")
    public String getUser(Principal principal, ModelMap model) {
        String email = principal.getName();
        System.out.println(email);
        model.addAttribute("user", userService.findUserByEmail(email));
        return "user";
    }

    @GetMapping(value = "/admin")
    public String getAllUsers(Principal principal, ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("admin",
                userService.findUserByEmail(principal.getName()));
        model.addAttribute("user1", new User());
        model.addAttribute("roles", userService.getAllRoles());

        return "admin1";
    }


    @PostMapping("/admin/{id}")
    public String updateUser(User user) {
        userService.editUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}


