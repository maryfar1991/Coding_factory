package com.jobfinder.jobportal.controller;

import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.payload.LoginRequest;
import com.jobfinder.jobportal.payload.LoginResponse;
import com.jobfinder.jobportal.payload.RegisterRequest;
import com.jobfinder.jobportal.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ===============================
    // 🔐 AUTH ENDPOINTS
    // ===============================

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user != null && user.getPassword().equals(request.getPassword())) {
            // TODO: χρησιμοποιήσε PasswordEncoder στην παραγωγή
            String token = jwtTokenProvider.generateToken(user.getEmail());
            return new LoginResponse(token);
        }
        throw new RuntimeException("Invalid email or password");
    }

    @PostMapping("/auth/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); // TODO: encode password
        newUser.setRole(request.getRole());
        userService.createUser(newUser);
        return "User registered successfully!";
    }

    // ===============================
    // 👤 USER CRUD ENDPOINTS
    // ===============================

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

