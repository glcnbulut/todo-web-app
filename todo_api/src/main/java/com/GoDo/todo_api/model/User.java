package com.GoDo.todo_api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String name;
private String surname;
private String email;
private String password;
private String role = "ROLE_USER";

// User'a ait olan ToDo'lar listesi
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonIgnore // Bu etiketi ekliyoruz ki sonsuz döngü hatası oluşmasın
private List<Todo> todos = new ArrayList<>();
@Override
@JsonIgnore
public Collection<? extends GrantedAuthority> getAuthorities() {
return Collections.singletonList(new SimpleGrantedAuthority(this.role));
}

@Override
public String getUsername() {
return this.email;
}

@Override
public boolean isAccountNonExpired() {
return true;
}

@Override
public boolean isAccountNonLocked() {
return true;
}

@Override
public boolean isCredentialsNonExpired() {
return true;
}

@Override
public boolean isEnabled() {
return true;
}
}