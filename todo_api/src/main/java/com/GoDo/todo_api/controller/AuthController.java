// Kullanıcı kimlik doğrulama işlemlerini yöneten controller.
package com.GoDo.todo_api.controller;

import com.GoDo.todo_api.dto.LoginRequest;
import com.GoDo.todo_api.dto.AuthResponse;
import com.GoDo.todo_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Controller'ın constructor'ı, bağımlılıkları enjekte eder
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Kullanıcı login isteği için endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        userDetails.getUsername(),
                        userDetails.getAuthorities().iterator().next().getAuthority()
                )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı e-posta veya şifre!");
        } catch (Exception e) {
            e.printStackTrace(); // loga yazdır
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Giriş sırasında bir hata oluştu.");
        }
    }
}
