package com.GoDo.todo_api.repository;

import com.GoDo.todo_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
// Spring Security için kullanıcıyı e-posta (username) ile bulma metodu
Optional<User> findByEmail(String email); // 'findByUsername' yerine 'findByEmail' olarak değiştirildi
}

