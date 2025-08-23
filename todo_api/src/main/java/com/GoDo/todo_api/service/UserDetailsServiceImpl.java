package com.GoDo.todo_api.service;

import com.GoDo.todo_api.model.User;
import com.GoDo.todo_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Optional;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

private final UserRepository userRepository;

public UserDetailsServiceImpl(UserRepository userRepository) {
this.userRepository = userRepository;
}

@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    System.out.println("loadUserByUsername çağrıldı: " + email);
    Optional<User> userOpt = userRepository.findByEmail(email);
    System.out.println("findByEmail döndü: " + userOpt.isPresent());

    User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole())) // burada role veriliyor
    );
}
}
