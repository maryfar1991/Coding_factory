package com.jobfinder.jobportal.controller;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.security.JwtTokenProvider;
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
        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(request.getPassword())) {
            String token = jwtTokenProvider.generateToken(optionalUser.get().getEmail());
            return new LoginResponse(token);
        }
        throw new RuntimeException("Invalid email or password");

    }


    @PostMapping("/auth/register")
    public String register(@RequestBody RegisterRequest request) {
        System.out.println("Request received: " + request.getEmail());
        // 🔎 Καταγραφή εισερχόμενων πεδίων για debugging
        System.out.println("📨 Email: " + request.getEmail());
        System.out.println("📨 Password: " + request.getPassword());
        System.out.println("📨 Role: " + request.getRole());

        // ✅ Έλεγχος για διπλό email
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Το email υπάρχει ήδη");
        }

        // 👤 Δημιουργία νέου χρήστη
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); // 💡 Πρόσθεσε PasswordEncoder για ασφάλεια
        newUser.setRole(request.getRole() != null ? request.getRole() : "USER"); // default σε περίπτωση null

        // 📦 Αποθήκευση
        userService.createUser(newUser);

        // 🎉 Επιτυχές μήνυμα
        return "Ο χρήστης δημιουργήθηκε με επιτυχία!";
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

