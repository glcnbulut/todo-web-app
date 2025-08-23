// Login cevabı için DTO.
package com.GoDo.todo_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AuthResponse {
    // JWT token
    private String token;
    // Kullanıcı adı
    private String username;
    // Kullanıcı rolü
    private String role;
}


