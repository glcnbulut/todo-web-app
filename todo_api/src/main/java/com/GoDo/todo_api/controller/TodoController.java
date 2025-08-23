package com.GoDo.todo_api.controller;

import com.GoDo.todo_api.model.Todo;
import com.GoDo.todo_api.model.User;
import com.GoDo.todo_api.repository.UserRepository;
import com.GoDo.todo_api.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173")
public class TodoController {

private final TodoService todoService;
private final UserRepository userRepository;

@Autowired
public TodoController(TodoService todoService, UserRepository userRepository) {
this.todoService = todoService;
this.userRepository = userRepository;
}

@GetMapping
public ResponseEntity<List<Todo>> getAllTodos(Authentication authentication) {
String userEmail = authentication.getName();
Optional<User> userOptional = userRepository.findByEmail(userEmail);

if (userOptional.isPresent()) {
List<Todo> todos = todoService.findByUser(userOptional.get());

return new ResponseEntity<>(todos, HttpStatus.OK);
} else {
return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}

@GetMapping("/{id}")
public ResponseEntity<Todo> getTodoById(@PathVariable Long id, Authentication authentication) {
String userEmail = authentication.getName();
return todoService.findById(id)
.filter(todo -> todo.getUser().getEmail().equals(userEmail))
.map(todo -> new ResponseEntity<>(todo, HttpStatus.OK))
.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

@PostMapping
public ResponseEntity<Todo> createTodo(@RequestBody Todo todo, Authentication authentication) {
    String userEmail = authentication.getName();
    Optional<User> userOptional = userRepository.findByEmail(userEmail);

    if (userOptional.isPresent()) {
        todo.setUser(userOptional.get());
        Todo savedTodo = todoService.save(todo);

        // 🔽 BURAYA LOG EKLE
        System.out.println("Kayıt edilen todo: " + savedTodo);

        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

@PutMapping("/{id}")
public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo, Authentication authentication) {
String userEmail = authentication.getName();
return todoService.findById(id)
.filter(existingTodo -> existingTodo.getUser().getEmail().equals(userEmail))
.map(existingTodo -> {
existingTodo.setTitle(updatedTodo.getTitle());
existingTodo.setDescription(updatedTodo.getDescription());
existingTodo.setCompleted(updatedTodo.isCompleted());
Todo savedTodo = todoService.save(existingTodo);
return new ResponseEntity<>(savedTodo, HttpStatus.OK);
})
.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTodo(@PathVariable Long id, Authentication authentication) {
String userEmail = authentication.getName();
return todoService.findById(id)
.filter(todo -> todo.getUser().getEmail().equals(userEmail))
.map(todo -> {
todoService.deleteById(id);
return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
})
.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
}
}
