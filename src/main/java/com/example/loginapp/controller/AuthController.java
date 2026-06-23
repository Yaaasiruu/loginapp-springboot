package com.example.loginapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.loginapp.dto.RegisterRequest;
import com.example.loginapp.entity.User;
import com.example.loginapp.repository.UserRepository;
import com.example.loginapp.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService,
            UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalUser",
                userRepository.countByRole("USER"));

        model.addAttribute(
                "totalAdmin",
                userRepository.countByRole("ADMIN"));

        model.addAttribute(
                "totalAkun",
                userRepository.count());

        return "dashboard";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegisterRequest request, Model model) {
        if (userService.usernameExists(request.getUsername())) {
            model.addAttribute("error", "Username sudah digunakan!");
            return "register";
        }
        userService.registerUser(request);
        return "redirect:/login?registered";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {

        model.addAttribute("users",
                userRepository.findByRole("USER"));

        return "admin";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        model.addAttribute("user", user);

        return "edit-user";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute User user) {

        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        existingUser.setUsername(user.getUsername());
        existingUser.setRole(user.getRole());

        userRepository.save(existingUser);

        return "redirect:/admin";
    }
}
