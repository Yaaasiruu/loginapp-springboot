package com.example.loginapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.loginapp.dto.RegisterRequest;
import com.example.loginapp.entity.User;
import com.example.loginapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void registerUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        // Password di-encode dengan BCrypt sebelum disimpan
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER"); // default role saat register
        userRepository.save(user);
    }
}
