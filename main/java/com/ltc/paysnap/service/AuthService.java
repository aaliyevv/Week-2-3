package com.ltc.paysnap.service;

import com.ltc.paysnap.dto.AuthResponse;
import com.ltc.paysnap.dto.LoginRequest;
import com.ltc.paysnap.dto.RegisterRequest;
import com.ltc.paysnap.entity.User;
import com.ltc.paysnap.entity.enums.Role;
import com.ltc.paysnap.repository.UserRepository;
import com.ltc.paysnap.security.JwtUtil;
import com.ltc.paysnap.security.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService blacklistService;

    public AuthResponse register(RegisterRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getEmail());

        return AuthResponse.builder().token(token).build();
    }

    public void logout(String token) {
        long expiration = 86400000; // match JWT expiration
        blacklistService.blacklist(token, expiration);
    }
}