package com.GoDo.todo_bff.service;

import org.springframework.stereotype.Service;

/**
 * TodoBffService, BFF katmanında iş mantığını ve backend servislerine çağrıları yönetir.
 */
@Service
public class TodoBffService {
    // Burada iş mantığı ve backend çağrıları yer alacak

    public java.util.List<com.GoDo.todo_bff.dto.TodoDto> getAllTodos() {
        return new java.util.ArrayList<>();
    }

    public com.GoDo.todo_bff.dto.TodoDto getTodoById(Long id) {
        return new com.GoDo.todo_bff.dto.TodoDto();
    }

    public com.GoDo.todo_bff.dto.TodoDto createTodo(com.GoDo.todo_bff.dto.TodoDto todoDto) {
        return todoDto;
    }

    public com.GoDo.todo_bff.dto.TodoDto updateTodo(Long id, com.GoDo.todo_bff.dto.TodoDto todoDto) {
        return todoDto;
    }

    public void deleteTodo(Long id) {
        // Silme işlemi
    }
}
