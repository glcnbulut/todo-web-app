package com.GoDo.todo_bff.controller;

import com.GoDo.todo_bff.dto.TodoDto;
import com.GoDo.todo_bff.service.TodoBffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * BFF katmanı için ana controller. Sadece iş mantığına sahip endpointler içerir.
 */
@RestController
@RequestMapping("/bff/todos")
public class TodoBffController {

    private final TodoBffService todoBffService;

    @Autowired
    public TodoBffController(TodoBffService todoBffService) {
        this.todoBffService = todoBffService;
    }

    /**
     * Tüm todo listesini döndürür.
     */
    @GetMapping
    public List<TodoDto> getAllTodos() {
        return todoBffService.getAllTodos();
    }

    /**
     * Tek bir todo bilgisini döndürür.
     */
    @GetMapping("/{id}")
    public TodoDto getTodoById(@PathVariable Long id) {
        return todoBffService.getTodoById(id);
    }

    /**
     * Yeni bir todo ekler.
     */
    @PostMapping
    public TodoDto createTodo(@RequestBody TodoDto todoDto) {
        return todoBffService.createTodo(todoDto);
    }

    /**
     * Bir todo'yu günceller.
     */
    @PutMapping("/{id}")
    public TodoDto updateTodo(@PathVariable Long id, @RequestBody TodoDto todoDto) {
        return todoBffService.updateTodo(id, todoDto);
    }

    /**
     * Bir todo'yu siler.
     */
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoBffService.deleteTodo(id);
    }
}
