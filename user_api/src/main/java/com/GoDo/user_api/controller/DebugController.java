package com.GoDo.user_api.controller;

import com.GoDo.user_api.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
public class DebugController {

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/api/debug/users/add")
    public ResponseEntity<String> debugAddUser(@RequestBody String body) {
        try {
            Files.writeString(Path.of("/tmp/controller_debug.log"), "[DebugController] Raw body: " + body + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            User u = mapper.readValue(body, User.class);
            Files.writeString(Path.of("/tmp/controller_debug.log"), "[DebugController] Mapped user: " + u + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return ResponseEntity.ok("mapped");
        } catch (Exception e) {
            try { Files.writeString(Path.of("/tmp/controller_debug.log"), "[DebugController] Mapping failed: " + e.toString() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND); } catch (Exception ignored) {}
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("mapping failed");
        }
    }
}
