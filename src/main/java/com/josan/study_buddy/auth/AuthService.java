package com.josan.study_buddy.auth;

import com.josan.study_buddy.User.Role;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserRepository;
import com.josan.study_buddy.auth.AuthDto.AuthResponse;
import com.josan.study_buddy.auth.AuthDto.LoginRequest;
import com.josan.study_buddy.auth.AuthDto.RegisterRequest;
import com.josan.study_buddy.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Delegates to DaoAuthenticationProvider: loads user, checks password.
        // Throws BadCredentialsException if credentials are wrong.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .build();
    }
}