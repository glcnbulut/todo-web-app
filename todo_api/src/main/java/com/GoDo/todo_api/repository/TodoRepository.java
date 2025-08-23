// ToDo entity'si için JPA repository arayüzü. Veritabanı işlemlerini sağlar.
package com.GoDo.todo_api.repository;

import com.GoDo.todo_api.model.Todo;
import com.GoDo.todo_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	// Bir kullanıcıya ait tüm görevleri döndürür
	List<Todo> findByUser(User user);
}
