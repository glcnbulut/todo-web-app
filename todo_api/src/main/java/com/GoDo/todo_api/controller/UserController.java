// Kullanıcı CRUD işlemlerini yöneten controller.
package com.GoDo.todo_api.controller;

import com.GoDo.todo_api.model.User;
import com.GoDo.todo_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

private final UserService userService;

// Controller'ın constructor'ı, bağımlılıkları enjekte eder
@Autowired
public UserController(UserService userService) {
this.userService = userService;
}

// Yeni kullanıcı ekler
@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
User savedUser = userService.save(user);
return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
}

// ID ile kullanıcıyı döndürür
@GetMapping("/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
return userService.findById(id)
.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
}
}
