package com.GoDo.todo_api.service;

import com.GoDo.todo_api.model.User;
import com.GoDo.todo_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Bu satırı ekleyin
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder; // Bu satırı ekleyin

@Autowired
public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
this.userRepository = userRepository;
this.passwordEncoder = passwordEncoder; // Bu satırı ekleyin
}

public List<User> findAll() {
return userRepository.findAll();
}

public Optional<User> findById(Long id) {
return userRepository.findById(id);
}

public User save(User user) {
// Kullanıcıyı kaydetmeden önce şifreyi şifrele
user.setPassword(passwordEncoder.encode(user.getPassword())); // Bu satırı değiştirin
return userRepository.save(user);
}

public void deleteById(Long id) {
userRepository.deleteById(id);
}
}