package com.GoDo.todo_api.service;

import com.GoDo.todo_api.model.Todo;
import com.GoDo.todo_api.model.User;
import com.GoDo.todo_api.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    /**
     * Bir kullanıcıya ait tüm görevleri döner.
     *
     * @param user Kullanıcı
     * @return Kullanıcının görev listesi
     */
    public List<Todo> findByUser(User user) {
        return todoRepository.findByUser(user);
    }

    /**
     * Var olan bir Todo'yu günceller.
     *
     * @param id          Güncellenecek Todo'nun id'si
     * @param updatedTodo Güncellenmiş Todo nesnesi
     * @return Güncellenmiş Todo
     * @throws RuntimeException Eğer Todo bulunamazsa
     */
    public Todo updateTodo(Long id, Todo updatedTodo) {
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(updatedTodo.getTitle());
            todo.setDescription(updatedTodo.getDescription());
            todo.setCompleted(updatedTodo.isCompleted());
            return todoRepository.save(todo);
        }).orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
    }
}
