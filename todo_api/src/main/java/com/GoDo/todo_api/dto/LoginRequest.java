// Kullanıcı login isteği için DTO.
package com.GoDo.todo_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    // Kullanıcı e-posta adresi
    private String email;
    // Kullanıcı şifresi
    private String password;
}
