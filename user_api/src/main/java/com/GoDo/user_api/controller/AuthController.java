// Kullanıcı kimlik doğrulama işlemlerini yöneten controller.
package com.GoDo.user_api.controller;

import com.GoDo.user_api.service.CustomUserDetailsService;
import com.GoDo.user_api.util.JwtTokenUtil;
import lombok.Data;
import com.GoDo.user_api.model.User;
import com.GoDo.user_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Controller'ın constructor'ı, bağımlılıkları enjekte eder
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Yeni kullanıcı kaydı (public)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest req) {
        try {
            if (userRepository.findByEmail(req.getEmail()).isPresent()) {
                return ResponseEntity.status(409).body("User already exists");
            }
            User u = new User();
            u.setEmail(req.getEmail());
            u.setName("");
            u.setSurname("");
            u.setPassword(passwordEncoder.encode(req.getPassword()));
            u.setRole("ROLE_USER");
            User saved = userRepository.save(u);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("error");
        }
    }

    // Kullanıcı login isteği için endpoint
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    // Login isteği için DTO
    @Data
    private static class AuthenticationRequest {
        private String email;
        private String password;
    }

    // Login cevabı için DTO
    @Data
    private static class AuthenticationResponse {
        private final String jwt;
    }
}
