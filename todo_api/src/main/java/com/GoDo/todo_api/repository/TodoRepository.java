package com.GoDo.todo_api.repository;

import com.GoDo.todo_api.model.Todo;
import com.GoDo.todo_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
// Bu metot, bir kullanıcıya ait tüm görevleri bulmak için otomatik olarak oluşturulur.
List<Todo> findByUser(User user);
}
