package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.payload.*;
import com.jobfinder.jobportal.repository.UserRepository; // ✅ import UserRepository
import com.jobfinder.jobportal.security.JwtTokenProvider; // ✅ import JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // check user exists & password matches
        // generate token using jwtTokenProvider
        return new LoginResponse("mocked_token_here");
    }

    @Override
    public void register(RegisterRequest request) {
        // check if user exists
        // save user with encoded password
    }
}

