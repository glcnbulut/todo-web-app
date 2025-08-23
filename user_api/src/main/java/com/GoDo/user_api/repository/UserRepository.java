// Kullanıcı entity'si için JPA repository arayüzü. Veritabanı işlemlerini sağlar.
package com.GoDo.user_api.repository;

import com.GoDo.user_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Email ile kullanıcıyı bulur
    Optional<User> findByEmail(String email);
}