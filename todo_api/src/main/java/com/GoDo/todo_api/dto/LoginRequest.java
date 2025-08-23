package com.GoDo.todo_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
